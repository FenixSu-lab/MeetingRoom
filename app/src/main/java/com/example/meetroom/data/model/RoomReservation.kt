package com.example.meetroom.data.model

import com.google.gson.annotations.SerializedName

data class RoomReservation(
    @SerializedName("accepted_number")
    val acceptedNumber: String,
    @SerializedName("check_in_device")
    val checkInDevice: String,
    @SerializedName("check_in_time")
    val checkInTime: String,
    @SerializedName("department_of_reserver")
    val departmentOfReserver: String,
    @SerializedName("event_duration")
    val eventDuration: String,
    @SerializedName("event_end_time")
    val eventEndTime: String,
    @SerializedName("event_start_time")
    val eventStartTime: String,
    @SerializedName("guests_number")
    val guestsNumber: String,
    @SerializedName("is_release_early")
    val isReleaseEarly: String,
    @SerializedName("releasing_person")
    val releasingPerson: String,
    @SerializedName("releasing_time")
    val releasingTime: String,
    @SerializedName("reservation_status")
    val reservationStatus: String,
    @SerializedName("reserver")
    val reserver: String,
    @SerializedName("event_title")
    val eventTitle: String,
    @SerializedName("reserver_user_id")
    val reserverUserId: String,
    @SerializedName("room_check_in_status")
    val roomCheckInStatus: String,
    @SerializedName("room_id")
    val roomId: String,
    @SerializedName("room_name")
    val roomName: String
)