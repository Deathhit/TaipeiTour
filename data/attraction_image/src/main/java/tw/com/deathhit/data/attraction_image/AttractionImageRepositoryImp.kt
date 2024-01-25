package tw.com.deathhit.data.attraction_image

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tw.com.deathhit.core.app_database.AppDatabase
import tw.com.deathhit.domain.AttractionImageRepository
import tw.com.deathhit.domain.model.AttractionImageDO

class AttractionImageRepositoryImp(appDatabase: AppDatabase) :
    AttractionImageRepository {
    private val attractionImageItemDao = appDatabase.attractionImageItemDao()

    override fun getAttractionImageListFlow(attractionId: String): Flow<List<AttractionImageDO>> =
        attractionImageItemDao.getEntitiesListFlow(attractionId = attractionId)
            .map { list -> list.map { it.toDO() } }
}