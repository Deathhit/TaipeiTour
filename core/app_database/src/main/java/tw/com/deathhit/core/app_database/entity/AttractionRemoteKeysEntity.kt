package tw.com.deathhit.core.app_database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import tw.com.deathhit.core.app_database.Column

@Entity(
    primaryKeys = [Column.ATTRACTION_ID],
    foreignKeys = [ForeignKey(
        childColumns = [Column.ATTRACTION_ID],
        entity = AttractionEntity::class,
        onDelete = ForeignKey.CASCADE,
        parentColumns = [Column.ATTRACTION_ID]
    )]
)
data class AttractionRemoteKeysEntity(
    @ColumnInfo(name = Column.ATTRACTION_ID) val attractionId: String,
    @ColumnInfo(name = Column.NEXT_KEY) val nextKey: Int?,
    @ColumnInfo(name = Column.PREVIOUS_KEY) val previousKey: Int?
)
