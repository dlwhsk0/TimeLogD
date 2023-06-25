package com.hana.roomprac

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.hana.roomprac.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    lateinit var helper:RoomHelper
    lateinit var memoAdapter: RecyclerAdapter
    val memoList = mutableListOf<RoomMemo>() // 초기화
    lateinit var memoDAO:RoomMemoDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

//        ivProfile.setOnClickListener {
//            val intent = Intent(this, DiaryActivity::class.java)
//            startActivity(intent)
//        }

        // 빌드가 호출되는 순간 룸 헬퍼 클래스를 만들어서 전달해줌
        helper = Room.databaseBuilder(this, RoomHelper::class.java, "room_db")
//            .allowMainThreadQueries() // 이건 공부할 때만 쓴대 : 쓰레드를 따로 분기를 안해도 메인에서 할 수 있게 해줌 원래 룸은 메인쓰레드에서 쿼리를 실행할 수 없다
            .build()
        memoDAO = helper.roomMemoDao()

        memoAdapter = RecyclerAdapter(memoList)
        refreshAdapter()

        with(binding) { // 리사이클러메모에 아답터를 연결
            recyclerMemo.adapter = memoAdapter
            recyclerMemo.layoutManager = LinearLayoutManager(this@MainActivity)

            buttonSave.setOnClickListener {
                val content = editMemo.text.toString()
                if(content.isNotEmpty()) {
                    val datetime = System.currentTimeMillis() // 시간 가져오기
                    val memo = RoomMemo(content, datetime)
                    editMemo.setText("") // 메모를 입력한 다음에 입력이 잘 됐으면 입력창이 지워짐
                    insertMemo(memo)
                    CloseKeyboard() // 키보드 내리기
                }
            }
        }

    }

    fun insertMemo(memo:RoomMemo) {
        CoroutineScope(Dispatchers.IO).launch {
            memoDAO.insert(memo) // insert도 서브에서
            refreshAdapter()
        }
    }

    fun refreshAdapter() { // 아답터가 추가 될때 항상 이 과정을 거치니까 함수로 만들어준다
        CoroutineScope(Dispatchers.IO).launch { // 룸과 관련된 코드는 서브쓰레드에서 실행시켜!
            // 아답터 갱신 (메모 리스트)
            memoList.clear() // 데이터가 많을 땐 이렇게 안 하고 중복 제거 같은 코드를 넣어주는데 지금은 적으니까
            memoList.addAll(memoDAO.getAll()) // 메모 넣어주기

            withContext(Dispatchers.Main) { // 화면을 갱신할때만 메인 쓰레드에서 실행해!
                memoAdapter.notifyDataSetChanged() // 마지막에 반영된 목록이 화면에 뿌려지게 됨
            }
        }
    }

    fun CloseKeyboard() { // 키보드 내리기
        var view = this.currentFocus

        if(view != null) {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}