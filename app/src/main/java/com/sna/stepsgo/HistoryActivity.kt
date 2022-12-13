package com.sna.stepsgo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sna.stepsgo.databinding.ActivityHistoryBinding
import kotlinx.coroutines.launch


class HistoryActivity : AppCompatActivity() {

    lateinit var binding: ActivityHistoryBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)


        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)




    }

    override fun onResume() {
        super.onResume()

        lifecycleScope.launch{
            val userList = AppDatabase(this@HistoryActivity).getUserDao().getAllUser()

            binding.recyclerView.apply {
                layoutManager = LinearLayoutManager(this@HistoryActivity)
                adapter = UserAdapter().apply {
                    setData(userList)
                }
            }
        }
    }





}