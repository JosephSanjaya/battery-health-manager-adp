package com.example.batteryhealthmanager.db.repository

import android.os.Parcelable
import com.tencent.mmkv.MMKV
import javax.inject.Inject

interface BHMRepository {
    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean
    fun setBoolean(key: String, value: Boolean)
    fun <T: Parcelable> getParcelable(key: String, clazz: Class<T>): T?
    fun setParcelable(key: String, value: Parcelable)
    fun getInt(key: String): Int
    fun setInt(key: String, value: Int)
}

class BHMRepositoryImpl @Inject constructor(private val mmkv: MMKV) : BHMRepository {

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return mmkv.getBoolean(key, defaultValue)
    }

    override fun setBoolean(key: String, value: Boolean) {
        mmkv.encode(key, value)
    }

    override fun <T : Parcelable> getParcelable(key: String, clazz: Class<T>): T? {
        return mmkv.decodeParcelable(key, clazz)
    }

    override fun setParcelable(key: String, value: Parcelable) {
        mmkv.encode(key, value)
    }

    override fun getInt(key: String): Int {
        return mmkv.decodeInt(key)
    }

    override fun setInt(key: String, value: Int) {
        mmkv.encode(key, value)
    }

}