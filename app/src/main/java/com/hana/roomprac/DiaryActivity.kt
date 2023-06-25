package com.hana.roomprac

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hana.roomprac.databinding.ActivityDiaryBinding

class DiaryActivity : AppCompatActivity() {

    val binding by lazy { ActivityDiaryBinding.inflate(layoutInflater) }
    lateinit var helper:RoomHelper
    lateinit var memoAdapter: RecyclerAdapter
    val memoList = mutableListOf<RoomMemo>() // 초기화
    lateinit var memoDAO:RoomMemoDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding) {
            val title = diaryTitle.text.toString()
            val content = diaryContent.text.toString()

//            ivComplete.setOnClickListener {
//                val intent = Intent(this, MainActivity::class.java)
//            }
        }



    }
}