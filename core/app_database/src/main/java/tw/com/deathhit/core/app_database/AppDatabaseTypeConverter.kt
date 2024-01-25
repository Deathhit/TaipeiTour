package tw.com.deathhit.core.app_database

import androidx.room.TypeConverter
import tw.com.deathhit.core.app_database.enum_type.Language

internal object AppDatabaseTypeConverter {
    @TypeConverter
    fun fromLanguage(value: Language?) = value?.id

    @TypeConverter
    fun toLanguage(value: Int?) = when (value) {
        Language.ENGLISH.id -> Language.ENGLISH
        Language.JAPANESE.id -> Language.JAPANESE
        Language.KOREAN.id -> Language.KOREAN
        Language.ZH_CN.id -> Language.ZH_CN
        Language.ZH_TW.id -> Language.ZH_TW
        else -> null
    }
}