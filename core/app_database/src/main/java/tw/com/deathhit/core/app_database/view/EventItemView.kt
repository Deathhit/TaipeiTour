package tw.com.deathhit.core.app_database.view

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import tw.com.deathhit.core.app_database.Column
import tw.com.deathhit.core.app_database.enum_type.Language

@DatabaseView(
    "SELECT " +
            "EventEntity.${Column.DESCRIPTION} AS ${Column.DESCRIPTION}," +
            "EventEntity.${Column.EVENT_ID} AS ${Column.EVENT_ID}," +
            "EventEntity.${Column.LANGUAGE} AS ${Column.LANGUAGE}," +
            "EventEntity.${Column.POST_TIME_TEXT} AS ${Column.POST_TIME_TEXT}," +
            "EventRemoteOrderEntity.${Column.REMOTE_ORDER} AS ${Column.REMOTE_ORDER}," +
            "EventEntity.${Column.TITLE} AS ${Column.TITLE}," +
            "EventEntity.${Column.UPDATE_TIME_TEXT} AS ${Column.UPDATE_TIME_TEXT}," +
            "EventEntity.${Column.WEBSITE_URL} AS ${Column.WEBSITE_URL} " +
            "FROM EventEntity " +
            "LEFT JOIN EventRemoteOrderEntity " +
            "ON EventRemoteOrderEntity.${Column.EVENT_ID} = EventEntity.${Column.EVENT_ID}"
)
data class EventItemView(
    @ColumnInfo(name = Column.DESCRIPTION) val description: String,
    @ColumnInfo(name = Column.EVENT_ID) val eventId: String,
    @ColumnInfo(name = Column.LANGUAGE) val language: Language,
    @ColumnInfo(name = Column.POST_TIME_TEXT) val postTimeText: String,
    @ColumnInfo(name = Column.REMOTE_ORDER) val remoteOrder: Int?,
    @ColumnInfo(name = Column.TITLE) val title: String,
    @ColumnInfo(name = Column.UPDATE_TIME_TEXT) val updateTimeText: String,
    @ColumnInfo(name = Column.WEBSITE_URL) val websiteUrl: String
)
