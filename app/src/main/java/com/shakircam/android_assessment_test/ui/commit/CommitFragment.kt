package com.shakircam.android_assessment_test.ui.commit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.facebook.shimmer.ShimmerFrameLayout
import com.shakircam.android_assessment_test.core.BaseFragment
import com.shakircam.android_assessment_test.databinding.FragmentCommitBinding
import com.shakircam.android_assessment_test.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class CommitFragment :  BaseFragment<FragmentCommitBinding, CommitViewModel>() {

    private val adapter by lazy { CommitAdapter() }
    private lateinit var shimmerFrameLayout: ShimmerFrameLayout



    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentCommitBinding::inflate



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        shimmerFrameLayout = binding.shimmerLayout

        initRecyclerView()
        requestApiData()
    }


    private fun requestApiData(){
        viewModel.commitsResponse.observe(viewLifecycleOwner){ response ->

            when(response){
                is Resource.Success -> {
                    response.data?.let { commitsResponse ->
                        shimmerFrameLayout.isVisible = false
                        adapter.submitList(commitsResponse)

                    }
                }

                is Resource.Error -> {

                    response.message?.let { message ->
                        Timber.e("An error occurred: $message")
                    }
                }
                is Resource.Loading -> shimmerFrameLayout.isVisible = true
            }
        }
    }

    override fun onResume() {
        super.onResume()
        shimmerFrameLayout.startShimmer()

    }

    override fun onPause() {
        shimmerFrameLayout.stopShimmer()
        super.onPause()
    }


    private fun initRecyclerView() {
        val mRecyclerView = binding.recyclerView
        mRecyclerView.adapter = adapter
        mRecyclerView.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL,false)
    }



    override val viewModel: CommitViewModel by viewModels()


}