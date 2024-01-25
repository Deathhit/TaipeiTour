package tw.com.deathhit.core.app_database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import tw.com.deathhit.core.app_database.Column
import tw.com.deathhit.core.app_database.enum_type.Language
import tw.com.deathhit.core.app_database.view.AttractionItemView

@Dao
interface AttractionItemDao {
    @Query("SELECT * FROM AttractionItemView WHERE :attractionId = ${Column.ATTRACTION_ID}")
    fun getEntity(attractionId: String): Flow<AttractionItemView?>

    @Query("SELECT * FROM AttractionItemView WHERE :language = ${Column.LANGUAGE} ORDER BY ${Column.REMOTE_ORDER} ASC")
    fun getEntitiesPagingSource(language: Language): PagingSource<Int, AttractionItemView>
}