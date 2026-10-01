# Google Play Games 설정 안내

앱에는 Google Play Games SDK v2 로그인과 리더보드 연결 코드가 적용되어 있습니다. 실제 서비스를 사용하려면 아래 Play Console 설정을 완료해야 합니다.

## 1. 프로젝트와 리더보드 생성

1. Google Play Console에서 앱의 Play Games Services 프로젝트를 생성하거나 기존 프로젝트를 연결합니다.
2. 프로젝트 설정 화면에서 숫자로 된 프로젝트 ID를 확인합니다.
3. 점수 리더보드를 생성합니다. 점수 형식은 정수, 정렬 방식은 큰 점수 우선으로 설정합니다.
4. 생성된 리더보드 ID를 확인합니다.

## 2. Android 인증 정보 등록

앱 패키지 이름은 `com.numberer.cozymerge`입니다.

- 개발용 APK를 테스트하려면 디버그 서명 인증서의 SHA-1에 맞는 Android 인증 정보를 등록합니다.
- Google Play 배포 앱에는 Play App Signing의 앱 서명 인증서 SHA-1에 맞는 Android 인증 정보를 등록합니다.
- Play Games Services 테스트 계정에 테스트할 Google 계정을 추가합니다.

## 3. 프로젝트에 ID 입력

`android/gradle.properties` 파일에 다음 두 항목을 추가합니다. 예시 문자열을 실제 발급받은 값으로 바꿔주세요.

```properties
PLAY_GAMES_PROJECT_ID=YOUR_NUMERIC_PROJECT_ID
PLAY_GAMES_LEADERBOARD_ID=YOUR_LEADERBOARD_ID
```

프로젝트 ID는 숫자이고, 리더보드 ID는 Play Console에서 발급한 문자열입니다. 값을 따옴표로 감싸지 않습니다.

## 4. 동기화 및 APK 빌드

프로젝트 루트에서 PowerShell로 실행합니다.

```powershell
npm run cap:sync
Push-Location android
.\gradlew.bat assembleDebug
Pop-Location
```

생성된 APK 경로:

```text
android/app/build/outputs/apk/debug/app-debug.apk
```

일반 사용자에게 배포하기 전에는 Play Games Services 설정도 게시해야 합니다. 앱 배포와 Play Games Services 설정 게시는 별도입니다.

## 5. 실제 기기 확인

Google Play 서비스가 있는 Android 기기에서 확인합니다.

1. 등록한 테스트 계정으로 Play Games에 로그인합니다.
2. 앱 설정에서 플레이어 이름과 로그인 상태가 표시되는지 확인합니다.
3. 게임을 종료한 뒤 점수가 공식 리더보드에 등록되는지 확인합니다.
4. 리더보드 버튼으로 공식 순위표가 열리는지 확인합니다.
5. 기기 계정 설정 버튼과 계정 변경 안내를 확인합니다.
6. 진동 옵션을 OFF로 변경하고 합체 시 진동이 없는지 확인합니다.
7. 앱을 다시 실행해 진동 옵션이 유지되는지 확인합니다.

## 현재 동작

- 점수는 로그인된 계정으로 게임 종료 시 제출됩니다.
- 로그인하기 전에 저장된 로컬 기록은 자동 업로드하지 않습니다.
- 로컬 기록과 Google Play Games 공식 리더보드는 별도입니다.
- 프로젝트 ID와 리더보드 ID가 없으면 Play Games 기능은 준비 중으로 표시됩니다.
- 실제 로그인과 순위 등록 검증은 Console 설정 후 실제 기기에서 진행해야 합니다.

공식 문서: [Android Play Games 인증 설정](https://developer.android.com/games/pgs/android/android-signin)