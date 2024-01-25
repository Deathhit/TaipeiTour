package tw.com.deathhit.taipei_tour.di.data

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tw.com.deathhit.core.app_database.AppDatabase
import tw.com.deathhit.core.travel_taipei_api.TravelTaipeiService
import tw.com.deathhit.data.attraction.AttractionRepositoryImp
import tw.com.deathhit.domain.AttractionRepository
import tw.com.deathhit.domain.LanguageRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AttractionRepositoryModule {
    @Provides
    @Singleton
    fun provideAttractionRepository(
        appDatabase: AppDatabase,
        languageRepository: LanguageRepository,
        travelTaipeiService: TravelTaipeiService
    ): AttractionRepository = AttractionRepositoryImp(
        appDatabase = appDatabase,
        languageRepository = languageRepository,
        travelTaipeiService = travelTaipeiService
    )
}