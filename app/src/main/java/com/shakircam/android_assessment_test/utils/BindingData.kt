package com.shakircam.android_assessment_test.utils

import android.annotation.SuppressLint
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import coil.load
import com.bumptech.glide.Glide
import com.shakircam.android_assessment_test.R
import com.shakircam.android_assessment_test.model.Repository
import com.shakircam.android_assessment_test.ui.repo.RepositoryFragmentDirections
import java.text.SimpleDateFormat

class BindingData {

    companion object {


        /** ----------- data binding for github repository ---------- */

        @BindingAdapter("loadImageFromUrl")
        @JvmStatic
        fun loadImageFromUrl(imageView: ImageView, imageUrl: String?) {
            imageView.load(imageUrl) {
                crossfade(600)
            }
        }

        @SuppressLint("SimpleDateFormat")
        @BindingAdapter("timeFormat")
        @JvmStatic
        fun timeFormat(textView: TextView, date: String) {

            val parser = SimpleDateFormat("yyyy-MM-dd")
            val formatter = SimpleDateFormat("MM/dd/yyyy")
            val formattedDate = formatter.format(parser.parse(date)!!)
            textView.text = formattedDate
        }

        @SuppressLint("SimpleDateFormat")
        @BindingAdapter("timeFormatter")
        @JvmStatic
        fun timeFormatter(textView: TextView, date: String) {

            val parser = SimpleDateFormat("yyyy-MM-dd")
            val formatter = SimpleDateFormat("MM/dd/yyyy")
            val formattedDate = formatter.format(parser.parse(date)!!)
            textView.text = formattedDate
        }

        @BindingAdapter("loadUserImage")
        @JvmStatic
        fun loadUserImage(imageView: ImageView, imageUrl: String?) {
            Glide.with(imageView.context)
                .load(imageUrl)
                .placeholder(R.drawable.ic_profile)
                .error(R.drawable.ic_profile)
                .into(imageView)
        }


        @BindingAdapter("android:sendDataToDetailsFragment")
        @JvmStatic
        fun sendDataToDetailsFragment(view: ConstraintLayout, currentItem: Repository.Item){
            view.setOnClickListener {
                val action = RepositoryFragmentDirections.actionRepositoryFragmentToRepositoryDetailsFragment(currentItem)
                view.findNavController().navigate(action)
            }
        }

    }
}