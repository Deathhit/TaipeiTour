package tw.com.deathhit.data.event.config

import android.content.Context
import androidx.room.Room
import tw.com.deathhit.core.app_database.AppDatabase
import tw.com.deathhit.core.app_database.entity.EventEntity
import tw.com.deathhit.core.travel_taipei_api.protocol.model.EventApiEntity
import tw.com.deathhit.data.event.toDatabaseType
import tw.com.deathhit.domain.enum_type.Language
import java.util.UUID
import kotlin.random.Random

fun buildAppDatabase(context: Context) =
    Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()

fun generateEventApiEntities(from: Int = 3, until: Int = 10) =
    mutableListOf<EventApiEntity>().apply {
        for (i in 0..getRandomInt(from = from, until = until)) {
            add(
                EventApiEntity(
                    description = getRandomStr(),
                    id = getRandomStr(),
                    modified = getRandomStr(),
                    posted = getRandomStr(),
                    title = getRandomStr(),
                    url = getRandomStr()
                )
            )
        }
    }.toList()

fun generateEventEntities(language: Language = generateLanguage()) = mutableListOf<EventEntity>().apply {
    for (i in 0..getRandomInt(from = 3, until = 10)) {
        add(
            EventEntity(
                description = getRandomStr(),
                eventId = getRandomStr(),
                language = language.toDatabaseType(),
                postTimeText = getRandomStr(),
                title = getRandomStr(),
                updateTimeText = getRandomStr(),
                websiteUrl = getRandomStr()
            )
        )
    }
}.toList()

fun generateLanguage() = Language.entries.toTypedArray().random()

private fun getRandomInt(from: Int = 0, until: Int = Int.MAX_VALUE) = Random.nextInt(from, until)
private fun getRandomStr() = UUID.randomUUID().toString()