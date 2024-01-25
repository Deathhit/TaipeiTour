package tw.com.deathhit.data.language.data_source

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

internal class LanguageDatastore(context: Context) {
    private val dataStore = context.dataStore
    private val data get() = dataStore.data

    val language get() = data.map { it[Key.language] }

    suspend fun setLanguage(value: Int?) {
        dataStore.edit {
            if (value == null)
                it.remove(Key.language)
            else
                it[Key.language] = value
        }
    }

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "language_datastore_cx432194e3f555089163ff5cdec59107")
    }

    private object Key {
        val language = intPreferencesKey("language")
    }
}