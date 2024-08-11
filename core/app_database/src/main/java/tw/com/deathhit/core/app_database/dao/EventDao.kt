package tw.com.deathhit.core.app_database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import tw.com.deathhit.core.app_database.entity.EventEntity

@Dao
interface EventDao {
    @Query("DELETE FROM EventEntity")
    suspend fun clearTable()

    @Upsert
    suspend fun upsert(entities: List<EventEntity>)
}