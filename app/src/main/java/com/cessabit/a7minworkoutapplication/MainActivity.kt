package com.cessabit.a7minworkoutapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toast
import com.cessabit.a7minworkoutapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

//        val flStartButton: FrameLayout = findViewById(R.id.flStart)
//        flStartButton.setOnClickListener { val intent = Intent(this,ExcerciseActivity::class.java)
//        startActivity(intent)}

        binding?.flStart?.setOnClickListener {
            val intent = Intent(this, ExcerciseActivity::class.java)
            startActivity(intent)
        }
        binding?.flBMI?.setOnClickListener {
            val intent = Intent(this, BMIActivity::class.java)
            startActivity(intent)
        }
        binding?.flHistory?.setOnClickListener{
            val intent = Intent(this,HistoryActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}