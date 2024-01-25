package tw.com.deathhit.core.app_database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import tw.com.deathhit.core.app_database.Column

@Entity(
    primaryKeys = [Column.ATTRACTION_ID, Column.IMAGE_URL],
    foreignKeys = [ForeignKey(
        childColumns = [Column.ATTRACTION_ID],
        entity = AttractionEntity::class,
        onDelete = ForeignKey.CASCADE,
        parentColumns = [Column.ATTRACTION_ID]
    )]
)
data class AttractionImageEntity(
    @ColumnInfo(name = Column.ATTRACTION_ID) val attractionId: String,
    @ColumnInfo(name = Column.IMAGE_URL) val imageUrl: String
)