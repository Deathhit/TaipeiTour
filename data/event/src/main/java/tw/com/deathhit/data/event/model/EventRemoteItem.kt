package tw.com.deathhit.data.event.model

import tw.com.deathhit.core.app_database.entity.EventEntity
import tw.com.deathhit.core.app_database.entity.EventRemoteOrderEntity

internal data class EventRemoteItem(
    val event: EventEntity,
    val eventRemoteOrder: EventRemoteOrderEntity
)
