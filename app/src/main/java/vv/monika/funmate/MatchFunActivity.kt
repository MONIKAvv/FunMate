package vv.monika.funmate

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import vv.monika.funmate.databinding.ActivityMatchFunBinding

class MatchFunActivity : BaseQuestionActivity() {
   private lateinit var binding: ActivityMatchFunBinding
    override fun getViewBinding(): ViewBinding {
        binding  = ActivityMatchFunBinding.inflate(layoutInflater)
        return binding
    }

    override fun getSubjectName(): String = "Maths"

}