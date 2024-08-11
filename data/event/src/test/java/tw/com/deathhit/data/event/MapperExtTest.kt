package tw.com.deathhit.data.event

import org.junit.Test
import tw.com.deathhit.core.app_database.view.EventItemView
import tw.com.deathhit.core.travel_taipei_api.model.EventDto
import tw.com.deathhit.domain.enum_type.Language
import java.util.UUID
import kotlin.random.Random

class MapperExtTest {
    @Test
    fun mapEventDtoListToEventRemoteItemsList() {
        //Given
        val eventDtoList = listOf(
            EventDto(
                description = getRandomStr(),
                id = getRandomStr(),
                modified = getRandomStr(),
                posted = getRandomStr(),
                title = getRandomStr(),
                url = getRandomStr()
            ),
            EventDto(
                description = getRandomStr(),
                id = getRandomStr(),
                modified = getRandomStr(),
                posted = getRandomStr(),
                title = getRandomStr(),
                url = getRandomStr()
            ),
            EventDto(
                description = getRandomStr(),
                id = getRandomStr(),
                modified = getRandomStr(),
                posted = getRandomStr(),
                title = getRandomStr(),
                url = getRandomStr()
            ),
        )
        val language = Language.entries.toTypedArray().random()

        val page = getRandomInt()
        val pageSize = getRandomInt()

        //When
        val eventRemoteItemList = eventDtoList.toEventRemoteItems(
            language = language,
            page = page,
            pageSize = pageSize
        )

        //Then
        val offset = (page - 1) * pageSize

        eventRemoteItemList.forEachIndexed { eventIndex, eventRemoteItem ->
            val eventDto = eventDtoList[eventIndex]

            with(eventRemoteItem.event) {
                assert(description == eventDto.description)
                assert(eventId == eventDto.id)
                assert(postTimeText == eventDto.posted)
                assert(this.language == language.toDatabaseType())
                assert(title == eventDto.title)
                assert(updateTimeText == eventDto.modified)
                assert(websiteUrl == eventDto.url)
            }

            with(eventRemoteItem.eventRemoteOrder) {
                assert(eventId == eventDto.id)
                assert(remoteOrder == eventIndex + offset)
            }
        }
    }

    @Test
    fun mapEventItemViewToEventDO() {
        //Given
        val eventItemView = EventItemView(
            description = getRandomStr(),
            eventId = getRandomStr(),
            language = Language.entries.toTypedArray().random().toDatabaseType(),
            postTimeText = getRandomStr(),
            remoteOrder = getRandomInt(),
            title = getRandomStr(),
            updateTimeText = getRandomStr(),
            websiteUrl = getRandomStr()
        )

        //When
        val eventDO = eventItemView.toDO()

        //Then
        with(eventDO) {
            assert(description == eventItemView.description)
            assert(eventId == eventItemView.eventId)
            assert(postTimeText == eventItemView.postTimeText)
            assert(title == eventItemView.title)
            assert(updateTimeText == eventItemView.updateTimeText)
            assert(websiteUrl == eventItemView.websiteUrl)
        }
    }

    private fun getRandomInt() = Random.nextInt()
    private fun getRandomStr() = UUID.randomUUID().toString()
}