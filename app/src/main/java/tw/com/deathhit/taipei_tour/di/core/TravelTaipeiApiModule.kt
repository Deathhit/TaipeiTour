package tw.com.deathhit.taipei_tour.di.core

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import tw.com.deathhit.core.travel_taipei_api.TravelTaipeiService
import tw.com.deathhit.taipei_tour.getTravelTaipeiBaseUrl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TravelTaipeiApiModule {
    @Provides
    @Singleton
    internal fun provideTravelTaipeiService(@ApplicationContext context: Context) =
        TravelTaipeiService.createTravelTaipeiService(baseUrl = context.getTravelTaipeiBaseUrl())
}