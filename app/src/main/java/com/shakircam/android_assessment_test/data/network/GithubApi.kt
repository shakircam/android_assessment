package com.shakircam.android_assessment_test.data.network


import com.shakircam.android_assessment_test.model.Repository
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubApi {


    @GET("search/repositories")
    suspend fun getMostStarsRepository(
        @Query("q")searchQuery : String,
        @Query("sort") sortBy : String,
        @Query("order") orderBy : String,
        @Query("page")page : Int,
        @Query("per_page")perPage : Int,
    ):Response<Repository>

}