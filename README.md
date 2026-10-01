# Cozy Pool Merge - 안드로이드 빌드 가이드

## AAB 빌드

프로젝트 루트에서 `npm run build:aab`를 실행하면 웹 리소스 빌드, Android 동기화, release 번들 빌드를 진행합니다.
결과 파일: `android/app/build/outputs/bundle/release/app-release.aab`.
`android/keystore.properties`의 로컬 서명 설정으로 자동 서명합니다. 설정이 없으면 위 명령은 실패합니다.
업로드 키는 `android/keystore/upload-key.jks`, 비밀번호와 별칭은 `android/keystore.properties`에 보관합니다. 두 파일은 Git에서 제외됩니다. 두 파일을 별도의 안전한 저장소에 함께 백업하고, 이후 업데이트에도 같은 키를 사용하세요.

이 프로젝트는 웹(HTML5/Canvas) 기반 게임을 **Capacitor 7**을 통해 안드로이드 네이티브 앱으로 패키징한 프로젝트입니다.

---

## 📋 프로젝트 사양 및 환경

- **App ID (Package Name):** `com.numberer.cozymerge`
- **Capacitor 버전:** v7.0.x
- **Compile / Target SDK:** 36 (Android 16)
- **Min SDK:** 23 (Android 6.0 Marshmallow)
- **Gradle Plugin:** 8.9.1

---

## 🛠️ 사전 필수 요구사항 (Prerequisites)

빌드를 진행하기 전에 다음 도구들이 설치되어 있어야 합니다:

