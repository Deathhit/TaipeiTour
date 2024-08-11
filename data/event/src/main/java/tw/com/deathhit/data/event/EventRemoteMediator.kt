package tw.com.deathhit.data.event

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import tw.com.deathhit.core.app_database.AppDatabase
import tw.com.deathhit.core.app_database.entity.EventRemoteKeysEntity
import tw.com.deathhit.core.app_database.view.EventItemView
import tw.com.deathhit.core.travel_taipei_api.TravelTaipeiService
import tw.com.deathhit.data.event.model.EventRemoteItem
import tw.com.deathhit.domain.enum_type.Language

@OptIn(ExperimentalPagingApi::class)
internal class EventRemoteMediator(
    private val appDatabase: AppDatabase,
    private val language: Language,
    private val travelTaipeiService: TravelTaipeiService
) : RemoteMediator<Int, EventItemView>() {
    private val eventDao = appDatabase.eventDao()
    private val eventRemoteKeysDao = appDatabase.eventRemoteKeysDao()
    private val eventRemoteOrderDao = appDatabase.eventRemoteOrderDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, EventItemView>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeysClosestToCurrentPosition(state)

                    remoteKeys?.nextKey?.minus(1) ?: FIRST_PAGE
                }

                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeysForFirstItem(state)
                    // If remoteKey is null, that means the refresh result is not in the database yet.
                    // We can return Success with 'endOfPaginationReached = false' because Paging
                    // will call this method again if RemoteKeys becomes non-null.
                    // If remoteKeys is NOT NULL but its previousKey is null, that means we've reached
                    // the end of pagination for prepend.
                    val previousKey = remoteKeys?.previousKey
                        ?: return MediatorResult.Success(remoteKeys != null)

                    previousKey
                }

                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeysForLastItem(state)
                    // If remoteKey is null, that means the refresh result is not in the database yet.
                    // We can return Success with 'endOfPaginationReached = false' because Paging
                    // will call this method again if RemoteKeys becomes non-null.
                    // If remoteKeys is NOT NULL but its nextKey is null, that means we've reached
                    // the end of pagination for append.
                    val nextKey =
                        remoteKeys?.nextKey ?: return MediatorResult.Success(
                            remoteKeys != null
                        )

                    nextKey
                }
            }

            // Suspending network load via Retrofit. This doesn't need to
            // be wrapped in a withContext(Dispatcher.IO) { ... } block
            // since Retrofit's Coroutine CallAdapter dispatches on a
            // worker thread.
            val itemList = getRemoteItems(page = loadKey, pageSize = state.config.pageSize)

            saveRemoteItems(itemList = itemList, loadKey = loadKey, loadType = loadType)

            MediatorResult.Success(endOfPaginationReached = itemList.isEmpty())
        } catch (e: Throwable) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteItems(page: Int, pageSize: Int): List<EventRemoteItem> =
        travelTaipeiService.getEvents(language = language.toApiType(), page = page)
            .toEventRemoteItems(language = language, page = page, pageSize = pageSize)

    private suspend fun getRemoteKeysClosestToCurrentPosition(
        state: PagingState<Int, EventItemView>
    ) = with(state) {
        anchorPosition?.let { closestItemToPosition(it) }
            ?.let {
                eventRemoteKeysDao.get(eventId = it.eventId)
            }
    }

    private suspend fun getRemoteKeysForFirstItem(
        state: PagingState<Int, EventItemView>
    ) = with(state) {
        firstItemOrNull()?.let {
            eventRemoteKeysDao.get(eventId = it.eventId)
        }
    }

    private suspend fun getRemoteKeysForLastItem(
        state: PagingState<Int, EventItemView>
    ) = with(state) {
        lastItemOrNull()?.let {
            eventRemoteKeysDao.get(eventId = it.eventId)
        }
    }

    private suspend fun saveRemoteItems(
        itemList: List<EventRemoteItem>,
        loadKey: Int,
        loadType: LoadType
    ) {
        val nextKey = loadKey + 1
        val previousKey = if (loadKey == FIRST_PAGE) null else loadKey - 1

        appDatabase.withTransaction {
            if (loadType == LoadType.REFRESH)
                eventDao.clearTable()

            //Upsert the master event entities.
            eventDao.upsert(entities = itemList.map { it.event })

            eventRemoteKeysDao.upsert(entities = itemList.map {
                EventRemoteKeysEntity(
                    eventId = it.event.eventId,
                    nextKey = nextKey,
                    previousKey = previousKey
                )
            })
            eventRemoteOrderDao.upsert(entities = itemList.map { it.eventRemoteOrder })
        }
    }

    companion object {
        private const val FIRST_PAGE = 1
    }
}