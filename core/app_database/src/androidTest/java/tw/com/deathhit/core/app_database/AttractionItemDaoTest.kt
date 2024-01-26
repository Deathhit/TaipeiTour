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
import tw.com.deathhit.core.app_database.config.generateAttractionEntities
import tw.com.deathhit.core.app_database.config.generateLanguage
import tw.com.deathhit.core.app_database.entity.AttractionEntity
import tw.com.deathhit.core.app_database.entity.AttractionRemoteOrderEntity
import tw.com.deathhit.core.app_database.view.AttractionItemView

@OptIn(ExperimentalCoroutinesApi::class)
class AttractionItemDaoTest {
    private lateinit var appDatabase: AppDatabase

    private val attractionDao get() = appDatabase.attractionDao()
    private val attractionItemDao get() = appDatabase.attractionItemDao()
    private val attractionRemoteOrderDao get() = appDatabase.attractionRemoteOrderDao()

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        appDatabase = buildAppDatabase(context)
    }

    @Test
    fun getAttractionItemsPagingSource() = runTest {
        //Given
        val language = generateLanguage()

        val attractions = generateAttractionEntities(language = generateLanguage())
        val attractionRemoteOrders =
            attractions.mapIndexed { index, attractionEntity ->
                AttractionRemoteOrderEntity(
                    attractionId = attractionEntity.attractionId,
                    remoteOrder = index
                )
            }

        attractionDao.upsert(attractions)

        attractionRemoteOrderDao.upsert(attractionRemoteOrders)

        advanceUntilIdle()

        //When
        val attractionItems =
            (TestPager(
                PagingConfig(attractions.size),
                attractionItemDao.getEntitiesPagingSource(language = language)
            ).refresh() as PagingSource.LoadResult.Page).data

        //Then
        attractionItems.forEachIndexed { index, attractionImageItem ->
            attractionImageItem.assertEqualsToEntity(attractions[index])
        }
    }

    private fun AttractionItemView.assertEqualsToEntity(entity: AttractionEntity) {
        assert(address == entity.address)
        assert(attractionId == entity.attractionId)
        assert(introduction == entity.introduction)
        assert(language == entity.language)
        assert(name == entity.name)
        assert(updateTimeText == entity.updateTimeText)
        assert(websiteUrl == entity.websiteUrl)
    }
}