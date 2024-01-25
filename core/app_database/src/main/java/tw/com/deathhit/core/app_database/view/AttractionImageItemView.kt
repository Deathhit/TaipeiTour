package tw.com.deathhit.core.app_database.view

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import tw.com.deathhit.core.app_database.Column

@DatabaseView(
    "SELECT " +
            "AttractionImageEntity.${Column.ATTRACTION_ID} AS ${Column.ATTRACTION_ID}," +
            "AttractionImageEntity.${Column.IMAGE_URL} AS ${Column.IMAGE_URL}," +
            "AttractionImageRemoteOrderEntity.${Column.REMOTE_ORDER} AS ${Column.REMOTE_ORDER} " +
            "FROM AttractionImageEntity " +
            "LEFT JOIN AttractionImageRemoteOrderEntity " +
            "ON AttractionImageRemoteOrderEntity.${Column.ATTRACTION_ID} = AttractionImageEntity.${Column.ATTRACTION_ID} " +
            "AND AttractionImageRemoteOrderEntity.${Column.IMAGE_URL} = AttractionImageEntity.${Column.IMAGE_URL}"
)
data class AttractionImageItemView(
    @ColumnInfo(name = Column.ATTRACTION_ID) val attractionId: String,
    @ColumnInfo(name = Column.IMAGE_URL) val imageUrl: String,
    @ColumnInfo(name = Column.REMOTE_ORDER) val remoteOrder: Int?
)
