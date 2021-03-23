package com.example.a7minuteworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_b_m_i.*
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {

    val METRICS_UNITS_VIEW = "METRIC_UNIT_VIEW"
    val US_UNITS_VIEW = "US_UNIT_VIEW"

    var currentVisibeView: String = "METRIC_UNIT_VIEW"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_b_m_i)

        setSupportActionBar(toolbar_bmi_activity)
        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.title = "CALCULATE BMI"
        }
        toolbar_bmi_activity.setNavigationOnClickListener {
            onBackPressed()
        }

        btn_calculate.setOnClickListener {
            if(currentVisibeView.equals(METRICS_UNITS_VIEW)) {
                if (validateMetricUnit()) {
                    val heightValue: Float = et_metric_height.text.toString().toFloat() / 100
                    val weightValue: Float = et_metric_weight.text.toString().toFloat()

                    val bmi = weightValue / (heightValue * heightValue)
                    displayBMIResult(bmi)
                } else {
                    Toast.makeText(
                        this@BMIActivity,
                        "Please enter valid values.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }else{
                if (validateUsUnit()) {
                    val feetValue: String = et_us_feet.text.toString()
                    val inchValue: String = et_us_inch.text.toString()
                    val weightValue: Float = et_us_weight.text.toString().toFloat()

                    val heightValue = inchValue.toFloat() + feetValue.toFloat() * 12

                    val bmi = 703 * (weightValue / (heightValue * heightValue))
                    displayBMIResult(bmi)
                } else {
                    Toast.makeText(
                        this@BMIActivity,
                        "Please enter valid values.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        makeVisibleMetricUnitsView()
        rg_metrics.setOnCheckedChangeListener { group, checkedId ->
            if(checkedId == R.id.rb_metric_units){
                makeVisibleMetricUnitsView()
            }else{
                makeVisibleUsUnitsView()
            }
        }
    }

    private fun makeVisibleUsUnitsView(){
        currentVisibeView = US_UNITS_VIEW
        ll_us_calc.visibility = View.VISIBLE
        ll_metric_calc.visibility = View.GONE
        ll_bmi_results.visibility = View.INVISIBLE

        et_us_weight.text!!.clear()
        et_us_feet.text!!.clear()
        et_us_inch.text!!.clear()
    }

    private fun makeVisibleMetricUnitsView(){
        currentVisibeView = METRICS_UNITS_VIEW
        ll_metric_calc.visibility = View.VISIBLE
        ll_us_calc.visibility = View.GONE
        ll_bmi_results.visibility = View.INVISIBLE

        et_metric_weight.text!!.clear()
        et_metric_height.text!!.clear()
    }

    private fun displayBMIResult(bmi: Float){
        val bmiLabel: String
        val bmiDescription: String

        if(bmi.compareTo(15f) <= 0){
            bmiLabel = "Very severely underweight"
            bmiDescription = "Oops! You really need to take care of you better! Eat more!"
        }else if (bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0){
            bmiLabel = "Severely underweight"
            bmiDescription = "Oops! You really need to take care of you better! Eat more!"
        }else if (bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0){
            bmiLabel = "Undeweight"
            bmiDescription = "Oops! You really need to take care of you better! Eat more!"
        }else if (bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0){
            bmiLabel = "Normal"
            bmiDescription = "Congratulations! You are in a good shape!"
        }else if (bmi.compareTo(25f) > 0 && bmi.compareTo(30f) <= 0){
            bmiLabel = "Overweight"
            bmiDescription = "Oops! You really need to take care of you better! Workout more!"
        }else if (bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0){
            bmiLabel = "Obese Class | (Moderately obese)"
            bmiDescription = "Oops! You really need to take care of you better! Workout more!"
        }else if (bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0){
            bmiLabel = "Obese Class || (Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        }else{
            bmiLabel = "Obese Class ||| (Very Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        }
        ll_bmi_results.visibility = View.VISIBLE

        val bmiValue = BigDecimal(bmi.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()

        tv_bmi.text = bmiValue
        tv_bmi_type.text = bmiLabel
        tv_bmi_description.text = bmiDescription
    }

    private fun validateMetricUnit():Boolean{
        var isValid = true
        if(et_metric_weight.text.toString().isEmpty()) {
            isValid = false
        }else if(et_metric_height.text.toString().isEmpty()){
            isValid = false
        }
        return isValid
    }

    private fun validateUsUnit():Boolean{
        var isValid = true
        when {
            et_us_weight.text.toString().isEmpty() -> isValid = false
            et_us_feet.text.toString().isEmpty() -> isValid = false
            et_us_inch.text.toString().isEmpty() -> isValid = false
        }
        return isValid
    }
}