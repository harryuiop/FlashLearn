package com.example.seng303_assignment1.viewModels

import androidx.lifecycle.ViewModel
import com.example.seng303_assignment1.model.FlashCard
import com.example.seng303_assignment1.model.GameRoundResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PlayViewModel: ViewModel() {
    private val _correctAnswersTotal = MutableStateFlow(0)
    val correctAnswersTotal: StateFlow<Int> = _correctAnswersTotal

    private val _gameResults = MutableStateFlow<List<GameRoundResult>>(emptyList())
    val gameResults: StateFlow<List<GameRoundResult>> = _gameResults

    private val _reRenderIndex = MutableStateFlow(0)
    val reRenderIndex: StateFlow<Int> = _reRenderIndex

    private val _selectedAnswerIndex = MutableStateFlow(-1)
    val selectedAnswerIndex: StateFlow<Int> = _selectedAnswerIndex

    private val _shuffledFlashCards = MutableStateFlow<List<FlashCard>>(emptyList())
    val shuffledFlashCards: StateFlow<List<FlashCard>> = _shuffledFlashCards

    private val _index = MutableStateFlow(0)
    val index: StateFlow<Int> = _index

    private val _shuffled = MutableStateFlow(false)
    val shuffled: StateFlow<Boolean> = _shuffled

    fun incrementIndex() {
        _index.value = index.value + 1
    }

    fun setShuffledFlashCards(flashCards: List<FlashCard>) {
        _shuffledFlashCards.value = flashCards
    }

    fun updateSelectedAnswerIndex(newIndex: Int) {
        _selectedAnswerIndex.value = newIndex
    }

    fun incrementReRenderIndex() {
        _reRenderIndex.value = reRenderIndex.value + 1
    }

    fun incrementCorrectAnswersTotal() {
        _correctAnswersTotal.value = correctAnswersTotal.value + 1
    }

    fun addToGameResults(newGameRoundResult: GameRoundResult) {
        val newResultAdded = _gameResults.value + newGameRoundResult
        _gameResults.value = newResultAdded
    }

    fun setShuffledFalse() {
        _shuffled.value = false
    }

    fun setShuffledTrue() {
        _shuffled.value = true
    }

    fun resetGameResults() {
        _gameResults.value = emptyList<GameRoundResult>()
        _correctAnswersTotal.value = 0
        _selectedAnswerIndex.value = -1
        _index.value = 0
    }
}