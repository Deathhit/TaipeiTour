package tw.com.deathhit.domain.model

data class AttractionDO(
    val address: String,
    val attractionId: String,
    val imageUrl: String?,
    val introduction: String,
    val name: String,
    val updateTimeText: String,
    val websiteUrl: String
)
