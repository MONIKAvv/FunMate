package vv.monika.funmate.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import vv.monika.funmate.getScore
import vv.monika.funmate.saveScore

class ScoreViewModel(application: Application) : AndroidViewModel(application) {
private val context = getApplication<Application>().applicationContext

    val score: StateFlow<Int> = getScore(context).stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    fun addScore(points: Int) {
        viewModelScope.launch {
            val newScope = score.value+points
            saveScore(context, newScope)
        }
    }
    fun setScore(newScope:Int){
        viewModelScope.launch {
            saveScore(context,newScope)
        }
    }
}