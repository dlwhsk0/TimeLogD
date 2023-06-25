package com.hana.roomprac

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hana.roomprac.databinding.ItemRecyclerBinding
import java.text.SimpleDateFormat

// 메모 클래스를 사용할 아답터
class RecyclerAdapter(val roomMemoList:List<RoomMemo>) : RecyclerView.Adapter<RecyclerAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemRecyclerBinding.inflate(
            LayoutInflater.from(parent.context), parent, false) // 패런트가 뷰 뷰에서 context 바로 꺼낼 수 있음
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) { // 화면에 실제 뿌려지는
        holder.setMemo(roomMemoList.get(position))
    }

    override fun getItemCount() = roomMemoList.size

    // 아답터를 만들 때 항상 제일 먼저 만들어야 하는 홀더 클래스
    // 우리가 사용하는 아이템
    class Holder (val binding: ItemRecyclerBinding) : RecyclerView.ViewHolder(binding.root) {

        fun setMemo(roomMemo:RoomMemo) {
            // 아이템 리사이클 화면에 메모에 있는 값들을 세팅해주는 코드
            with(binding) {
                textNo.text = "${roomMemo.no}"
                textContent.text = roomMemo.content

                // 날짜 형태로 변환
                val sdf = SimpleDateFormat("yyyy/MM/dd hh:mm")
                textDatetime.text = sdf.format(roomMemo.datetime)
            }
        }
    }
}