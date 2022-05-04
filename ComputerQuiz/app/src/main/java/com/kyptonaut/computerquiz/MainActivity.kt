package com.kyptonaut.computerquiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val easyBtn = findViewById<Button>(R.id.easyBtn)
        easyBtn.setOnClickListener() {
            start("easy")
        }

        val hardBtn = findViewById<Button>(R.id.hardBtn)
        hardBtn.setOnClickListener() {
            start("medium")
        }

        val nightmareBtn = findViewById<Button>(R.id.nightmareBtn)
        nightmareBtn.setOnClickListener() {
            start("hard")
        }


    }

    // Function for starting the app and choosing difficulty
    fun start(difficulty: String) {
        val intent = Intent(this, StartGame::class.java)
        intent.putExtra("difficulty", difficulty)
        startActivity(intent)
        finish()
    }
}