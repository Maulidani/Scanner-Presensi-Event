package com.skripsi.traditionalfood.network

import com.app.scannerpresensievent.model.ResponseModel
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface ApiService {

    @FormUrlEncoded
    @POST("scanner")
    fun scanner(
        @Field("kode") idKegiatan: String
    ): Call<ResponseModel>

}