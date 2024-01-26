package tw.com.deathhit.data.attraction_image

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import tw.com.deathhit.core.app_database.AppDatabase
import tw.com.deathhit.core.app_database.entity.AttractionImageRemoteOrderEntity
import tw.com.deathhit.data.attraction_image.config.buildAppDatabase
import tw.com.deathhit.data.attraction_image.config.generateAttractionEntity
import tw.com.deathhit.data.attraction_image.config.generateAttractionImageEntities
import tw.com.deathhit.domain.AttractionImageRepository

@OptIn(ExperimentalCoroutinesApi::class)
class AttractionImageRepositoryTest {
    private lateinit var appDatabase: AppDatabase

    private val attractionDao get() = appDatabase.attractionDao()
    private val attractionImageDao get() = appDatabase.attractionImageDao()
    private val attractionImageRemoteOrderDao get() = appDatabase.attractionImageRemoteOrderDao()

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        appDatabase = buildAppDatabase(context)
    }

    @Test
    fun getAttractionImageListFlow() = runTest {
        //Given
        val attraction = generateAttractionEntity()

        val attractionImages =
            generateAttractionImageEntities(attractionId = attraction.attractionId)

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
        val attractionImageRepository: AttractionImageRepository =
            AttractionImageRepositoryImp(appDatabase)

        val result =
            attractionImageRepository.getAttractionImageListFlow(attractionId = attraction.attractionId)
                .first()

        //Then
        result.forEachIndexed { index, attractionImageDO ->
            val attractionImageEntity = attractionImages[index]

            assert(attractionImageDO.attractionId == attractionImageEntity.attractionId)
            assert(attractionImageDO.imageUrl == attractionImageEntity.imageUrl)
        }
    }
}