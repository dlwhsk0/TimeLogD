package com.hana.roomprac

import androidx.room.*

@Dao
interface RoomMemoDAO {
    // dao는 세 개의 메소드를 만들어줌

    @Query("select * from room_memo")
    fun getAll() : List<RoomMemo>

    @Insert(onConflict = OnConflictStrategy.REPLACE) // 이미 있는 값일 경우 업데이트를 한다
    fun insert(memo:RoomMemo)

    @Delete
    fun delete(memo:RoomMemo)



    // dao 인터페이스는 중괄호가 없음 @로 인터페이스 이름만 있으면 room이 몸체를 생성
}