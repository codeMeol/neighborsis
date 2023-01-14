package com.example.neighborsis.activity

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.neighborsis.databinding.AdminPushToFirebaseLayoutBinding

class AdminPushToFirebaseActivity: AppCompatActivity() {
    var binding : AdminPushToFirebaseLayoutBinding? =null
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        binding= AdminPushToFirebaseLayoutBinding.inflate(layoutInflater)
        setContentView(binding!!.root)



    }
}