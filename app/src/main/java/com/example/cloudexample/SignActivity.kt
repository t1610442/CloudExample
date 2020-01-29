package com.example.cloudexample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.nifcloud.mbaas.core.NCMB
import com.nifcloud.mbaas.core.NCMBObject
import com.nifcloud.mbaas.core.NCMBQuery
import com.nifcloud.mbaas.core.NCMBUser
import kotlinx.android.synthetic.main.activity_sign.*

class SignActivity : AppCompatActivity() {
    
    private var state = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign)

        //NCMB.initialize(applicationContext, "9b85daffb63c9794922a25efda8e8d50f11014f244ac4e4374a5514b1ab4142e", "7ab1f242b405418cb105d72b87f076d89619702cd9ceb61c3c17fd1ed9cb6dfc")
        NCMB.initialize(applicationContext, "13560c451c65f37972a40d9fddb081f69684e9da3bf6ad3164190d7a5de29b4b", "409f16f2ecc212cb5764b16dd8ffb2d7d68b0d7097bd5f3e27bf7ce1a3ea399c")

        // 会員登録処理用のボタン
        btnSignUp.setOnClickListener {
            this.signUp()
        }

        // ログイン用のボタン
        btnSignIn.setOnClickListener {
            this.signIn()
        }

        //メイン画面遷移用のボタン
        btnMainAct.setOnClickListener {
            if(!state){
                Toast.makeText(this, "ログインしていないためオフラインでの使用になります", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }else {
                val intent = Intent(this, DataSelectActivity::class.java)
                startActivity(intent)
            }
        }

        btn_show_diary.setOnClickListener {
            val intent = Intent(this, DiaryListActivity::class.java)
            startActivity(intent)
        }
    }

    fun signUp() {
        var userName = (findViewById(R.id.userName) as TextView).text.toString()
        var password = (findViewById(R.id.password) as TextView).text.toString()
        val user = NCMBUser()
        NCMBUser.logout()
        user.userName = userName
        user.setPassword(password)
        user.saveInBackground {e ->
            if (e != null) {
                Log.d("[Error1]", e.toString())
            } else {
                (findViewById(R.id.lblStats) as TextView).text = "新規登録が完了しました"
            }
        }
    }

    fun signIn() {
        var userName = (findViewById(R.id.userName) as TextView).text.toString()
        var password = (findViewById(R.id.password) as TextView).text.toString()
        //NCMBUser.logout()
        NCMBUser.loginInBackground(userName, password, {ncmbUser, e ->
            if (e != null) {
                Log.d("[Error2]", e.toString())
            } else {
                (findViewById(R.id.lblStats) as TextView).text = "${ncmbUser.userName} さんでログインしました"
                state = true
            }

        })
    }
}
