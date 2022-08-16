package com.shakircam.android_assessment_test.ui.commit

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.shakircam.android_assessment_test.core.BaseAndroidViewModel
import com.shakircam.android_assessment_test.core.BaseViewModel
import com.shakircam.android_assessment_test.domain.repository.GithubRepository
import com.shakircam.android_assessment_test.model.Commits
import com.shakircam.android_assessment_test.utils.ExtensionFunction.hasInternetConnection
import com.shakircam.android_assessment_test.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class CommitViewModel @Inject constructor(private val userRepository: GithubRepository,application: Application
) : BaseAndroidViewModel(application) {

    /**  ------ github commits call ----- */

    private val _commitsResponse : MutableLiveData<Resource<MutableList<Commits.CommitsItem>>> = MutableLiveData()
    val commitsResponse: LiveData<Resource<MutableList<Commits.CommitsItem>>> = _commitsResponse


    init {
        if (hasInternetConnection(application)){
            getCommit()
        }

    }

    private fun getCommit() = viewModelScope.launch {
        _commitsResponse.postValue(Resource.Loading())
        val response = userRepository.getGithubCommit()
        _commitsResponse.postValue(handleCommitsResponse(response))
    }


    private fun handleCommitsResponse(response: Response<MutableList<Commits.CommitsItem>>) : Resource<MutableList<Commits.CommitsItem>> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->

                val filterList = mutableListOf<Commits.CommitsItem>()

                for (element in resultResponse){
                    if (!element.commit.author.name.startsWith("g") && !element.commit.author.name.startsWith("x")){
                        filterList.add(element)
                    }
                }

                return Resource.Success(filterList)
            }

        }
        return Resource.Error(response.message())
    }


}