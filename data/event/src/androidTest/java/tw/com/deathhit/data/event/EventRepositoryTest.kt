package tw.com.deathhit.data.event

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
import tw.com.deathhit.core.app_database.entity.EventRemoteOrderEntity
import tw.com.deathhit.core.travel_taipei_api.TravelTaipeiService
import tw.com.deathhit.core.travel_taipei_api.protocol.model.AttractionApiEntity
import tw.com.deathhit.core.travel_taipei_api.protocol.model.EventApiEntity
import tw.com.deathhit.data.event.config.buildAppDatabase
import tw.com.deathhit.data.event.config.generateEventEntities
import tw.com.deathhit.data.event.config.generateLanguage
import tw.com.deathhit.domain.EventRepository
import tw.com.deathhit.domain.LanguageRepository
import tw.com.deathhit.domain.enum_type.Language
import tw.com.deathhit.domain.model.LanguageDO

@OptIn(ExperimentalCoroutinesApi::class)
class EventRepositoryTest {
    private lateinit var appDatabase: AppDatabase

    private val eventDao get() = appDatabase.eventDao()
    private val eventRemoteOrderDao get() = appDatabase.eventRemoteOrderDao()

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        appDatabase = buildAppDatabase(context)
    }

    @Test
    fun getEventFlow() = runTest {
        //Given
        val language = generateLanguage()

        val eventEntities = generateEventEntities(language = language)
        val eventRepository: EventRepository = EventRepositoryImp(
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
                ): List<AttractionApiEntity> = emptyList()

                override suspend fun getEvents(
                    language: tw.com.deathhit.core.travel_taipei_api.enum_type.Language,
                    page: Int
                ): List<EventApiEntity> = emptyList()
            }
        )

        val eventEntity = eventEntities.random()

        eventDao.upsert(eventEntities)
        advanceUntilIdle()

        //When
        val eventDO =
            eventRepository.getEventFlow(eventEntity.eventId).first()!!

        //Then
        assert(eventDO.description == eventEntity.description)
        assert(eventDO.eventId == eventEntity.eventId)
        assert(eventDO.postTimeText == eventEntity.postTimeText)
        assert(eventDO.title == eventEntity.title)
        assert(eventDO.updateTimeText == eventEntity.updateTimeText)
        assert(eventDO.websiteUrl == eventEntity.websiteUrl)
    }

    @Test
    fun getEventPagingDataFlow() = runTest {
        //Given
        val language = generateLanguage()

        val eventRepository: EventRepository = EventRepositoryImp(
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
                ): List<AttractionApiEntity> = emptyList()

                override suspend fun getEvents(
                    language: tw.com.deathhit.core.travel_taipei_api.enum_type.Language,
                    page: Int
                ): List<EventApiEntity> = emptyList()
            }
        )

        val eventEntities = generateEventEntities(language = language)
        val eventRemoteOrderEntities =
            eventEntities.mapIndexed { index, eventEntity ->
                EventRemoteOrderEntity(
                    eventId = eventEntity.eventId,
                    remoteOrder = index
                )
            }

        eventDao.upsert(eventEntities)
        eventRemoteOrderDao.upsert(eventRemoteOrderEntities)
        advanceUntilIdle()

        //When
        val events = eventRepository.getEventPagingDataFlow()
        val eventsSnapshot = events.asSnapshot {
            scrollTo(eventEntities.size)
        }

        //Then
        eventsSnapshot.forEachIndexed { index, eventDO ->
            val eventEntity = eventEntities[index]

            assert(eventDO.description == eventEntity.description)
            assert(eventDO.eventId == eventEntity.eventId)
            assert(eventDO.postTimeText == eventEntity.postTimeText)
            assert(eventDO.title == eventEntity.title)
            assert(eventDO.updateTimeText == eventEntity.updateTimeText)
            assert(eventDO.websiteUrl == eventEntity.websiteUrl)
        }
    }
}