package com.example.myapplication6.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Address Book" //수정함. 기존 - This is home Fragment
    }
    val text: LiveData<String> = _text
}