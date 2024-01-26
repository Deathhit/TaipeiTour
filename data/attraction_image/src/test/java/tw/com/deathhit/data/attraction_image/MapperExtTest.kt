package tw.com.deathhit.data.attraction_image

import org.junit.Test
import tw.com.deathhit.core.app_database.view.AttractionImageItemView
import java.util.UUID
import kotlin.random.Random

class MapperExtTest {
    @Test
    fun mapAttractionImageItemToDO() {
        //Given
        val attractionImageItem = AttractionImageItemView(
            attractionId = getRandomStr(),
            imageUrl = getRandomStr(),
            remoteOrder = getRandomInt()
        )

        //When
        val attractionImageDO = attractionImageItem.toDO()

        //Then
        assert(attractionImageDO.attractionId == attractionImageItem.attractionId)
        assert(attractionImageDO.imageUrl == attractionImageItem.imageUrl)
    }

    private fun getRandomInt() = Random.nextInt()
    private fun getRandomStr() = UUID.randomUUID().toString()
}