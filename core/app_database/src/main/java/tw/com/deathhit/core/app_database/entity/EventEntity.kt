package tw.com.deathhit.core.app_database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import tw.com.deathhit.core.app_database.Column
import tw.com.deathhit.core.app_database.enum_type.Language

@Entity(primaryKeys = [Column.EVENT_ID])
data class EventEntity(
    @ColumnInfo(name = Column.DESCRIPTION) val description: String,
    @ColumnInfo(name = Column.EVENT_ID) val eventId: String,
    @ColumnInfo(name = Column.LANGUAGE) val language: Language,
    @ColumnInfo(name = Column.POST_TIME_TEXT) val postTimeText: String,
    @ColumnInfo(name = Column.TITLE) val title: String,
    @ColumnInfo(name = Column.UPDATE_TIME_TEXT) val updateTimeText: String,
    @ColumnInfo(name = Column.WEBSITE_URL) val websiteUrl: String
)
