package com.example.seng303_assignment1.viewModels

import androidx.lifecycle.ViewModel
import com.example.seng303_assignment1.model.GameRoundResult

class PlayViewModel: ViewModel() {
     var correctAnswersTotal = 0
        private set

    var gameResults = mutableListOf<GameRoundResult>()
        private set

    fun incrementCorrectAnswersTotal() {
        correctAnswersTotal += 1;
    }

    fun returnCorrectAnswersTotal(): Int {
        return correctAnswersTotal
    }

    fun addToGameResults(newGameRoundResult: GameRoundResult) {
        gameResults.add(newGameRoundResult)
    }

    fun resetGameResults() {
        gameResults.clear()
        correctAnswersTotal = 0
    }

    fun returnGameResults(): List<GameRoundResult> {
        return gameResults
    }
}