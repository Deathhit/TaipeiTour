package tw.com.deathhit.core.travel_taipei_api.protocol.response

data class GetEventsResponse(val data: List<Event>) {
    data class Event(
        val description: String,
        val id: String,
        val modified: String,
        val posted: String,
        val title: String,
        val url: String
    )
}
