package tw.com.deathhit.core.app_database

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import tw.com.deathhit.core.app_database.config.buildAppDatabase
import tw.com.deathhit.core.app_database.config.generateAttractionEntity
import tw.com.deathhit.core.app_database.config.generateAttractionId
import tw.com.deathhit.core.app_database.config.generateAttractionImageEntities
import tw.com.deathhit.core.app_database.entity.AttractionImageEntity
import tw.com.deathhit.core.app_database.entity.AttractionImageRemoteOrderEntity
import tw.com.deathhit.core.app_database.view.AttractionImageItemView

@OptIn(ExperimentalCoroutinesApi::class)
class AttractionImageItemDaoTest {
    private lateinit var appDatabase: AppDatabase

    private val attractionDao get() = appDatabase.attractionDao()
    private val attractionImageDao get() = appDatabase.attractionImageDao()
    private val attractionImageItemDao get() = appDatabase.attractionImageItemDao()
    private val attractionImageRemoteOrderDao get() = appDatabase.attractionImageRemoteOrderDao()

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        appDatabase = buildAppDatabase(context)
    }

    @Test
    fun getAttractionImageItemsFlow() = runTest {
        //Given
        val attractionId = generateAttractionId()

        val attraction = generateAttractionEntity(attractionId = attractionId)
        val attractionImages = generateAttractionImageEntities(attractionId = attractionId)
        val attractionImageRemoteOrders =
            attractionImages.mapIndexed { index, attractionImageEntity ->
                AttractionImageRemoteOrderEntity(
                    attractionId = attractionImageEntity.attractionId,
                    imageUrl = attractionImageEntity.imageUrl,
                    remoteOrder = index
                )
            }

        attractionDao.upsert(listOf(attraction))

        attractionImageDao.upsert(attractionImages)

        attractionImageRemoteOrderDao.upsert(attractionImageRemoteOrders)

        advanceUntilIdle()

        //When
        val attractionImageItems =
            attractionImageItemDao.getEntitiesListFlow(attractionId = attractionId).first()

        //Then
        attractionImageItems.forEachIndexed { index, attractionImageItem ->
            attractionImageItem.assertEqualsToEntity(attractionImages[index])
        }
    }

    private fun AttractionImageItemView.assertEqualsToEntity(entity: AttractionImageEntity) {
        assert(attractionId == entity.attractionId)
        assert(imageUrl == entity.imageUrl)
    }
}