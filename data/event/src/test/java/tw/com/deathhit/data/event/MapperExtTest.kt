package tw.com.deathhit.data.event

import org.junit.Test
import tw.com.deathhit.core.app_database.view.EventItemView
import tw.com.deathhit.core.travel_taipei_api.protocol.model.EventApiEntity
import tw.com.deathhit.domain.enum_type.Language
import java.util.UUID
import kotlin.random.Random

class MapperExtTest {
    @Test
    fun mapEventApiEntityListToEventRemoteItemsList() {
        //Given
        val eventApiEntities = listOf(
            EventApiEntity(
                description = getRandomStr(),
                id = getRandomStr(),
                modified = getRandomStr(),
                posted = getRandomStr(),
                title = getRandomStr(),
                url = getRandomStr()
            ),
            EventApiEntity(
                description = getRandomStr(),
                id = getRandomStr(),
                modified = getRandomStr(),
                posted = getRandomStr(),
                title = getRandomStr(),
                url = getRandomStr()
            ),
            EventApiEntity(
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
        val eventRemoteItemList = eventApiEntities.toEventRemoteItems(
            language = language,
            page = page,
            pageSize = pageSize
        )

        //Then
        val offset = (page - 1) * pageSize

        eventRemoteItemList.forEachIndexed { eventIndex, eventRemoteItem ->
            val eventApiEntity = eventApiEntities[eventIndex]

            with(eventRemoteItem.event) {
                assert(description == eventApiEntity.description)
                assert(eventId == eventApiEntity.id)
                assert(postTimeText == eventApiEntity.posted)
                assert(this.language == language.toDatabaseType())
                assert(title == eventApiEntity.title)
                assert(updateTimeText == eventApiEntity.modified)
                assert(websiteUrl == eventApiEntity.url)
            }

            with(eventRemoteItem.eventRemoteOrder) {
                assert(eventId == eventApiEntity.id)
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