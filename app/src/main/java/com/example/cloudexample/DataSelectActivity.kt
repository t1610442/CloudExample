package com.example.cloudexample

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Point
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Display
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.nifcloud.mbaas.core.NCMBFile
import com.nifcloud.mbaas.core.NCMBObject
import com.nifcloud.mbaas.core.NCMBQuery
import com.nifcloud.mbaas.core.NCMBUser
import kotlinx.android.synthetic.main.activity_data_select.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DataSelectActivity : AppCompatActivity() {

    private var objList = listOf<NCMBObject>()
    var dbHandler = DatabaseHandler(this)

    var REQUEST_CAMERA = 1000
    var REQUEST_GARALLY1 = 1001
    var REQUEST_GARALLY2 = 1002
    var REQUEST_GARALLY3 = 1003
    val PERMISSION_REQUEST = 1010
    var path: String =""
    var filename: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_select)

        var user = NCMBUser.getCurrentUser()
        Log.d("name", user.userName.toString())
        Log.d("objectId", user.objectId.toString())

        var buttonsList = arrayListOf<Button>(button_user1, button_user2, button_user3, button_user4, button_user5)
        var imageList = arrayListOf<ImageView>(imageView3, imageView4, imageView5)
        var name = user.userName
        textView_user.text = name + "さんが選んだ写真の画面です"
        val querySelect = NCMBQuery<NCMBObject>("photoPath")
        //var objList = listOf<NCMBObject>()
        querySelect.findInBackground { objects, error ->
            if (error != null) {
                Log.d("[Error2]", error.toString())
            } else {
                Log.d("[DEBUG3]", objects.toString())
                Log.d("[DEBUG4]", objects.size.toString())
                objList = objects
                for(i in 1..objList.size){
                    buttonsList[i-1].text = objList[i-1].getString("name")
                    /*if(user.userName == buttonsList[i-1].text){
                        setImages(objList[i-1].getList("array"), imageList)
                    }*/
                }
                Log.d("[DEBUG4]", objList.size.toString())
            }
        }

        //カメラ撮影用のボタン
        button_user_update.setOnClickListener {
            /*for(i in 1..objList.size){
                buttonsList[i-1].text = objList[i-1].getString("name")
            }*/
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).resolveActivity(packageManager)?.let {
                if (checkPermission()) {
                    takePicture()
                } else {
                    grantCameraPermission()
                }
            } ?: Toast.makeText(this, "カメラを扱うアプリがありません", Toast.LENGTH_LONG).show()
        }

        //user1の画像データ表示用のボタン
        button_user1.setOnClickListener {
            name = button_user1.text.toString()
            textView_user.text = name + "さんが選んだ写真の画面です"
            if(objList.isEmpty()){
                Toast.makeText(this, "データがありません", Toast.LENGTH_SHORT).show()
            }else {
                setImages(objList[0].getList("array"), imageList)
                //setImages2(objList[0].getList("array"))
            }
        }

        //user2の画像データ表示用のボタン
        button_user2.setOnClickListener {
            name = button_user2.text.toString()
            textView_user.text = name + "さんが選んだ写真の画面です"
            if(objList.size < 2){
                Toast.makeText(this, "データがありません", Toast.LENGTH_SHORT).show()
            }else{
                setImages(objList[1].getList("array"), imageList)
                //setImages2(objList[1].getList("array"))
            }
        }

        //user3の画像データ表示用のボタン
        button_user3.setOnClickListener {
            name = button_user3.text.toString()
            textView_user.text = name + "さんが選んだ写真の画面です"
            if(objList.size < 3){
                Toast.makeText(this, "データがありません", Toast.LENGTH_SHORT).show()
            }else{
                setImages(objList[2].getList("array"), imageList)
                //setImages2(objList[2].getList("array"))
            }
        }

        //user4の画像データ表示用のボタン
        button_user4.setOnClickListener {
            name = button_user4.text.toString()
            textView_user.text = name + "さんが選んだ写真の画面です"
            if(objList.size < 4){
                Toast.makeText(this, "データがありません", Toast.LENGTH_SHORT).show()
            }else{
                setImages(objList[3].getList("array"), imageList)
                //setImages2(objList[3].getList("array"))
            }
        }

        //user5の画像データ表示用のボタン
        button_user5.setOnClickListener {
            name = button_user5.text.toString()
            textView_user.text = name + "さんが選んだ写真の画面です"
            if(objList.size < 5){
                Toast.makeText(this, "データがありません", Toast.LENGTH_SHORT).show()
            }else{
                setImages(objList[4].getList("array"), imageList)
                //setImages2(objList[4].getList("array"))
            }
        }

        imageView3.setOnClickListener{
            if(null == imageView3.drawable){
                Toast.makeText(this, "データがありません", Toast.LENGTH_SHORT).show()
                selectPhoto1()
            }else {
                var dialog = Dialog(this)
                dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
                var bitmap = (imageView3.drawable as BitmapDrawable).bitmap
                val customdialogView: View = layoutInflater.inflate(R.layout.custom_dialog_layout, null)
                dialog.setContentView(customdialogView)
                Log.d("bitmap", bitmap.width.toString())
                //imageView.setImageBitmap(bitmap)
                var imageView_dialog = customdialogView.findViewById<ImageView>(R.id.imageView_dialog)
                imageView_dialog.setImageBitmap(bitmap)
                var textView4 = customdialogView.findViewById<TextView>(R.id.textView4)
                textView4.text = "dialog"
                var btn_change = customdialogView.findViewById<Button>(R.id.btn_change)
                btn_change.setOnClickListener {
                    selectPhoto1()
                }

                var btn_close = customdialogView.findViewById<Button>(R.id.btn_close)
                btn_close.setOnClickListener {
                    dialog.dismiss()
                }

                var display: Display = windowManager.defaultDisplay
                var size = Point()
                display.getSize(size)
                var width = size.x
                var factor = width.toFloat() / bitmap.width.toFloat()
                dialog.window?.setLayout(
                    (bitmap.width * factor).toInt(),
                    (bitmap.height * factor *0.7).toInt()
                )

                dialog.show()
            }
        }

        imageView4.setOnClickListener{
            if(null == imageView4.drawable){
                Toast.makeText(this, "データがありません", Toast.LENGTH_SHORT).show()
                selectPhoto2()
            }else {
                var dialog = Dialog(this)
                dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
                var bitmap = (imageView4.drawable as BitmapDrawable).bitmap
                val customdialogView: View = layoutInflater.inflate(R.layout.custom_dialog_layout, null)
                dialog.setContentView(customdialogView)
                Log.d("bitmap", bitmap.width.toString())
                //imageView.setImageBitmap(bitmap)
                var imageView_dialog = customdialogView.findViewById<ImageView>(R.id.imageView_dialog)
                imageView_dialog.setImageBitmap(bitmap)
                var textView4 = customdialogView.findViewById<TextView>(R.id.textView4)
                textView4.text = "dialog"
                var btn_change = customdialogView.findViewById<Button>(R.id.btn_change)
                btn_change.setOnClickListener {
                    selectPhoto1()
                }

                var btn_close = customdialogView.findViewById<Button>(R.id.btn_close)
                btn_close.setOnClickListener {
                    dialog.dismiss()
                }

                var display: Display = windowManager.defaultDisplay
                var size = Point()
                display.getSize(size)
                var width = size.x
                var factor = width.toFloat() / bitmap.width.toFloat()
                dialog.window?.setLayout(
                    (bitmap.width * factor).toInt(),
                    (bitmap.height * factor *0.7).toInt()
                )

                dialog.show()
            }
        }

        imageView5.setOnClickListener {
            if (null == imageView5.drawable) {
                Toast.makeText(this, "データがありません", Toast.LENGTH_SHORT).show()
                selectPhoto3()
            } else {
                var dialog = Dialog(this)
                dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
                var bitmap = (imageView5.drawable as BitmapDrawable).bitmap
                val customdialogView: View = layoutInflater.inflate(R.layout.custom_dialog_layout, null)
                dialog.setContentView(customdialogView)
                Log.d("bitmap", bitmap.width.toString())
                //imageView.setImageBitmap(bitmap)
                var imageView_dialog = customdialogView.findViewById<ImageView>(R.id.imageView_dialog)
                imageView_dialog.setImageBitmap(bitmap)
                var textView4 = customdialogView.findViewById<TextView>(R.id.textView4)
                textView4.text = "dialog"
                var btn_change = customdialogView.findViewById<Button>(R.id.btn_change)
                btn_change.setOnClickListener {
                    selectPhoto1()
                }

                var btn_close = customdialogView.findViewById<Button>(R.id.btn_close)
                btn_close.setOnClickListener {
                    dialog.dismiss()
                }

                var display: Display = windowManager.defaultDisplay
                var size = Point()
                display.getSize(size)
                var width = size.x
                var factor = width.toFloat() / bitmap.width.toFloat()
                dialog.window?.setLayout(
                    (bitmap.width * factor).toInt(),
                    (bitmap.height * factor *0.7).toInt()
                )

                dialog.show()
            }
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

    private fun takePicture() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            addCategory(Intent.CATEGORY_DEFAULT)
            putExtra(MediaStore.EXTRA_OUTPUT, createSaveFileUri())
        }

        startActivityForResult(intent, REQUEST_CAMERA)
    }

    private fun selectPhoto1(){
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_GARALLY1 )
    }

    private fun selectPhoto2(){
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_GARALLY2 )
    }

    private fun selectPhoto3(){
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_GARALLY3 )
    }

    private fun checkPermission(): Boolean {
        val cameraPermission = PackageManager.PERMISSION_GRANTED ==
                ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA)

        val extraStoragePermission = PackageManager.PERMISSION_GRANTED ==
                ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA)

        return cameraPermission && extraStoragePermission
    }

    private fun grantCameraPermission() =
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
            PERMISSION_REQUEST)

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put("_data", path)
            }

            contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

            val user = Users()
            user.photoName = filename.toString()
            dbHandler!!.addUser(user)

           /*contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

            val user = Users()
            user.photoName = filename.toString()
            dbHandler!!.addUser(user)*/

            //val inputStream = FileInputStream(File(path))
            //val bitmap = BitmapFactory.decodeStream(inputStream)
            //imageView.setImageBitmap(bitmap)
            //Toast.makeText(this, "この写真で良ければ更新ボタンでアップロードしてください", Toast.LENGTH_SHORT).show()
        }else if(requestCode == REQUEST_GARALLY1 && resultCode == Activity.RESULT_OK) {
            var uri: Uri?
            if(data != null){
                uri = data.data
                try{
                    //val inputStream = contentResolver.openInputStream(uri)
                    Log.d("[DEBUG]", uri.toString())
                    Log.d("[DEBUG]", uri?.path.toString())
                    var projection = arrayOf(MediaStore.MediaColumns.DISPLAY_NAME)
                    var cursor = contentResolver.query(uri!!, projection, null, null, null)
                    if(cursor != null){
                        var name : String = ""
                        if(cursor.moveToFirst()){
                            name = cursor.getString(0)
                        }
                        cursor.close()
                        Log.d("[DEBUG]", name.toString())
                    }
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                    imageView3.setImageBitmap(bitmap)
                }catch (e: IOException){
                }
            }
        }else if(requestCode == REQUEST_GARALLY2 && resultCode == Activity.RESULT_OK) {
            var uri: Uri?
            if(data != null){
                uri = data.data
                try{
                    //val inputStream = contentResolver.openInputStream(uri)
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                    imageView4.setImageBitmap(bitmap)
                }catch (e: IOException){
                }
            }
        }else if(requestCode == REQUEST_GARALLY3 && resultCode == Activity.RESULT_OK) {
            var uri: Uri?
            if(data != null){
                uri = data.data
                try{
                    //val inputStream = contentResolver.openInputStream(uri)
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                    imageView5.setImageBitmap(bitmap)
                }catch (e: IOException){
                }
            }
        }
    }

    private fun createSaveFileUri(): Uri {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.JAPAN).format(Date())
        val imageFileName = "example" + timeStamp

        val storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + "/example")
        if (!storageDir.exists()) {
            storageDir.mkdir()
        }
        Log.d("Dir place", storageDir.toString())

        val file = File(storageDir, "$imageFileName.jpg")
        Log.d("Maked file name", file.toString())
        filename = "$imageFileName.jpg"
        path = file.absolutePath
        Log.d("path", path.toString())

        return FileProvider.getUriForFile(this, "com.example.cloudexample", file)
    }
}
