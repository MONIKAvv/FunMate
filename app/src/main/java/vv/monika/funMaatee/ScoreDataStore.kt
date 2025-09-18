package vv.monika.funMaatee
import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*


//saving data the function called from scoreviewmodel
//data storage instance
val Context.dataStore by preferencesDataStore("app_prefs")
//keys
object ScoreKeys{
    val SCORE = intPreferencesKey("score")
    private const val KEY_DATE_SUFFIX = "_date"
    private const val KEY_COUNT_SUFFIX = "_count"
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

//Get today's Date String
fun getTodayDate(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
    return sdf.format(Date())
}
fun getCheckInDateKey(): String{
    return "checkin_date"
}

//generate dynamic keys per sbject
fun getDateKey(subject: String) = stringPreferencesKey("${subject}_date")
fun getCountKey(subject: String) = intPreferencesKey("${subject}_count")
// Check if user can attempt question today
fun canAttemptQuestion(context: Context, subject: String,dailyLimit: Int): Flow<Boolean>{
    val today = getTodayDate()
    val dateKey = getDateKey(subject)
    val countKey = getCountKey(subject)

    return context.dataStore.data.map { prefs ->
        val lastDate = prefs[dateKey] ?: ""
        val count = prefs[countKey] ?:0

        if(lastDate != today){
            true
        }else{
            count < dailyLimit
        }
    }
}

// Increment question count
suspend fun incrementQuestionCount(context: Context, subject: String) {
    val today = getTodayDate()
    val dateKey = getDateKey(subject)
    val countKey = getCountKey(subject)

    context.dataStore.edit { prefs ->
        val lastDate = prefs[dateKey] ?: ""
        var currentCount = prefs[countKey] ?: 0

        if (lastDate != today) {
            // Reset if new day
            prefs[dateKey] = today
            prefs[countKey] = 1
        } else {
            prefs[countKey] = currentCount + 1
        }
    }
}

fun reFreshDailyCount(context: Context, subject: String) = runBlocking{

    val today = getTodayDate()
    val dateKey = getDateKey(subject)
    val countKey = getCountKey(subject)
    val prefs = context.dataStore.data.first()
    val lastDate = prefs[dateKey]?: ""

    if(lastDate != today){
        context.dataStore.edit { prefsEdit ->
            prefsEdit[dateKey] = today
            prefsEdit[countKey] = 0
        }
    }

}



















