package com.hana.roomprac

import android.content.Intent
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

            ivComplete.setOnClickListener {
                val intent = Intent(this@DiaryActivity, UpdateDiaryActivity::class.java)
                    .putExtra("title", memoTitle)
                    .putExtra("content", memoContent)
                    .putExtra("starTime", memoStartTime)
                    .putExtra("endTime", memoEndTime)
                startActivity(intent)
                // TODO("이렇게 하나하나 말고 position을 넘겨줘서 memoList[position]으로 데이터를 사용하는게 더 좋을 것 같은데 어떻게 하는거지")
            }
        }

    }
}