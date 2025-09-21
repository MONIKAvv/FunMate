package vv.monika.funMaatee.data

import java.sql.Date

data class user_data(

    val uid: String,
    val name:String?,
    val email: String?,

    val device_id: String?,
    val id_Token: String,
    val coins: Int?,

    val alphabet_fun_daily_limit: Int,
    val bigvssmall_fun_daily_limit: Int,
    val daily_checkin_daily_limit: Int,
    val game_fun_daily_limit: Int,
    val math_fun_daily_limit: Int,
    val sound_fun_daily_limit: Int,

    val alphabet_fun_last_date: Date,
    val bigvssmall_fun_last_date: Date,
    val daily_checkin_last_date: Date,
    val game_fun_last_date: Date,
    val math_fun_last_date: Date,
    val sound_fun_last_date: Date,



)


//data class should be individual
