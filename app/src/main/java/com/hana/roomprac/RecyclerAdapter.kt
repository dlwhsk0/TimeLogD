package com.hana.roomprac

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hana.roomprac.databinding.ItemRecyclerBinding
import java.text.SimpleDateFormat

// 메모 클래스를 사용할 아답터
class RecyclerAdapter(val roomMemoList: List<RoomMemo>) : RecyclerView.Adapter<RecyclerAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder { // 어떤 목록 레이아웃을 만들 것인지
        val binding = ItemRecyclerBinding.inflate(
            LayoutInflater.from(parent.context), parent, false) // 패런트가 뷰 뷰에서 context 바로 꺼낼 수 있음
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) { // 화면에 실제 뿌려지는, 생성된 뷰에 무슨 데이터를 넣을 것인지
        holder.setMemo(roomMemoList.get(position))
    }

    override fun getItemCount() = roomMemoList.size // 몇 개의 목록을 만들 것인지

    // 아답터를 만들 때 항상 제일 먼저 만들어야 하는 홀더 클래스
    // 목록의 개별 레이아웃을 포함하는 View 래퍼, 각 목록 레이아웃에 필요한 기능 구현, 아이템 레이아웃에 버튼이 있는 경우 버튼 리스너는 Holer 클래스에서 구현
    class Holder (val binding: ItemRecyclerBinding) : RecyclerView.ViewHolder(binding.root) {

        private val mainActivity = MainActivity.getInstance()

        fun setMemo(roomMemo:RoomMemo) { // onbind 혹은 bind라는 이름을 많이 쓰는 듯
            // 아이템 리사이클 화면에 메모에 있는 값들을 세팅해주는 코드
            with(binding) {
                textTitle.text = roomMemo.title

                // 날짜 형태로 변환
                val sdfhhmm = SimpleDateFormat("hh:mm")
                textDatetime.text = sdfhhmm.format(roomMemo.datetime) + " - " + sdfhhmm.format(roomMemo.datetimeEnd)

                itemView.setOnClickListener {
                    mainActivity?.goToDiary(roomMemo)
                }

                ivReport.setOnClickListener {
                    mainActivity?.toast("작성")
                }

                ivDelete.setOnClickListener {
                    mainActivity?.deleteCheckPopup(roomMemo)
                }

            }

        }
    }

}