package com.kyptonaut.computerquiz

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class GameOver : AppCompatActivity() {

    lateinit var tvScore: TextView
    lateinit var tvEasyHighScore: TextView
    lateinit var tvHardHighScore: TextView
    lateinit var tvNightmareHighScore: TextView
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_over)
        var score: Int = getIntent().extras!!.getInt("score")
        var difficulty: String = getIntent().extras!!.getString("difficulty").toString()
        println("Game over screen difficulty variable: " + difficulty)
        val tvScore = findViewById<TextView>(R.id.tvScore)

        tvScore.setText("" + score.toString())
        sharedPreferences = getSharedPreferences("pref", 0)

        val tvEasyHighScore = findViewById<TextView>(R.id.tvEasyHighScore)
        var scoreEasySP: Int = sharedPreferences.getInt("scoreEasySP", 0)
        val tvHardHighScore = findViewById<TextView>(R.id.tvHardHighScore)
        var scoreHardSP: Int = sharedPreferences.getInt("scoreHardSP", 0)
        val tvNightmareHighScore = findViewById<TextView>(R.id.tvNightmareHighScore)
        var scoreNightmareSP: Int = sharedPreferences.getInt("scoreNightmareSP", 0)

        val editor: SharedPreferences.Editor = sharedPreferences.edit()

        when (difficulty) {
            "easy" -> {
                if (score > scoreEasySP) {
                    scoreEasySP = score
                    editor.putInt("scoreEasySP", scoreEasySP)
                }
            }
            "medium" -> {
                if (score > scoreHardSP) {
                    scoreHardSP = score
                    editor.putInt("scoreHardSP", scoreHardSP)
                }
            }
            "hard" -> {
                if (score > scoreNightmareSP) {
                    scoreNightmareSP = score
                    editor.putInt("scoreNightmareSP", scoreNightmareSP)
                }
            } else -> {
                println("Unknown difficulty.")
            }
        }
        editor.commit()
        tvEasyHighScore.setText("" + scoreEasySP.toString())
        tvHardHighScore.setText("" + scoreHardSP.toString())
        tvNightmareHighScore.setText("" + scoreNightmareSP.toString())




        /*val f: File? = getDatabasePath("pref.xml")

        if (f != null) Log.d("Location", f.absolutePath)*/
    }

    fun restart() {
        val intent = Intent(this@GameOver, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun close() {
        finish()
    }
}