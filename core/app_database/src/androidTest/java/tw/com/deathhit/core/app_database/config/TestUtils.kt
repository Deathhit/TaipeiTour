package tw.com.deathhit.core.app_database.config

import android.content.Context
import androidx.room.Room
import tw.com.deathhit.core.app_database.AppDatabase
import tw.com.deathhit.core.app_database.entity.AttractionEntity
import tw.com.deathhit.core.app_database.entity.AttractionImageEntity
import tw.com.deathhit.core.app_database.entity.EventEntity
import tw.com.deathhit.core.app_database.enum_type.Language
import java.util.UUID
import kotlin.random.Random

fun buildAppDatabase(context: Context) =
    Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()

fun generateAttractionEntities(language: Language = generateLanguage()) =
    mutableListOf<AttractionEntity>().apply {
        for (i in 0..getRandomInt()) {
            add(generateAttractionEntity(language = language))
        }
    }.toList()

fun generateAttractionEntity(
    attractionId: String = generateAttractionId(),
    language: Language = generateLanguage()
) = AttractionEntity(
    address = getRandomStr(),
    attractionId = attractionId,
    introduction = getRandomStr(),
    language = language,
    name = getRandomStr(),
    updateTimeText = getRandomStr(),
    websiteUrl = getRandomStr()
)

fun generateAttractionId() = getRandomStr()

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

fun generateEventEntities(language: Language = generateLanguage()) =
mutableListOf<EventEntity>().apply {
    for (i in 0..getRandomInt()) {
        add(generateEventEntity(language = language))
    }
}.toList()

fun generateEventEntity(
    eventId: String = generateEventId(),
    language: Language = generateLanguage()
) = EventEntity(
    description = getRandomStr(),
    eventId = eventId,
    language = language,
    postTimeText = getRandomStr(),
    title = getRandomStr(),
    updateTimeText = getRandomStr(),
    websiteUrl = getRandomStr()
)

fun generateEventId() = getRandomStr()

fun generateLanguage() = Language.entries.toTypedArray().random()

private fun getRandomInt() = Random.nextInt(3, 10)
private fun getRandomStr() = UUID.randomUUID().toString()