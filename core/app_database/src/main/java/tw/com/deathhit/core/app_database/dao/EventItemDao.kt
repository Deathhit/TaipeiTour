package tw.com.deathhit.core.app_database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import tw.com.deathhit.core.app_database.Column
import tw.com.deathhit.core.app_database.enum_type.Language
import tw.com.deathhit.core.app_database.view.EventItemView

@Dao
interface EventItemDao {
    @Query("SELECT * FROM EventItemView WHERE :eventId = ${Column.EVENT_ID}")
    fun getEntity(eventId: String): Flow<EventItemView?>

    @Query("SELECT * FROM EventItemView WHERE :language = ${Column.LANGUAGE} ORDER BY ${Column.REMOTE_ORDER} ASC")
    fun getEntitiesPagingSource(language: Language): PagingSource<Int, EventItemView>
}