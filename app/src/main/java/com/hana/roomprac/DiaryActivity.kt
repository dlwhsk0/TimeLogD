package com.hana.roomprac

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.hana.roomprac.databinding.ActivityDiaryBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DiaryActivity : AppCompatActivity() {

    val binding by lazy { ActivityDiaryBinding.inflate(layoutInflater) }
    lateinit var helper: RoomHelper
    lateinit var memoAdapter: RecyclerAdapter
    val memoList = mutableListOf<RoomMemo>() // 초기화
    lateinit var memoDAO: RoomMemoDAO

//    lateinit var memo: RoomMemo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 빌드가 호출되는 순간 룸 헬퍼 클래스를 만들어서 전달해줌
        helper = Room.databaseBuilder(this, RoomHelper::class.java, "room_db")
            .build()
        memoDAO = helper.roomMemoDao()

        memoAdapter = RecyclerAdapter(memoList)

//        memo = intent.getSerializableExtra("memo") as RoomMemo

        with(binding) {

            ivComplete.setOnClickListener {// 화면 전환
                val title = diaryTitle.text.toString()
                if(title.isNotEmpty()) {
                    val content = diaryContent.text.toString()
                    val datetime = System.currentTimeMillis() // 시간 가져오기
                    val memo = RoomMemo(title, content, datetime)
                    insertMemo(memo)

                    val intent = Intent(this@DiaryActivity, MainActivity::class.java)
                    startActivity(intent)
                }
            }
        }

    }

    fun insertMemo(memo:RoomMemo) {
        CoroutineScope(Dispatchers.IO).launch {
            memoDAO.insert(memo) // insert도 서브에서
        }
    }
}