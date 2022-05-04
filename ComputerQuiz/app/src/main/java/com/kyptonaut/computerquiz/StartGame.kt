package com.kyptonaut.computerquiz

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

const val BASE_URL: String = "https://opentdb.com/"

class StartGame : AppCompatActivity() {

    lateinit var tvTimer: TextView
    lateinit var tvQuestion: TextView
    var index: Int = 0
    var score: Int = 0
    lateinit var btn1: Button
    lateinit var btn2: Button
    lateinit var btn3: Button
    lateinit var btn4: Button
    lateinit var tvScore: TextView
    lateinit var countDownTimer: CountDownTimer
    var millisUntilFinished: Long = 10000
    var questionObjects: MutableList<QuestionObject> = mutableListOf<QuestionObject>()
    var difficulty: String = "easy"


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_game)

        //Extract difficulty variable to use when fetching questions from API
        difficulty = getIntent().extras!!.getString("difficulty").toString()

        //Assign variables for elements in UI.
        tvTimer = findViewById(R.id.tvTimer)
        btn1 = findViewById(R.id.btn1)
        btn2 = findViewById(R.id.btn2)
        btn3= findViewById(R.id.btn3)
        btn4 = findViewById(R.id.btn4)
        tvScore = findViewById(R.id.tvScore)
        tvQuestion = findViewById(R.id.tvQuestionText)

        //Fetch questions from API, starts the game when after response is received and handled.
        getTheData()

    }

    //Main game loop, runs until the app has run out of questions.
    fun startGame() {
        println("startGame")
        millisUntilFinished = 20000
        tvTimer.setText("" + (millisUntilFinished / 1000) + "s")
        tvScore.setText(score.toString() + " / " + questionObjects.size)
        generateQuestion(index)
        //Timer for each question, will automatically move on to next question if timer reaches zero.
        countDownTimer = object : CountDownTimer(millisUntilFinished, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                println("Tick Tock")
                tvTimer?.setText("" + millisUntilFinished / 1000 + "s")
            }

            override fun onFinish() {
                println("onFinish")
                //Increment index to move to next question.
                index++
                if (index > questionObjects.size -1) {
                    btn1.setVisibility(View.GONE)
                    btn2.setVisibility(View.GONE)
                    btn3.setVisibility(View.GONE)
                    btn4.setVisibility(View.GONE)
                    intent = Intent(this@StartGame, GameOver::class.java)
                    intent.putExtra("score",score)
                    intent.putExtra("difficulty",difficulty)
                    startActivity(intent)
                    finish()
                } else {
                    startGame()
                }

            }
        }.start()
    }

    //Function for displaying one question, one correct answer and three incorrect answers on buttons.
    fun generateQuestion(index: Int) {
        println("generateQuestion")

        var correctAnswer = questionObjects.get(index).correct_answer
        var allAnswersTemp: ArrayList<String> = questionObjects.get(index).incorrect_answers as ArrayList<String>
        allAnswersTemp.add(correctAnswer)
        allAnswersTemp.shuffle()
        btn1.setText(allAnswersTemp.get(0))
        btn2.setText(allAnswersTemp.get(1))
        btn3.setText(allAnswersTemp.get(2))
        btn4.setText(allAnswersTemp.get(3))
        tvQuestion.text = questionObjects.get(index).question
    }

    private fun logThread(methodName: String) {
        println("debug: ${methodName}: ${Thread.currentThread().name}")
    }

    //Fetch questions from API and start main game loop.
   fun getTheData() {
        logThread("getTheData")

        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.getData("api.php?amount=20&category=18&difficulty="+difficulty+"&type=multiple")

        retrofitData.enqueue(object : Callback<ResponseData> {
            override fun onResponse(call: Call<ResponseData>, response: Response<ResponseData>) {
                if (response.isSuccessful) {
                    //println("SUCCESS!")
                    val rB = response.body()!!

                    for (item in rB.results) {
                        var question: String = item.question.replace("&#039;","'").replace("&quot;","\"")
                        var correct_answer: String = item.correct_answer.replace("&#039;","'").replace("&quot;","\"")
                        var inc_answers: List<String> = item.incorrect_answers
                        var inc_answers_fixed: MutableList<String> = mutableListOf()
                        for (el in inc_answers) {
                            inc_answers_fixed.add(el.replace("&#039;","'").replace("&quot;","\""))
                        }
                        questionObjects.add(QuestionObject(item.category,correct_answer,item.difficulty, inc_answers_fixed, question, item.type))
                    }

                    startGame()

                }

            }

            override fun onFailure(call: Call<ResponseData>, t: Throwable) {
                Log.d("StartGame", "onFailure:"  + t.message)
            }
        })

    }

    //Function for moving on to the next question. Moves to GameOver screen if all questions have been answered.
    fun nextQuestion() {
        countDownTimer.cancel()
        index++
        if (index > questionObjects.size -1) {
            btn1.setVisibility(View.GONE)
            btn2.setVisibility(View.GONE)
            btn3.setVisibility(View.GONE)
            btn4.setVisibility(View.GONE)
            val intent = Intent(this@StartGame, GameOver::class.java)
            intent.putExtra("score", score)
            intent.putExtra( "difficulty", difficulty)
            startActivity(intent)
            finish()
        } else {
            startGame()
        }
    }

    //Function for handling answer button functionality.
    fun answerSelected(view: View) {
        println("answerSelected")
        var answer: String = (view as Button).getText().toString().trim()

        if (!answer.equals("")) {
            countDownTimer.cancel()

            var correctAnswer: String = questionObjects.get(index).correct_answer
            if (answer.equals(correctAnswer)) {
                score++
                tvScore.setText(score.toString() + " / " + questionObjects.size)
                println("Correct answer!")
            } else {
                println("Wrong answer!")
            }
            nextQuestion()
        }

    }

}