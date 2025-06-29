package com.example.goongoonalo.utils

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.goongoonalo.data.model.VideoItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class PreferenceManager(private val context: Context) {

    companion object {
        private val Context.dataStore by preferencesDataStore("continue_watching_prefs")
        private val CONTINUE_KEY = stringPreferencesKey("continue_list") // single string
    }

    suspend fun addItem(item: VideoItem) {
        context.dataStore.edit { prefs ->
            val existingListJson = prefs[CONTINUE_KEY]
            val currentList = existingListJson?.let {
                Json.decodeFromString<List<VideoItem>>(it)
            } ?: emptyList()

            val updatedList = listOf(item) + currentList.filter { it.Thumbnail != item.Thumbnail }

            prefs[CONTINUE_KEY] = Json.encodeToString(updatedList)
        }
    }

    fun getItems(): Flow<List<VideoItem>> {
        return context.dataStore.data.map { prefs ->
            prefs[CONTINUE_KEY]?.let {
                Json.decodeFromString<List<VideoItem>>(it)
            } ?: emptyList()
        }
    }


    suspend fun clearAllItems() {
        context.dataStore.edit { prefs ->
            prefs.remove(CONTINUE_KEY)
        }
    }

}
