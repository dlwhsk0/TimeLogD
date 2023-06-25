package com.hana.roomprac

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hana.roomprac.databinding.ActivityDiaryBinding

class DiaryActivity : AppCompatActivity() {

    val binding by lazy { ActivityDiaryBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}