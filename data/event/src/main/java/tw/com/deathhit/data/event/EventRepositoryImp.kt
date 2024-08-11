package tw.com.deathhit.data.event

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import tw.com.deathhit.core.app_database.AppDatabase
import tw.com.deathhit.core.travel_taipei_api.TravelTaipeiService
import tw.com.deathhit.domain.EventRepository
import tw.com.deathhit.domain.LanguageRepository
import tw.com.deathhit.domain.model.EventDO

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalPagingApi::class)
class EventRepositoryImp(
    private val appDatabase: AppDatabase,
    private val languageRepository: LanguageRepository,
    private val travelTaipeiService: TravelTaipeiService
) : EventRepository {
    private val eventItemDao = appDatabase.eventItemDao()

    override fun getEventFlow(eventId: String): Flow<EventDO?> =
        eventItemDao.getEntity(eventId = eventId).map { it?.toDO() }

    override fun getEventPagingDataFlow(): Flow<PagingData<EventDO>> =
        languageRepository.getSelectedLanguageFlow().flatMapLatest { language ->
            Pager(
                config = PagingConfig(pageSize = travelTaipeiService.pageSize),
                remoteMediator = EventRemoteMediator(
                    appDatabase = appDatabase,
                    language = language,
                    travelTaipeiService = travelTaipeiService
                )
            ) {
                eventItemDao.getEntitiesPagingSource(language.toDatabaseType())
            }.flow.map { pagingData ->
                pagingData.map { it.toDO() }
            }
        }
}