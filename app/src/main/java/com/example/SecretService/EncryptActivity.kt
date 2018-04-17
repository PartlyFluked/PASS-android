package com.example.SecretService

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import java.util.*

class EncryptActivity : AppCompatActivity() {
    val rndGen = Random()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_encrypt)
    }

    fun keySplit(view: View) {
        val key: String = (findViewById<TextView>(R.id.input_key)).text.toString()
        val n: Int = ((findViewById<EditText>(R.id.input_n)).text.toString()).toInt()
        val k: Int = ((findViewById<EditText>(R.id.input_k)).text.toString()).toInt()
        val outputList = findViewById<ListView>(R.id.output_keys)
        val intKey = key.toInt()
        val newSecret: Secret = shamirEncrypt("SECRET", intKey, n, rndGen)
        val keyList = List<ShamirKey>(k, {newSecret.keygen("p${it+1}", it+1)} )
        val arrayAdapter = ArrayAdapter<String>(this,
                R.layout.layout_list,
                R.id.textView,
                keyList.map{it -> it.point.toString()}
        )
        outputList.adapter = arrayAdapter
        this.openFileOutput("secrets", Context.MODE_PRIVATE and Context.MODE_APPEND ).use {
            it.write()
        }
        Toast.makeText(applicationContext, "Saved to Vault.", Toast.LENGTH_SHORT).show()
    }
}
