package tw.com.deathhit.data.attraction

import tw.com.deathhit.core.app_database.entity.AttractionEntity
import tw.com.deathhit.core.app_database.entity.AttractionImageEntity
import tw.com.deathhit.core.app_database.entity.AttractionImageRemoteOrderEntity
import tw.com.deathhit.core.app_database.entity.AttractionRemoteOrderEntity
import tw.com.deathhit.core.app_database.view.AttractionItemView
import tw.com.deathhit.core.travel_taipei_api.model.AttractionDto
import tw.com.deathhit.data.attraction.model.AttractionRemoteItem
import tw.com.deathhit.domain.enum_type.Language
import tw.com.deathhit.domain.model.AttractionDO

internal fun AttractionItemView.toDO() = AttractionDO(
    address = address,
    attractionId = attractionId,
    imageUrl = imageUrl,
    introduction = introduction,
    name = name,
    updateTimeText = updateTimeText,
    websiteUrl = websiteUrl
)

internal fun Language.toDatabaseType() = when(this) {
    Language.ENGLISH -> tw.com.deathhit.core.app_database.enum_type.Language.ENGLISH
    Language.JAPANESE -> tw.com.deathhit.core.app_database.enum_type.Language.JAPANESE
    Language.KOREAN -> tw.com.deathhit.core.app_database.enum_type.Language.KOREAN
    Language.ZH_CN -> tw.com.deathhit.core.app_database.enum_type.Language.ZH_CN
    Language.ZH_TW -> tw.com.deathhit.core.app_database.enum_type.Language.ZH_TW
}

internal fun Language.toApiType() = when (this) {
    Language.ENGLISH -> tw.com.deathhit.core.travel_taipei_api.enum_type.Language.ENGLISH
    Language.JAPANESE -> tw.com.deathhit.core.travel_taipei_api.enum_type.Language.JAPANESE
    Language.KOREAN -> tw.com.deathhit.core.travel_taipei_api.enum_type.Language.KOREAN
    Language.ZH_CN -> tw.com.deathhit.core.travel_taipei_api.enum_type.Language.ZH_CN
    Language.ZH_TW -> tw.com.deathhit.core.travel_taipei_api.enum_type.Language.ZH_TW
}

internal fun List<AttractionDto>.toAttractionRemoteItems(language: Language, page: Int, pageSize: Int) =
    mapIndexed { attractionIndex, attractionDto ->
        AttractionRemoteItem(
            attraction = AttractionEntity(
                address = attractionDto.address,
                attractionId = attractionDto.attractionId,
                introduction = attractionDto.introduction,
                language = language.toDatabaseType(),
                name = attractionDto.name,
                updateTimeText = attractionDto.updateTimeText,
                websiteUrl = attractionDto.url
            ),
            attractionImages = attractionDto.images.map { attractionImageDto ->
                AttractionImageEntity(
                    attractionId = attractionDto.attractionId,
                    imageUrl = attractionImageDto.src
                )
            },
            attractionImageRemoteOrders = attractionDto.images.mapIndexed { attractionImageIndex, attractionImageDto ->
                AttractionImageRemoteOrderEntity(
                    attractionId = attractionDto.attractionId,
                    imageUrl = attractionImageDto.src,
                    remoteOrder = attractionImageIndex
                )
            },
            attractionRemoteOrder = AttractionRemoteOrderEntity(
                attractionId = attractionDto.attractionId,
                remoteOrder = attractionIndex + (page - 1) * pageSize /*offset*/
            )
        )
    }