package com.fizhu.qrcode

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fizhu.qrcode.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onInit()
    }

    private fun onInit() {
        binding.btnScan.setOnClickListener { startActivity(Intent(this, ScanActivity::class.java)) }
        binding.btnGenerate.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    GenerateActivity::class.java
                )
            )
        }
    }
}