package com.example.cloudexample

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.nifcloud.mbaas.core.NCMBFile
import com.nifcloud.mbaas.core.NCMBQuery
import kotlinx.android.synthetic.main.activity_second.*

class SecondActivity : AppCompatActivity() {

    var dbHandler = DatabaseHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)


    }

    override fun onResume() {
        super.onResume()

        button5.setOnClickListener {
            val photoNameList = dbHandler.getAllData()
            val query: NCMBQuery<NCMBFile> = NCMBFile.getQuery()
            for (i in 0..2) {
                query.whereEqualTo("fileName", photoNameList[i])
                query.findInBackground { list, ncmbException ->
                    if (ncmbException != null) {
                        Log.d("[Error]", ncmbException.toString())
                    } else {
                        Log.d("debug", list.get(0).toString())
                        list.get(0).fetchInBackground { dataFetch, er ->
                            if (er != null) {
                                //失敗処理
                                AlertDialog.Builder(this@SecondActivity)
                                    .setTitle("Notification from NIFCloud")
                                    .setMessage("Error:" + er.message)
                                    .setPositiveButton("OK", null)
                                    .show()
                            } else {
                                //成功処理
                                val bMap = BitmapFactory.decodeByteArray(dataFetch, 0, dataFetch.size)
                                if(i==0) imageView01.setImageBitmap(bMap)
                                else if(i==1) imageView02.setImageBitmap(bMap)
                                else imageView03.setImageBitmap(bMap)
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
}
