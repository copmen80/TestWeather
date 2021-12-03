package ua.devserhii.testapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import ua.devserhii.testapp.feature.data.remote.TemperatureService

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideAuthService(retrofit: Retrofit): TemperatureService =
        retrofit.create(TemperatureService::class.java)

}