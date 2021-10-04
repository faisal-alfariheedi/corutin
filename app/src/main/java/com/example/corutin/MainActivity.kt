package com.example.corutin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.*
import org.json.JSONObject
import java.lang.Exception
import java.net.URL

class MainActivity : AppCompatActivity() {
    private lateinit var tvadv:TextView
    private lateinit var adv:Button
    private lateinit var pu :Button
    var cont:Boolean = true
    val advurl = "https://api.adviceslip.com/advice"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvadv = findViewById(R.id.tv_adv)
        adv = findViewById(R.id.getadv)
        pu=findViewById(R.id.pu)

        adv.setOnClickListener(){

            requestApi()

        }
        pu.setOnClickListener{cont=!cont}





    }

    private fun requestApi()
    {

        CoroutineScope(Dispatchers.IO).launch {
            while (cont) {
                val data = async {

                    getadvv()

                }.await()

                if (data.isNotEmpty()) {

                    updateAdviceText(data)
                }
            }

        }

    }

    private fun getadvv():String{

        var repl=""
        try {
            repl =URL(advurl).readText(Charsets.UTF_8)

        }catch (e:Exception)
        {
            println("Error $e")

        }
        return repl

    }

    private suspend fun updateAdviceText(data:String)
    {
        withContext(Dispatchers.Main)
        {

            val jsobj = JSONObject(data)
            val slip = jsobj.getJSONObject("slip")
            val id = slip.getInt("id")
            val adv = slip.getString("advice")

            tvadv.text = adv

        }

    }

}