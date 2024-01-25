package tw.com.deathhit.data.attraction

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
import tw.com.deathhit.domain.AttractionRepository
import tw.com.deathhit.domain.LanguageRepository
import tw.com.deathhit.domain.model.AttractionDO

@OptIn(ExperimentalPagingApi::class, ExperimentalCoroutinesApi::class)
class AttractionRepositoryImp(
    private val appDatabase: AppDatabase,
    private val languageRepository: LanguageRepository,
    private val travelTaipeiService: TravelTaipeiService
) : AttractionRepository {
    private val attractionItemDao = appDatabase.attractionItemDao()
    override fun getAttractionFlow(attractionId: String): Flow<AttractionDO?> =
        attractionItemDao.getEntity(attractionId = attractionId).map { it?.toDO() }

    override fun getAttractionPagingDataFlow(): Flow<PagingData<AttractionDO>> =
        languageRepository.getSelectedLanguageFlow().flatMapLatest { language ->
            Pager(
                config = PagingConfig(pageSize = travelTaipeiService.pageSize),
                remoteMediator = AttractionRemoteMediator(
                    appDatabase = appDatabase,
                    language = language,
                    travelTaipeiService = travelTaipeiService
                )
            ) {
                attractionItemDao.getEntitiesPagingSource(language.toDatabaseType())
            }.flow.map { pagingData ->
                pagingData.map { it.toDO() }
            }
        }
}