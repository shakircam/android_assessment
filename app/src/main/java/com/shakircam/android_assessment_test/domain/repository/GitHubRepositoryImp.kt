package com.shakircam.android_assessment_test.domain.repository

import androidx.lifecycle.LiveData
import com.shakircam.android_assessment_test.data.local.RepositoryDao
import com.shakircam.android_assessment_test.data.network.GithubApi
import com.shakircam.android_assessment_test.model.Repository
import com.shakircam.android_assessment_test.model.Repository.Item
import retrofit2.Response
import javax.inject.Inject

class GitHubRepositoryImp @Inject constructor(
    private val githubApi: GithubApi, private val repositoryDao: RepositoryDao
) : GithubRepository {
    override suspend fun getMostStarsRepository(
        searchQuery: String,
        sortBy: String,
        orderBy: String,
        pageNumber: Int,
        perPage: Int
    ): Response<Repository> {
        return githubApi.getMostStarsRepository(searchQuery, sortBy, orderBy, pageNumber, perPage)
    }

    override suspend fun insertListOfRepository(item: List<Item>) {
        repositoryDao.insertListOfRepository(item)
    }

    override  fun getAllRepositoryItem(): LiveData<List<Item>> {
       return repositoryDao.getAllRepositoryItem()
    }
}