1. **Node.js**: v18 이상 권장 ([Node.js 공식 사이트](https://nodejs.org/))
2. **JDK (Java Development Kit)**: **JDK 17 또는 JDK 21** 권장 (Gradle 8.11.1 호환)
   - 시스템 환경 변수 `JAVA_HOME`이 해당 JDK 경로로 설정되어 있어야 합니다.
3. **Android Studio**: 최신 버전 권장 ([Android Studio 다운로드](https://developer.android.com/studio))
   - Android SDK Platform 36 및 Android SDK Build-Tools 설치 필요
4. **Android SDK 경로 설정**:
   - `android/local.properties` 파일에 SDK 경로가 올바르게 지정되어 있는지 확인합니다.
   - 예시 (Windows):
     ```properties
     sdk.dir=C\:\\Users\\<사용자명>\\AppData\\Local\\Android\\Sdk
     ```

---

## 🚀 빠른 시작 (Quick Start)

### 1. 패키지 설치
프로젝트 루트 디렉토리에서 의존성 패키지를 설치합니다:
```bash
npm install
```

### 2. 웹 에셋 빌드 및 동기화 (Sync)
웹 게임 소스(`index.html`, `images/`, `sounds/`, `bgm.mp3` 등)를 `www/` 폴더로 복사하고, 안드로이드 네이티브 프로젝트에 반영합니다:
```bash
npm run cap:sync
```
> ⚠️ **주의:** 게임 코드나 리소스를 수정한 후에는 **반드시 `npm run cap:sync`를 실행**해야 네이티브 앱에 변경 사항이 반영됩니다.

---

## 📱 빌드 방법

### 방법 1: Android Studio 사용 (가장 권장)

1. **Android Studio 열기**
   프로젝트 루트에서 다음 명령어를 실행하면 Android Studio가 자동으로 실행됩니다:
   ```bash
   npm run cap:android
   ```
   *(또는 Android Studio를 켜고 `android` 폴더를 직접 Open 하셔도 됩니다.)*

2. **Gradle 동기화 (Sync Project with Gradle Files)**
   Android Studio 오른쪽 상단의 코끼리 아이콘(Sync)을 눌러 Gradle 동기화가 오류 없이 끝나는지 확인합니다.

3. **디버그 APK 빌드**
   - 상단 메뉴: **Build** > **Build Bundle(s) / APK(s)** > **Build APK(s)** 클릭
   - 빌드가 완료되면 우측 하단 알림창에서 **`locate`** 링크를 클릭하여 생성된 `app-debug.apk` 확인

4. **기기 또는 에뮬레이터에서 바로 실행**
   - USB 디버깅이 켜진 안드로이드 기기를 연결하거나 가상 에뮬레이터(AVD)를 선택합니다.
   - 상단의 **Run (초록색 재생 버튼 ▶)** 클릭

5. **배포용 릴리즈 빌드 (AAB / Signed APK)**
   - 상단 메뉴: **Build** > **Generate Signed Bundle / APK...** 선택
   - **Android App Bundle (Google Play 배포용)** 또는 **APK** 선택 후 서명 키스토어(Keystore)를 지정하여 빌드

---

### 방법 2: CLI 터미널에서 직접 빌드 (Debug APK)

Android Studio를 켜지 않고 명령 프롬프트 / PowerShell / 터미널에서 바로 APK를 빌드할 수 있습니다.

#### [Windows PowerShell / CMD]
```powershell
# 1. 웹 리소스 빌드 및 안드로이드 동기화
npm run cap:sync

# 2. android 디렉토리로 이동
cd android

# 3. Gradle 빌드 실행
.\gradlew assembleDebug

# 4. 루트로 복귀 (선택)
cd ..
```

#### [Mac / Linux / Git Bash]
```bash
# 1. 웹 리소스 빌드 및 안드로이드 동기화
npm run cap:sync

# 2. android 디렉토리로 이동 후 빌드 실행
cd android
./gradlew assembleDebug
cd ..
```

---

## 📦 생성된 APK 파일 위치

터미널이나 Android Studio에서 디버그 빌드를 마치면 아래 경로에 APK가 생성됩니다:

```
android/app/build/outputs/apk/debug/app-debug.apk
```

---

## 📜 NPM 스크립트 요약

| 명령어 | 설명 |
| :--- | :--- |
| `npm run build` | 웹 리소스(`index.html`, 이미지, 사운드 등)를 `www/` 폴더로 번들링 |
| `npm run cap:sync` | 웹 리소스를 빌드하고 안드로이드 프로젝트(`android/`)에 동기화 |
| `npm run cap:android` | 웹 빌드 및 동기화 후 Android Studio 실행 |
| `npm run build:apk` | 웹 빌드 + 동기화 + Gradle 디버그 APK 빌드 일괄 실행 |

---

## ❓ 문제 해결 (Troubleshooting)

### Q1. 코드나 이미지를 수정했는데 앱에서 변경사항이 안 보입니다.
- 웹 파일을 수정한 뒤 `npm run cap:sync`를 실행하지 않으면 네이티브 프로젝트에 반영되지 않습니다. 항상 동기화 명령을 먼저 실행해 주세요.

### Q2. `Unsupported class file major version` 또는 Java 버전 에러가 발생합니다.
- Android Gradle Plugin 8.9.1은 **JDK 17** 또는 **JDK 21**이 필요합니다.
- 터미널에서 `java -version`을 확인하고, 환경 변수 `JAVA_HOME`이 JDK 17/21을 가리키고 있는지 확인하세요.
- Android Studio에서는 **Settings (Preferences)** > **Build, Execution, Deployment** > **Build Tools** > **Gradle** 메뉴의 **Gradle JDK** 항목이 17 또는 21로 설정되어 있는지 확인합니다.

### Q3. `SDK location not found` 에러가 발생합니다.
- `android/` 디렉토리 안에 `local.properties` 파일이 있는지 확인하고, Android SDK 경로가 맞게 설정되어 있는지 확인하세요:
  ```properties
  sdk.dir=C\:\\Users\\<본인계정>\\AppData\\Local\\Android\\Sdk
  ```

### Q4. Mac / Linux에서 `./gradlew: Permission denied` 오류가 발생합니다.
- 실행 권한을 부여합니다:
  ```bash
  chmod +x android/gradlew
  ```
