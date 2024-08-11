package tw.com.deathhit.core.app_database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import tw.com.deathhit.core.app_database.dao.AttractionDao
import tw.com.deathhit.core.app_database.dao.AttractionImageDao
import tw.com.deathhit.core.app_database.dao.AttractionImageItemDao
import tw.com.deathhit.core.app_database.dao.AttractionImageRemoteOrderDao
import tw.com.deathhit.core.app_database.dao.AttractionItemDao
import tw.com.deathhit.core.app_database.dao.AttractionRemoteKeysDao
import tw.com.deathhit.core.app_database.dao.AttractionRemoteOrderDao
import tw.com.deathhit.core.app_database.dao.EventDao
import tw.com.deathhit.core.app_database.dao.EventItemDao
import tw.com.deathhit.core.app_database.dao.EventRemoteKeysDao
import tw.com.deathhit.core.app_database.dao.EventRemoteOrderDao
import tw.com.deathhit.core.app_database.entity.AttractionEntity
import tw.com.deathhit.core.app_database.entity.AttractionImageEntity
import tw.com.deathhit.core.app_database.entity.AttractionImageRemoteOrderEntity
import tw.com.deathhit.core.app_database.entity.AttractionRemoteKeysEntity
import tw.com.deathhit.core.app_database.entity.AttractionRemoteOrderEntity
import tw.com.deathhit.core.app_database.entity.EventEntity
import tw.com.deathhit.core.app_database.entity.EventRemoteKeysEntity
import tw.com.deathhit.core.app_database.entity.EventRemoteOrderEntity
import tw.com.deathhit.core.app_database.view.AttractionImageItemView
import tw.com.deathhit.core.app_database.view.AttractionItemView
import tw.com.deathhit.core.app_database.view.EventItemView

@Database(
    autoMigrations = [AutoMigration(from = 1, to = 2)],
    entities = [
        AttractionEntity::class,
        AttractionImageEntity::class,
        AttractionImageRemoteOrderEntity::class,
        AttractionRemoteKeysEntity::class,
        AttractionRemoteOrderEntity::class,
        EventEntity::class,
        EventRemoteKeysEntity::class,
        EventRemoteOrderEntity::class
    ],
    version = 2,
    views = [
        AttractionImageItemView::class,
        AttractionItemView::class,
        EventItemView::class
    ]
)
@TypeConverters(AppDatabaseTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun attractionDao(): AttractionDao
    abstract fun attractionImageDao(): AttractionImageDao
    abstract fun attractionImageItemDao(): AttractionImageItemDao
    abstract fun attractionImageRemoteOrderDao(): AttractionImageRemoteOrderDao
    abstract fun attractionItemDao(): AttractionItemDao
    abstract fun attractionRemoteKeysDao(): AttractionRemoteKeysDao
    abstract fun attractionRemoteOrderDao(): AttractionRemoteOrderDao
    abstract fun eventDao(): EventDao
    abstract fun eventItemDao(): EventItemDao
    abstract fun eventRemoteKeysDao(): EventRemoteKeysDao
    abstract fun eventRemoteOrderDao(): EventRemoteOrderDao
}