package tw.com.deathhit.core.travel_taipei_api.protocol.response

import tw.com.deathhit.core.travel_taipei_api.protocol.model.AttractionApiEntity

internal data class GetAttractionsResponse(
    val data: List<AttractionApiEntity>
)
