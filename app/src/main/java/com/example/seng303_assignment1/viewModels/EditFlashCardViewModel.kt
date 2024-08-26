package com.example.seng303_assignment1.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.seng303_assignment1.model.AnswerOption
import com.example.seng303_assignment1.model.FlashCard
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class EditFlashCardViewModel: ViewModel() {
    private var _question = MutableStateFlow("")
    val question: StateFlow<String> = _question

    private var _answerOptions = MutableStateFlow<List<AnswerOption>>(List(3) { AnswerOption("", false) })
    val answerOptions: StateFlow<List<AnswerOption>> = _answerOptions

    fun updateQuestion(newQuestion: String) {
        _question.value = newQuestion
    }

    fun updateCorrectAnswer(inputtedCorrectAnswer: AnswerOption) {
        _answerOptions.value = _answerOptions.value.map { option ->
            if (option == inputtedCorrectAnswer) {
                option.copy(isCorrect = !option.isCorrect)
            } else {
                option
            }
        }
    }

    fun setCorrectAnswerFalse(index: Int) {
        _answerOptions.value = _answerOptions.value.mapIndexed { i, option ->
            if (i == index) {
                option.copy(isCorrect = false)
            } else {
                option
            }
        }
    }

    fun updateAnswerOptions(index: Int, newAnswer: String, isCorrect: Boolean) {
        _answerOptions.value = _answerOptions.value.mapIndexed { i, option ->
            if (i == index) {
                option.copy(answerText = newAnswer, isCorrect = isCorrect)
            } else {
                option
            }
        }
    }

    fun addAnswerOption() {
        val newList = _answerOptions.value + AnswerOption("", false)
        _answerOptions.value = newList
        Log.e("list", newList.toString())
    }

    fun removeAnswerOption() {
        if (_answerOptions.value.size > 3) {
            val newList = _answerOptions.value.dropLast(1)
            _answerOptions.value = newList
        }
    }

    fun setDefaultValues(selectedFlashCard: FlashCard?) {
        selectedFlashCard?.let {
            _question.value = it.question
            _answerOptions.value = it.answerOptions
        }
    }
}