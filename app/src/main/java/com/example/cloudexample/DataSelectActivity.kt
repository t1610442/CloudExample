package com.example.cloudexample

import android.app.Dialog
import android.graphics.BitmapFactory
import android.graphics.Point
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Display
import android.view.Window
import android.widget.Button
import android.widget.ImageView
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
        var imageList = arrayListOf<ImageView>(imageView3, imageView4, imageView5)
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

        //メンバー更新用のボタン
        button_user_update.setOnClickListener {
            for(i in 1..objList.size){
                buttonsList[i-1].text = objList[i-1].getString("name")
            }
        }

        //user1の画像データ表示用のボタン
        button_user1.setOnClickListener {
            if(objList.isEmpty()){
                Toast.makeText(this, "データがありません", Toast.LENGTH_SHORT).show()
            }else {
                setImages(objList[0].getList("array"), imageList)
                //setImages2(objList[0].getList("array"))
            }
        }

        //user2の画像データ表示用のボタン
        button_user2.setOnClickListener {
            if(objList.size < 2){
                Toast.makeText(this, "データがありません", Toast.LENGTH_SHORT).show()
            }else{
                setImages(objList[1].getList("array"), imageList)
                //setImages2(objList[1].getList("array"))
            }
        }

        //user3の画像データ表示用のボタン
        button_user3.setOnClickListener {
            if(objList.size < 3){
                Toast.makeText(this, "データがありません", Toast.LENGTH_SHORT).show()
            }else{
                setImages(objList[2].getList("array"), imageList)
                //setImages2(objList[2].getList("array"))
            }
        }

        //user4の画像データ表示用のボタン
        button_user4.setOnClickListener {
            if(objList.size < 4){
                Toast.makeText(this, "データがありません", Toast.LENGTH_SHORT).show()
            }else{
                setImages(objList[3].getList("array"), imageList)
                //setImages2(objList[3].getList("array"))
            }
        }

        //user5の画像データ表示用のボタン
        button_user5.setOnClickListener {
            if(objList.size < 5){
                Toast.makeText(this, "データがありません", Toast.LENGTH_SHORT).show()
            }else{
                setImages(objList[4].getList("array"), imageList)
                //setImages2(objList[4].getList("array"))
            }
        }

        imageView3.setOnClickListener{
            var imageView = ImageView(this)
            var bitmap = (imageView3.drawable as BitmapDrawable).bitmap
            Log.d("bitmap", bitmap.toString())
            imageView.setImageBitmap(bitmap)

            var display : Display = windowManager.defaultDisplay
            var size = Point()
            display.getSize(size)
            Log.d("size.x", size.x.toString())
            Log.d("size.y", size.y.toString())
            var width = size.x

            //var a : Float = bitmap.width as Float
            Log.d("bitmap.width", bitmap.width.toString())

            var factor = width.toFloat() / bitmap.width.toFloat()
            Log.d("factor", factor.toString())
            imageView.scaleType = ImageView.ScaleType.FIT_CENTER

            var dialog = Dialog(this)

            dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
            Log.d("width",(bitmap.width*factor).toInt().toString())

            dialog.setContentView(imageView)
            dialog.window?.setLayout((bitmap.width*factor*0.7).toInt(), (bitmap.height*factor*0.7).toInt())

            dialog.show()
        }

        imageView4.setOnClickListener{
            var imageView = ImageView(this)
            var bitmap = (imageView4.drawable as BitmapDrawable).bitmap
            Log.d("bitmap", bitmap.toString())
            imageView.setImageBitmap(bitmap)

            var display : Display = windowManager.defaultDisplay
            var size = Point()
            display.getSize(size)
            Log.d("size.x", size.x.toString())
            Log.d("size.y", size.y.toString())
            var width = size.x

            //var a : Float = bitmap.width as Float
            Log.d("bitmap.width", bitmap.width.toString())

            var factor = width.toFloat() / bitmap.width.toFloat()
            Log.d("factor", factor.toString())
            imageView.scaleType = ImageView.ScaleType.FIT_CENTER

            var dialog = Dialog(this)

            dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
            Log.d("width",(bitmap.width*factor).toInt().toString())

            dialog.setContentView(imageView)
            dialog.window?.setLayout((bitmap.width*factor*0.7).toInt(), (bitmap.height*factor*0.7).toInt())

            dialog.show()
        }

        imageView5.setOnClickListener{
            var imageView = ImageView(this)
            var bitmap = (imageView5.drawable as BitmapDrawable).bitmap
            Log.d("bitmap", bitmap.toString())
            imageView.setImageBitmap(bitmap)

            var display : Display = windowManager.defaultDisplay
            var size = Point()
            display.getSize(size)
            Log.d("size.x", size.x.toString())
            Log.d("size.y", size.y.toString())
            var width = size.x

            //var a : Float = bitmap.width as Float
            Log.d("bitmap.width", bitmap.width.toString())

            var factor = width.toFloat() / bitmap.width.toFloat()
            Log.d("factor", factor.toString())
            imageView.scaleType = ImageView.ScaleType.FIT_CENTER

            var dialog = Dialog(this)

            dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
            Log.d("width",(bitmap.width*factor).toInt().toString())

            dialog.setContentView(imageView)
            dialog.window?.setLayout((bitmap.width*factor*0.7).toInt(), (bitmap.height*factor*0.7).toInt())

            dialog.show()
        }
    }

    private fun setImages(selectName: List<*>, imageName: ArrayList<ImageView>){
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
                            //*f(i==0) imageView3.setImageBitmap(bMap)
                            //else if(i==1) imageView4.setImageBitmap(bMap)
                            //else {
                            //  imageView5.setImageBitmap(bMap)
                            //}
                            imageName[i].setImageBitmap(bMap)
                            //val inputStream = FileInputStream(File(path))
                            //val bitmap = BitmapFactory.decodeStream(inputStream)
                            //imageView2.setImageBitmap(bitmap)
                        }
                    }
                }
            }
        }
        Toast.makeText(this, "画像の表示完了", Toast.LENGTH_SHORT).show()
    }
}
