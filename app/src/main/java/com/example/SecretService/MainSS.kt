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

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import java.lang.Math.pow

class MainSS : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun gotoEncrypt(view: View) {
        val intent = Intent(this, EncryptActivity::class.java)
        startActivity(intent)
    }

    fun gotoKeys(view: View) {
        setContentView(R.layout.activity_keys)
    }

    fun gotoSecrets(view: View) {
        setContentView(R.layout.activity_secrets)
    }

    external fun CkeySplit(key: String, n: Int, k: Int, keyList: Array<String>)

    fun getSecret(view: View) {
        val splitList = findViewById<ListView>(R.id.output_keys)
        val listAdapter = splitList.adapter
        val keyList = List<String>(listAdapter.count - 1, { i -> listAdapter.getItem(i + 1).toString() })

        val matrix = List<List<String>>(listAdapter.count - 1, { i ->
            List<String>(listAdapter.count - 1, { j ->
                pow(i + 1.0, j + 0.0).toString()
            })
        })
        /*
        for (int i = 0; i < keyList.size(); i++) {
            matrix.push_back(List<String>());
            matrix[i].push_back(1);
            for (int j = 0; j < keySet.size(); j++) {
            matrix[i].push_back(pow((i+1), (j+1)));
        }
            for (int j = 0; j <= keySet.size(); j++) {
            if (j==i) { matrix[i].push_back(1); }
            else { matrix[i].push_back(0); }
        }
            //matrix[i].push_back(keySet[i]);
        }
        */



        //findViewById<TextView>(R.id.output_secret).setText(CgetSecret(keyList));
    }

    external fun CgetSecret(keyList: List<String>): String


    external fun bigMath(A: String, B: String, op: Int): String

    fun List<String>.bigSum(): String {
        var sum: String = ""
        for (element in this) {
            sum = bigMath(sum, element, 0);
        }
        return sum
    }
/*
    fun List<String>.innerProduct(B:List<String>): String {
        return zip(B)
                .map { bigMath(it.first, it.second, 2)}
                .bigSum()
    }

    fun List<String>.vandermond(): List<List<String>> {
        return map { xval ->
            fold( listOf(), { soFar: List<String>, _ : String -> soFar
                    .plusElement(bigMath(xval, soFar.last(), 2))
            })
        }
    }

    fun List<String>.polyeval(input: String): String {
        return foldRight("", { soFar, alpha -> bigMath(alpha, bigMath(soFar, input, 2), 0) })
    }

    fun List<String>.polyAdd(summand:List<String>): List<String> {
        return mapIndexed{index, next -> next + summand.get(index)}
    }

    fun List<String>.polyMult(multipland: List<String>): List<String> {
        return foldIndexed( multipland , { index, soFar, next -> soFar
                .polyAdd(List<String>( index,{"0"}) + multipland.map{bigMath(it, next, 2)} )
        })
    }

    fun List<List<String>>.transpose(): List<List<String>> {
        return fold(listOf(), { soFar, next -> soFar
                .mapIndexed { index, newrow -> newrow
                        .plus( next[index] )
                }
        })
    }

    fun List<List<String>>.submatrix( droprow: Int, dropcol: Int ): List<List<String>> {
        return filterIndexed { index, _ -> index != droprow }
                .map { it.filterIndexed { index, _ -> index != dropcol } }
                .fold( listOf(), { current, next -> current.plusElement(next) } )
    }

    fun List<List<String>>.det(): String {
        return mapIndexed { colnum, _ -> submatrix(0, colnum).det() }
                .mapIndexed { index, entry -> bigMath(entry, pow(-1.0, index.toDouble()).toString(), 2) }
                .innerProduct( get(0) )
    }

    fun List<List<String>>.adjunct(): List<List<String>> {
        return mapIndexed { rownum: Int, row: List<String> -> row
                .mapIndexed { colnum, _ -> submatrix(rownum, colnum).det() }
        }
    }

    fun List<List<String>>.invert(): List<List<String>>{
        return adjunct()
                .map{ row -> row
                        .map{ entry -> bigMath(entry, det(), 2) }}
    }

    fun List<List<String>>.matrixMultiplyRight( B: List<List<String>> ): List<List<String>> {
        return mapIndexed { rownum:Int, brow -> brow
                .mapIndexed { colnum:Int, _ -> brow
                        .innerProduct( B.transpose()[colnum] )
                }
        }
    }*/

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
