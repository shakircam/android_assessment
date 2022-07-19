package com.shakircam.android_assessment_test.domain.repository

import androidx.lifecycle.LiveData
import com.shakircam.android_assessment_test.model.Commits
import com.shakircam.android_assessment_test.model.Repository
import com.shakircam.android_assessment_test.model.Repository.Item
import retrofit2.Response

interface GithubRepository {


    fun  getAllRepositoryItem(): LiveData<List<Item>>

    suspend fun insertListOfRepository(item: List<Item>)

    suspend fun getMostStarsRepository(
        searchQuery : String,
        sortBy : String,
        orderBy : String,
        pageNumber : Int,
        perPage : Int)
    : Response<Repository>

    suspend fun getGithubCommit(): Response<MutableList<Commits.CommitsItem>>



}