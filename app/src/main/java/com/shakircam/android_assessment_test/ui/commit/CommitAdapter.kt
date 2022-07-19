package com.shakircam.android_assessment_test.ui.commit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.shakircam.android_assessment_test.core.BaseAdapter
import com.shakircam.android_assessment_test.databinding.RecyclerItemRowBinding
import com.shakircam.android_assessment_test.model.Commits

class CommitAdapter : BaseAdapter<Commits.CommitsItem>(DiffCallback) {


    override fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding {

        return RecyclerItemRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    }

    override fun bind(binding: ViewDataBinding, position: Int) {
        val item = getItem(position)
        (binding as RecyclerItemRowBinding).commit = item
        binding.executePendingBindings()
    }



    object DiffCallback : DiffUtil.ItemCallback<Commits.CommitsItem>() {
        override fun areItemsTheSame(oldItem: Commits.CommitsItem, newItem: Commits.CommitsItem): Boolean {
            return oldItem.author == newItem.author
        }

        override fun areContentsTheSame(oldItem: Commits.CommitsItem, newItem: Commits.CommitsItem): Boolean {
            return  oldItem == newItem
        }

    }


}