package tw.com.deathhit.data.attraction

import tw.com.deathhit.core.app_database.entity.AttractionEntity
import tw.com.deathhit.core.app_database.entity.AttractionImageEntity
import tw.com.deathhit.core.app_database.entity.AttractionImageRemoteOrderEntity
import tw.com.deathhit.core.app_database.entity.AttractionRemoteOrderEntity
import tw.com.deathhit.core.app_database.view.AttractionItemView
import tw.com.deathhit.core.travel_taipei_api.protocol.model.AttractionApiEntity
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
    Language.SPANISH -> tw.com.deathhit.core.app_database.enum_type.Language.SPANISH
    Language.THAI -> tw.com.deathhit.core.app_database.enum_type.Language.THAI
    Language.VIETNAMESE -> tw.com.deathhit.core.app_database.enum_type.Language.VIETNAMESE
    Language.ZH_CN -> tw.com.deathhit.core.app_database.enum_type.Language.ZH_CN
    Language.ZH_TW -> tw.com.deathhit.core.app_database.enum_type.Language.ZH_TW
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

internal fun List<AttractionApiEntity>.toAttractionRemoteItems(language: Language, page: Int, pageSize: Int) =
    mapIndexed { attractionIndex, attractionApiEntity ->
        AttractionRemoteItem(
            attraction = AttractionEntity(
                address = attractionApiEntity.address,
                attractionId = attractionApiEntity.id,
                introduction = attractionApiEntity.introduction,
                language = language.toDatabaseType(),
                name = attractionApiEntity.name,
                updateTimeText = attractionApiEntity.modified,
                websiteUrl = attractionApiEntity.url
            ),
            attractionImages = attractionApiEntity.images.map { attractionImageDto ->
                AttractionImageEntity(
                    attractionId = attractionApiEntity.id,
                    imageUrl = attractionImageDto.src
                )
            },
            attractionImageRemoteOrders = attractionApiEntity.images.mapIndexed { attractionImageIndex, attractionImageDto ->
                AttractionImageRemoteOrderEntity(
                    attractionId = attractionApiEntity.id,
                    imageUrl = attractionImageDto.src,
                    remoteOrder = attractionImageIndex
                )
            },
            attractionRemoteOrder = AttractionRemoteOrderEntity(
                attractionId = attractionApiEntity.id,
                remoteOrder = attractionIndex + (page - 1) * pageSize /*offset*/
            )
        )
    }