package com.cessabit.a7minworkoutapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.cessabit.a7minworkoutapplication.databinding.ActivityBmiBinding
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {
    companion object {
        private const val METRIC_UNTIS_VIEW = "METRIC_UNIT_VIEW" //metric unit view
        private const val US_UNITS_VIEW = "US_UNIT_VIEW"  //US Unit View
    }

    private var binding: ActivityBmiBinding? = null

    private var currentVisibleView: String = METRIC_UNTIS_VIEW


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        setSupportActionBar(binding?.toolbarbmiactivity)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "CALCULATE BMI"
        }
        binding?.toolbarbmiactivity?.setNavigationOnClickListener {
            onBackPressed()
        }

        makeVisibleMetricUnitsView()

        binding?.rgUnits?.setOnCheckedChangeListener { _, checkedId: Int ->
            if (checkedId == R.id.rbMetricUnits) {
                makeVisibleMetricUnitsView()
            } else {
                makeVisibleUSUnitsView()
            }
        }

        binding?.btnCalculateUnits?.setOnClickListener {
            if (validateMetricUnits()) {
                val heightvalue: Float =
                    binding?.etMetricUnitHeight?.text.toString().toFloat() / 100
                val weightvalue: Float = binding?.etMetricUnitWeight?.text.toString().toFloat()

                val bmi = weightvalue / (heightvalue * heightvalue)
                displayBMIResult(bmi)
            } else {
                Toast.makeText(
                    this,
                    "Please Enter Valid values",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }


    private fun makeVisibleMetricUnitsView() {
        currentVisibleView = METRIC_UNTIS_VIEW
        binding?.tilMetricUnitHeight?.visibility = View.VISIBLE
        binding?.tilMetricUnitWeight?.visibility = View.VISIBLE
        binding?.tilUsMetricUnitWeight?.visibility = View.GONE
        binding?.tilMetricUsUnitHeightFeet?.visibility = View.GONE
        binding?.tilMetricUsUnitHeightInch?.visibility = View.GONE

        binding?.etMetricUnitHeight?.text!!.clear()
        binding?.etMetricUnitWeight?.text!!.clear()

        binding?.llDiplayBMIResult?.visibility = View.INVISIBLE

    }

    private fun makeVisibleUSUnitsView() {
        currentVisibleView = US_UNITS_VIEW
        binding?.tilMetricUnitHeight?.visibility = View.INVISIBLE
        binding?.tilMetricUnitWeight?.visibility = View.INVISIBLE
        binding?.tilUsMetricUnitWeight?.visibility = View.VISIBLE
        binding?.tilMetricUsUnitHeightFeet?.visibility = View.VISIBLE
        binding?.tilMetricUsUnitHeightInch?.visibility = View.VISIBLE

        binding?.etUsMetricUnitWeight?.text!!.clear()
        binding?.etUsMetricUnitHeightFeet?.text!!.clear()
        binding?.etUsMetricUnitHeightFeet?.text!!.clear()

        binding?.llDiplayBMIResult?.visibility = View.INVISIBLE

    }

    private fun displayBMIResult(bmi: Float) {

        val bmilabel: String
        val bmiDescription: String
        if (bmi.compareTo(15f) <= 0) {
            bmilabel = "Very Severely UnderWeight"
            bmiDescription =
                "Oops! You really need to take better care of yourself! eat more healthy!"
        } else if (bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0) {
            bmilabel = "severely UnderWeight"
            bmiDescription =
                "Oops! You really need to take better care of yourself! eat more healthy!"
        } else if (bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0) {
            bmilabel = "Underweight"
            bmiDescription =
                " Oops! You really need to take better care of yourself! eat more healthy!"
        } else if (bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0) {
            bmilabel = "Normal"
            bmiDescription = "Congratulations! You are in great shape!"
        } else if (bmi.compareTo(25f) > 0 && bmi.compareTo(30f) <= 0) {
            bmilabel = "OverWeight"
            bmiDescription = "Oops! You really need to take better care of yourself! Workout more!"
        } else if (bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0) {
            bmilabel = "Obese Class || (Moderately Obese)"
            bmiDescription = "Oops! You really need to take better care of yourself! Workout more!"
        } else if (bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0) {
            bmilabel = "Obese Class || (Severely Obese)"
            bmiDescription = " OMG! You are in a very dangerous condition! Act Now!"
        } else {
            bmilabel = "Obese Class ||| (Very Severely Obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act Now!"
        }

        val bmiValue = BigDecimal(bmi.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()

        binding?.llDiplayBMIResult?.visibility = View.VISIBLE
        binding?.tvBMIValue?.text = bmiValue
        binding?.tvBMIType?.text = bmilabel
        binding?.tvBMIDescription?.text = bmiDescription

    }


    private fun validateMetricUnits(): Boolean {
        var isValid = true
        if (binding?.etMetricUnitWeight?.text.toString().isEmpty()) {
            isValid = false
        } else if (binding?.etMetricUnitHeight?.text.toString().isEmpty()) {
            isValid = false
        }
        return isValid
    }

}