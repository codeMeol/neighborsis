package com.example.neighborsis.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.neighborsis.databinding.SettingItemBinding
import com.example.neighborsis.dataclass.SettingModel


class SettingAdapter(private val settingModelList: ArrayList<SettingModel>) : BaseAdapter() {


    override fun getCount(): Int {

        return settingModelList.size
    }

    override fun getItem(position: Int): SettingModel = settingModelList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val holder: ViewHolder
        if (convertView == null) {
            val itemBinding: SettingItemBinding =
                SettingItemBinding.inflate(LayoutInflater.from(parent!!.context), parent, false)
            holder = ViewHolder(itemBinding)
            holder.view = itemBinding.root
            holder.view!!.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }
        holder.binding!!.settingExplanationIcon.setImageDrawable(settingModelList[position].settingExplanationIcon)
        holder.binding!!.settingExplanationText.setText(settingModelList[position].settingExplanationText)
        holder.binding!!.settingValueText.setText(settingModelList[position].settingValueText)
        holder.binding!!.settingValueRightIcon.setImageDrawable(settingModelList[position].settingValueRightIcon)
        return holder.view

    }

    class ViewHolder(itemBinding: SettingItemBinding) {

        var view: View? = null
        var binding: SettingItemBinding? = itemBinding

    }
}