# Implementation Plan - Production-Ready Login & Data Persistence

Implement a professional two-step login flow (Credentials -> API -> OTP), local data persistence with Jetpack DataStore, and session-based startup navigation.

## User Review Required

> [!IMPORTANT]
> - **Login Flow**: Mobile + Password -> API Call -> OTP (123456) -> Success.
> - **Persistence**: User data and tokens will be cached in Jetpack DataStore.
> - **Session Management**: The app will automatically navigate to the Home screen on launch if a user is already logged in.
> - **Networking**: A Bearer token will be automatically added to all API requests via a Retrofit Interceptor.

## Proposed Changes

### 1. Build Configuration
#### [MODIFY] [libs.versions.toml](file:///home/jp/RynzoDriver/gradle/libs.versions.toml)
- Add Jetpack DataStore dependencies.

#### [MODIFY] [build.gradle.kts (App)](file:///home/jp/RynzoDriver/app/build.gradle.kts)
- Add DataStore dependency.

---

### 2. Data Layer
#### [NEW] `data/local/DataStoreManager.kt`
- Handle saving and retrieving user data and authentication tokens.

#### [NEW] `data/api/AuthInterceptor.kt`
- OkHttp Interceptor to inject `Authorization: Bearer <token>` into headers.

#### [MODIFY] [ApiService.kt](file:///home/jp/RynzoDriver/app/src/main/java/com/example/rynzodriver/data/api/ApiService.kt)
- Update with `@POST("auth/login")` and its DTOs.

#### [MODIFY] [LoginRepositoryImpl.kt](file:///home/jp/RynzoDriver/app/src/main/java/com/example/rynzodriver/data/repository/LoginRepositoryImpl.kt)
- Update to perform real API authentication and save results to `DataStoreManager`.

---

### 3. UI Layer (MVVM)
#### [MODIFY] [LoginViewModel.kt](file:///home/jp/RynzoDriver/app/src/main/java/com/example/rynzodriver/ui/login/LoginViewModel.kt)
- Manage state for the multi-step flow: `CredentialsEntry` -> `Loading` -> `OTPEntry` -> `Finalizing`.
- Add logic to verify OTP (123456).

#### [MODIFY] [LoginScreen.kt](file:///home/jp/RynzoDriver/app/src/main/java/com/example/rynzodriver/ui/login/LoginScreen.kt)
- Redesign for a two-step UI:
    - Step 1: Mobile & Password form.
    - Step 2: 6-digit OTP input.

#### [NEW] `ui/main/MainViewModel.kt`
- Check authentication status on app startup.

#### [MODIFY] [RootNavGraph.kt](file:///home/jp/RynzoDriver/app/src/main/java/com/example/rynzodriver/ui/navigation/RootNavGraph.kt) & [MainActivity.kt](file:///home/jp/RynzoDriver/app/src/main/java/com/example/rynzodriver/MainActivity.kt)
- Implement splash/session check logic to decide between `Intro` and `MainContainer` on startup.

---

### 4. Dependency Injection
#### [NEW] `di/DataStoreModule.kt`
- Provide `DataStoreManager`.

#### [MODIFY] [NetworkModule.kt](file:///home/jp/RynzoDriver/app/src/main/java/com/example/rynzodriver/di/NetworkModule.kt)
- Add `AuthInterceptor` to the `OkHttpClient`.

## Verification Plan

### Manual Verification
1. **Fresh Launch**: App starts at **Intro**.
2. **Login**:
    - Enter Mobile/Pass -> Successful API response.
    - UI switches to **OTP Screen**.
    - Enter `123456` -> Navigate to **Home**.
3. **Persisted Launch**: Close and reopen app -> Should land directly on **Home**.
4. **API Headers**: Verify (via Logs) that subsequent API calls include the Bearer token.
