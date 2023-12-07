package com.cessabit.a7minworkoutapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import com.cessabit.a7minworkoutapplication.databinding.ActivityExcerciseBinding

class ExcerciseActivity : AppCompatActivity() {

    private var restTimer: CountDownTimer? = null
    private var restProgress = 0
    private var exerciseTimer: CountDownTimer? = null
    private var exerciseProgress = 0

    private var binding: ActivityExcerciseBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExcerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setSupportActionBar(binding?.tbexcercise)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        binding?.tbexcercise?.setNavigationOnClickListener {
            onBackPressed()
        }

        setupRestView()
    }

    private fun setupRestView() {
        if(restTimer!=null)
        {
            restTimer?.cancel()
            restProgress = 0
        }

        setRestProgressBar()

    }
    private fun setupExerciseView() {
        binding?.flProgressBar?.visibility = View.INVISIBLE
        binding?.tvTitle?.text = "Exercise Name"
        binding?.flExerciseView?.visibility = View.VISIBLE

        if(exerciseTimer!=null)
        {
            exerciseTimer?.cancel()
            exerciseProgress= 0
        }

        setExerciseProgressBar()

    }

    private fun setRestProgressBar() {
        binding?.progressBar?.progress = restProgress

        restTimer = object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                binding?.progressBar?.progress = 10 - restProgress
                binding?.tvTimer?.text = (10 - restProgress).toString()
            }

            override fun onFinish() {
                setupExerciseView()
            }
        }.start()
    }

    private fun setExerciseProgressBar() {
        binding?.progressBarExercise?.progress = exerciseProgress

        exerciseTimer = object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++
                binding?.progressBarExercise?.progress = 10 - exerciseProgress
                binding?.tvTimerExercise?.text = (10 - exerciseProgress).toString()
            }

            override fun onFinish() {
                Toast.makeText(
                    this@ExcerciseActivity,
                    "30 seconds are over, lets go to rest view",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }.start()
    }
    override fun onDestroy() {
        super.onDestroy()
        if (restTimer != null) {
            restTimer?.cancel()
            restProgress = 0
        }
        if(exerciseTimer!=null)
        {
            exerciseTimer?.cancel()
            exerciseProgress= 0
        }

        binding = null
    }
}