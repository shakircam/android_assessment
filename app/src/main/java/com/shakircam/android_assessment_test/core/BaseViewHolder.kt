package com.shakircam.android_assessment_test.core

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

open class BaseViewHolder <out T : ViewDataBinding>(val binding: T) :
    RecyclerView.ViewHolder(binding.root)