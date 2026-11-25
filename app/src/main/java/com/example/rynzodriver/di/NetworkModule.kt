package com.example.rynzodriver.di

// Senior explanation:
// Hilt dependency providers and interface bindings.
// Read top-down: imports provide dependencies, declarations define state/contracts, and the function body coordinates them.

// File purpose: This file configures Hilt dependency injection.

// Project imports: shared domain, data, UI, navigation, or utility types from this app.
import com.example.rynzodriver.BuildConfig
import com.example.rynzodriver.data.api.ApiService
import com.example.rynzodriver.data.api.AuthInterceptor
import com.example.rynzodriver.data.api.auth.AuthApiService
import com.example.rynzodriver.data.api.trips.TripApiService
// Serialization imports: converting JSON or structured data to/from Kotlin objects.
import com.google.gson.Gson
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Library imports: supporting types or annotations used by this declaration.
import dagger.Module
import dagger.Provides
// Hilt imports: dependency injection annotations and ViewModel integration.
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
// OkHttp/Okio imports: network transport, request interception, and binary/text data.
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
// Retrofit imports: HTTP endpoint declarations and response handling.
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Library imports: supporting types or annotations used by this declaration.
import javax.inject.Singleton

// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Dependency module: Hilt reads the providers or bindings declared by this class/object.
@Module
// Scope binding: these dependencies are installed into the specified Hilt component lifecycle.
@InstallIn(SingletonComponent::class)
// Singleton object: `NetworkModule` has one process-wide instance; initialization occurs the first time it is accessed.
object NetworkModule {

// Provider method: Hilt calls this method to create the declared dependency type.
    @Provides
// Application lifetime: Hilt/this framework keeps one shared instance in its matching component.
    @Singleton
// Function `provideGson`: normal synchronous execution is used unless the body starts a coroutine itself.
    fun provideGson(): Gson {
// `return` control: execution leaves this function immediately; a labeled return can leave a lambda instead.
        return Gson()
    }

// Provider method: Hilt calls this method to create the declared dependency type.
    @Provides
// Application lifetime: Hilt/this framework keeps one shared instance in its matching component.
    @Singleton
// Function `provideLoggingInterceptor`: normal synchronous execution is used unless the body starts a coroutine itself.
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
// `return` control: execution leaves this function immediately; a labeled return can leave a lambda instead.
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

// Provider method: Hilt calls this method to create the declared dependency type.
    @Provides
// Application lifetime: Hilt/this framework keeps one shared instance in its matching component.
    @Singleton
// Function `provideOkHttpClient`: normal synchronous execution is used unless the body starts a coroutine itself.
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor
    ): OkHttpClient {
// `return` control: execution leaves this function immediately; a labeled return can leave a lambda instead.
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .build()
    }

// Provider method: Hilt calls this method to create the declared dependency type.
    @Provides
// Application lifetime: Hilt/this framework keeps one shared instance in its matching component.
    @Singleton
// Function `provideRetrofit`: normal synchronous execution is used unless the body starts a coroutine itself.
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
// `return` control: execution leaves this function immediately; a labeled return can leave a lambda instead.
        return Retrofit.Builder()
            .baseUrl("http://192.168.0.110:8000/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

// Provider method: Hilt calls this method to create the declared dependency type.
    @Provides
// Application lifetime: Hilt/this framework keeps one shared instance in its matching component.
    @Singleton
// Function `provideApiService`: normal synchronous execution is used unless the body starts a coroutine itself.
    fun provideApiService(retrofit: Retrofit): ApiService {
// `return` control: execution leaves this function immediately; a labeled return can leave a lambda instead.
        return retrofit.create(ApiService::class.java)
    }

// Provider method: Hilt calls this method to create the declared dependency type.
    @Provides
// Application lifetime: Hilt/this framework keeps one shared instance in its matching component.
    @Singleton
// Function `provideAuthApiService`: normal synchronous execution is used unless the body starts a coroutine itself.
    fun provideAuthApiService(retrofit: Retrofit): AuthApiService {
// `return` control: execution leaves this function immediately; a labeled return can leave a lambda instead.
        return retrofit.create(AuthApiService::class.java)
    }

// Provider method: Hilt calls this method to create the declared dependency type.
    @Provides
// Application lifetime: Hilt/this framework keeps one shared instance in its matching component.
    @Singleton
// Function `provideTripApiService`: normal synchronous execution is used unless the body starts a coroutine itself.
    fun provideTripApiService(retrofit: Retrofit): TripApiService {
// `return` control: execution leaves this function immediately; a labeled return can leave a lambda instead.
        return retrofit.create(TripApiService::class.java)
    }
}
