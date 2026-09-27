# Implementation Plan - Trip Request Cards & Horizontal Scroll View on HomeScreen

Implement trip request cards with Rapido-style UI, update DTOs & repository mapping to parse API response (`/api/trips/requests`), and display these cards in a horizontal scroll view (`LazyRow`) on `HomeScreen.kt`.

## User Review Required

> [!IMPORTANT]
> The API response includes `createdBy` (`Name`, `MobileNumber`), `tripType`, `tripMode`, and lowercase stop types (`"pickup"`, `"drop"`), along with coordinates `[longitude, latitude]`. We are updating DTOs and mapping to correctly parse these fields.

## Open Questions
- None. The API schema and requirements are clearly specified.

## Proposed Changes

### Data & Domain Layer

#### [MODIFY] [TripDto.kt](file:///home/jp/RynzoDriver/app/src/main/java/com/example/rynzodriver/data/dto/trips/TripDto.kt)
- Add `CreatedByDto` for customer details (`Name`, `MobileNumber`).
- Add `tripMode` and `createdBy` fields to `TripDto`.
- Support lowercase stop types in `StopDto`.

#### [MODIFY] [Trip.kt](file:///home/jp/RynzoDriver/app/src/main/java/com/example/rynzodriver/domain/model/trips/Trip.kt)
- Update `Trip` domain model to include `customerName`, `customerPhone`, `tripType`, and `tripMode`.

#### [MODIFY] [TripRepositoryImpl.kt](file:///home/jp/RynzoDriver/app/src/main/java/com/example/rynzodriver/data/repository/trips/TripRepositoryImpl.kt)
- Update mapping to populate customer details and case-insensitive stop types (`pickup`/`drop`).

---

### UI Layer

#### [MODIFY] [RequestedTripsCard.kt](file:///home/jp/RynzoDriver/app/src/main/java/com/example/rynzodriver/ui/orders/RequestedTripsCard.kt)
- Implement a Rapido-style trip request card showing customer info, trip type, pickup & drop locations with coordinates, status, and Accept/Reject buttons.

#### [MODIFY] [HomeScreen.kt](file:///home/jp/RynzoDriver/app/src/main/java/com/example/rynzodriver/ui/home/HomeScreen.kt)
- Inject `TripRequestsViewModel` (or add state collection) to fetch trip requests.
- Display trip request cards in a horizontal scroll view (`LazyRow`) on `HomeScreen.kt`.

## Verification Plan

### Automated Tests
- Build project with `gradle_build("app:assembleDebug")` to ensure compilation succeeds without errors.

### Manual Verification
- Deploy to emulator / device and verify trip request cards appear in horizontal scroll view on `HomeScreen.kt` with correct customer details, stop locations, coordinates, and action buttons.
