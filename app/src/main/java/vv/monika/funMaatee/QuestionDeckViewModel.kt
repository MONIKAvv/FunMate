package vv.monika.funMaatee.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import vv.monika.funMaatee.data.QuestionsItem
import vv.monika.funMaatee.data.LocalQuestionRepository

class QuestionDeckViewModel(app: Application) : AndroidViewModel(app) {

    private var all: List<QuestionsItem> = emptyList()
    private var order: MutableList<Int> = mutableListOf()
    private var index: Int = 0
    private var initialized = false

    fun initIfNeeded(repo: LocalQuestionRepository, Subjects: String? = null) {
        if (initialized) return
        all = repo.loadBySubject(Subjects)
        order = all.indices.shuffled().toMutableList()
        index = 0
        initialized = true
    }

    fun next(): QuestionsItem? {
        if (!initialized || index >= order.size) return null
        val q = all[order[index]]
        index++
        return q
    }

    fun progress(): Pair<Int, Int> = (index to order.size)
    fun hasMore(): Boolean = index < order.size
    fun reset() {
        if (!initialized) return
        order = all.indices.shuffled().toMutableList()
        index = 0
    }
}
