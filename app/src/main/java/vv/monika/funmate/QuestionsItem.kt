package vv.monika.funmate

import com.google.gson.annotations.SerializedName

data class QuestionsItem(
    @SerializedName("Answer Index")  // Exact field name from your MongoDB
    val AnswerIndex: String?,

    @SerializedName("AudioUrl")  // Matches your MongoDB
    val AudioUrl: String?,

    @SerializedName("Option A")  // Exact field name with space
    val OptionA: String?,

    @SerializedName("Option B")  // Exact field name with space
    val OptionB: String?,

    @SerializedName("Option C")  // Exact field name with space
    val OptionC: String?,

    @SerializedName("Option D")  // Exact field name with space
    val OptionD: String?,

    @SerializedName("Question")  // Matches your MongoDB
    val Question: String?,

    @SerializedName("Subjects")  // Matches your MongoDB
    val Subjects: String?
)