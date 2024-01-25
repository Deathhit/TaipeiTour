package tw.com.deathhit.core.travel_taipei_api.model

data class AttractionDto(
    val address: String,
    val attractionId: String,
    val images: List<AttractionImageDto>,
    val introduction: String,
    val name: String,
    val updateTimeText: String,
    val url: String
)
