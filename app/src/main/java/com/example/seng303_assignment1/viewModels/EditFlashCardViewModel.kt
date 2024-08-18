package com.example.seng303_assignment1.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.seng303_assignment1.model.AnswerOption
import com.example.seng303_assignment1.model.FlashCard

class EditFlashCardViewModel: ViewModel() {
    var question by mutableStateOf("")
        private set

    var answerOptions by mutableStateOf(listOf(
        AnswerOption("", false),
        AnswerOption("", false), AnswerOption("", false)
    ))
        private set

    fun updateQuestion(newQuestion: String) {
        question = newQuestion
    }

    fun updateCorrectAnswer(inputtedCorrectAnswer: AnswerOption) {
        answerOptions.forEach {
            if (it == inputtedCorrectAnswer) {
                if (it.isCorrect) {
                    it.isCorrect = false
                } else {
                    it.isCorrect = true
                }

            }
        }
    }

    fun removeAnswerOption() {
        answerOptions = answerOptions.dropLast(1)
    }

    fun setCorrectAnswerFalse(index: Int) {
        answerOptions[index].isCorrect = false
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

    fun refreshAnswerOptions() {
        answerOptions = listOf(
            AnswerOption("", false),
            AnswerOption("", false), AnswerOption("", false)
        )
    }

    fun setDefaultValues(selectedFlashCard: FlashCard?) {
        selectedFlashCard?.let {
            question = it.question
            answerOptions = it.answerOptions
        }
    }
}