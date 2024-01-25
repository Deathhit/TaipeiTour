package tw.com.deathhit.data.attraction.model

import tw.com.deathhit.core.app_database.entity.AttractionEntity
import tw.com.deathhit.core.app_database.entity.AttractionImageEntity
import tw.com.deathhit.core.app_database.entity.AttractionImageRemoteOrderEntity
import tw.com.deathhit.core.app_database.entity.AttractionRemoteOrderEntity

internal data class AttractionRemoteItem(
    val attraction: AttractionEntity,
    val attractionImages: List<AttractionImageEntity>,
    val attractionImageRemoteOrders: List<AttractionImageRemoteOrderEntity>,
    val attractionRemoteOrder: AttractionRemoteOrderEntity
)
