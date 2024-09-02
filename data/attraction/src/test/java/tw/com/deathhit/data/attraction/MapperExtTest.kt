package tw.com.deathhit.data.attraction

import org.junit.Test
import tw.com.deathhit.core.app_database.view.AttractionItemView
import tw.com.deathhit.core.travel_taipei_api.protocol.model.AttractionApiEntity
import tw.com.deathhit.core.travel_taipei_api.protocol.model.ImageApiEntity
import tw.com.deathhit.domain.enum_type.Language
import java.util.UUID
import kotlin.random.Random

class MapperExtTest {
    @Test
    fun mapAttractionApiEntitiesToAttractionRemoteItemsList() {
        //Given
        val attractionApiEntities = listOf(
            AttractionApiEntity(
                address = getRandomStr(),
                id = getRandomStr(),
                images = listOf(
                    ImageApiEntity(
                        src = getRandomStr()
                    )
                ),
                introduction = getRandomStr(),
                modified = getRandomStr(),
                name = getRandomStr(),
                url = getRandomStr()
            ),
            AttractionApiEntity(
                address = getRandomStr(),
                id = getRandomStr(),
                images = listOf(
                    ImageApiEntity(
                        src = getRandomStr()
                    )
                ),
                introduction = getRandomStr(),
                modified = getRandomStr(),
                name = getRandomStr(),
                url = getRandomStr()
            ),
            AttractionApiEntity(
                address = getRandomStr(),
                id = getRandomStr(),
                images = listOf(
                    ImageApiEntity(
                        src = getRandomStr()
                    )
                ),
                introduction = getRandomStr(),
                modified = getRandomStr(),
                name = getRandomStr(),
                url = getRandomStr()
            ),
        )
        val language = Language.entries.toTypedArray().random()

        val page = getRandomInt()
        val pageSize = getRandomInt()

        //When
        val attractionRemoteItemList = attractionApiEntities.toAttractionRemoteItems(
            language = language,
            page = page,
            pageSize = pageSize
        )

        //Then
        val offset = (page - 1) * pageSize

        attractionRemoteItemList.forEachIndexed { attractionIndex, attractionRemoteItem ->
            val attractionApiEntity = attractionApiEntities[attractionIndex]

            with(attractionRemoteItem.attraction) {
                assert(address == attractionApiEntity.address)
                assert(attractionId == attractionApiEntity.id)
                assert(introduction == attractionApiEntity.introduction)
                assert(this.language == language.toDatabaseType())
                assert(name == attractionApiEntity.name)
                assert(updateTimeText == attractionApiEntity.modified)
                assert(websiteUrl == attractionApiEntity.url)
            }

            with(attractionRemoteItem.attractionRemoteOrder) {
                assert(attractionId == attractionApiEntity.id)
                assert(remoteOrder == attractionIndex + offset)
            }

            attractionRemoteItem.attractionImages.forEach { attractionImageEntity ->
                with(attractionImageEntity) {
                    assert(attractionId == attractionImageEntity.attractionId)
                    assert(imageUrl == attractionImageEntity.imageUrl)
                }
            }

            attractionRemoteItem.attractionImageRemoteOrders.forEachIndexed { attractionImageIndex, attractionImageRemoteOrderEntity ->
                with(attractionImageRemoteOrderEntity) {
                    assert(attractionId == attractionImageRemoteOrderEntity.attractionId)
                    assert(imageUrl == attractionImageRemoteOrderEntity.imageUrl)
                    assert(remoteOrder == attractionImageIndex)
                }
            }
        }
    }

    @Test
    fun mapAttractionItemViewToAttractionDO() {
        //Given
        val attractionItemView = AttractionItemView(
            address = getRandomStr(),
            attractionId = getRandomStr(),
            imageUrl = getRandomStr(),
            introduction = getRandomStr(),
            language = Language.entries.toTypedArray().random().toDatabaseType(),
            name = getRandomStr(),
            remoteOrder = getRandomInt(),
            updateTimeText = getRandomStr(),
            websiteUrl = getRandomStr()
        )

        //When
        val attractionDO = attractionItemView.toDO()

        //Then
        with(attractionDO) {
            assert(address == attractionItemView.address)
            assert(attractionId == attractionItemView.attractionId)
            assert(imageUrl == attractionItemView.imageUrl)
            assert(introduction == attractionItemView.introduction)
            assert(name == attractionItemView.name)
            assert(updateTimeText == attractionItemView.updateTimeText)
            assert(websiteUrl == attractionItemView.websiteUrl)
        }
    }

    private fun getRandomInt() = Random.nextInt()
    private fun getRandomStr() = UUID.randomUUID().toString()
}