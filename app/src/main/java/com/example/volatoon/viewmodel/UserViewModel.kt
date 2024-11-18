package com.example.volatoon.viewmodel

//import android.app.Application
//import androidx.lifecycle.AndroidViewModel
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.viewModelScope
//import com.example.volatoon.model.MyPreferences
//import kotlinx.coroutines.launch
//
//class UserViewModel(application: Application) : AndroidViewModel(application) {
//    private val myPreferences = MyPreferences(application)
//
//    fun saveData(value: String) {
//        viewModelScope.launch {
//            myPreferences.writeToDataStore(value)
//        }
//    }
//
//    fun readData(): LiveData<String> {
//        val liveData = MutableLiveData<String>()
//        viewModelScope.launch {
//            liveData.value = myPreferences.readFromDataStore()
//        }
//        return liveData
//    }
//}