package tw.com.deathhit.data.attraction.config

import android.content.Context
import androidx.room.Room
import tw.com.deathhit.core.app_database.AppDatabase
import tw.com.deathhit.core.app_database.entity.AttractionEntity
import tw.com.deathhit.core.travel_taipei_api.model.AttractionDto
import tw.com.deathhit.data.attraction.toDatabaseType
import tw.com.deathhit.domain.enum_type.Language
import java.util.UUID
import kotlin.random.Random

fun buildAppDatabase(context: Context) =
    Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()

fun generateAttractionDtoList(from: Int = 3, until: Int = 10) =
    mutableListOf<AttractionDto>().apply {
        for (i in 0..getRandomInt(from = from, until = until)) {
            add(
                AttractionDto(
                    address = getRandomStr(),
                    attractionId = getRandomStr(),
                    images = emptyList(),
                    introduction = getRandomStr(),
                    name = getRandomStr(),
                    updateTimeText = getRandomStr(),
                    url = getRandomStr()
                )
            )
        }
    }.toList()

fun generateAttractionEntities(language: Language = generateLanguage()) = mutableListOf<AttractionEntity>().apply {
    for (i in 0..getRandomInt(from = 3, until = 10)) {
        add(
            AttractionEntity(
                address = getRandomStr(),
                attractionId = getRandomStr(),
                introduction = getRandomStr(),
                language = language.toDatabaseType(),
                name = getRandomStr(),
                updateTimeText = getRandomStr(),
                websiteUrl = getRandomStr()
            )
        )
    }
}.toList()

fun generateLanguage() = Language.entries.toTypedArray().random()

private fun getRandomInt(from: Int = 0, until: Int = Int.MAX_VALUE) = Random.nextInt(from, until)
private fun getRandomStr() = UUID.randomUUID().toString()