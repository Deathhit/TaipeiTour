package tw.com.deathhit.core.travel_taipei_api

import tw.com.deathhit.core.travel_taipei_api.enum_type.Language
import tw.com.deathhit.core.travel_taipei_api.model.AttractionDto
import tw.com.deathhit.core.travel_taipei_api.protocol.TravelTaipeiRetrofitService

internal class TravelTaipeiServiceImp(private val retrofitService: TravelTaipeiRetrofitService) :
    TravelTaipeiService {
    override val pageSize: Int = 30

    override suspend fun getAttractions(language: Language, page: Int): List<AttractionDto> =
        retrofitService.getAttractions(lang = language.toValue(), page = page).toDtoList()
}