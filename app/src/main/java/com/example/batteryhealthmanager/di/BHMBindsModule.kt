package com.example.batteryhealthmanager.di

import com.example.batteryhealthmanager.db.repository.BHMRepository
import com.example.batteryhealthmanager.db.repository.BHMRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class BHMBindsModule {

    @Binds
    abstract fun bindsBHMRepo(bhmRepositoryImpl: BHMRepositoryImpl): BHMRepository
}