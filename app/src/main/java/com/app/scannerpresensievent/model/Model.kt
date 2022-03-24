package com.app.scannerpresensievent.model

data class ResponseModel(
    val message: String,
    val status: Boolean,
    val data: DataModel
)

data class DataModel(
    val id: Int,
    val id_kegiatan: String,
    val kode: String,
    val status: String,
    val updated_at: String,
    val created_at: String,
)