package com.example.seng303_assignment1.model

import com.example.seng303_assignment1.datastore.Identifiable

data class FlashCard(
    val id: Int,
    val question: String,
    val answerOptions: List<AnswerOption>,
    val correctAnswers: List<AnswerOption>
) : Identifiable {

    override fun getIdentifier(): Int {
        return id
    }

    companion object {
        fun getFlashCards(): List<FlashCard> {
            return listOf(
                FlashCard(
                    id = 1,
                    question = "test",
                    answerOptions = listOf(AnswerOption("test2", isCorrect = true)),
                    correctAnswers = emptyList()
                )
            )
        }
    }
}