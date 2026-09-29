# Reverse Geocoding & Permissions

This project uses OpenStreetMap Nominatim for reverse geocoding (no API key required).

Important notes:

- Nominatim requires a valid `User-Agent` header that identifies the application and contact information. The implementation in `app/src/main/java/com/example/rynzodriver/location/GeocodeService.kt` sets a simple `User-Agent` string; replace `contact@example.com` with a real contact email or domain.

- The app manifest already requests location-related permissions: `ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION`, and `ACCESS_BACKGROUND_LOCATION`. At runtime you must request `ACCESS_FINE_LOCATION` (and background location when needed) from the user. The project contains a `PermissionHandler` composable used on the `HomeScreen` to centralize permission handling — ensure it requests runtime permissions on modern Android versions.

- To build the app locally, run:

```bash
./gradlew :app:assembleDebug
```

- If you prefer Google Geocoding instead, I can add a switch and README for adding an API key.
