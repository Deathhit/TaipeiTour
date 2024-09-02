package tw.com.deathhit.core.travel_taipei_api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tw.com.deathhit.core.travel_taipei_api.enum_type.Language
import tw.com.deathhit.core.travel_taipei_api.protocol.TravelTaipeiRetrofitService
import tw.com.deathhit.core.travel_taipei_api.protocol.model.AttractionApiEntity
import tw.com.deathhit.core.travel_taipei_api.protocol.model.EventApiEntity

interface TravelTaipeiService {
    val pageSize: Int

    suspend fun getAttractions(language: Language, page: Int): List<AttractionApiEntity>
    suspend fun getEvents(language: Language, page: Int): List<EventApiEntity>

    companion object {
        fun createTravelTaipeiService(baseUrl: String): TravelTaipeiService =
            TravelTaipeiServiceImp(
                createTravelTaipeiRetrofitService(baseUrl = baseUrl)
            )

        private fun createRetrofit(baseUrl: String) = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .build()

        private fun createTravelTaipeiRetrofitService(baseUrl: String) =
            createRetrofit(baseUrl = baseUrl).create(TravelTaipeiRetrofitService::class.java)
    }
}