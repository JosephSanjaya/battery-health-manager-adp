package com.example.batteryhealthmanager.di

import android.content.Context
import com.tencent.mmkv.MMKV
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BHMProvidesModule {

    private var isInitialized = false

    // this also helps maintain thread safety only if internal implementations are thread safe

    @Provides
    @Singleton
    fun provideMMKV(@ApplicationContext context: Context): MMKV {

        if (!isInitialized) {
            MMKV.initialize(context)
            isInitialized = true
        }
        return MMKV.defaultMMKV()

    }
}

// check if initialise logic should be here or not
// working of how this will be recognized that mmkv should be provided from here on