package tw.com.deathhit.data.attraction_image.config

import android.content.Context
import androidx.room.Room
import tw.com.deathhit.core.app_database.AppDatabase
import tw.com.deathhit.core.app_database.entity.AttractionEntity
import tw.com.deathhit.core.app_database.entity.AttractionImageEntity
import tw.com.deathhit.domain.enum_type.Language
import java.util.UUID
import kotlin.random.Random

fun buildAppDatabase(context: Context) =
    Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()

fun generateAttractionEntity() = AttractionEntity(
    address = getRandomStr(),
    attractionId = getRandomStr(),
    introduction = getRandomStr(),
    language = tw.com.deathhit.core.app_database.enum_type.Language.ZH_TW,
    name = getRandomStr(),
    updateTimeText = getRandomStr(),
    websiteUrl = getRandomStr()
)

fun generateAttractionImageEntities(attractionId: String) =
    mutableListOf<AttractionImageEntity>().apply {
        for (i in 0..getRandomInt()) {
            add(
                AttractionImageEntity(
                    attractionId = attractionId,
                    imageUrl = getRandomStr()
                )
            )
        }
    }.toList()

private fun getRandomInt() = Random.nextInt(3, 10)
private fun getRandomStr() = UUID.randomUUID().toString()