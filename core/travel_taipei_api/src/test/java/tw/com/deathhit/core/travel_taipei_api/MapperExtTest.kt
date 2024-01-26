package tw.com.deathhit.core.travel_taipei_api

import org.junit.Test
import tw.com.deathhit.core.travel_taipei_api.config.generateGetAttractionsResponse
import tw.com.deathhit.core.travel_taipei_api.model.AttractionDto
import tw.com.deathhit.core.travel_taipei_api.protocol.response.GetAttractionsResponse

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

    private fun GetAttractionsResponse.Attraction.assertEqualsToEntity(dto: AttractionDto) {
        assert(address == dto.address)
        assert(id == dto.attractionId)
        assert(introduction == dto.introduction)
        assert(modified == dto.updateTimeText)
        assert(name == dto.name)
        assert(url == dto.url)
    }
}