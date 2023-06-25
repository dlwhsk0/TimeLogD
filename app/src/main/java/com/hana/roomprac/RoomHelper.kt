package com.hana.roomprac

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(RoomMemo::class), version = 1, exportSchema = false)
abstract class RoomHelper : RoomDatabase() { // 추상 클래스
    // dao가 지금은 하나뿐이지만 여러개 있을 때 각 dao 들을 컨트롤 할 수 있는 헬퍼
    abstract fun roomMemoDao(): RoomMemoDAO
}