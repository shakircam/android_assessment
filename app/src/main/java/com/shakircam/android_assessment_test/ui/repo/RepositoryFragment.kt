package com.shakircam.android_assessment_test.ui.repo

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenStarted
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.facebook.shimmer.ShimmerFrameLayout
import com.shakircam.android_assessment_test.databinding.FragmentRepositoryBinding
import com.shakircam.android_assessment_test.utils.BindingFragment
import com.shakircam.android_assessment_test.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import timber.log.Timber


@AndroidEntryPoint
class RepositoryFragment : BindingFragment<FragmentRepositoryBinding, RepositoryViewModel>() {

    private val adapter by lazy { RepositoryAdapter() }
    private lateinit var shimmerFrameLayout: ShimmerFrameLayout

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentRepositoryBinding::inflate

    override val viewModel: RepositoryViewModel by viewModels()


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        shimmerFrameLayout = binding.shimmerLayout

        initRecyclerView()
        getRepoList()
        readGithubRepository()
    }



    private fun initRecyclerView() {
        val mRecyclerView = binding.recyclerView
        mRecyclerView.adapter = adapter
        mRecyclerView.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL,false)
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun getRepoList(){
        viewModel.repoResponse.observe(viewLifecycleOwner){ response->

            when(response){
                is Resource.Success -> {
                    response.data?.let {

                        Timber.d("owner name: " + response.data)
                        shimmerFrameLayout.isVisible = false

                    }
                }

                is Resource.Error -> {

                    response.message?.let { message ->
                        Timber.e("An error occurred: $message")
                    }
                }
                is Resource.Loading -> {
                    shimmerFrameLayout.isVisible = true
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun readGithubRepository(){
        viewModel.readGithubRepository.observe(viewLifecycleOwner){
            adapter.submitList(it)
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

}