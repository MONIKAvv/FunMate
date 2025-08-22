package vv.monika.funmate

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import vv.monika.funmate.databinding.ActivitySoundMatchBinding

class SoundMatchActivity : BaseQuestionActivity() {
    override fun getViewBinding(): ViewBinding {
        return ActivitySoundMatchBinding.inflate(layoutInflater)
    }

    override fun getSubjectName(): String = "Sound"
}
