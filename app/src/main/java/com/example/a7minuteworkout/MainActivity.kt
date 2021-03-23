package com.example.a7minuteworkout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.a7minuteworkout.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ll_start.setOnClickListener{
            val intent = Intent(this, ExcerciseActivity::class.java)
            startActivity(intent)
            //Toast.makeText(this, "Starting aplication", Toast.LENGTH_LONG).show()
        }

        ll_bmi.setOnClickListener {
            val intent = Intent(this, BMIActivity::class.java)
            startActivity(intent)
            //Toast.makeText(this, "Starting aplication", Toast.LENGTH_LONG).show()
        }

        ll_history.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
            //Toast.makeText(this, "Starting aplication", Toast.LENGTH_LONG).show()
        }
    }
}