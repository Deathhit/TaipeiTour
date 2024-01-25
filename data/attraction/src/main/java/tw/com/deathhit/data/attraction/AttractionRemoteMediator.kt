package tw.com.deathhit.data.attraction

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import tw.com.deathhit.core.app_database.AppDatabase
import tw.com.deathhit.core.app_database.entity.AttractionRemoteKeysEntity
import tw.com.deathhit.core.app_database.view.AttractionItemView
import tw.com.deathhit.core.travel_taipei_api.TravelTaipeiService
import tw.com.deathhit.data.attraction.model.AttractionRemoteItem
import tw.com.deathhit.domain.enum_type.Language

@OptIn(ExperimentalPagingApi::class)
internal class AttractionRemoteMediator(
    private val appDatabase: AppDatabase,
    private val language: Language,
    private val travelTaipeiService: TravelTaipeiService
) : RemoteMediator<Int, AttractionItemView>() {
    private val attractionDao = appDatabase.attractionDao()
    private val attractionImageDao = appDatabase.attractionImageDao()
    private val attractionImageRemoteOrderDao = appDatabase.attractionImageRemoteOrderDao()
    private val attractionRemoteKeysDao = appDatabase.attractionRemoteKeysDao()
    private val attractionRemoteOrderDao = appDatabase.attractionRemoteOrderDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, AttractionItemView>
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

    private suspend fun getRemoteItems(page: Int, pageSize: Int): List<AttractionRemoteItem> =
        travelTaipeiService.getAttractions(language = language.toApiType(), page = page)
            .toAttractionRemoteItems(language = language, page = page, pageSize = pageSize)

    private suspend fun getRemoteKeysClosestToCurrentPosition(
        state: PagingState<Int, AttractionItemView>
    ) = with(state) {
        anchorPosition?.let { closestItemToPosition(it) }
            ?.let {
                attractionRemoteKeysDao.get(attractionId = it.attractionId)
            }
    }

    private suspend fun getRemoteKeysForFirstItem(
        state: PagingState<Int, AttractionItemView>
    ) = with(state) {
        firstItemOrNull()?.let {
            attractionRemoteKeysDao.get(attractionId = it.attractionId)
        }
    }

    private suspend fun getRemoteKeysForLastItem(
        state: PagingState<Int, AttractionItemView>
    ) = with(state) {
        lastItemOrNull()?.let {
            attractionRemoteKeysDao.get(attractionId = it.attractionId)
        }
    }

    private suspend fun saveRemoteItems(
        itemList: List<AttractionRemoteItem>,
        loadKey: Int,
        loadType: LoadType
    ) {
        val nextKey = loadKey + 1
        val previousKey = if (loadKey == FIRST_PAGE) null else loadKey - 1

        appDatabase.withTransaction {
            if (loadType == LoadType.REFRESH)
                attractionDao.clearTable()

            //Upsert the master attraction entities.
            attractionDao.upsert(entities = itemList.map { it.attraction })

            //Upsert the master attraction image entities.
            attractionImageDao.upsert(entities = itemList.map { it.attractionImages }.flatten())

            //Upsert the slave entities.
            attractionImageRemoteOrderDao.upsert(entities = itemList.map { it.attractionImageRemoteOrders }
                .flatten())

            attractionRemoteKeysDao.upsert(entities = itemList.map {
                AttractionRemoteKeysEntity(
                    attractionId = it.attraction.attractionId,
                    nextKey = nextKey,
                    previousKey = previousKey
                )
            })
            attractionRemoteOrderDao.upsert(entities = itemList.map { it.attractionRemoteOrder })
        }
    }

    companion object {
        private const val FIRST_PAGE = 1
    }
}