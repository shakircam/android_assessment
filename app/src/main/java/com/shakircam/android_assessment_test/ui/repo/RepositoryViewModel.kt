package com.shakircam.android_assessment_test.ui.repo

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.shakircam.android_assessment_test.core.BaseViewModel
import com.shakircam.android_assessment_test.domain.repository.GithubRepository
import com.shakircam.android_assessment_test.model.Repository.Item
import com.shakircam.android_assessment_test.model.Repository
import com.shakircam.android_assessment_test.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@RequiresApi(Build.VERSION_CODES.M)
@HiltViewModel
class RepositoryViewModel@Inject constructor(private val githubRepository: GithubRepository, application: Application
) : BaseViewModel(application) {


    val readGithubRepository: LiveData<List<Repository.Item>> = githubRepository.getAllRepositoryItem()

    private fun insertListOfRepository(item: List<Item>) =
        viewModelScope.launch(Dispatchers.IO) {
            githubRepository.insertListOfRepository(item)
        }

    private val _repoResponse : MutableLiveData<Resource<Repository>> = MutableLiveData()
    val repoResponse: LiveData<Resource<Repository>> = _repoResponse

    var  searchQuery = "Android"
    var  sortBy = "stars"
    var  orderBy = "asc"
    var  page = 1
    var  perPage = 50

    init {
        /** Checking internet connection first. if internet is available then we call the getCommmit function. */
        if (hasInternetConnection()){
            getRepo()
        }
    }

    private fun getRepo()= viewModelScope.launch {
        _repoResponse.postValue(Resource.Loading())
        val response = githubRepository.getMostStarsRepository(searchQuery,sortBy,orderBy,page,perPage)
        _repoResponse.postValue(handleRepoResponse(response))
    }

    private fun handleRepoResponse(response: Response<Repository>): Resource<Repository>? {

        if (response.isSuccessful){

            response.body().let { result->
                val item = result!!.items
                if (item.isNotEmpty()){
                    offlineCacheRepository(item)
                }
                return Resource.Success(result)
            }
        }

        return Resource.Error(response.code().toString())
    }

    private fun offlineCacheRepository(item: List<Item>) {
        insertListOfRepository(item)
    }


    @RequiresApi(Build.VERSION_CODES.M)
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