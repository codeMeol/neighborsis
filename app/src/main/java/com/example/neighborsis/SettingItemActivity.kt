package com.example.neighborsis

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.neighborsis.databinding.SettingItemBinding

class SettingItemActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        val binding = SettingItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}