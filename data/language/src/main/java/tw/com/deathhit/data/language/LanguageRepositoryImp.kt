package tw.com.deathhit.data.language

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tw.com.deathhit.data.language.data_source.LanguageDatastore
import tw.com.deathhit.domain.LanguageRepository
import tw.com.deathhit.domain.enum_type.Language
import tw.com.deathhit.domain.model.LanguageDO

class LanguageRepositoryImp(val context: Context) : LanguageRepository {
    private val datastore = LanguageDatastore(context)

    override fun getLanguageListFlow(): Flow<List<LanguageDO>> =
        getSelectedLanguageFlow().map { selectedLanguage ->
            orderedLanguageList.map {
                LanguageDO(language = it, isSelected = it == selectedLanguage)
            }
        }

    override fun getSelectedLanguageFlow(): Flow<Language> =
        datastore.language.map { it?.toLanguage() ?: Language.ZH_TW }

    override suspend fun setLanguage(language: Language) = datastore.setLanguage(language.toValue())

    companion object {
        private val orderedLanguageList = listOf(
            Language.ZH_TW,
            Language.ZH_CN,
            Language.ENGLISH,
            Language.JAPANESE,
            Language.KOREAN
        )
    }
}