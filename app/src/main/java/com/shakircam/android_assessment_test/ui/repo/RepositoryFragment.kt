package com.shakircam.android_assessment_test.ui.repo


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.facebook.shimmer.ShimmerFrameLayout
import com.shakircam.android_assessment_test.databinding.FragmentRepositoryBinding
import com.shakircam.android_assessment_test.utils.BindingFragment
import com.shakircam.android_assessment_test.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class RepositoryFragment : BindingFragment<FragmentRepositoryBinding, RepositoryViewModel>() {

    private val adapter by lazy { RepositoryAdapter() }
    private lateinit var shimmerFrameLayout: ShimmerFrameLayout

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentRepositoryBinding::inflate

    override val viewModel: RepositoryViewModel by viewModels()



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        shimmerFrameLayout = binding.shimmerLayout
        initRecyclerView()
        readGithubRepository()
    }



    private fun initRecyclerView() {
        val mRecyclerView = binding.recyclerView
        mRecyclerView.adapter = adapter
        mRecyclerView.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL,false)
    }


    private fun readGithubRepository(){
        viewModel.readGithubRepository.observe(viewLifecycleOwner){item->
            if (item.isNotEmpty()){
                shimmerFrameLayout.isVisible = false
                adapter.submitList(item)
            }else{
                shimmerFrameLayout.isVisible = true
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

}