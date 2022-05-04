package com.kyptonaut.computerquiz

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import java.io.File

class GameOver : AppCompatActivity() {

    //Variable for holding Score during 1 session.
    lateinit var tvScore: TextView

    //Variables refering to saved high scores.
    lateinit var tvEasyHighScore: TextView
    lateinit var tvHardHighScore: TextView
    lateinit var tvNightmareHighScore: TextView

    //Object responsible for storing high scores.
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_over)

        //Get final score
        var score: Int = getIntent().extras!!.getInt("score")

        var difficulty: String = getIntent().extras!!.getString("difficulty").toString()
        val tvScore = findViewById<TextView>(R.id.tvScore)

        tvScore.setText("" + score.toString())

        //High scores will be stored in pref.xml on device
        sharedPreferences = getSharedPreferences("pref", 0)

        //Get high score and display in text views, default values 0.
        val tvEasyHighScore = findViewById<TextView>(R.id.tvEasyHighScore)
        var scoreEasySP: Int = sharedPreferences.getInt("scoreEasySP", 0)
        val tvHardHighScore = findViewById<TextView>(R.id.tvHardHighScore)
        var scoreHardSP: Int = sharedPreferences.getInt("scoreHardSP", 0)
        val tvNightmareHighScore = findViewById<TextView>(R.id.tvNightmareHighScore)
        var scoreNightmareSP: Int = sharedPreferences.getInt("scoreNightmareSP", 0)

        val editor: SharedPreferences.Editor = sharedPreferences.edit()

        //See if current high score in the played difficulty has been beaten.
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

        //Commit changes and display scores
        editor.commit()
        tvEasyHighScore.setText("" + scoreEasySP.toString())
        tvHardHighScore.setText("" + scoreHardSP.toString())
        tvNightmareHighScore.setText("" + scoreNightmareSP.toString())


        //Code for printing out path to pref.xml on device
        //Should be: data/data/com.kyptonaut.computerquiz/shared_prefs/pref.xml
        val f: File? = getDatabasePath("pref.xml")

        if (f != null) Log.d("Location", f.absolutePath)
    }

    //Restart program
    fun restart() {
        val intent = Intent(this@GameOver, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    //Quit
    fun close() {
        finish()
    }
}