package tw.com.deathhit.domain

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import tw.com.deathhit.domain.model.AttractionDO

interface AttractionRepository {
    fun getAttractionFlow(attractionId: String): Flow<AttractionDO?>
    fun getAttractionPagingDataFlow(): Flow<PagingData<AttractionDO>>
}