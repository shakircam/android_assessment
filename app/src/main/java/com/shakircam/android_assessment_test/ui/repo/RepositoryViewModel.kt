package com.shakircam.android_assessment_test.ui.repo

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.shakircam.android_assessment_test.core.BaseAndroidViewModel
import com.shakircam.android_assessment_test.domain.repository.GithubRepository
import com.shakircam.android_assessment_test.model.Repository.Item
import com.shakircam.android_assessment_test.model.Repository
import com.shakircam.android_assessment_test.utils.AppPreference
import com.shakircam.android_assessment_test.utils.AppPreferenceImpl
import com.shakircam.android_assessment_test.utils.Constants.IS_STORE
import com.shakircam.android_assessment_test.utils.Constants.ORDER
import com.shakircam.android_assessment_test.utils.Constants.PAGE_LIMIT
import com.shakircam.android_assessment_test.utils.Constants.PAGE_NUMBER
import com.shakircam.android_assessment_test.utils.Constants.SEARCH_QUERY
import com.shakircam.android_assessment_test.utils.Constants.SORT
import com.shakircam.android_assessment_test.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject



@HiltViewModel
class RepositoryViewModel@Inject constructor(private val githubRepository: GithubRepository, application: Application
) : BaseAndroidViewModel(application) {

    private  var appPreference: AppPreference = AppPreferenceImpl(getApplication())


    val readGithubRepository: LiveData<List<Item>> = githubRepository.getAllRepositoryItem()
    private val _repoResponse : MutableLiveData<Resource<Repository>> = MutableLiveData()
    val repoResponse: LiveData<Resource<Repository>> = _repoResponse


    /** Checking the internet connection first.
     * If internet is available then we check is item sored in room db.
     * If not then call the getRepo function. */

    init {

        if (hasInternetConnection()){
            if (appPreference.getBoolean(IS_STORE) == false){
                getRepo()
            }
        }
    }

    private fun getRepo() = viewModelScope.launch {
        _repoResponse.postValue(Resource.Loading())
        val response = githubRepository.getMostStarsRepository(SEARCH_QUERY,SORT,ORDER,PAGE_NUMBER,PAGE_LIMIT)
        _repoResponse.postValue(handleRepoResponse(response))
    }

    private fun handleRepoResponse(response: Response<Repository>): Resource<Repository> {

        if (response.isSuccessful){

            response.body().let { result->
                val item = result!!.items
                if (item.isNotEmpty()){
                    offlineCacheRepository(item)
                    appPreference.setBoolean(IS_STORE,true)
                }
                return Resource.Success(result)
            }
        }

        return Resource.Error(response.code().toString())
    }

    private fun offlineCacheRepository(item: List<Item>) {
        insertListOfRepository(item)
    }

    private fun insertListOfRepository(item: List<Item>) =
        viewModelScope.launch(Dispatchers.IO) {
            githubRepository.insertListOfRepository(item)
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