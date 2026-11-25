package com.example.rynzodriver.di

// Senior explanation:
// Hilt dependency providers and interface bindings.
// Read top-down: imports provide dependencies, declarations define state/contracts, and the function body coordinates them.

// File purpose: This file configures Hilt dependency injection.

// Project imports: shared domain, data, UI, navigation, or utility types from this app.
import com.example.rynzodriver.data.repository.websocket.WebSocketRepositoryImpl
import com.example.rynzodriver.domain.repository.websocket.WebSocketRepository
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Library imports: supporting types or annotations used by this declaration.
import dagger.Binds
import dagger.Module
// Hilt imports: dependency injection annotations and ViewModel integration.
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Library imports: supporting types or annotations used by this declaration.
import javax.inject.Singleton

// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Dependency module: Hilt reads the providers or bindings declared by this class/object.
@Module
// Scope binding: these dependencies are installed into the specified Hilt component lifecycle.
@InstallIn(SingletonComponent::class)
// Abstract base: `WebSocketModule` can require implementations while sharing lifecycle or utility behavior.
abstract class WebSocketModule {

// Interface binding: Hilt maps a dependency contract to the implementation supplied at runtime.
    @Binds
// Application lifetime: Hilt/this framework keeps one shared instance in its matching component.
    @Singleton
    abstract fun bindWebSocketRepository(
        impl: WebSocketRepositoryImpl
    ): WebSocketRepository
}
