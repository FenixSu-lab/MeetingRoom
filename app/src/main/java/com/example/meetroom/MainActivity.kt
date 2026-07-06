package com.example.meetroom

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.meetroom.adapter.MeetingListAdapter
import com.example.meetroom.data.model.RoomReservation

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: com.example.meetroom.viewmodel.RoomDisplayViewModel
    private lateinit var textRoomName: TextView
    private lateinit var textDate: TextView
    private lateinit var textWeekday: TextView
    private lateinit var textTime: TextView
    private lateinit var layoutCurrentMeetingContainer: LinearLayout
    private lateinit var layoutCurrentDetails: LinearLayout
    private lateinit var textCurrentTitle: TextView
    private lateinit var textCurrentStatus: TextView
    private lateinit var textNoMeetingStatus: TextView
    private lateinit var layoutNextMeetingContainer: LinearLayout
    private lateinit var layoutNextDetails: LinearLayout
    private lateinit var textNextEmptyStatus: TextView
    private lateinit var textError: TextView
    private lateinit var progressLoading: ProgressBar
    private lateinit var textCurrentTime: TextView
    private lateinit var textCurrentRemaining: TextView
    private lateinit var textCurrentReserver: TextView
    private lateinit var textCurrentDepartment: TextView
    private lateinit var textNextStatus: TextView
    private lateinit var textNextTime: TextView
    private lateinit var textNextMinutes: TextView
    private lateinit var textNextReserver: TextView
    private lateinit var textNextDepartment: TextView
    private lateinit var textScheduleCount: TextView
    private lateinit var recyclerMeetingList: RecyclerView
    private lateinit var meetingListAdapter: MeetingListAdapter

    private val timeHandler = Handler(Looper.getMainLooper())
    private val timeRunnable = object : Runnable {
        override fun run() {
            updateTime()
            updateRemainingTime()
            timeHandler.postDelayed(this, 1000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        enterFullScreen()

        viewModel = ViewModelProvider(this).get(com.example.meetroom.viewmodel.RoomDisplayViewModel::class.java)
        bindViews()
        setupRecyclerView()
        setupObservers()
        startTimeUpdate()
    }

    override fun onStart() {
        super.onStart()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        viewModel.loadMeetingData()
        viewModel.startPolling()
    }

    override fun onStop() {
        super.onStop()
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        viewModel.stopPolling()
    }

    override fun onDestroy() {
        super.onDestroy()
        timeHandler.removeCallbacks(timeRunnable)
    }

    private fun bindViews() {
        textRoomName = findViewById(R.id.text_room_name)
        textDate = findViewById(R.id.text_date)
        textWeekday = findViewById(R.id.text_weekday)
        textTime = findViewById(R.id.text_time)
        layoutCurrentMeetingContainer = findViewById(R.id.layout_current_meeting_container)
        layoutCurrentDetails = findViewById(R.id.layout_current_details)
        textCurrentTitle = findViewById(R.id.text_current_title)
        textCurrentStatus = findViewById(R.id.text_current_status)
        textNoMeetingStatus = findViewById(R.id.text_no_meeting_status)
        layoutNextMeetingContainer = findViewById(R.id.layout_next_meeting_container)
        layoutNextDetails = findViewById(R.id.layout_next_details)
        textNextEmptyStatus = findViewById(R.id.text_next_empty_status)
        textError = findViewById(R.id.text_error)
        progressLoading = findViewById(R.id.progress_loading)
        textScheduleCount = findViewById(R.id.text_schedule_count)
        recyclerMeetingList = findViewById(R.id.recycler_meeting_list)

        textCurrentTime = findViewById(R.id.text_current_time)
        textCurrentRemaining = findViewById(R.id.text_current_remaining)
        textCurrentReserver = findViewById(R.id.text_current_reserver)
        textCurrentDepartment = findViewById(R.id.text_current_department)

        textNextStatus = findViewById(R.id.text_next_status)
        textNextTime = findViewById(R.id.text_next_time)
        textNextMinutes = findViewById(R.id.text_next_minutes)
        textNextReserver = findViewById(R.id.text_next_reserver)
        textNextDepartment = findViewById(R.id.text_next_department)
    }

    private fun setupRecyclerView() {
        meetingListAdapter = MeetingListAdapter()
        recyclerMeetingList.layoutManager = LinearLayoutManager(this)
        recyclerMeetingList.adapter = meetingListAdapter
    }

    private fun setupObservers() {
        viewModel.roomName.observe(this) {
            textRoomName.text = if (it.isNotEmpty()) it else "会议室"
        }

        viewModel.currentMeeting.observe(this) { meeting ->
            if (meeting != null) {
                textCurrentTitle.visibility = View.VISIBLE
                textCurrentStatus.visibility = View.VISIBLE
                layoutCurrentDetails.visibility = View.VISIBLE
                textNoMeetingStatus.visibility = View.GONE
                layoutCurrentMeetingContainer.setBackgroundColor(0xFF2E7D32.toInt())
                updateCurrentMeetingUI(meeting)
            } else {
                textCurrentTitle.visibility = View.VISIBLE
                textCurrentStatus.visibility = View.GONE
                layoutCurrentDetails.visibility = View.GONE
                textNoMeetingStatus.visibility = View.VISIBLE
                layoutCurrentMeetingContainer.setBackgroundColor(0xFF2C4A6D.toInt())
            }
        }

        viewModel.nextMeeting.observe(this) { meeting ->
            if (meeting != null) {
                layoutNextDetails.visibility = View.VISIBLE
                textNextEmptyStatus.visibility = View.GONE
                updateNextMeetingUI(meeting)
            } else {
                layoutNextDetails.visibility = View.GONE
                textNextEmptyStatus.visibility = View.VISIBLE
            }
        }

        viewModel.meetingList.observe(this) { meetings ->
            meetingListAdapter.setMeetings(meetings)
            textScheduleCount.text = "共 ${meetings.size} 场会议"
        }

        viewModel.loading.observe(this) {
            progressLoading.visibility = if (it) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(this) { errorMsg ->
            textError.visibility = if (!errorMsg.isNullOrEmpty()) View.VISIBLE else View.GONE
            textError.text = errorMsg
        }
    }

    private fun startTimeUpdate() {
        timeHandler.post(timeRunnable)
    }

    private fun updateTime() {
        textTime.text = viewModel.getCurrentTime()
        textDate.text = viewModel.getCurrentDate()
        textWeekday.text = viewModel.getWeekDay()
    }

    private fun updateRemainingTime() {
        if (viewModel.currentMeeting.value != null) {
            val remaining = viewModel.getCurrentMeetingRemainingMinutes()
            textCurrentRemaining.text = if (remaining > 0) {
                "剩余 ${remaining}分钟"
            } else {
                "即将结束"
            }
        }

        if (viewModel.nextMeeting.value != null) {
            val minutes = viewModel.getNextMeetingMinutes()
            textNextMinutes.text = if (minutes > 0) {
                "${minutes}分钟后"
            } else {
                "即将开始"
            }
        }
    }

    private fun updateCurrentMeetingUI(meeting: RoomReservation) {
        textCurrentStatus.text = meeting.reservationStatus
        textCurrentTime.text = formatTimeRange(meeting.eventStartTime, meeting.eventEndTime)
        textCurrentReserver.text = "预订人: ${meeting.reserver}"
        textCurrentDepartment.text = meeting.departmentOfReserver
    }

    private fun updateNextMeetingUI(meeting: RoomReservation) {
        val minutes = viewModel.getNextMeetingMinutes()
        textNextStatus.text = if (minutes <= 30) {
            "未来${minutes}分钟有会议"
        } else {
            "${minutes}分钟后有会议"
        }
        textNextTime.text = formatTimeRange(meeting.eventStartTime, meeting.eventEndTime)
        textNextReserver.text = "预订人: ${meeting.reserver}"
        textNextDepartment.text = meeting.departmentOfReserver
    }

    private fun formatTimeRange(startTime: String, endTime: String): String {
        return try {
            val start = startTime.substring(11, 16)
            val end = endTime.substring(11, 16)
            "$start - $end"
        } catch (e: Exception) {
            "$startTime - $endTime"
        }
    }

    private fun enterFullScreen() {
        window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            or View.SYSTEM_UI_FLAG_FULLSCREEN
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        )
        supportActionBar?.hide()
    }
}