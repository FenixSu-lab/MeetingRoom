package com.example.meetroom.data.remote

import com.example.meetroom.data.model.MeetingRoomResponse
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

interface MeetingRoomApi {
    @POST("dapi/MeetingRoom/MeetingInquiry")
    suspend fun getMeetingInquiry(
        @Query("roomid") roomId: String
    ): Response<MeetingRoomResponse>
}