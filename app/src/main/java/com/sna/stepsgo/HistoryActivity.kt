package com.sna.stepsgo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sna.stepsgo.databinding.ActivityHistoryBinding
import kotlinx.coroutines.launch


class HistoryActivity : AppCompatActivity() {

    lateinit var binding: ActivityHistoryBinding
    private var mAdapter : UserAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)


        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.gotomain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


    }

    private fun setAdapter(list: List<User>){
        mAdapter?.setData(list)
    }


    override fun onResume() {
        super.onResume()

        lifecycleScope.launch{
            val userList = AppDatabase(this@HistoryActivity).getUserDao().getAllUser()

            mAdapter = UserAdapter()


            binding.recyclerView.apply {
                layoutManager = LinearLayoutManager(this@HistoryActivity)
                adapter = mAdapter
                    setAdapter(userList)


                    mAdapter?.setOnActionDeleteListener {

                        Toast.makeText(this@HistoryActivity,
                            "Удалено",
                            Toast.LENGTH_LONG).show()


                        lifecycleScope.launch {
                            AppDatabase(this@HistoryActivity).getUserDao().deleteUser(it)
                            val list = AppDatabase(this@HistoryActivity).getUserDao().getAllUser()
                            setAdapter(list)


                        }


                    }



                binding.deleteAll.setOnClickListener {

                    val builder = AlertDialog.Builder(this@HistoryActivity)
                    builder.setMessage("Все данные удалятся! Вы уверены?")
                    builder.setPositiveButton("Да"){ p0, p1 ->

                        lifecycleScope.launch {
                            AppDatabase(this@HistoryActivity).getUserDao().deleteAll()
                            val listnew = AppDatabase(this@HistoryActivity).getUserDao().getAllUser()
                            setAdapter(listnew)

                        }



                        p0.dismiss()
                    }
                    builder.setNegativeButton("Отмена"){p0, p1 ->
                        p0.dismiss()
                    }
                    val dialog = builder.create()
                    dialog.show()






                }




            }
        }
    }





}