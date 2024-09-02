package tw.com.deathhit.data.event

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import tw.com.deathhit.core.app_database.AppDatabase
import tw.com.deathhit.core.app_database.view.EventItemView
import tw.com.deathhit.core.travel_taipei_api.TravelTaipeiService
import tw.com.deathhit.core.travel_taipei_api.enum_type.Language
import tw.com.deathhit.core.travel_taipei_api.protocol.model.AttractionApiEntity
import tw.com.deathhit.core.travel_taipei_api.protocol.model.EventApiEntity
import tw.com.deathhit.data.event.config.buildAppDatabase
import tw.com.deathhit.data.event.config.generateEventApiEntities
import tw.com.deathhit.data.event.config.generateLanguage

@OptIn(ExperimentalPagingApi::class)
class EventRemoteMediatorTest {
    private lateinit var appDatabase: AppDatabase

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        appDatabase = buildAppDatabase(context)
    }

    @Test
    fun refreshLoad_errorOccurs_returnsErrorResult() = runTest {
        //Given
        val language = generateLanguage()

        val pagingState = PagingState<Int, EventItemView>(
            listOf(),
            null,
            PagingConfig(1),
            0
        )
        val travelTaipeiService = object : TravelTaipeiService {
            override val pageSize: Int
                get() = 1

            override suspend fun getAttractions(
                language: Language,
                page: Int
            ): List<AttractionApiEntity> = emptyList()

            override suspend fun getEvents(
                language: Language,
                page: Int
            ): List<EventApiEntity> {
                throw RuntimeException("Test")
            }
        }

        val remoteMediator = EventRemoteMediator(
            appDatabase = appDatabase,
            language = language,
            travelTaipeiService = travelTaipeiService
        )

        //When
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)

        //Then
        Assert.assertTrue(result is RemoteMediator.MediatorResult.Error)
    }

    @Test
    fun refreshLoad_moreDataIsPresent_returnsSuccessResult() = runTest {
        //Given
        val language = generateLanguage()
        val pageSize = 3

        val pagingState = PagingState<Int, EventItemView>(
            listOf(),
            null,
            PagingConfig(1),
            0
        )
        val travelTaipeiService = object : TravelTaipeiService {
            override val pageSize: Int
                get() = pageSize

            override suspend fun getAttractions(
                language: Language,
                page: Int
            ): List<AttractionApiEntity> = emptyList()

            override suspend fun getEvents(
                language: Language,
                page: Int
            ): List<EventApiEntity> = generateEventApiEntities(from = pageSize + 1, until = pageSize + 2)
        }

        val remoteMediator = EventRemoteMediator(
            appDatabase = appDatabase,
            language = language,
            travelTaipeiService = travelTaipeiService
        )

        //When
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)

        //Then
        Assert.assertTrue(result is RemoteMediator.MediatorResult.Success)
        Assert.assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    fun refreshLoad_noMoreData_returnSuccessAndEndOfPagination() = runTest {
        //Given
        val language = generateLanguage()

        val pagingState = PagingState<Int, EventItemView>(
            listOf(),
            null,
            PagingConfig(1),
            0
        )
        val travelTaipeiService = object : TravelTaipeiService {
            override val pageSize: Int
                get() = 1

            override suspend fun getAttractions(
                language: Language,
                page: Int
            ): List<AttractionApiEntity> = emptyList()

            override suspend fun getEvents(
                language: Language,
                page: Int
            ): List<EventApiEntity> = emptyList()
        }

        val remoteMediator = EventRemoteMediator(
            appDatabase = appDatabase,
            language = language,
            travelTaipeiService = travelTaipeiService
        )

        //When
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)

        //Then
        Assert.assertTrue(result is RemoteMediator.MediatorResult.Success)
        Assert.assertTrue((result is RemoteMediator.MediatorResult.Success) && result.endOfPaginationReached)
    }
}