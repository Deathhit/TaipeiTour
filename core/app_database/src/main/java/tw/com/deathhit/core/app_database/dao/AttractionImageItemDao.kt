package tw.com.deathhit.core.app_database.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import tw.com.deathhit.core.app_database.Column
import tw.com.deathhit.core.app_database.view.AttractionImageItemView

@Dao
interface AttractionImageItemDao {
    @Query(
        "SELECT * FROM AttractionImageItemView " +
                "WHERE :attractionId = ${Column.ATTRACTION_ID} ORDER BY ${Column.REMOTE_ORDER} ASC"
    )
    fun getEntitiesListFlow(attractionId: String): Flow<List<AttractionImageItemView>>
}