package com.shakircam.android_assessment_test.di


import com.shakircam.android_assessment_test.domain.repository.GitHubRepositoryImp
import com.shakircam.android_assessment_test.domain.repository.GithubRepository
import com.shakircam.android_assessment_test.utils.AppPreference
import com.shakircam.android_assessment_test.utils.AppPreferenceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton



 /**
  * Our repository is abstract & that's why we are using @Binds annotations
 */

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRepository(
        githubRepositoryImp: GitHubRepositoryImp
    ) : GithubRepository

 }