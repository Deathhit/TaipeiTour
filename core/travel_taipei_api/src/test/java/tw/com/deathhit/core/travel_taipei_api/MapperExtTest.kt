package tw.com.deathhit.core.travel_taipei_api

import org.junit.Test
import tw.com.deathhit.core.travel_taipei_api.config.generateGetAttractionsResponse
import tw.com.deathhit.core.travel_taipei_api.config.generateGetEventsResponse
import tw.com.deathhit.core.travel_taipei_api.model.AttractionDto
import tw.com.deathhit.core.travel_taipei_api.model.EventDto
import tw.com.deathhit.core.travel_taipei_api.protocol.response.GetAttractionsResponse
import tw.com.deathhit.core.travel_taipei_api.protocol.response.GetEventsResponse

class MapperExtTest {
    @Test
    fun mapGetAttractionsResponseToAttractions() {
        //Given
        val getAttractionsResponse = generateGetAttractionsResponse()

        //When
        val attractionDtoList = getAttractionsResponse.toDtoList()

        //Then
        getAttractionsResponse.data.forEachIndexed { index, attraction ->
            attraction.assertEqualsToEntity(attractionDtoList[index])
        }
    }

    @Test
    fun mapGetEventsResponseToEvents() {
        //Given
        val getEventsResponse = generateGetEventsResponse()

        //When
        val eventsDtoList = getEventsResponse.toDtoList()

        //Then
        getEventsResponse.data.forEachIndexed{ index, event ->
            event.assertEqualsToEntity(eventsDtoList[index])
        }
    }

    private fun GetAttractionsResponse.Attraction.assertEqualsToEntity(dto: AttractionDto) {
        assert(address == dto.address)
        assert(id == dto.attractionId)
        assert(introduction == dto.introduction)
        assert(modified == dto.updateTimeText)
        assert(name == dto.name)
        assert(url == dto.url)
    }

    private fun GetEventsResponse.Event.assertEqualsToEntity(dto: EventDto) {
        assert(description == dto.description)
        assert(id == dto.id)
        assert(modified == dto.modified)
        assert(posted == dto.posted)
        assert(title == title)
        assert(url == dto.url)
    }
}