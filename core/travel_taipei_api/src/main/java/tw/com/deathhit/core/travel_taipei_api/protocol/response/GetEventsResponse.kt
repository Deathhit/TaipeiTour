package tw.com.deathhit.core.travel_taipei_api.protocol.response

import tw.com.deathhit.core.travel_taipei_api.protocol.model.EventApiEntity

data class GetEventsResponse(val data: List<EventApiEntity>)