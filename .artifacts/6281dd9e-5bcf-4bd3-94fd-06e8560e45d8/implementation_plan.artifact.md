# Implementation Plan - WebSocket Connection Architecture

Implement a clean, robust, coroutine-based WebSocket connection architecture using OkHttp WebSocket, exposed via Kotlin `StateFlow` and `SharedFlow`, and injected via Hilt.

## User Review Required

> [!IMPORTANT]
> - The architecture will use OkHttp's built-in `WebSocket` API wrapped inside a reactive manager (`WebSocketManager`).
> - Exposes connection state (`StateFlow<WebSocketState>`) and incoming messages (`SharedFlow<String>` or deserialized event objects).
> - Provides automated reconnection logic and Hilt singleton injection (`WebSocketModule`).

## Open Questions

- What is the WebSocket base URL? (Will configure a placeholder or configurable URL, e.g., `"ws://192.168.1.23:8000/ws"` or similar).

## Proposed Changes

### WebSocket Architecture Components

#### [NEW] [WebSocketState.kt](file:///home/jp/RynzoDriver/app/src/main/java/com/example/rynzodriver/data/websocket/WebSocketState.kt)
- Sealed class representing connection states: `Disconnected`, `Connecting`, `Connected`, `Error(val throwable: Throwable)`.

#### [NEW] [WebSocketManager.kt](file:///home/jp/RynzoDriver/app/src/main/java/com/example/rynzodriver/data/websocket/WebSocketManager.kt)
- Manages OkHttp `WebSocket` lifecycle (`connect`, `disconnect`, `send`, `sendMessage`).
- Exposes `connectionState: StateFlow<WebSocketState>` and `incomingMessages: SharedFlow<String>`.
- Implements `WebSocketListener` callbacks to update states and relay incoming messages/errors.

#### [NEW] [WebSocketRepository.kt](file:///home/jp/RynzoDriver/app/src/main/java/com/example/rynzodriver/data/repository/websocket/WebSocketRepository.kt) & Implementation
- Domain-friendly repository interface and implementation wrapping `WebSocketManager`.

#### [NEW] [WebSocketModule.kt](file:///home/jp/RynzoDriver/app/src/main/java/com/example/rynzodriver/di/WebSocketModule.kt)
- Hilt `@Module` providing `WebSocketManager` and `WebSocketRepository` as singletons.

## Verification Plan

### Automated Tests
- Run Gradle build (`:app:assembleDebug`) to verify compilation and dependency injection graph.
