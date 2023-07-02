package com.hana.roomprac

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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

    init {
        instance = this
    }

    companion object {
        private var instance:MainActivity? = null
        fun getInstance(): MainActivity? {
            return instance
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 빌드가 호출되는 순간 룸 헬퍼 클래스를 만들어서 전달해줌
        helper = Room.databaseBuilder(this, RoomHelper::class.java, "room_db")
            .build()
        memoDAO = helper.roomMemoDao()

        memoAdapter = RecyclerAdapter(memoList)
        refreshAdapter()

        with(binding) {
            recyclerMemo.adapter = memoAdapter // 리사이클러메모에 아답터를 연결
            recyclerMemo.layoutManager = LinearLayoutManager(this@MainActivity)

            fbtnAdd.setOnClickListener {
                val intent = Intent(this@MainActivity, NewDiaryActivity::class.java)
                startActivity(intent)
            }

        }
    }

    fun goToDiary(memo: RoomMemo) {
        println("goToDiary 실행")
        val intent = Intent(this, DiaryActivity::class.java)
            .putExtra("title", memo.title)
            .putExtra("content" , memo.content)
            .putExtra("startTime", memo.datetime.toString())
            .putExtra("endTime", memo.datetimeEnd.toString())
        startActivity(intent)
    }


    fun update(memo: RoomMemo) { // 직렬화 필요
        // TODO("no 번호 넘겨줘서 업데이트하면 되지 않을까?")
    }

    fun deleteCheckPopup(memo: RoomMemo) { // 삭제 확인 팝업
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.delete_dialog, null)

        val alertDialog = AlertDialog.Builder(this)
            .create()

        val tvCancel = view.findViewById<TextView>(R.id.tvCancel)
        tvCancel.setOnClickListener {
            alertDialog.dismiss()
        }

        val tvDelete = view.findViewById<TextView>(R.id.tvDelete)
        tvDelete.setOnClickListener {
            deleteMemo(memo)
            alertDialog.dismiss()
        }

        alertDialog.setView(view)
        alertDialog.show()

        // 레이아웃 배경을 투명하게 (title 없애기)
        alertDialog?.window?.setBackgroundDrawableResource(R.drawable.edge_popup)
        alertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        alertDialog.window?.setLayout(700, WindowManager.LayoutParams.WRAP_CONTENT)
    }

    fun deleteMemo(memo: RoomMemo) {
        CoroutineScope(Dispatchers.IO).launch {
            memoDAO.delete(memo = memo)
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

    fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}


