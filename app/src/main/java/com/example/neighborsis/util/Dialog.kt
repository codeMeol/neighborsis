package com.example.neighborsis.util

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.example.neighborsis.databinding.DialogLayoutBinding
import com.google.android.gms.ads.*
import com.google.android.gms.ads.admanager.AdManagerAdRequest

class PopupDialog(context: Context,val finishApp: ()->Unit) : Dialog(context) {
     lateinit var binding: DialogLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = DialogLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.myTemplate.
        initViews()

    }

    private fun initViews() =with(binding) {
        setCancelable(false)
        MobileAds.initialize(context)
        // background를 투명하게 만듦
        // (중요) Dialog는 내부적으로 뒤에 흰 사각형 배경이 존재하므로, 배경을 투명하게 만들지 않으면
        // corner radius의 적용이 보이지 않는다.
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // OK Button 클릭에 대한 Callback 처리. 이 부분은 상황에 따라 자유롭게!

        dialogButtonOk.setOnClickListener {
            System.exit(0)
        }
        dialogButtonCancel.setOnClickListener {
            this@PopupDialog.dismiss()
        }

    }
}