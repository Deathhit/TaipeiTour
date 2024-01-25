package tw.com.deathhit.data.attraction_image

import tw.com.deathhit.core.app_database.view.AttractionImageItemView
import tw.com.deathhit.domain.model.AttractionImageDO

internal fun AttractionImageItemView.toDO() = AttractionImageDO(
    attractionId = attractionId,
    imageUrl = imageUrl
)