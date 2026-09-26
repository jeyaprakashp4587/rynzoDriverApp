# Trip Details and Google Maps Integration Plan

Implement a detailed trip view with Google Maps integration, refactor the Home screen UI, and fetch specific trip details from the API.

## User Review Required

> [!IMPORTANT]
> The Google Maps API key provided will be added to the `AndroidManifest.xml`. Ensure that the key has "Maps SDK for Android" enabled in the Google Cloud Console.

> [!NOTE]
> The "Uber style" map will be implemented using a JSON style string applied to the `GoogleMap` component to achieve thin lines and black/white roads.

## Proposed Changes

### [Dependencies]

#### [MODIFY] [libs.versions.toml](file:///home/jp/RynzoDriver/gradle/libs.versions.toml)
- Add `mapsCompose = "6.1.0"` and `playServicesMaps = "19.0.0"`.
- Add `maps-compose` and `play-services-maps` libraries.

#### [MODIFY] [build.gradle.kts](file:///home/jp/RynzoDriver/app/build.gradle.kts)
- Add Google Maps dependencies to the `dependencies` block.

### [Manifest]

#### [MODIFY] [AndroidManifest.xml](file:///home/jp/RynzoDriver/app/src/main/AndroidManifest.xml)
- Add `<meta-data android:name="com.google.android.geo.API_KEY" android:value="AIzaSyB9PAgkLhYcLp9Z_gyp9rjSW9u3evWKbn0"/>` inside the `<application>` tag.

### [Data Layer]

#### [NEW] [ParticularTripResponse.kt](file:///home/jp/RynzoDriver/app/src/main/java/com/example/rynzodriver/data/dto/trips/ParticularTripResponse.kt)
- Define DTOs for the `/api/trips/getParticularRequestedTripDetails/${tripId}` response, including `TripDetailsDto`, `TripUserDto`, `UserCompactDto`, `DriverCompactDto`, and `VehicleCompactDto`.

#### [MODIFY] [TripApiService.kt](file:///home/jp/RynzoDriver/app/src/main/java/com/example/rynzodriver/data/api/trips/TripApiService.kt)
- Add `suspend fun getParticularTripDetails(@Path("tripId") tripId: String): Response<ParticularTripResponse>`.

#### [MODIFY] [TripRepository.kt](file:///home/jp/RynzoDriver/app/src/main/java/com/example/rynzodriver/domain/repository/trips/TripRepository.kt) & [TripRepositoryImpl.kt](file:///home/jp/RynzoDriver/app/src/main/java/com/example/rynzodriver/data/repository/trips/TripRepositoryImpl.kt)
- Add method `getParticularTripDetails(tripId: String): Result<TripDetails>` to the interface and implementation.

### [Domain Layer]

#### [NEW] [TripDetails.kt](file:///home/jp/RynzoDriver/app/src/main/java/com/example/rynzodriver/domain/model/trips/TripDetails.kt)
- Define domain models for detailed trip info, including driver and vehicle details.

#### [NEW] [GetParticularTripDetailsUseCase.kt](file:///home/jp/RynzoDriver/app/src/main/java/com/example/rynzodriver/domain/usecase/trips/GetParticularTripDetailsUseCase.kt)
- Create a use case to fetch specific trip details.

### [UI Layer]

#### [MODIFY] [HomeScreen.kt](file:///home/jp/RynzoDriver/app/src/main/java/com/example/rynzodriver/ui/home/HomeScreen.kt)
- Remove earnings, trips, rating, and hours stats.
- Show `TripRequestCard` list directly on the Home screen (or keep the "TRIP REQUESTS" navigation if preferred, but user said "in that home screen... show that request trip tha cards").
- I will integrate `TripRequestsViewModel` into `HomeScreen` to show the cards.

#### [MODIFY] [TripRequestCard.kt](file:///home/jp/RynzoDriver/app/src/main/java/com/example/rynzodriver/ui/trips/components/TripRequestCard.kt)
- Replace "ACCEPT" button with "VIEW ON MAP" button.
- Pass the `tripId` to the navigation callback.

#### [NEW] [TripDetailScreen.kt](file:///home/jp/RynzoDriver/app/src/main/java/com/example/rynzodriver/ui/trips/TripDetailScreen.kt)
- Implement Google Map (60% height) with markers for stops.
- Apply "Uber style" map styling.
- Implement a scrollable bottom section (40% height) showing:
    - Stops list (scrollable if > 2).
    - Driver and Vehicle details.
    - "ACCEPT" and "REJECT" action buttons.

#### [NEW] [TripDetailViewModel.kt](file:///home/jp/RynzoDriver/app/src/main/java/com/example/rynzodriver/ui/trips/TripDetailViewModel.kt)
- Manage loading and state for the trip details screen.

### [Navigation]

#### [MODIFY] [NavRoutes.kt](file:///home/jp/RynzoDriver/app/src/main/java/com/example/rynzodriver/ui/navigation/NavRoutes.kt)
- Add `TripDetail(val tripId: String)` route.

#### [MODIFY] [RootNavGraph.kt](file:///home/jp/RynzoDriver/app/src/main/java/com/example/rynzodriver/ui/navigation/RootNavGraph.kt)
- Register `TripDetailScreen` with the `tripId` argument.

## Verification Plan

### Automated Tests
- Build the project to verify dependencies and code compilation.

### Manual Verification
- Launch the app and verify the Home screen shows trip cards with "VIEW ON MAP".
- Click "VIEW ON MAP" and verify navigation to `TripDetailScreen`.
- Verify Google Map renders with markers and Uber-style styling.
- Verify trip details (stops, driver) are displayed correctly in the bottom section.
- Verify "ACCEPT" and "REJECT" buttons are present.
