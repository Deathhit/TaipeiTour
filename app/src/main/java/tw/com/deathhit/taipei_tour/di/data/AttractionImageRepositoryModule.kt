package tw.com.deathhit.taipei_tour.di.data

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tw.com.deathhit.core.app_database.AppDatabase
import tw.com.deathhit.data.attraction_image.AttractionImageRepositoryImp
import tw.com.deathhit.domain.AttractionImageRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AttractionImageRepositoryModule {
    @Provides
    @Singleton
    fun provideAttractionImageRepository(
        appDatabase: AppDatabase,
    ): AttractionImageRepository = AttractionImageRepositoryImp(
        appDatabase = appDatabase
    )
}