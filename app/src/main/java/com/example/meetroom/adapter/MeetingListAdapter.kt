package com.example.meetroom.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.meetroom.R
import com.example.meetroom.data.model.RoomReservation

class MeetingListAdapter : RecyclerView.Adapter<MeetingListAdapter.MeetingViewHolder>() {

    private var meetings: List<RoomReservation> = emptyList()

    fun setMeetings(meetings: List<RoomReservation>) {
        this.meetings = meetings
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeetingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_meeting, parent, false)
        return MeetingViewHolder(view)
    }

    override fun onBindViewHolder(holder: MeetingViewHolder, position: Int) {
        holder.bind(meetings[position])
    }

    override fun getItemCount(): Int = meetings.size

    inner class MeetingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textStatus: TextView = itemView.findViewById(R.id.text_meeting_status)
        private val textTime: TextView = itemView.findViewById(R.id.text_meeting_time)
        private val textReserver: TextView = itemView.findViewById(R.id.text_meeting_reserver)
        private val textDepartment: TextView = itemView.findViewById(R.id.text_meeting_department)
        private val viewIndicator: View = itemView.findViewById(R.id.view_indicator)

        fun bind(meeting: RoomReservation) {
            textStatus.text = meeting.reservationStatus
            textTime.text = formatTimeRange(meeting.eventStartTime, meeting.eventEndTime)
            textReserver.text = meeting.reserver
            textDepartment.text = meeting.departmentOfReserver

            if (meeting.reservationStatus == "会议进行中") {
                viewIndicator.setBackgroundColor(0xFF4CAF50.toInt())
            } else {
                viewIndicator.setBackgroundColor(0xFF81C784.toInt())
            }
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
    }
}