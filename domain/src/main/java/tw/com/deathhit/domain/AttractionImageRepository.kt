package tw.com.deathhit.domain

import kotlinx.coroutines.flow.Flow
import tw.com.deathhit.domain.model.AttractionImageDO

interface AttractionImageRepository {
    fun getAttractionImageListFlow(attractionId: String): Flow<List<AttractionImageDO>>
}