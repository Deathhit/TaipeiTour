package tw.com.deathhit.core.app_database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import tw.com.deathhit.core.app_database.Column

@Entity(primaryKeys = [Column.EVENT_ID],
    foreignKeys = [ForeignKey(
        childColumns = [Column.EVENT_ID],
        entity = EventEntity::class,
        onDelete = ForeignKey.CASCADE,
        parentColumns = [Column.EVENT_ID]
    )])
data class EventRemoteOrderEntity(
    @ColumnInfo(name = Column.EVENT_ID) val eventId: String,
    @ColumnInfo(name = Column.REMOTE_ORDER) val remoteOrder: Int
)
