package com.example.samaan.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.samaan.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShoppingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping)
    }
}