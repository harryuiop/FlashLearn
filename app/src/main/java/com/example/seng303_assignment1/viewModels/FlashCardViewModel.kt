package com.example.seng303_assignment1.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seng303_assignment1.datastore.Storage
import com.example.seng303_assignment1.model.FlashCard
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class FlashCardViewModel(private val flashCardStorage: Storage<FlashCard>) : ViewModel() {
    private val _flashCards = MutableStateFlow<List<FlashCard>>(emptyList())
    val flashCards: StateFlow<List<FlashCard>> get() = _flashCards

    fun getCards() = viewModelScope.launch {
        flashCardStorage.getAll().catch { Log.e("VIEW_FLASH_CARD_VIEW_MODEL", it.toString()) }.collect{_flashCards.emit(it)}
    }

    fun loadDefaultCardsIfNoneExist() = viewModelScope.launch {
        val currentNotes = flashCardStorage.getAll().first()
        if(currentNotes.isEmpty()) {
            Log.d("VIEW_FLASH_CARD_VIEW_MODEL", "Inserting default flash cards...")
            flashCardStorage.insertAll(FlashCard.getFlashCards()).catch { Log.w("VIEW_FLASH_CARD_VIEW_MODEL", "Could not insert default notes")
        }.collect {
            Log.d("VIEW_FLASH_CARD_VIEW_MODEL", "Default flash cards inserted successfully")
            _flashCards.emit(FlashCard.getFlashCards())
            }
        }
    }


}