package com.termix.core.proot

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "termix_prefs")

class ProotPrefs(private val context: Context) {

    private val KEY_INSTALLED = booleanPreferencesKey("rootfs_installed")
    private val KEY_DISTRO_ID = stringPreferencesKey("distro_id")
    private val KEY_ROOTFS_PATH = stringPreferencesKey("rootfs_path")

    val isInstalled: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[KEY_INSTALLED] ?: false
    }

    val distroId: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[KEY_DISTRO_ID]
    }

    val rootfsPath: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[KEY_ROOTFS_PATH]
    }

    suspend fun markInstalled(distroId: String, rootfsPath: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_INSTALLED] = true
            prefs[KEY_DISTRO_ID] = distroId
            prefs[KEY_ROOTFS_PATH] = rootfsPath
        }
    }

    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }
}
