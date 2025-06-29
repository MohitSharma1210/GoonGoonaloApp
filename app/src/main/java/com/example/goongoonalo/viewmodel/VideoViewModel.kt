package com.example.goongoonalo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goongoonalo.data.model.VideoItem
import com.example.goongoonalo.data.repository.VideoRepository
import com.example.goongoonalo.utils.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoViewModel @Inject constructor(
    private val repository: VideoRepository,
    private val prefManager: PreferenceManager
) : ViewModel() {

    private val _newList = MutableStateFlow<List<VideoItem>>(emptyList())
    val newList: StateFlow<List<VideoItem>> = _newList

    private val _topList = MutableStateFlow<List<VideoItem>>(emptyList())
    val topList: StateFlow<List<VideoItem>> = _topList

    private val _serverContinueList = MutableStateFlow<List<VideoItem>>(emptyList())
    private val _savedContinueList = MutableStateFlow<List<VideoItem>>(emptyList())

    private val _continueList = MutableStateFlow<List<VideoItem>>(emptyList())
    val continueList: StateFlow<List<VideoItem>> = _continueList

    private val _isInitialLoading = MutableStateFlow(true)
    val isInitialLoading: StateFlow<Boolean> = _isInitialLoading

    var isRefreshing = MutableStateFlow(false)

    init {
        fetchVideos(clearPrefs = false) // don't clear prefs
        observeSavedItems()
    }

    fun fetchVideos(clearPrefs: Boolean = false) {
        viewModelScope.launch {
            if (_newList.value.isEmpty() && _topList.value.isEmpty() && _serverContinueList.value.isEmpty()) {
                _isInitialLoading.value = true
            }

            isRefreshing.value = true

            if (clearPrefs) {
                prefManager.clearAllItems()
            }

            val result = repository.fetchVideos()

            _newList.value = result.New.sortedBy { it.Order }
            _topList.value = result.TOP.sortedBy { it.Order }
            _serverContinueList.value = result.Continue.sortedBy { it.Order }

            if (clearPrefs) {
                _savedContinueList.value = emptyList()
            }

            mergeLists()

            isRefreshing.value = false
            _isInitialLoading.value = false
        }
    }

    private fun observeSavedItems() {
        viewModelScope.launch {
            prefManager.getItems().collect { saved ->
                _savedContinueList.value = saved
                // Merge server + saved only if server is not freshly fetched (to avoid overwriting)
                if (_serverContinueList.value.isEmpty().not()) {
                    mergeLists()
                }
            }
        }
    }

    private fun mergeLists() {
        val saved = _savedContinueList.value
        val server = _serverContinueList.value

        val filteredServer = server.filterNot { serverItem ->
            saved.any { it.Thumbnail == serverItem.Thumbnail }
        }

        val combined = saved + filteredServer // saved items first
        _continueList.value = combined
    }

    fun addToContinueWatching(item: VideoItem) {
        viewModelScope.launch {
            prefManager.addItem(item)
        }
    }
}
