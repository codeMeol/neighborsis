package com.example.neighborsis.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.neighborsis.databinding.SettingItemBinding
import com.example.neighborsis.dataclass.SettingModel

class SettingAdapter : ListAdapter<SettingModel, SettingAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(var binding: SettingItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SettingModel) {
            binding.apply {

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            SettingItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // currentList: 해당 Adapter에 "submitList()"를 통해 삽입한 아이템 리스트
        holder.bind(currentList[position])
    }

    companion object {
        // diffUtil: currentList에 있는 각 아이템들을 비교하여 최신 상태를 유지하도록 한다.
        val diffUtil = object : DiffUtil.ItemCallback<SettingModel>() {
            override fun areItemsTheSame(oldItem: SettingModel, newItem: SettingModel): Boolean {

                return false;
            }

            override fun areContentsTheSame(oldItem: SettingModel, newItem: SettingModel): Boolean {

                return false
            }
        }
    }
}