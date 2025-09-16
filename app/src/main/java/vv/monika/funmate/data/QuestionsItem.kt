package vv.monika.funmate.data

import com.google.gson.annotations.SerializedName

data class QuestionsItem(
    @SerializedName("AnswerIndex")
    val AnswerIndex: String?,

    @SerializedName("AudioUrl")
    val AudioUrl: String?,   // optional, keep if you need audio later

    @SerializedName("OptionA")
    val OptionA: String?,

    @SerializedName("OptionB")
    val OptionB: String?,

    @SerializedName("OptionC")
    val OptionC: String?,

    @SerializedName("OptionD")
    val OptionD: String?,

    @SerializedName("Question")
    val Question: String?,

    @SerializedName("Subject")
    val Subject: String?,   // ✅ match JSON key exactly

    @SerializedName("Hint")
    val Hint: String?
)