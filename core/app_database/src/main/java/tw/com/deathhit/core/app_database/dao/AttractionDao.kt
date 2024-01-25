package tw.com.deathhit.core.app_database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import tw.com.deathhit.core.app_database.entity.AttractionEntity

@Dao
interface AttractionDao {
    @Query("DELETE FROM AttractionEntity")
    suspend fun clearTable()

    @Upsert
    suspend fun upsert(entities: List<AttractionEntity>)
}