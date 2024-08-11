package tw.com.deathhit.core.app_database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import tw.com.deathhit.core.app_database.Column
import tw.com.deathhit.core.app_database.entity.AttractionRemoteKeysEntity
import tw.com.deathhit.core.app_database.entity.EventRemoteKeysEntity

@Dao
interface EventRemoteKeysDao {
    @Query("SELECT * FROM EventRemoteKeysEntity WHERE :eventId = ${Column.EVENT_ID}")
    suspend fun get(eventId: String): EventRemoteKeysEntity?

    @Upsert
    suspend fun upsert(entities: List<EventRemoteKeysEntity>)
}