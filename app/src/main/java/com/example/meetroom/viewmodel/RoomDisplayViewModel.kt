package com.example.meetroom.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.meetroom.data.model.RoomReservation
import com.example.meetroom.data.repository.MeetingRoomRepository
import com.example.meetroom.data.remote.RoomConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class RoomDisplayViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = MeetingRoomRepository()

    private val _currentMeeting = MutableLiveData<RoomReservation?>()
    val currentMeeting: LiveData<RoomReservation?> = _currentMeeting

    private val _nextMeeting = MutableLiveData<RoomReservation?>()
    val nextMeeting: LiveData<RoomReservation?> = _nextMeeting

    private val _meetingList = MutableLiveData<List<RoomReservation>>()
    val meetingList: LiveData<List<RoomReservation>> = _meetingList

    private val _roomName = MutableLiveData<String>()
    val roomName: LiveData<String> = _roomName

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _lastUpdateTime = MutableLiveData<String>()
    val lastUpdateTime: LiveData<String> = _lastUpdateTime

    private var pollingJob: Job? = null

    init {
        _roomName.value = RoomConfig.DEFAULT_ROOM_NAME
    }

    fun startPolling() {
        pollingJob?.cancel()
        pollingJob = viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                try {
                    fetchMeetingList()
                } catch (e: Exception) {
                    Log.e("RoomDisplayViewModel", "轮询异常: ${e.message}", e)
                }
                delay(60000)
            }
        }
    }

    fun stopPolling() {
        pollingJob?.cancel()
    }

    fun loadMeetingData() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchMeetingList()
        }
    }

    private suspend fun fetchMeetingList() {
        _loading.postValue(true)
        _error.postValue(null)

        val roomId = RoomConfig.DEFAULT_ROOM_ID

        val result = repository.getMeetingList(roomId)
        result.onSuccess { response ->
            try {
                if (response.data != null && response.data.data != null) {
                    val reservations = response.data.data.roomReservationList ?: emptyList()
                    _meetingList.postValue(reservations)
                    processMeetings(reservations)
                    _lastUpdateTime.postValue(getCurrentTime())
                    Log.d("RoomDisplayViewModel", "数据刷新成功，会议数量: ${reservations.size}")
                } else {
                    _error.postValue("数据格式异常")
                }
            } catch (e: Exception) {
                _error.postValue("数据解析失败: ${e.message}")
                Log.e("RoomDisplayViewModel", "数据解析失败", e)
            }
        }.onFailure { e ->
            _error.postValue("获取数据失败: ${e.message}")
            Log.e("RoomDisplayViewModel", "网络请求失败", e)
        }

        _loading.postValue(false)
    }

    private fun processMeetings(reservations: List<RoomReservation>) {
        val now = Date()
        var current: RoomReservation? = null
        var next: RoomReservation? = null
        var minDiff = Long.MAX_VALUE

        for (reservation in reservations) {
            val startTime = parseDateTime(reservation.eventStartTime)
            val endTime = parseDateTime(reservation.eventEndTime)

            if (startTime != null && endTime != null) {
                if (now.after(startTime) && now.before(endTime)) {
                    current = reservation
                } else if (now.before(startTime)) {
                    val diff = startTime.time - now.time
                    if (diff < minDiff) {
                        minDiff = diff
                        next = reservation
                    }
                }
            }
        }

        _currentMeeting.postValue(current)
        _nextMeeting.postValue(next)
    }

    private fun parseDateTime(dateTimeStr: String): Date? {
        return try {
            val cleanStr = dateTimeStr.substringBefore("(").trim()
            val sdf = SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.getDefault())
            sdf.parse(cleanStr)
        } catch (e: Exception) {
            null
        }
    }

    fun getNextMeetingMinutes(): Long {
        val next = _nextMeeting.value ?: return 0L
        val startTime = parseDateTime(next.eventStartTime) ?: return 0L
        val now = Date()
        val diff = startTime.time - now.time
        return (diff / 60000).coerceAtLeast(0)
    }

    fun getCurrentMeetingRemainingMinutes(): Long {
        val current = _currentMeeting.value ?: return 0L
        val endTime = parseDateTime(current.eventEndTime) ?: return 0L
        val now = Date()
        val diff = endTime.time - now.time
        return (diff / 60000).coerceAtLeast(0)
    }

    fun getCurrentTime(): String {
        val calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(calendar.time)
    }

    fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault())
        return sdf.format(calendar.time)
    }

    fun getWeekDay(): String {
        val calendar = Calendar.getInstance()
        val days = arrayOf("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")
        return days[calendar.get(Calendar.DAY_OF_WEEK) - 1]
    }

    override fun onCleared() {
        super.onCleared()
        stopPolling()
    }
}