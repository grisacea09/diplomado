package com.intercam.folklore.rest

import com.intercam.folklore.model.detalleVestimenta
import com.intercam.folklore.model.vestimenta
import retrofit2.Call
import retrofit2.http.*

interface vestimentaApi {

    @GET("vestimenta/vestimenta_list")
    fun getVestimenta(): Call<ArrayList<vestimenta>>
    @GET("vestimenta/vestimenta_detail/{id}")
    fun getVestimentaDetailApiary(@Path("id") id: Int?): Call<detalleVestimenta>

}