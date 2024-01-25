package tw.com.deathhit.domain

import kotlinx.coroutines.flow.Flow
import tw.com.deathhit.domain.enum_type.Language
import tw.com.deathhit.domain.model.LanguageDO

interface LanguageRepository {
    fun getLanguageListFlow(): Flow<List<LanguageDO>>
    fun getSelectedLanguageFlow(): Flow<Language>
    suspend fun setLanguage(language: Language)
}