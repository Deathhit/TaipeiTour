package tw.com.deathhit.core.travel_taipei_api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tw.com.deathhit.core.travel_taipei_api.enum_type.Language
import tw.com.deathhit.core.travel_taipei_api.model.AttractionDto
import tw.com.deathhit.core.travel_taipei_api.protocol.TravelTaipeiRetrofitService

interface TravelTaipeiService {
    val pageSize: Int

    suspend fun getAttractions(language: Language, page: Int): List<AttractionDto>

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