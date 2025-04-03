package com.example.analysisgame.domain.repository

import com.example.analysisgame.domain.model.LoremText

interface AnalysisGameRepository {

    suspend fun getLoremText(num: String): LoremText

    /*suspend fun registerUser(userRequest: UserRequest): User

    suspend fun loginUser(userRequest: UserRequest): LoginResponse

    suspend fun getAllRestaurants(): RestaurantsResponse

    suspend fun getRestaurantById(id: String): RestaurantResponse

    suspend fun getAllBookings(): BookingResponse

    suspend fun getTableScheme(restaurantId: String): TableSchemeResponse

    suspend fun createBooking(bookingRequest: BookingRequest): BookingIdResponse*/

}