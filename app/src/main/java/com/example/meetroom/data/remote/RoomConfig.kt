package com.example.meetroom.data.remote

object RoomConfig {
    const val BASE_URL = "http://192.168.1.106:98/"

    const val DEFAULT_ROOM_ID = "omm_ad664f5245a1ded68f9d82486a405184"
    const val DEFAULT_ROOM_NAME = "聚贤堂"

    val ROOM_MAP = mapOf(
        "聚贤堂" to "omm_ad664f5245a1ded68f9d82486a405184",
        "纳贤室" to "omm_fa99bff7b0907c3dfb7835708255e19f",
        "培训教室" to "omm_fdb1eef204ca4fae27448e354a535fe8",
        "行政2楼会议室" to "omm_ed84e689a2a229736bc01d8c46d209e0",
        "业务会议室" to "omm_d91e3b30f10fdca8d091225146c0de39",
        "天然居" to "omm_e55bf26a92ee364b12f5ea486a414e79",
        "财务会议室" to "omm_e72584d700d953fdbae03945708075c3",
        "生产副总会议室" to "omm_b5ff98e86f33cfc595ebdc12f525e150",
        "生产计划会议室" to "omm_faa315033a9207f891560c247c6e6d26"
    )

    fun getRoomNameById(roomId: String): String {
        return ROOM_MAP.entries.find { it.value == roomId }?.key ?: "未知会议室"
    }

    fun getRoomIdByName(roomName: String): String {
        return ROOM_MAP[roomName] ?: ""
    }
}