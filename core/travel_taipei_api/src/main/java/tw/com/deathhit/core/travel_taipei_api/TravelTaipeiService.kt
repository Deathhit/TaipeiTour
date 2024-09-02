package tw.com.deathhit.core.travel_taipei_api

import tw.com.deathhit.core.travel_taipei_api.enum_type.Language
import tw.com.deathhit.core.travel_taipei_api.protocol.model.AttractionApiEntity
import tw.com.deathhit.core.travel_taipei_api.protocol.model.EventApiEntity

interface TravelTaipeiService {
    val pageSize: Int

    suspend fun getAttractions(language: Language, page: Int): List<AttractionApiEntity>
    suspend fun getEvents(language: Language, page: Int): List<EventApiEntity>
}