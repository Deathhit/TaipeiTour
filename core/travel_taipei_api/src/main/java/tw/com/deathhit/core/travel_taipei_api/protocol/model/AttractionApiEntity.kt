package tw.com.deathhit.core.travel_taipei_api.protocol.model

data class AttractionApiEntity(
    val address: String,
    val id: String,
    val images: List<ImageApiEntity>,
    val introduction: String,
    val modified: String,
    val name: String,
    val url: String
)
