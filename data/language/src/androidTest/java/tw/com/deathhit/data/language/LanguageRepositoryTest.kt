package tw.com.deathhit.data.language

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import tw.com.deathhit.domain.LanguageRepository
import tw.com.deathhit.domain.enum_type.Language

@OptIn(ExperimentalCoroutinesApi::class)
class LanguageRepositoryTest {
    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun setLanguage() = runTest {
        //Given
        val languageRepository: LanguageRepository = LanguageRepositoryImp(context)

        val language = Language.entries.toTypedArray().random()

        //When
        languageRepository.setLanguage(language)

        advanceUntilIdle()

        val result = languageRepository.getSelectedLanguageFlow().first()

        //Then
        assert(result == language)
    }
}