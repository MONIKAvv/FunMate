package vv.monika.funMaatee.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import vv.monika.funMaatee.getScore
import vv.monika.funMaatee.saveScore

//view model is like brain of the screen, it is used to store things that we want to be constant and stored in local
// place

// A special class in android mvvm architecture that stores and manages data( score, username, email etc) screeon rotation me bhi survive kr jata hai
class ScoreViewModel(application: Application) : AndroidViewModel(application) {
private val context = getApplication<Application>().applicationContext

    val score: StateFlow<Int> = getScore(context).stateIn(viewModelScope, SharingStarted.Eagerly, 0)
// this stateFlow is like changes the things live, get score (storeddata y sharepreference se data fetch kr rha hai, getScore(context)
//    satein-> flow ko stateflow main convert kr deta hai
//    viewModelScope coroutine lifecycler se tied hai,  early matlb jaise viewmodel create hoga ye datastream start ho jaayega(chahe koe observe kare y na kare
//     0 means ki ye if score mila then ye update kar dega otherwise it will store 0 value

    fun addScore(points: Int) {
        viewModelScope.launch {
            val newScope = score.value+points
            saveScore(context, newScope)
//            passing values to the function
        }
    }
    fun setScore(newScope:Int){
        viewModelScope.launch {
            saveScore(context,newScope)
        }
    }
}