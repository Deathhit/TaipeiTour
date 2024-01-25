package tw.com.deathhit.core.app_database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import tw.com.deathhit.core.app_database.Column
import tw.com.deathhit.core.app_database.enum_type.Language

@Entity(primaryKeys = [Column.ATTRACTION_ID])
data class AttractionEntity(
    @ColumnInfo(name = Column.ADDRESS) val address: String,
    @ColumnInfo(name = Column.ATTRACTION_ID) val attractionId: String,
    @ColumnInfo(name = Column.INTRODUCTION) val introduction: String,
    @ColumnInfo(name = Column.LANGUAGE) val language: Language,
    @ColumnInfo(name = Column.NAME) val name: String,
    @ColumnInfo(name = Column.UPDATE_TIME_TEXT) val updateTimeText: String,
    @ColumnInfo(name = Column.WEBSITE_URL) val websiteUrl: String
)