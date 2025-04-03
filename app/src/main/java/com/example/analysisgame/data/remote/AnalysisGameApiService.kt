package com.example.analysisgame.data.remote

import com.example.analysisgame.domain.model.LoremText
import retrofit2.http.GET
import retrofit2.http.Path

interface AnalysisGameApiService {

    @GET("lorem/{num}")
    suspend fun getLorem(@Path("num") num: String): LoremText

    /*@POST("user/createUser/")
    suspend fun registerUser(@Body userRequest: UserRequest): User

    @POST("user/auth/")
    suspend fun loginUser(@Body userRequest: UserRequest): LoginResponse

    @GET("restaurant/list/")
    suspend fun getAllRestaurants(): RestaurantsResponse

    @GET("restaurant/restaurant/{id}")
    suspend fun getRestaurantById(
        @Path("id") id: String
    ): RestaurantResponse

    @GET("table-scheme/scheme/{restaurantId}")
    suspend fun getTableScheme(
        @Path("restaurantId") restaurantId: String
    ) : TableSchemeResponse

    @GET("booking/list")
    suspend fun getAllBookings() : BookingResponse

    @POST("booking/create/")
    suspend fun createBooking(
        @Body bookingRequest: BookingRequest
    ) : BookingIdResponse*/




}