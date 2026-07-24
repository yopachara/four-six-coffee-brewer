package com.yopachara.fourtosixmethod.core.data.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.yopachara.fourtosixmethod.core.data.datastore.DATASTORE_FILE_NAME
import com.yopachara.fourtosixmethod.core.data.datastore.createUserSettingsDataStore
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
actual val platformDataModule: Module = module {
    single<DataStore<Preferences>> {
        createUserSettingsDataStore {
            val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
                directory = NSDocumentDirectory,
                inDomain = NSUserDomainMask,
                appropriateForURL = null,
                create = false,
                error = null,
            )
            requireNotNull(documentDirectory?.path) + "/$DATASTORE_FILE_NAME"
        }
    }
}
