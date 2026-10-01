package com.numberer.cozymerge;

import android.content.Intent;
import android.provider.Settings;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.google.android.gms.games.PlayGames;

@CapacitorPlugin(name = "PlayGames")
public class PlayGamesPlugin extends Plugin {
    private boolean configured() {
        return !getContext().getString(R.string.game_services_project_id).equals("0")
            && !getContext().getString(R.string.play_games_leaderboard_id).isEmpty();
    }
    private boolean requireConfig(PluginCall call) {
        if (configured()) return true;
        call.reject("Play Console IDs are not configured", "NOT_CONFIGURED");
        return false;
    }
    private void player(PluginCall call) {
        PlayGames.getPlayersClient(getActivity()).getCurrentPlayer()
            .addOnSuccessListener(p -> {
                JSObject result = new JSObject();
                result.put("configured", true);
                result.put("authenticated", true);
                result.put("displayName", p.getDisplayName());
                call.resolve(result);
            }).addOnFailureListener(e -> call.reject("Unable to load player", e));
    }
    @PluginMethod public void getStatus(PluginCall call) {
        if (!configured()) {
            JSObject result = new JSObject();
            result.put("configured", false);
            result.put("authenticated", false);
            call.resolve(result);
            return;
        }
        PlayGames.getGamesSignInClient(getActivity()).isAuthenticated()
            .addOnSuccessListener(r -> {
                if (r.isAuthenticated()) player(call);
                else {
                    JSObject result = new JSObject();
                    result.put("configured", true);
                    result.put("authenticated", false);
                    call.resolve(result);
                }
            }).addOnFailureListener(e -> call.reject("Authentication check failed", e));
    }
    @PluginMethod public void signIn(PluginCall call) {
        if (!requireConfig(call)) return;
        PlayGames.getGamesSignInClient(getActivity()).signIn()
            .addOnSuccessListener(r -> {
                if (r.isAuthenticated()) player(call);
                else call.reject("Sign in was cancelled", "SIGN_IN_REQUIRED");
            }).addOnFailureListener(e -> call.reject("Sign in failed", e));
    }
    @PluginMethod public void submitScore(PluginCall call) {
        if (!requireConfig(call)) return;
        Double score = call.getDouble("score");
        if (score == null || !Double.isFinite(score) || score <= 0 || score != Math.floor(score) || score > 9007199254740991d) {
            call.reject("Invalid score"); return;
        }
        PlayGames.getLeaderboardsClient(getActivity()).submitScoreImmediate(
            getContext().getString(R.string.play_games_leaderboard_id), score.longValue())
            .addOnSuccessListener(r -> call.resolve())
            .addOnFailureListener(e -> call.reject("Score submission failed", e));
    }
    @PluginMethod public void showLeaderboard(PluginCall call) {
        if (!requireConfig(call)) return;
        PlayGames.getLeaderboardsClient(getActivity()).getLeaderboardIntent(
            getContext().getString(R.string.play_games_leaderboard_id))
            .addOnSuccessListener(intent -> {
                getActivity().startActivityForResult(intent, 9001);
                call.resolve();
            }).addOnFailureListener(e -> call.reject("Unable to open leaderboard", e));
    }
    @PluginMethod public void openAccountSettings(PluginCall call) {
        getActivity().runOnUiThread(() -> {
            try {
                getActivity().startActivity(new Intent(Settings.ACTION_SETTINGS));
                call.resolve();
            } catch (Exception e) { call.reject("Unable to open device settings", e); }
        });
    }
}
