package tw.com.deathhit.core.travel_taipei_api.protocol.response

internal data class GetAttractionsResponse(
    val data: List<Attraction>
) {
    data class Attraction(
        val address: String,
        val id: String,
        val images: List<Image>,
        val introduction: String,
        val modified: String,
        val name: String,
        val url: String
    ) {
        data class Image(val src: String)
    }
}
