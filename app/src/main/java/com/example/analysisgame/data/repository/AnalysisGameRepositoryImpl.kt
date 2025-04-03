package com.example.analysisgame.data.repository

import com.example.analysisgame.data.remote.AnalysisGameApiService
import com.example.analysisgame.domain.model.LoremText
import com.example.analysisgame.domain.repository.AnalysisGameRepository

class AnalysisGameRepositoryImpl(
    private val api: AnalysisGameApiService
) : AnalysisGameRepository {

    override suspend fun getLoremText(num: String): LoremText {
        return api.getLorem(num)
    }
    /*override suspend fun registerUser(userRequest: UserRequest): User {
        return api.registerUser(userRequest)
    }

    override suspend fun loginUser(userRequest: UserRequest): LoginResponse {
        return api.loginUser(userRequest)
    }

    override suspend fun getAllRestaurants(): RestaurantsResponse {
        return api.getAllRestaurants()
    }

    override suspend fun getRestaurantById(id: String): RestaurantResponse {
        return api.getRestaurantById(id)
    }

    override suspend fun getAllBookings(): BookingResponse {
        return api.getAllBookings()
    }

    override suspend fun getTableScheme(restaurantId: String): TableSchemeResponse {
        return api.getTableScheme(restaurantId)
    }

    override suspend fun createBooking(bookingRequest: BookingRequest): BookingIdResponse {
        return api.createBooking(bookingRequest)
    }*/

}