package com.hana.roomprac

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.hana.roomprac.databinding.ActivityDiaryUpdateBinding
import java.text.SimpleDateFormat

class UpdateDiaryActivity : AppCompatActivity() {

    val binding by lazy { ActivityDiaryUpdateBinding.inflate(layoutInflater) }

    lateinit var helper: RoomHelper
    lateinit var memoAdapter: RecyclerAdapter
    val memoList = mutableListOf<RoomMemo>() // 초기화
    lateinit var memoDAO: RoomMemoDAO

    val sdfhhmm = SimpleDateFormat("hh:mm")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        helper = Room.databaseBuilder(this, RoomHelper::class.java, "room_db")
            .build()
        memoDAO = helper.roomMemoDao()
        memoAdapter = RecyclerAdapter(memoList)

        val memoTitle = intent.getStringExtra("title")
        val memoContent = intent.getStringExtra("content")
        val memoStartTime = intent.getStringExtra("startTime")
        val memoEndTime = intent.getStringExtra("endTime")

        val startTime = sdfhhmm.format(memoStartTime)
        val endTime = sdfhhmm.format(memoEndTime)

        with(binding) {
            diaryTitle.setText(memoTitle)
            diaryContent.setText(memoContent)
            diaryTimeStart.text = startTime
            diaryTimeEnd.text = endTime

            ivComplete.setOnClickListener {
                val title = diaryTitle.text.toString()
                val content = diaryContent.text.toString()
                if(title.isNotEmpty() && content.isNotEmpty()) {
                    val memo = RoomMemo(title, content, memoStartTime!!.toLong(), memoEndTime!!.toLong())
                    update(memo)

                    val intent = Intent(this@UpdateDiaryActivity, MainActivity::class.java)
                        .putExtra("title", title)
                        .putExtra("content", content)
                        .putExtra("starTime", memoStartTime)
                        .putExtra("endTime", memoEndTime)
                    startActivity(intent) // 이건 그냥 값을 넘겨주는 거잖음..
                }
            }
        }
    }

    fun update(memo: RoomMemo) { // 직렬화 필요
        // TODO("no 번호 넘겨줘서 업데이트하면 되지 않을까?")
    }
}