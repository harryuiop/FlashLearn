package com.example.seng303_assignment1.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.seng303_assignment1.model.AnswerOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NewFlashCardViewModel() : ViewModel() {

    private val _question = MutableStateFlow("")
    val question: StateFlow<String> = _question

    private val _answerOptions = MutableStateFlow<List<AnswerOption>>(List(3) { AnswerOption("", false) })
    val answerOptions: StateFlow<List<AnswerOption>> = _answerOptions

    private val _selectedAnswerIndex = MutableStateFlow(-1)
    val selectedAnswerIndex: StateFlow<Int> = _selectedAnswerIndex

    fun updateSelectedAnswerIndex(newIndex: Int) {
        _selectedAnswerIndex.value = newIndex
    }

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

    fun refreshQuestion() {
        _question.value= ""
    }

    fun refreshAnswerOptions() {
        _answerOptions.value = listOf(
            AnswerOption("", false),
            AnswerOption("", false),
            AnswerOption("", false)
        )
    }

    fun refreshSelectedIndex() {
        _selectedAnswerIndex.value = -1
    }

}


