package com.example.meetroom.data.model

import com.google.gson.annotations.SerializedName

data class RoomReservationList(
    @SerializedName("has_more")
    val hasMore: Boolean,
    @SerializedName("room_reservation_list")
    val roomReservationList: List<RoomReservation>
)