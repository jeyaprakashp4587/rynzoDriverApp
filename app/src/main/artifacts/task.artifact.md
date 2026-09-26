# Task List - Trip Details and Google Maps Integration

- `[ ]` Dependencies: Add Google Maps to `libs.versions.toml` and `build.gradle.kts`
- `[ ]` Manifest: Add Google Maps API Key to `AndroidManifest.xml`
- `[ ]` Data Layer: Create `ParticularTripResponse` DTO
- `[ ]` Data Layer: Update `TripApiService` with `getParticularRequestedTripDetails`
- `[ ]` Domain Layer: Create `TripDetails` model and `GetParticularTripDetailsUseCase`
- `[ ]` Data Layer: Update `TripRepository` and `TripRepositoryImpl`
- `[ ]` UI Layer: Refactor `HomeScreen` to show `TripRequestCard` list
- `[ ]` UI Layer: Update `TripRequestCard` with "VIEW ON MAP" button
- `[ ]` UI Layer: Create `TripDetailViewModel`
- `[ ]` UI Layer: Create `TripDetailScreen` with Google Maps (Uber style)
- `[ ]` Navigation: Add `TripDetail` route and register in `RootNavGraph`
- `[ ]` Verification: Build and verify the flow
