package com.hana.roomprac

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hana.roomprac.databinding.ActivityDiaryBinding
import java.text.SimpleDateFormat

class DiaryActivity : AppCompatActivity() {

    val binding by lazy { ActivityDiaryBinding.inflate(layoutInflater) }

    val sdfhhmm = SimpleDateFormat("hh:mm")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        println("DiaryActivity 열림")

        val memoTitle = intent.getStringExtra("title")
        val memoContent = intent.getStringExtra("content")
        val memoStartTime = intent.getStringExtra("startTime")!!.toLong()
        val memoEndTime = intent.getStringExtra("endTime")!!.toLong()

        val startTime = sdfhhmm.format(memoStartTime)
        val endTime = sdfhhmm.format(memoEndTime)

        with(binding) {
            diaryTitle.setText(memoTitle)
            diaryContent.setText(memoContent)
            diaryTimeStart.text = startTime
            diaryTimeEnd.text = endTime
        }

    }
}