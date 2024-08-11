package tw.com.deathhit.core.travel_taipei_api.protocol

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import tw.com.deathhit.core.travel_taipei_api.protocol.response.GetAttractionsResponse
import tw.com.deathhit.core.travel_taipei_api.protocol.response.GetEventsResponse

internal interface TravelTaipeiRetrofitService {
    @Headers("Accept: application/json")
    @GET("{lang}/Attractions/All")
    suspend fun getAttractions(
        @Path("lang") lang: String,
        @Query("page") page: Int
    ): GetAttractionsResponse

    @Headers("Accept: application/json")
    @GET("{lang}/Events/News")
    suspend fun getEvents(
        @Path("lang") lang: String,
        @Query("page") page: Int
    ): GetEventsResponse
}