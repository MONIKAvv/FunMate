package vv.monika.funMaatee.data

data class promocode_data(

    val id: String,
    val promocode: String,
    val value: Int,
    val forwhole: String,
    val onetimecode: String,
    val total_used: Int,
    val using_limit: Int,
    val created_date: String,
    val coupon_status: String,

)


data class ApiResponse(
    val success: Boolean,
    val data: List<promocode_data>

)