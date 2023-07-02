package com.hana.roomprac

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.hana.roomprac.databinding.ActivityDiaryNewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class NewDiaryActivity : AppCompatActivity() {

    val binding by lazy { ActivityDiaryNewBinding.inflate(layoutInflater) }
    lateinit var helper: RoomHelper
    lateinit var memoAdapter: RecyclerAdapter
    val memoList = mutableListOf<RoomMemo>() // 초기화
    lateinit var memoDAO: RoomMemoDAO

    val sdf = SimpleDateFormat("yyyy-MM-dd") // Date 객체로 변환
    val sdfhh = SimpleDateFormat("hh")
    val sdfmm = SimpleDateFormat("mm")
    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

    var datetime = System.currentTimeMillis() // 현재 시간 가져오기 (마일초)
    var datetimeEnd = datetime
    var dateString = sdf.format(datetime) // "yyyy-MM-dd"
    var hourString = sdfhh.format(datetime) // "hh"
    var minuteString = sdfmm.format(datetime) // "mm"
    var timeString = ""
    var localDateMillis:Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 빌드가 호출되는 순간 룸 헬퍼 클래스를 만들어서 전달해줌
        helper = Room.databaseBuilder(this, RoomHelper::class.java, "room_db")
            .build()
        memoDAO = helper.roomMemoDao()
        memoAdapter = RecyclerAdapter(memoList)

        with(binding) {

            diaryTimeStart.text = hourString + ":" + minuteString // 현재 시간 세팅 해주기
            if(hourString.toInt() + 1 >= 25) {
                minuteString = "59"
            } else {
                hourString = String.format("%02d", (hourString.toInt() + 1))
            }
            diaryTimeEnd.text = hourString + ":" + minuteString

            localDateMillis = formatter.parse((dateString + " " + hourString + ":" + minuteString)).time
            datetimeEnd = localDateMillis

            diaryTimeStart.setOnClickListener {
                val calView = Calendar.getInstance() // 캘린더뷰 만들기
                val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    hourString = "${hourOfDay}" // 끝나는 시간보다 이후가 안되게 하기 위한 저장
                    minuteString = "${minute}"

                    timeString = "${hourOfDay}:${minute}"
                    diaryTimeStart.text = timeString

                    localDateMillis = formatter.parse((dateString + " " + hourString + ":" + minuteString)).time
                    datetime = localDateMillis
                }
                TimePickerDialog(this@NewDiaryActivity, timeSetListener, calView.get(Calendar.HOUR_OF_DAY), calView.get(Calendar.MINUTE), true).show()
            }

            diaryTimeEnd.setOnClickListener {
                val calView = Calendar.getInstance() // 캘린더뷰 만들기
                val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    if (hourString.toInt() == "${hourOfDay}".toInt() && minuteString.toInt() > "${hourOfDay}".toInt() || hourString.toInt() > "${hourOfDay}".toInt()) {
                        Toast.makeText(this@NewDiaryActivity, "ERROR: 끝나는 시간이 더 이전입니다.", Toast.LENGTH_SHORT).show()
                        // TODO("토스트 메시지로 끝나는 게 아니라 시간을 다시 고르도록 해야함")
                    } else {
                        timeString = "${hourOfDay}:${minute}"
                        diaryTimeEnd.text = timeString

                        localDateMillis = formatter.parse((dateString + " " + hourString + ":" + minuteString)).time
                        datetimeEnd = localDateMillis
                    }
                }
                TimePickerDialog(this@NewDiaryActivity, timeSetListener, calView.get(Calendar.HOUR_OF_DAY), calView.get(Calendar.MINUTE), true).show()
            }

            ivComplete.setOnClickListener {// 화면 전환
                val title = diaryTitle.text.toString()
                if(title.isNotEmpty()) {
                    val content = diaryContent.text.toString()
                    val memo = RoomMemo(title, content, datetime, datetimeEnd)
                    insertMemo(memo)

                    val intent = Intent(this@NewDiaryActivity, MainActivity::class.java)
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