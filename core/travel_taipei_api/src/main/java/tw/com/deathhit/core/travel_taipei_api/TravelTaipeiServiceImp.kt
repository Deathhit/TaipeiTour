package tw.com.deathhit.core.travel_taipei_api

import tw.com.deathhit.core.travel_taipei_api.enum_type.Language
import tw.com.deathhit.core.travel_taipei_api.protocol.TravelTaipeiRetrofitService
import tw.com.deathhit.core.travel_taipei_api.protocol.model.AttractionApiEntity
import tw.com.deathhit.core.travel_taipei_api.protocol.model.EventApiEntity

internal class TravelTaipeiServiceImp(private val retrofitService: TravelTaipeiRetrofitService) :
    TravelTaipeiService {
    override val pageSize: Int = 30

    override suspend fun getAttractions(language: Language, page: Int): List<AttractionApiEntity> =
        retrofitService.getAttractions(lang = language.toValue(), page = page).data

    override suspend fun getEvents(language: Language, page: Int): List<EventApiEntity> =
        retrofitService.getEvents(lang = language.toValue(), page = page).data
}