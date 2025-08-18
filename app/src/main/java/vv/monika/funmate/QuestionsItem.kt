package vv.monika.funmate

import com.google.gson.annotations.SerializedName

data class QuestionsItem(
    @SerializedName("Answer Index")
    val AnswerIndex: String?,

    @SerializedName("AudioUrl")
    val AudioUrl: String?,

    @SerializedName("Option A")
    val OptionA: String?,

    @SerializedName("Option B")
    val OptionB: String?,

    @SerializedName("Option C")
    val OptionC: String?,

    @SerializedName("Option D")
    val OptionD: String?,

    @SerializedName("Question")
    val Question: String?,

    @SerializedName("Subjects")
    val Subjects: String?,

    @SerializedName("Hint")
    val Hint : String
)