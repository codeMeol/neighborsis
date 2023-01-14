package com.example.neighborsis.dataclass

import android.R
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class SettingModel(
    val settingExplanationIcon:Drawable,
    val settingExplanationText:String,
    val settingValueText:String,
    val settingValueRightIcon:Drawable

)
