package com.example.myapplication6

import PreferenceUtil
import android.app.Application

class MyApplication : Application()
{
    companion object
    {
        lateinit var prefs: PreferenceUtil
    }

    override fun onCreate()
    {
        prefs = PreferenceUtil(applicationContext)
        super.onCreate()
    }
}