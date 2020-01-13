package com.example.cloudexample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.nifcloud.mbaas.core.NCMB
import com.nifcloud.mbaas.core.NCMBUser
import kotlinx.android.synthetic.main.activity_sign.*

class SignActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign)

        NCMB.initialize(applicationContext, "9b85daffb63c9794922a25efda8e8d50f11014f244ac4e4374a5514b1ab4142e",
            "7ab1f242b405418cb105d72b87f076d89619702cd9ceb61c3c17fd1ed9cb6dfc")

        // 会員登録処理用のボタン
        btnSignUp.setOnClickListener {
            this.signUp()
        }

        // ログイン用のボタン
        btnSignIn.setOnClickListener {
            this.signIn()
        }

        currentButton.setOnClickListener {
            val user = NCMBUser.getCurrentUser()
            Log.d("name", user.userName.toString())
            val intent = Intent(this, MainActivity::class.java)
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
                Log.d("[Error]", e.toString())
            } else {
                (findViewById(R.id.lblStats) as TextView).text = "Sign Up successful"
            }
        }
    }

    fun signIn() {
        var userName = (findViewById(R.id.userName) as TextView).text.toString()
        var password = (findViewById(R.id.password) as TextView).text.toString()
        //NCMBUser.logout()
        NCMBUser.loginInBackground(userName, password, {ncmbUser, e ->
            if (e != null) {
                Log.d("[Error]", e.toString())
            } else {
                (findViewById(R.id.lblStats) as TextView).text = "Log in successful by ${ncmbUser.userName}"
            }

        })
    }
}
