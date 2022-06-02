package com.kyptonaut.computerquiz.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kyptonaut.computerquiz.ApiInterface
import com.kyptonaut.computerquiz.BASE_URL
import com.kyptonaut.computerquiz.QuestionObject
import com.kyptonaut.computerquiz.ResponseData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class QuestionsViewModel() : ViewModel() {

    var questionObjects: MutableList<QuestionObject> = mutableListOf<QuestionObject>()
    private var liveDataObject: MutableLiveData<MutableList<QuestionObject>> = MutableLiveData<MutableList<QuestionObject>>()

    init {
        questionObjects = mutableListOf<QuestionObject>()
        liveDataObject = MutableLiveData<MutableList<QuestionObject>>()
    }

    fun getQuestions(): MutableList<QuestionObject> {
        return questionObjects
    }

    fun getLiveDataObject(): MutableLiveData<MutableList<QuestionObject>> {
        return liveDataObject
    }

    fun startApiCall(difficulty: String) {
        logThread("getTheData")
        println("Starting API Call")

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
                    liveDataObject.postValue(questionObjects)
                } else {
                    liveDataObject.postValue(null)
                }
            }

            override fun onFailure(call: Call<ResponseData>, t: Throwable) {
                Log.d("StartGame", "onFailure:"  + t.message)
                liveDataObject.postValue(null)
            }
        })
    }

    private fun logThread(s: String) {

    }

}