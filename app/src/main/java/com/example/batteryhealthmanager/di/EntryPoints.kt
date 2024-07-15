package com.example.batteryhealthmanager.di

import com.tencent.mmkv.MMKV
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface MMKVEntryPoint {
    fun mmkv(): MMKV
}
