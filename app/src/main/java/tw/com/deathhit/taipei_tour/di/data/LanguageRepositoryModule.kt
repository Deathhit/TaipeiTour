package tw.com.deathhit.taipei_tour.di.data

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import tw.com.deathhit.data.language.LanguageRepositoryImp
import tw.com.deathhit.domain.LanguageRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LanguageRepositoryModule {
    @Provides
    @Singleton
    fun provideAttractionImageRepository(
        @ApplicationContext context: Context
    ): LanguageRepository = LanguageRepositoryImp(context = context)
}