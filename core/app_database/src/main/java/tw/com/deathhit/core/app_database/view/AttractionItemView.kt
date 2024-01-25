package tw.com.deathhit.core.app_database.view

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import tw.com.deathhit.core.app_database.Column
import tw.com.deathhit.core.app_database.enum_type.Language

@DatabaseView(
    "SELECT " +
            "AttractionEntity.${Column.ADDRESS} AS ${Column.ADDRESS}," +
            "AttractionEntity.${Column.ATTRACTION_ID} AS ${Column.ATTRACTION_ID}," +
            "(SELECT " +
            "AttractionImageItemView.${Column.IMAGE_URL} " +
            "FROM AttractionImageItemView " +
            "WHERE AttractionEntity.${Column.ATTRACTION_ID} = AttractionImageItemView.${Column.ATTRACTION_ID} " +
            "ORDER BY AttractionImageItemView.${Column.REMOTE_ORDER} ASC LIMIT 1" +
            ") AS ${Column.IMAGE_URL}," +
            "AttractionEntity.${Column.INTRODUCTION} AS ${Column.INTRODUCTION}," +
            "AttractionEntity.${Column.LANGUAGE} AS ${Column.LANGUAGE}," +
            "AttractionEntity.${Column.NAME} AS ${Column.NAME}," +
            "AttractionRemoteOrderEntity.${Column.REMOTE_ORDER} AS ${Column.REMOTE_ORDER}," +
            "AttractionEntity.${Column.UPDATE_TIME_TEXT} AS ${Column.UPDATE_TIME_TEXT}," +
            "AttractionEntity.${Column.WEBSITE_URL} AS ${Column.WEBSITE_URL} " +
            "FROM AttractionEntity " +
            "LEFT JOIN AttractionRemoteOrderEntity " +
            "ON AttractionRemoteOrderEntity.${Column.ATTRACTION_ID} = AttractionEntity.${Column.ATTRACTION_ID}"
)
data class AttractionItemView(
    @ColumnInfo(name = Column.ADDRESS) val address: String,
    @ColumnInfo(name = Column.ATTRACTION_ID) val attractionId: String,
    @ColumnInfo(name = Column.IMAGE_URL) val imageUrl: String?,
    @ColumnInfo(name = Column.INTRODUCTION) val introduction: String,
    @ColumnInfo(name = Column.LANGUAGE) val language: Language,
    @ColumnInfo(name = Column.NAME) val name: String,
    @ColumnInfo(name = Column.REMOTE_ORDER) val remoteOrder: Int?,
    @ColumnInfo(name = Column.UPDATE_TIME_TEXT) val updateTimeText: String,
    @ColumnInfo(name = Column.WEBSITE_URL) val websiteUrl: String
)
