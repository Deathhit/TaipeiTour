package tw.com.deathhit.data.attraction

import org.junit.Test
import tw.com.deathhit.core.app_database.view.AttractionItemView
import tw.com.deathhit.core.travel_taipei_api.model.AttractionDto
import tw.com.deathhit.core.travel_taipei_api.model.AttractionImageDto
import tw.com.deathhit.domain.enum_type.Language
import java.util.UUID
import kotlin.random.Random

class MapperExtTest {
    @Test
    fun mapAttractionDtoListToAttractionRemoteItemsList() {
        //Given
        val attractionDtoList = listOf(
            AttractionDto(
                address = getRandomStr(),
                attractionId = getRandomStr(),
                images = listOf(
                    AttractionImageDto(
                        attractionId = getRandomStr(),
                        src = getRandomStr()
                    )
                ),
                introduction = getRandomStr(),
                name = getRandomStr(),
                updateTimeText = getRandomStr(),
                url = getRandomStr()
            ),
            AttractionDto(
                address = getRandomStr(),
                attractionId = getRandomStr(),
                images = listOf(
                    AttractionImageDto(
                        attractionId = getRandomStr(),
                        src = getRandomStr()
                    )
                ),
                introduction = getRandomStr(),
                name = getRandomStr(),
                updateTimeText = getRandomStr(),
                url = getRandomStr()
            ),
            AttractionDto(
                address = getRandomStr(),
                attractionId = getRandomStr(),
                images = listOf(
                    AttractionImageDto(
                        attractionId = getRandomStr(),
                        src = getRandomStr()
                    )
                ),
                introduction = getRandomStr(),
                name = getRandomStr(),
                updateTimeText = getRandomStr(),
                url = getRandomStr()
            ),
        )
        val language = Language.entries.toTypedArray().random()

        val page = getRandomInt()
        val pageSize = getRandomInt()

        //When
        val attractionRemoteItemList = attractionDtoList.toAttractionRemoteItems(
            language = language,
            page = page,
            pageSize = pageSize
        )

        //Then
        val offset = (page - 1) * pageSize

        attractionRemoteItemList.forEachIndexed { attractionIndex, attractionRemoteItem ->
            val attractionDto = attractionDtoList[attractionIndex]

            with(attractionRemoteItem.attraction) {
                assert(address == attractionDto.address)
                assert(attractionId == attractionDto.attractionId)
                assert(introduction == attractionDto.introduction)
                assert(this.language == language.toDatabaseType())
                assert(name == attractionDto.name)
                assert(updateTimeText == attractionDto.updateTimeText)
                assert(websiteUrl == attractionDto.url)
            }

            with(attractionRemoteItem.attractionRemoteOrder) {
                assert(attractionId == attractionDto.attractionId)
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