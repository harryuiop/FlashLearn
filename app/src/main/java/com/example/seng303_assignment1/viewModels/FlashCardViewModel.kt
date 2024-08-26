package com.example.seng303_assignment1.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seng303_assignment1.datastore.Storage
import com.example.seng303_assignment1.model.AnswerOption
import com.example.seng303_assignment1.model.FlashCard
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.random.Random

class FlashCardViewModel(
    private val flashCardStorage: Storage<FlashCard>
) : ViewModel() {

    private val _flashCards = MutableStateFlow<List<FlashCard>>(emptyList())
    val flashCard: StateFlow<List<FlashCard>> get() = _flashCards

    private val _selectedFlashCard = MutableStateFlow<FlashCard?>(null)
    val selectedFlashCard: StateFlow<FlashCard?> = _selectedFlashCard

    fun loadDefaultFlashCardIfNoneExist() = viewModelScope.launch {
        val currentNotes = flashCardStorage.getAll().first()
        if (currentNotes.isEmpty()) {
            Log.d("NOTE_VIEW_MODEL", "Inserting default notes...")
            flashCardStorage.insertAll(FlashCard.getFlashCards())
                .catch { Log.w("NOTE_VIEW_MODEL", "Could not insert default notes") }.collect {
                    Log.d("NOTE_VIEW_MODEL", "Default notes inserted successfully")
                    _flashCards.emit(FlashCard.getFlashCards())
                }
        }
    }

    fun getFlashCardById(flashCardId: Int?) = viewModelScope.launch {
        if (flashCardId != null) {
            _selectedFlashCard.value = flashCardStorage.get { it.getIdentifier() == flashCardId }.first()
        } else {
            _selectedFlashCard.value = null
        }
    }

    fun createFlashCard(question: String, answerOptions: List<AnswerOption>) =
        viewModelScope.launch {
        val newFlashCard = FlashCard(
            id = generateFlashCardId(),
            question = question,
            answerOptions = answerOptions
        )
        flashCardStorage.insert(newFlashCard).catch { Log.e("NOTE_VIEW_MODEL", "Could not insert note") }
            .collect()
        flashCardStorage.getAll().catch { Log.e("NOTE_VIEW_MODEL", it.toString()) }
            .collect { _flashCards.emit(it) }
    }

    fun editFlashCard(flashCardId: Int?, flashCard: FlashCard) =
        viewModelScope.launch {
            if (flashCardId != null) {
                flashCardStorage.edit(flashCardId, flashCard).collect()
                flashCardStorage.getAll().catch { Log.e("NOTE_VIEW_MODEL", it.toString()) }
                    .collect { _flashCards.emit(it) }
            }

        }

    fun getAllFlashCards() {
         viewModelScope.launch {
             flashCardStorage.getAll().catch { Log.e("NOTE_VIEW_MODEL", it.toString()) }
                 .collect { _flashCards.emit(it) }
         }
    }

    fun deleteFlashCard(id: Int) {
        viewModelScope.launch {
            flashCardStorage.delete(identifier = id).catch { Log.e("NOTE_VIEW_MODEL", it.toString()) }
                .collect { result ->
                    if (result != -1) {
                        _flashCards.emit(flashCardStorage.getAll().first())
                    } else {
                        Log.e("NOTE_VIEW_MODEL", "Delete operation failed")
                    }
                }
        }
    }

     fun generateFlashCardId(): Int {
        val randomInt = Random.nextInt(1, 1000)
        return randomInt
    }

}