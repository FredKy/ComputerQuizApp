package com.kyptonaut.computerquiz

data class QuestionObject(
    val category: String,
    val correct_answer: String,
    val difficulty: String,
    val incorrect_answers: MutableList<String>,
    val question: String,
    val type: String
)