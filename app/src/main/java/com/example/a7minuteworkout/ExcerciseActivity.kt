package com.example.a7minuteworkout

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_excercise.*
import kotlinx.android.synthetic.main.exit_exercise_dialog.*
import java.util.*
import kotlin.collections.ArrayList

class ExcerciseActivity : AppCompatActivity() , TextToSpeech.OnInitListener{

    private var tts: TextToSpeech? = null

    private var player: MediaPlayer? = null

    private var restTimer: CountDownTimer? = null
    private var restProgress = 0
    private var restTimerDuration: Long = 1 //10

    private var exerciseTimer: CountDownTimer? = null
    private var exerciseProgress = 0
    private var exerciseTimerDuration: Long = 1 //30

    private var exerciseAdapter: ExerciseStatusAdapter? = null

    private var exerciseList: ArrayList<ExerciseModel>? = null
    private var currentExerciseModel = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_excercise)

        tts = TextToSpeech(this, this)

        setSupportActionBar(toolbar_exercise_activity)
        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        toolbar_exercise_activity.setNavigationOnClickListener {
            showCustomExitDialog()
        }

        exerciseList = Constants.defaultExerciseList()

        setupRestView()

        setupExerciseStatusRecyclerView()
    }

    private fun speakOut(text: String){
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    private fun setRestProgressBar(){
        progress_bar.progress = restProgress
        restTimer = object: CountDownTimer(restTimerDuration* 1000, 1000){
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                progress_bar.progress = restTimerDuration.toInt() -restProgress
                tv_timer.text = (restTimerDuration.toInt() - restProgress).toString()
            }

            override fun onFinish() {
                currentExerciseModel++
                exerciseList!![currentExerciseModel].setIsSelected(true)
                exerciseAdapter!!.notifyDataSetChanged()
                //Toast.makeText(this@ExcerciseActivity, "Here now we will start the exercise.", Toast.LENGTH_SHORT).show()
                setupExerciseView()
            }
        }.start()
    }

    private fun setExerciseProgressBar(){
        exercise_progress_bar.progress = exerciseProgress
        exerciseTimer = object: CountDownTimer(exerciseTimerDuration * 1000, 1000){
            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++
                exercise_progress_bar.progress = exerciseTimerDuration.toInt()-exerciseProgress
                tv_exercise_timer.text = (exerciseTimerDuration.toInt() - exerciseProgress).toString()
            }

            override fun onFinish() {
                if(currentExerciseModel < exerciseList?.size!! -1){
                    exerciseList!![currentExerciseModel].setIsSelected(false)
                    exerciseList!![currentExerciseModel].setIsCompleted(true)
                    exerciseAdapter!!.notifyDataSetChanged()
                    setupRestView()
                }else{
                    //Toast.makeText(this@ExcerciseActivity, "Congratulations! You have completed the 7 minutes workout", Toast.LENGTH_SHORT).show()
                    finish()
                    val intent = Intent(this@ExcerciseActivity, FinishActivity::class.java)
                    startActivity(intent)
                }

            }
        }.start()
    }

    override fun onDestroy() {
        if(restTimer != null){
            restTimer!!.cancel()
            restProgress = 0
        }

        if(exerciseTimer != null){
            exerciseTimer!!.cancel()
            exerciseProgress = 0
        }

        if(tts != null){
            tts!!.stop()
            tts!!.shutdown()
        }

        if(player != null){
            player!!.stop()
        }

        super.onDestroy()
    }

    private fun setupExerciseView(){
        ll_rest_view.visibility = View.GONE
        ll_exercise_routine.visibility = View.VISIBLE
        if(exerciseTimer != null){
            exerciseTimer!!.cancel()
            exerciseProgress = 0
        }
        exercise_progress_bar.max = exerciseTimerDuration.toInt()
        exercise_progress_bar.progress = exerciseTimerDuration.toInt()
        setExerciseProgressBar()

        iv_image.setImageResource(exerciseList!![currentExerciseModel].getImage())
        tv_exercise_name.text = exerciseList!![currentExerciseModel].getName()

        speakOut(tv_exercise_name.text.toString())
    }

    private fun setupRestView(){
        ll_rest_view.visibility = View.VISIBLE
        ll_exercise_routine.visibility = View.GONE

        try {
            //val soundUri = Uri.parse("adroid:resource://com.example.7minuteworkout/")
            player = MediaPlayer.create(applicationContext, R.raw.press_start)
            player!!.isLooping = false
            player!!.start()
        }catch(e: Exception){
            e.printStackTrace()
        }

        if(restTimer != null){
            restTimer!!.cancel()
            restProgress = 0
        }


        tv_upcoming_exercise.text = exerciseList!![currentExerciseModel+1].getName()

        setRestProgressBar()
    }

    override fun onInit(status: Int) {
        if(status == TextToSpeech.SUCCESS){
            val result = tts!!.setLanguage(Locale.US)

            if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                Log.e("TTS", "The language specified is not supported")
            }
        }else{
            Log.e("TTS", "Initialization failed")
        }
    }

    private fun setupExerciseStatusRecyclerView(){
        rv_exercise_status.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!, this)
        rv_exercise_status.adapter = exerciseAdapter
    }

    private fun showCustomExitDialog(){
        val customDialog = Dialog(this)
        /*Set the screen content from a layout resource.
        * The resource will be inflated, adding all top-level views to the screen */
        customDialog.setContentView(R.layout.exit_exercise_dialog)

        customDialog.btn_yes_quit.setOnClickListener{
            //Toast.makeText(applicationContext, "clicked submit", Toast.LENGTH_SHORT).show()
            finish()
            customDialog.dismiss()
        }
        customDialog.btn_no_quit.setOnClickListener{
            //Toast.makeText(applicationContext, "clicked cancel", Toast.LENGTH_SHORT).show()
            customDialog.dismiss()
        }

        //Start the dialog and display it on screen
        customDialog.show()
    }
}