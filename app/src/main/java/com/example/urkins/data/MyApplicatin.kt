package com.example.urkins.data

import android.app.Application
import com.example.urkins.data.pref.UserPreference
import com.example.urkins.data.pref.dataStore

class MyApplication : Application() {
    lateinit var userPreference: UserPreference

    override fun onCreate() {
        super.onCreate()
        userPreference = UserPreference.getInstance(dataStore)
    }
}
