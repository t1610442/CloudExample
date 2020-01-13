package com.example.cloudexample

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.nifcloud.mbaas.core.NCMBFile
import com.nifcloud.mbaas.core.NCMBObject
import com.nifcloud.mbaas.core.NCMBQuery
import com.nifcloud.mbaas.core.NCMBUser
import kotlinx.android.synthetic.main.activity_data_select.*

class DataSelectActivity : AppCompatActivity() {

    private var objList = listOf<NCMBObject>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_select)

        var user = NCMBUser.getCurrentUser()
        Log.d("name", user.userName.toString())
        Log.d("objectId", user.objectId.toString())

        var buttonsList = arrayListOf<Button>(button_user1, button_user2, button_user3, button_user4, button_user5)
        val querySelect = NCMBQuery<NCMBObject>("photoPath")
        //var objList = listOf<NCMBObject>()
        querySelect.findInBackground { objects, error ->
            if (error != null) {
                Log.d("[Error2]", error.toString())
            } else {
                Log.d("[DEBUG3]", objects.toString())
                Log.d("[DEBUG4]", objects.size.toString())
                objList = objects
                Log.d("[DEBUG4]", objList.size.toString())
            }
        }

        button_user_update.setOnClickListener {
            for(i in 1..objList.size){
                buttonsList[i-1].text = objList[i-1].getString("name")
            }
        }

        button_user1.setOnClickListener {
            if(objList.isEmpty()){
                Toast.makeText(this, "データがありません", Toast.LENGTH_SHORT).show()
            }else {
                setImages(objList[0].getList("array"))
            }
        }

        button_user2.setOnClickListener {
            if(objList.size < 2){
                Toast.makeText(this, "データがありません", Toast.LENGTH_SHORT).show()
            }else{
                setImages(objList[1].getList("array"))
            }
        }

        button_user3.setOnClickListener {
            if(objList.size < 3){
                Toast.makeText(this, "データがありません", Toast.LENGTH_SHORT).show()
            }else{
                setImages(objList[2].getList("array"))
            }
        }

        button_user4.setOnClickListener {
            if(objList.size < 4){
                Toast.makeText(this, "データがありません", Toast.LENGTH_SHORT).show()
            }else{
                setImages(objList[3].getList("array"))
            }
        }

        button_user5.setOnClickListener {
            if(objList.size < 5){
                Toast.makeText(this, "データがありません", Toast.LENGTH_SHORT).show()
            }else{
                setImages(objList[4].getList("array"))
            }
        }
    }

    private fun setImages(selectName: List<*>){
        //Log.d("button9", selectName.toString())
        Toast.makeText(this, "画像を準備中", Toast.LENGTH_SHORT).show()
        val query: NCMBQuery<NCMBFile> = NCMBFile.getQuery()
        for (i in 0..2) {
            query.whereEqualTo("fileName", selectName[i])
            query.findInBackground { list, ncmbException ->
                if (ncmbException != null) {
                    Log.d("[Error]", ncmbException.toString())
                } else {
                    Log.d("debug", list.get(0).toString())
                    list.get(0).fetchInBackground { dataFetch, er ->
                        if (er != null) {
                            //失敗処理
                            AlertDialog.Builder(this@DataSelectActivity)
                                .setTitle("Notification from NIFCloud")
                                .setMessage("Error:" + er.message)
                                .setPositiveButton("OK", null)
                                .show()
                        } else {
                            //成功処理
                            val bMap = BitmapFactory.decodeByteArray(dataFetch, 0, dataFetch.size)
                            if(i==0) imageView3.setImageBitmap(bMap)
                            else if(i==1) imageView4.setImageBitmap(bMap)
                            else {
                                imageView5.setImageBitmap(bMap)
                                Toast.makeText(this, "画像の表示完了", Toast.LENGTH_SHORT).show()
                            }
                            //val inputStream = FileInputStream(File(path))
                            //val bitmap = BitmapFactory.decodeStream(inputStream)
                            //imageView2.setImageBitmap(bitmap)
                        }
                    }
                }
            }
        }
    }
}
