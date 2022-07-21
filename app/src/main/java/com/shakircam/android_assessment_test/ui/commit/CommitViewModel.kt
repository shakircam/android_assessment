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
import com.shakircam.android_assessment_test.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class CommitViewModel @Inject constructor(private val userRepository: GithubRepository,application: Application
) : BaseAndroidViewModel(application) {

    /** RETROFIT */
    /**  ------ github commits call ----- */


    private val _commitsResponse : MutableLiveData<Resource<MutableList<Commits.CommitsItem>>> = MutableLiveData()
    val commitsResponse: LiveData<Resource<MutableList<Commits.CommitsItem>>> = _commitsResponse


    init {
        if (hasInternetConnection()){
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
                val updatedList = mutableListOf<Commits.CommitsItem>()

                /** we are checking user name starts with g & x .If match with condition then we are keeping those a list.After that removing from main list.
                And finally keeping only last 10 commits   */


                for (element in resultResponse){
                    if (element.commit.author.name.startsWith("g") || element.commit.author.name.startsWith("x")){
                        filterList.add(element)
                    }
                }

                resultResponse.removeAll(filterList)
                var commitItem = 0
                for (item in resultResponse){

                    if (commitItem<10){
                        updatedList.add(item)
                        commitItem++
                    }
                }
                return Resource.Success(updatedList)
            }

        }
        return Resource.Error(response.message())
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}