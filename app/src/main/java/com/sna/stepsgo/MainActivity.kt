package com.sna.stepsgo

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.ui.AppBarConfiguration
import com.sna.stepsgo.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class MainActivity : AppCompatActivity(), SensorEventListener {
//class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    lateinit var timer: Runnable
    val handler: Handler = Handler()
    var iterator = 1




    var sensorManager: SensorManager? = null
    var totalSteps = 0f
    var previousTotalSteps = 0f
    var running = false
    var lastDateStep: String = timenow().toString()



    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        timer = Runnable() {
            //binding.dateNowView.text = timeOnDisplay().toString()
            timeOnDisplay()
                handler.postDelayed(timer, 30000)
        }

        handler.post(timer)

        binding.buttonHistory.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }


        binding.HelpInfo.setOnClickListener {
            val builderHelp = AlertDialog.Builder(this@MainActivity)
            builderHelp.setTitle("Шагомер StepsGo")
            builderHelp.setMessage("""
                *Версия v.1.1
                *Разработчик: vk.com/endofempty
                *Приложение считает ваши шаги в процессе хотьбы
                *Для активации просто активируйте счетчик
                *Для сброса счетчика и сохранения маршрута нажмите на кнопку Обнулить
                *В истории активности отображаются все ваши маршруты
            """.trimIndent())
            builderHelp.setPositiveButton("Ок"){ p0, p1 -> p0.dismiss() }
            val dialogHelp = builderHelp.create()
            dialogHelp.show()
        }

       binding.switch1.setOnCheckedChangeListener { _, isChecked ->
           if (isChecked) {
               binding.switch1.text = "Шагомер активен"
           } else {
               binding.switch1.text = "Шагомер неактивен"
           }
       }



    // binding.counts.text = "0".toString()
       // previousTotalSteps = 0f

        if (ContextCompat.checkSelfPermission(this,
               android.Manifest.permission.ACTIVITY_RECOGNITION) !=
            PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACTIVITY_RECOGNITION)) {
                ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACTIVITY_RECOGNITION), 1)
            } else {
                ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACTIVITY_RECOGNITION), 1)
            }
        }



      //  val sharedPreferences = getSharedPreferences( "myPrefs", Context.MODE_PRIVATE)
       // val editor = sharedPreferences.edit()
       // editor.putFloat("key1", 0f)
       // editor.putString("key2", lastDateStep)
       // editor.apply()


        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
       running = true





       resetSteps()
       loadData()





      //  binding.counts.text = "0"

            }






fun timenow(): String? {
    val dateTime = LocalDateTime.now()
        .format(DateTimeFormatter.ofPattern("MMM dd yyyy, HH:mm:ss"))
    return dateTime
}


    fun timeOnDisplay() {
       // binding.dateNowViewTime.text = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMM dd yyyy, HH:mm"))
        binding.dateNowViewTime.text = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy"))
        binding.dateNowViewMonth.text = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM"))
        binding.dateNowView.text = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
    }












    override fun onResume()
    {
        super.onResume()

       // binding.dateNowView.text = timenow().toString()





       running = true
          val stepSensor: Sensor? = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepSensor == null)
        {
            Toast.makeText(this, "Сенсор не определен", Toast.LENGTH_SHORT).show()
        } else
       {
          sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_FASTEST)
        }



    }



    private fun resetSteps()
    {

        binding.buttonStart.setOnClickListener {
            if( binding.counts.text == "0" )
            {


                val sharedPreferences = getSharedPreferences( "myPrefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                //editor.putFloat("key1", previousTotalSteps)
                editor.putString("key2", lastDateStep)
                editor.apply()


                Toast.makeText(this,
                    "Начните движение",
                    Toast.LENGTH_LONG).show()
            }
            else {





                val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
                val savedtime = sharedPreferences.getString("key2", 0f.toString()).toString()
                previousTotalSteps = totalSteps
                val lastStep = binding.counts.text
                binding.counts.text = 0.toString()
                lastDateStep = timenow().toString()
                saveData()


                if(lastStep.contains("-")) {
                    //Toast.makeText(this,
                    //  "меньше нуля!",
                    //Toast.LENGTH_LONG).show()
                    lifecycleScope.launch {
                        val user = User(firstName = "Ваш первый запуск!",
                            lastName = "Сегодня: $lastDateStep",
                            "Начните двигаться!")
                        AppDatabase(this@MainActivity).getUserDao().addUser(user)

                    }
                }
                else
                {



                    val builder = AlertDialog.Builder(this@MainActivity)
                    builder.setMessage("Количество шагов: $lastStep за период с $savedtime по $lastDateStep")
                    builder.setPositiveButton("Сохранить"){ p0, p1 ->
                        lifecycleScope.launch {
                            val user = User(firstName = "Старт: $savedtime", lastName = "Финиш: $lastDateStep", "Шаги: ${lastStep.toString()}")
                            AppDatabase(this@MainActivity).getUserDao().addUser(user)
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


                true
             //  Toast.makeText(this,
               //     "Вы прошли: $lastStep с начала времени $savedtime",
                 //   Toast.LENGTH_LONG).show()
                 Toast.makeText(this,
                     "Счетчик сброшен",
                   Toast.LENGTH_LONG).show()



            }
        }




  //  Toast.makeText(this, timenow(), Toast.LENGTH_LONG).show()



    private fun saveData()
    {
        val sharedPreferences = getSharedPreferences( "myPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putFloat("key1", previousTotalSteps)
        editor.putString("key2", lastDateStep)
        editor.apply()


    }

     fun loadData()
    {
        val sharedPreferences = getSharedPreferences( "myPrefs", Context.MODE_PRIVATE)
        val savedNumber = sharedPreferences.getFloat("key1", 0f)
            previousTotalSteps = savedNumber









    }


    override fun onPause() {
        super.onPause()
        // sensorManager?.unregisterListener(this)

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }



    override fun onSensorChanged(event: SensorEvent?)
    {
      //  if (running)
        if(binding.switch1.isChecked)
        {
            totalSteps = event!!.values[0]
            val currentSteps = totalSteps.toInt() - previousTotalSteps.toInt()
           //Toast.makeText(applicationContext, totalSteps.toString(), Toast.LENGTH_SHORT).show()
                   binding.counts.text = currentSteps.toString()

        }




    }






}