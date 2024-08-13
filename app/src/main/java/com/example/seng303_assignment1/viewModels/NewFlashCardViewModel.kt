package com.example.seng303_assignment1.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seng303_assignment1.datastore.Storage
import com.example.seng303_assignment1.model.AnswerOption
import com.example.seng303_assignment1.model.FlashCard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlin.random.Random


class NewFlashCardViewModel(
    private val flashCardViewModel: FlashCardViewModel,
    private val flashCardStorage: Storage<FlashCard>)
    : ViewModel() {

    private var question by mutableStateOf("")

    private var answerOptions by mutableStateOf(listOf(AnswerOption("", false)))

    private val _flashCards = MutableStateFlow<List<FlashCard>>(emptyList())
    private val flashCards: StateFlow<List<FlashCard>> get() = _flashCards

    fun updateQuestion(newQuestion: String) {
        question = newQuestion
    }

    fun updateAnswerOptions(index: Int, newAnswerText: String, isCorrect: Boolean) {
        answerOptions = answerOptions.toMutableList().apply {
            this[index] = this[index].copy(answerText = newAnswerText, isCorrect = isCorrect)
        }
    }

    fun addAnswerOption(newOption: AnswerOption) {
        answerOptions = answerOptions + newOption
    }

    fun fetchAnswerOptions(): List<AnswerOption> {
        return answerOptions
    }

    fun fetchQuestion(): String {
        return question
    }

    fun saveFlashCard() {
        val newFlashCard = FlashCard(
            id = generateFlashCardId(),
            question = question,
            answerOptions = answerOptions,
            correctAnswers = answerOptions.filter { it.isCorrect }
        )
        viewModelScope.launch {
            flashCardStorage.insert(newFlashCard)
                .catch { Log.e("NOTE_VIEW_MODEL", it.toString()) }.collect()
            flashCardStorage.getAll().catch { Log.e("NOTE_VIEW_MODEL", it.toString()) }
                .collect { _flashCards.emit(it) }
        }
    }
}

private fun generateFlashCardId(): Int {
    val randomInt = Random.nextInt(1, 1000)
    return randomInt
}

