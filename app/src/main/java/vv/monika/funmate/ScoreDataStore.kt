package vv.monika.funmate
import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


//saving data the function called from scoreviewmodel
//data storage instance
val Context.dataStore by preferencesDataStore("app_prefs")
//keys
object ScoreKeys{
    val SCORE = intPreferencesKey("score")
}

suspend fun saveScore(context:Context, score:Int){
    context.dataStore.edit {  prefs ->
        prefs[ScoreKeys.SCORE] = score
    }
}

fun getScore(context: Context): Flow<Int> {
    return context.dataStore.data.map { prefs ->
        prefs[ScoreKeys.SCORE]?: 0
    }
}