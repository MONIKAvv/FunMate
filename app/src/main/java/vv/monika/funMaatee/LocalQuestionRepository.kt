package vv.monika.funMaatee.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader

class LocalQuestionRepository(
    private val context: Context,
    private val assetFile: String
) {
    private fun readJson(): String {
        return context.assets.open(assetFile).bufferedReader().use(BufferedReader::readText)
    }

    fun loadAll(): List<QuestionsItem> {
        val json = readJson()
        val type = object : TypeToken<List<QuestionsItem>>() {}.type
        return Gson().fromJson(json, type) ?: emptyList()
    }

    fun loadBySubject(Subjects: String?): List<QuestionsItem> {
        val all = loadAll()
        return if (Subjects.isNullOrBlank()) all
        else all.filter { it.Subject?.equals(Subjects, ignoreCase = true) == true }
    }
}
