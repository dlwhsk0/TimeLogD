package com.hana.roomprac

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "room_memo")
class RoomMemo { // 클래스 자체는 테이블, 변수 하나하나는 컬럼
    @PrimaryKey(autoGenerate = true) // no에 값이 없을 때 자동 증가한 숫자값을 입력해준다
    @ColumnInfo
    var no: Long? = null
    @ColumnInfo
    var content: String = ""
    @ColumnInfo(name = "date") // 이름을 내가 임의로 정하는 거 datetime은 자동으로 실제 시간을 넣어줌
    var datetime: Long = 0

    constructor(content:String, datetime:Long) { // 편리하게 사용하기 위해 생성자 작성
        this.content = content
        this.datetime = datetime
    }
}
