/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.SecretService

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.TextView
import android.widget.ArrayAdapter
import android.widget.EditText

class mainSS : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /* Retrieve our TextView and set its content.
         * the text is retrieved by calling a native
         * function.
         */
        setContentView(R.layout.activity_home)
        //val tv = findViewById(R.id.hello_textview) as TextView
        //tv.text = stringFromJNI()
    }

    fun gotoHome(view: View){
        setContentView(R.layout.activity_home)
    }
    fun gotoKeys(view: View){
        setContentView(R.layout.activity_keys)
    }
    fun gotoVault(view: View){
        setContentView(R.layout.activity_vault)
    }
    fun gotoSecret(view: View){
        setContentView(R.layout.activity_secret)
    }
    fun keySplit(view: View){
        val key: String = (findViewById<TextView>(R.id.input_key)).text.toString()
        val n: Int = ((findViewById<EditText>(R.id.input_n)).text.toString()).toInt()
        val k: Int = ((findViewById<EditText>(R.id.input_k)).text.toString()).toInt()
        val outputList = findViewById<ListView>(R.id.output_keys)
        val keyList = Array<String>(k+1,{""})
        encryptKey(key, n, k, keyList)
        val arrayAdapter = ArrayAdapter < String >(this, R.layout.layout_list, R.id.textView, keyList)
        outputList.adapter = arrayAdapter
    }

    external fun encryptKey(key: String, n: Int, k: Int, keyList: Array<String>)

    /* A native method that is implemented by the
     * 'hello-jni' native library, which is packaged
     * with this application.
     */
    external fun stringFromJNI(): String

    /* This is another native method declaration that is *not*
     * implemented by 'hello-jni'. This is simply to show that
     * you can declare as many native methods in your Java code
     * as you want, their implementation is searched in the
     * currently loaded native libraries only the first time
     * you call them.
     *
     * Trying to call this function will result in a
     * java.lang.UnsatisfiedLinkError exception !
     */
    external fun unimplementedStringFromJNI(): String

    companion object {

        /* this is used to load the 'hello-jni' library on application
     * startup. The library has already been unpacked into
     * /data/data/com.example.SecretService/lib/libhello-jni.so at
     * installation time by the package manager.
     */
        init {
            System.loadLibrary("mpir")
            System.loadLibrary("mpirxx")
            System.loadLibrary("secret-service")
        }
    }
}
