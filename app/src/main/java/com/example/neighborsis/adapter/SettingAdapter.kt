package com.example.neighborsis.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.neighborsis.R


import com.example.neighborsis.dataclass.SettingModel


class SettingAdapter(private val settingModelList: ArrayList<SettingModel>) : BaseAdapter() {



    override fun getCount(): Int {

        return settingModelList.size
    }

    override fun getItem(position: Int): SettingModel = settingModelList[position]

    override fun getItemId(position: Int): Long =position.toLong()

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        var convertView = view
        val viewHolder=ViewHolder()
        if (convertView == null) {
            convertView = LayoutInflater.from(parent?.context).inflate(R.layout.setting_item, parent, false)
        }
        val item: SettingModel = settingModelList[position]

        viewHolder.settingExplanationIcon?.setImageDrawable(item.settingExplanationIcon)
        viewHolder.settingExplanationText?.setText(item.settingExplanationText)
        viewHolder.settingValueText?.setText(item.settingValueText)
        viewHolder.settingValueRightIcon?.setImageDrawable(item.settingValueRightIcon)


        return convertView!!

    }

    class ViewHolder {
        var settingExplanationIcon: ImageView? =null
        var settingExplanationText: TextView?=null
        var settingValueText: TextView?=null
        var settingValueRightIcon: ImageView?=null
    }

}