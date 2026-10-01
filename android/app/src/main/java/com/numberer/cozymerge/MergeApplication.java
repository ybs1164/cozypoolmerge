package com.numberer.cozymerge;
import android.app.Application;
import com.google.android.gms.games.PlayGamesSdk;
public class MergeApplication extends Application {
    @Override public void onCreate() {
        super.onCreate();
        if (!getString(R.string.game_services_project_id).equals("0")) PlayGamesSdk.initialize(this);
    }
}
