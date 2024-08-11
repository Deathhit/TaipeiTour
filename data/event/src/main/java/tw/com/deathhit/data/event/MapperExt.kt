package tw.com.deathhit.data.event

import tw.com.deathhit.core.app_database.entity.EventEntity
import tw.com.deathhit.core.app_database.entity.EventRemoteOrderEntity
import tw.com.deathhit.core.app_database.view.EventItemView
import tw.com.deathhit.core.travel_taipei_api.model.EventDto
import tw.com.deathhit.data.event.model.EventRemoteItem
import tw.com.deathhit.domain.enum_type.Language
import tw.com.deathhit.domain.model.EventDO

internal fun EventItemView.toDO() = EventDO(
    description = description,
    eventId = eventId,
    postTimeText = postTimeText,
    title = title,
    updateTimeText = updateTimeText,
    websiteUrl = websiteUrl
)

internal fun List<EventDto>.toEventRemoteItems(language: Language, page: Int, pageSize: Int) =
    mapIndexed { index, dto ->
        EventRemoteItem(
            event = EventEntity(
                description = dto.description,
                eventId = dto.id,
                language = language.toDatabaseType(),
                postTimeText = dto.posted,
                title = dto.title,
                updateTimeText = dto.modified,
                websiteUrl = dto.url
            ),
            eventRemoteOrder = EventRemoteOrderEntity(
                eventId = dto.id,
                remoteOrder = index + (page - 1) * pageSize /*offset*/
            )
        )
    }

internal fun Language.toApiType() = when (this) {
    Language.ENGLISH -> tw.com.deathhit.core.travel_taipei_api.enum_type.Language.ENGLISH
    Language.JAPANESE -> tw.com.deathhit.core.travel_taipei_api.enum_type.Language.JAPANESE
    Language.KOREAN -> tw.com.deathhit.core.travel_taipei_api.enum_type.Language.KOREAN
    Language.SPANISH -> tw.com.deathhit.core.travel_taipei_api.enum_type.Language.SPANISH
    Language.THAI -> tw.com.deathhit.core.travel_taipei_api.enum_type.Language.THAI
    Language.VIETNAMESE -> tw.com.deathhit.core.travel_taipei_api.enum_type.Language.VIETNAMESE
    Language.ZH_CN -> tw.com.deathhit.core.travel_taipei_api.enum_type.Language.ZH_CN
    Language.ZH_TW -> tw.com.deathhit.core.travel_taipei_api.enum_type.Language.ZH_TW
}

internal fun Language.toDatabaseType() = when (this) {
    Language.ENGLISH -> tw.com.deathhit.core.app_database.enum_type.Language.ENGLISH
    Language.JAPANESE -> tw.com.deathhit.core.app_database.enum_type.Language.JAPANESE
    Language.KOREAN -> tw.com.deathhit.core.app_database.enum_type.Language.KOREAN
    Language.SPANISH -> tw.com.deathhit.core.app_database.enum_type.Language.SPANISH
    Language.THAI -> tw.com.deathhit.core.app_database.enum_type.Language.THAI
    Language.VIETNAMESE -> tw.com.deathhit.core.app_database.enum_type.Language.VIETNAMESE
    Language.ZH_CN -> tw.com.deathhit.core.app_database.enum_type.Language.ZH_CN
    Language.ZH_TW -> tw.com.deathhit.core.app_database.enum_type.Language.ZH_TW
}