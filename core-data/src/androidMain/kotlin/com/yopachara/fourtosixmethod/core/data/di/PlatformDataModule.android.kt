package com.yopachara.fourtosixmethod.core.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.yopachara.fourtosixmethod.core.data.datastore.DATASTORE_FILE_NAME
import com.yopachara.fourtosixmethod.core.data.datastore.createUserSettingsDataStore
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformDataModule: Module = module {
    single<DataStore<Preferences>> {
        val context: Context = get()
        // Mirrors the old `preferencesDataStore(name = "user_settings")` location
        // (filesDir/datastore/user_settings.preferences_pb) so existing data is preserved.
        createUserSettingsDataStore {
            context.filesDir.resolve("datastore/$DATASTORE_FILE_NAME").absolutePath
        }
    }
}
