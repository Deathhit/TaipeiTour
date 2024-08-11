package tw.com.deathhit.domain

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import tw.com.deathhit.domain.model.EventDO

interface EventRepository {
    fun getEventFlow(eventId: String): Flow<EventDO?>
    fun getEventPagingDataFlow(): Flow<PagingData<EventDO>>
}