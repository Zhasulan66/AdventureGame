package com.example.analysisgame.di

import android.content.Context
import com.example.analysisgame.data.remote.AnalysisGameApiService
import com.example.analysisgame.data.repository.AnalysisGameRepositoryImpl
import com.example.analysisgame.domain.repository.AnalysisGameRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AnalysisGameModule {

    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideAnalysisGameApiService(): AnalysisGameApiService {

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl("http://192.168.1.3:8080/")    //https://goplacekz.onrender.com/api/
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(AnalysisGameApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAnalysisGameRepository(
        analysisGameRepository: AnalysisGameApiService
    ) : AnalysisGameRepository {
        return AnalysisGameRepositoryImpl(analysisGameRepository)
    }
}