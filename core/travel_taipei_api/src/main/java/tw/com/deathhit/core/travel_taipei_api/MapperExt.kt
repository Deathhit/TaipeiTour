package tw.com.deathhit.core.travel_taipei_api

import tw.com.deathhit.core.travel_taipei_api.enum_type.Language
import tw.com.deathhit.core.travel_taipei_api.model.AttractionDto
import tw.com.deathhit.core.travel_taipei_api.model.AttractionImageDto
import tw.com.deathhit.core.travel_taipei_api.model.EventDto
import tw.com.deathhit.core.travel_taipei_api.protocol.response.GetAttractionsResponse
import tw.com.deathhit.core.travel_taipei_api.protocol.response.GetEventsResponse

internal fun GetAttractionsResponse.toDtoList(): List<AttractionDto> = data.map { attraction ->
    AttractionDto(
        address = attraction.address,
        attractionId = attraction.id,
        images = attraction.images.map { image ->
            AttractionImageDto(
                attractionId = attraction.id,
                src = image.src
            )
        },
        introduction = attraction.introduction,
        name = attraction.name,
        updateTimeText = attraction.modified,
        url = attraction.url
    )
}

internal fun GetEventsResponse.toDtoList(): List<EventDto> = data.map { event ->
    EventDto(
        description = event.description,
        id = event.id,
        modified = event.modified,
        posted = event.posted,
        title = event.title,
        url = event.url
    )
}

internal fun Language.toValue(): String = when(this) {
    Language.ENGLISH -> "en"
    Language.JAPANESE -> "ja"
    Language.KOREAN -> "ko"
    Language.SPANISH -> "es"
    Language.THAI -> "th"
    Language.VIETNAMESE -> "vi"
    Language.ZH_CN -> "zh-cn"
    Language.ZH_TW -> "zh-tw"
}