package tw.com.deathhit.core.app_database

import android.content.Context
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.testing.TestPager
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import tw.com.deathhit.core.app_database.config.buildAppDatabase
import tw.com.deathhit.core.app_database.config.generateEventEntities
import tw.com.deathhit.core.app_database.config.generateLanguage
import tw.com.deathhit.core.app_database.entity.EventEntity
import tw.com.deathhit.core.app_database.entity.EventRemoteOrderEntity
import tw.com.deathhit.core.app_database.view.EventItemView

@OptIn(ExperimentalCoroutinesApi::class)
class EventItemDaoTest {
    private lateinit var appDatabase: AppDatabase

    private val eventDao get() = appDatabase.eventDao()
    private val eventItemDao get() = appDatabase.eventItemDao()
    private val eventRemoteOrderDao get() = appDatabase.eventRemoteOrderDao()

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        appDatabase = buildAppDatabase(context)
    }

    @Test
    fun getEventItemsPagingSource() = runTest {
        //Given
        val language = generateLanguage()

        val events = generateEventEntities(language = generateLanguage())
        val eventRemoteOrders =
            events.mapIndexed { index, entity ->
                EventRemoteOrderEntity(
                    eventId = entity.eventId,
                    remoteOrder = index
                )
            }

        eventDao.upsert(events)

        eventRemoteOrderDao.upsert(eventRemoteOrders)

        advanceUntilIdle()

        //When
        val attractionItems =
            (TestPager(
                PagingConfig(events.size),
                eventItemDao.getEntitiesPagingSource(language = language)
            ).refresh() as PagingSource.LoadResult.Page).data

        //Then
        attractionItems.forEachIndexed { index, item ->
            item.assertEqualsToEntity(events[index])
        }
    }

    private fun EventItemView.assertEqualsToEntity(entity: EventEntity) {
        assert(description == entity.description)
        assert(eventId == entity.eventId)
        assert(language == entity.language)
        assert(postTimeText == entity.postTimeText)
        assert(title == entity.title)
        assert(updateTimeText == entity.updateTimeText)
        assert(websiteUrl == entity.websiteUrl)
    }
}