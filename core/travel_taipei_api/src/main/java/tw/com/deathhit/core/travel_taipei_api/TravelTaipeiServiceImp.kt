package tw.com.deathhit.core.travel_taipei_api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tw.com.deathhit.core.travel_taipei_api.enum_type.Language
import tw.com.deathhit.core.travel_taipei_api.protocol.TravelTaipeiRetrofitService
import tw.com.deathhit.core.travel_taipei_api.protocol.model.AttractionApiEntity
import tw.com.deathhit.core.travel_taipei_api.protocol.model.EventApiEntity

class TravelTaipeiServiceImp(serverUrl: String) : TravelTaipeiService {
    private val retrofitService: TravelTaipeiRetrofitService =
        createTravelTaipeiRetrofitService(serverUrl = serverUrl)

    override val pageSize: Int = 128

    override suspend fun getAttractions(language: Language, page: Int): List<AttractionApiEntity> =
        retrofitService.getAttractions(lang = language.toValue(), page = page).data

    override suspend fun getEvents(language: Language, page: Int): List<EventApiEntity> =
        retrofitService.getEvents(lang = language.toValue(), page = page).data

    private fun createRetrofit(serverUrl: String) = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(serverUrl)
        .build()

    private fun createTravelTaipeiRetrofitService(serverUrl: String) =
        createRetrofit(serverUrl = serverUrl).create(TravelTaipeiRetrofitService::class.java)
}