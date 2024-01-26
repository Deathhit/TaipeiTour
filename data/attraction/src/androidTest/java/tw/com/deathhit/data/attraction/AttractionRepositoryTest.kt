package tw.com.deathhit.data.attraction

import android.content.Context
import androidx.paging.testing.asSnapshot
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import tw.com.deathhit.core.app_database.AppDatabase
import tw.com.deathhit.core.app_database.entity.AttractionRemoteOrderEntity
import tw.com.deathhit.core.travel_taipei_api.TravelTaipeiService
import tw.com.deathhit.core.travel_taipei_api.model.AttractionDto
import tw.com.deathhit.data.attraction.config.buildAppDatabase
import tw.com.deathhit.data.attraction.config.generateAttractionEntities
import tw.com.deathhit.data.attraction.config.generateLanguage
import tw.com.deathhit.domain.AttractionRepository
import tw.com.deathhit.domain.LanguageRepository
import tw.com.deathhit.domain.enum_type.Language
import tw.com.deathhit.domain.model.LanguageDO

@OptIn(ExperimentalCoroutinesApi::class)
class AttractionRepositoryTest {
    private lateinit var appDatabase: AppDatabase

    private val attractionDao get() = appDatabase.attractionDao()
    private val attractionRemoteOrderDao get() = appDatabase.attractionRemoteOrderDao()

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        appDatabase = buildAppDatabase(context)
    }

    @Test
    fun getAttractionFlow() = runTest {
        //Given
        val language = generateLanguage()

        val attractionEntities = generateAttractionEntities(language = language)
        val attractionRepository: AttractionRepository = AttractionRepositoryImp(
            appDatabase = appDatabase,
            languageRepository = object : LanguageRepository {
                override fun getLanguageListFlow(): Flow<List<LanguageDO>> = emptyFlow()

                override fun getSelectedLanguageFlow(): Flow<Language> = flowOf(language)

                override suspend fun setLanguage(language: Language) {

                }
            },
            travelTaipeiService = object : TravelTaipeiService {
                override val pageSize: Int
                    get() = 30

                override suspend fun getAttractions(
                    language: tw.com.deathhit.core.travel_taipei_api.enum_type.Language,
                    page: Int
                ): List<AttractionDto> = emptyList()
            }
        )

        val attractionEntity = attractionEntities.random()

        attractionDao.upsert(attractionEntities)
        advanceUntilIdle()

        //When
        val attractionDO =
            attractionRepository.getAttractionFlow(attractionEntity.attractionId).first()!!

        //Then
        assert(attractionDO.address == attractionEntity.address)
        assert(attractionDO.attractionId == attractionEntity.attractionId)
        assert(attractionDO.introduction == attractionEntity.introduction)
        assert(attractionDO.name == attractionEntity.name)
        assert(attractionDO.updateTimeText == attractionEntity.updateTimeText)
        assert(attractionDO.websiteUrl == attractionEntity.websiteUrl)
    }

    @Test
    fun getAttractionPagingDataFlow() = runTest {
        //Given
        val language = generateLanguage()

        val attractionRepository: AttractionRepository = AttractionRepositoryImp(
            appDatabase = appDatabase,
            languageRepository = object : LanguageRepository {
                override fun getLanguageListFlow(): Flow<List<LanguageDO>> = emptyFlow()

                override fun getSelectedLanguageFlow(): Flow<Language> = flowOf(language)

                override suspend fun setLanguage(language: Language) {

                }
            },
            travelTaipeiService = object : TravelTaipeiService {
                override val pageSize: Int
                    get() = 30

                override suspend fun getAttractions(
                    language: tw.com.deathhit.core.travel_taipei_api.enum_type.Language,
                    page: Int
                ): List<AttractionDto> = emptyList()
            }
        )

        val attractionEntities = generateAttractionEntities(language = language)
        val attractionRemoteOrderEntities =
            attractionEntities.mapIndexed { index, attractionEntity ->
                AttractionRemoteOrderEntity(
                    attractionId = attractionEntity.attractionId,
                    remoteOrder = index
                )
            }

        attractionDao.upsert(attractionEntities)
        attractionRemoteOrderDao.upsert(attractionRemoteOrderEntities)
        advanceUntilIdle()

        //When
        val attractions = attractionRepository.getAttractionPagingDataFlow()
        val attractionsSnapshot = attractions.asSnapshot {
            scrollTo(attractionEntities.size)
        }

        //Then
        attractionsSnapshot.forEachIndexed { index, attractionDO ->
            val attractionEntity = attractionEntities[index]

            assert(attractionDO.address == attractionEntity.address)
            assert(attractionDO.attractionId == attractionEntity.attractionId)
            assert(attractionDO.introduction == attractionEntity.introduction)
            assert(attractionDO.name == attractionEntity.name)
            assert(attractionDO.updateTimeText == attractionEntity.updateTimeText)
            assert(attractionDO.websiteUrl == attractionEntity.websiteUrl)
        }
    }
}