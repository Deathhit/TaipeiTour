package tw.com.deathhit.core.travel_taipei_api

import tw.com.deathhit.core.travel_taipei_api.enum_type.Language
import tw.com.deathhit.core.travel_taipei_api.model.AttractionDto
import tw.com.deathhit.core.travel_taipei_api.model.AttractionImageDto
import tw.com.deathhit.core.travel_taipei_api.protocol.response.GetAttractionsResponse

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

internal fun Language.toValue(): String = when(this) {
    Language.ENGLISH -> "en"
    Language.JAPANESE -> "ja"
    Language.KOREAN -> "ko"
    Language.ZH_CN -> "zh-cn"
    Language.ZH_TW -> "zh-tw"
}