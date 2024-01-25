package tw.com.deathhit.core.app_database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import tw.com.deathhit.core.app_database.Column
import tw.com.deathhit.core.app_database.entity.AttractionRemoteKeysEntity

@Dao
interface AttractionRemoteKeysDao {
    @Query("SELECT * FROM AttractionRemoteKeysEntity WHERE :attractionId = ${Column.ATTRACTION_ID}")
    suspend fun get(attractionId: String): AttractionRemoteKeysEntity?

    @Upsert
    suspend fun upsert(entities: List<AttractionRemoteKeysEntity>)
}