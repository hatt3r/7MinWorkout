package com.cessabit.a7minworkoutapplication

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
import android.widget.Adapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.cessabit.a7minworkoutapplication.databinding.ActivityExcerciseBinding
import com.cessabit.a7minworkoutapplication.databinding.DialogCustomBackConfirmationBinding
import java.util.Locale

class ExcerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    // - variable for the rest counter timer that is 10 seconds
    //START
    private var restTimer: CountDownTimer? =
        null   //variable for rest timer and later will be initialized
    private var restProgress =
        0    //variable for timer progress. As Initial value the rest progress is set to 0.
    //END

    // - variables for exercise timer that is 30 seconds
    //START
    private var exerciseTimer: CountDownTimer? =
        null   //variable for the exercise timer and later will be initialized
    private var exerciseProgress =
        0    //variable exercise timer progress. as initial value is set to 0.
    //END

    private var tts: TextToSpeech? = null    //variable for Text To Speech

    //variable for Media player for playing a notification sound when the exercise is about to start
    //START
    private var player: MediaPlayer? = null
    //END

    //variable for the exercise list and current position of exercise
    //START
    private var exerciseList: ArrayList<ExerciseModel>? =
        null      //we will initialize the list later
    private var currentExercisePosition = -1        //current position of exercise
    //END

    //Declaring a variable of an adapter class to bind to recycler view
    //START
    //object will be initialized later
    private var exerciseAdapter: ExerciseStatusAdapter? = null
    //END


    private var restTimerDuration: Long =
        1   //this variable is used when testing by assigning value here we can change the time for rest timer because 1 is 1 second hence change accordingly
    private var exerciseTimerDuration: Long =
        1     //this variable is used when testing by assigning value here we can change the time for  Exercise timer because 1 is 1 second hence change accordingly

    private var binding: ActivityExcerciseBinding? = null   //Declaring a data binding Variable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //inflate the layout
        binding = ActivityExcerciseBinding.inflate(layoutInflater)

        //pass in binding.root to put the set content view
        setContentView(binding?.root)

        //then set support action bar and get toolbarexercise using binding
        setSupportActionBar(binding?.tbexcercise)

        //START
        tts = TextToSpeech(this, this)
        //END

        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        //initializing and assigning a default exercise list to our list variable
        //START
        exerciseList = Constants.defaultExerciseList()
        //END

        //on back clicking will open the custom dialog
        binding?.tbexcercise?.setNavigationOnClickListener {
            customeDialogForBackButton()
        }

        //setting the rest view
        setupRestView()

        //setting the exercise status recycler view updating it as well
        setupExerciseStatusRecyclerView()
    }

    override fun onBackPressed() {
        //on pressing the back button open the custom dialog
        customeDialogForBackButton()
        //super.onBackPressed()
    }

    /**
     * function is used to launch the custom confirmation dialog
     */
    //Performing the steps to show the custom dialog for back button confirmation while the exercise is going on
    //START
    private fun customeDialogForBackButton() {
        val customDialog = Dialog(this)

        //create a binding variable
        val dialogBinding = DialogCustomBackConfirmationBinding.inflate(layoutInflater)
        //setting the content view with binding root
        customDialog.setContentView(dialogBinding.root)

        //method used when clicking outside the dialog will cancel the dialog
        customDialog.setCanceledOnTouchOutside(true)

        //clicking on the button yes will finish the main activity
        //and dismiss the dialog as well
        dialogBinding.btnYes.setOnClickListener {
            this@ExcerciseActivity.finish()
            customDialog.dismiss()
        }

        //clicking on the button no will just dismiss the dialog
        dialogBinding.btnNo.setOnClickListener {
            customDialog.dismiss()
        }

        //just show the custom dialog
        customDialog.show()
    }

    /**
     * function is used to set up the recycler view to UI and assigning
     * the Layout manager and adapter class is attached to it
     */
    //binding adapter class to recycler view and setting the recycler view layout manager and passing the list to adapter
    //START
    private fun setupExerciseStatusRecyclerView() {
        //Defining a layout manager for the recycler view using binding
        //LinearLayout manager is used with horizontal scroll
        binding?.rvExerciseStatus?.layoutManager =
            LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false
            )

        //adapter expecting the exercise list and context to initialize it
        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!)
        //attaching the adapter for recycler view with exercise adapter
        binding?.rvExerciseStatus?.adapter = exerciseAdapter
    }

    //setting the get ready view with 10 seconds of timer
    //Start
    /**
     * function is used to set the timer for REST
     */
    private fun setupRestView() {

        //playing a notification sound when exercise is about to start when you are in the rest state
        //the sound file is added in the raw folder as resource
        //START
        /**
         * here the sound file is added in to "raw" folder in resource
         * and played using MediaPlayer. Media Player class can be used to control playback
         * of audio/video files and streams.
         **/
        try {
            val soundURI =
                Uri.parse(
                    "android.resource://com.cessabit.a7minworkoutapplication/" + R.raw.press_start
                )
            player = MediaPlayer.create(applicationContext, soundURI)
            player?.isLooping = false
            player?.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        //END

        binding?.flRestView?.visibility = View.VISIBLE
        binding?.tvTitle?.visibility = View.VISIBLE
        binding?.tvExerciseName?.visibility = View.INVISIBLE
        binding?.flExerciseView?.visibility = View.INVISIBLE
        binding?.ivImage?.visibility = View.INVISIBLE
        binding?.tvUpcomingLabel?.visibility = View.VISIBLE
        binding?.tvUpcomingExerciseName?.visibility = View.VISIBLE
        /**
         * Here firstly we will check is the timer is running and its not null then cancel the running timer and start the new one\
         * set the progress to initial which is 0
         */
        if (restTimer != null) {
            restTimer?.cancel()
            restProgress = 0
        }

        //setting the upcoming exercise name in the UI element
        //START
        //here we have set the upcoming exercise name to the text view
        //here as the current position is -1 by default so to selected from the list it should be 0 so we have increased it by +1
        speakOUt("please rest for 10 seconds and the next exercise is ${exerciseList!![currentExercisePosition + 1].getName()}")
        binding?.tvUpcomingExerciseName?.text =
            exerciseList!![currentExercisePosition + 1].getName()
        //function to set the progress bar details
        setRestProgressBar()

    }

    private fun setupExerciseView() {
        binding?.flRestView?.visibility = View.INVISIBLE
        binding?.tvTitle?.visibility = View.INVISIBLE
        binding?.tvExerciseName?.visibility = View.VISIBLE
        binding?.flExerciseView?.visibility = View.VISIBLE
        binding?.ivImage?.visibility = View.VISIBLE
        binding?.tvUpcomingLabel?.visibility = View.INVISIBLE
        binding?.tvUpcomingExerciseName?.visibility = View.INVISIBLE

        if (exerciseTimer != null) {
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }

        speakOUt(exerciseList!![currentExercisePosition].getName())
        binding?.ivImage?.setImageResource(exerciseList!![currentExercisePosition].getImage())
        binding?.tvExerciseName?.text = exerciseList!![currentExercisePosition].getName()

        setExerciseProgressBar()

    }

    private fun setRestProgressBar() {
        binding?.progressBar?.progress = restProgress

        restTimer = object : CountDownTimer(restTimerDuration * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                binding?.progressBar?.progress = 10 - restProgress
                binding?.tvTimer?.text = (10 - restProgress).toString()
            }

            override fun onFinish() {
                currentExercisePosition++
                exerciseList!![currentExercisePosition].setIsSelected(true)
                exerciseAdapter!!.notifyDataSetChanged()

                setupExerciseView()
            }
        }.start()
    }

    private fun setExerciseProgressBar() {
        binding?.progressBarExercise?.progress = exerciseProgress

        exerciseTimer = object : CountDownTimer(exerciseTimerDuration * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++
                binding?.progressBarExercise?.progress = 30 - exerciseProgress
                binding?.tvTimerExercise?.text = (30 - exerciseProgress).toString()
            }

            override fun onFinish() {
                if (currentExercisePosition < exerciseList?.size!! - 1) {
                    exerciseList!![currentExercisePosition].setIsSelected(false)
                    exerciseList!![currentExercisePosition].setIsCompleted(true)
                    exerciseAdapter!!.notifyDataSetChanged()
                    setupRestView()
                } else {
                    finish()
                    val intent = Intent(this@ExcerciseActivity, FinishActivity::class.java)
                    startActivity(intent)
                }
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (restTimer != null) {
            restTimer?.cancel()
            restProgress = 0
        }
        if (exerciseTimer != null) {
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        if (player != null) {
            player!!.stop()
        }

        binding = null
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e(
                    "TTS",
                    "The Language specified is not supported"
                )
            }
        } else {
            Log.e(
                "TTS",
                "Initialization Failed"
            )
        }
    }

    private fun speakOUt(text: String) {
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

}