package com.example.meetroom.data.model

import com.google.gson.annotations.SerializedName

data class MeetingRoomResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("data")
    val data: ResponseData,
    @SerializedName("timestamp")
    val timestamp: Long,
    @SerializedName("executeTime")
    val executeTime: Long
)

data class ResponseData(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val data: RoomReservationList,
    @SerializedName("msg")
    val msg: String
)