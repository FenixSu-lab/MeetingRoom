package com.example.meetroom.data.repository

import com.example.meetroom.data.model.MeetingRoomResponse
import com.example.meetroom.data.remote.RetrofitClient

class MeetingRoomRepository {
    suspend fun getMeetingList(roomId: String): Result<MeetingRoomResponse> {
        return try {
            val response = RetrofitClient.instance.getMeetingInquiry(roomId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("请求失败: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}