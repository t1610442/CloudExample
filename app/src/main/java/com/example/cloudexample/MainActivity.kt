package com.example.cloudexample

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.nifcloud.mbaas.core.*
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    var dbHandler: DatabaseHandler? = null

    var RESULT_CAMERA = 1001
    val PERMISSION_REQUEST = 1002
    var path: String =""
    var filename: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHandler = DatabaseHandler(this)

        var user = NCMBUser.getCurrentUser()
        Log.d("name", user.userName.toString())
        Log.d("objectid", user.objectId.toString())

        val queryphoto = NCMBQuery<NCMBObject>("photoPath")
        var abc = NCMBObject("photoPath")
        queryphoto.whereEqualTo("userID", user.objectId.toString())
        queryphoto.findInBackground {objects, error ->
            if (error != null) {
                Log.d("[Error2]", error.toString())
            } else {
                Log.d("[DEBUG3]", objects.toString())
                Log.d("[DEBUG4]", objects.size.toString())
                //Log.d("[objectid]", objects[0].objectId.toString())
                if (objects.size == 1) {
                    abc = objects[0]
                } else {
                }
            }
        }

        button3.setOnClickListener{
            val photoNameList = dbHandler!!.getAllData()
            val photoNameListCloud = arrayListOf<String>()
            Log.d("[DEBUG5]", photoNameList.toString())
            if(photoNameList.size==0){

            }else if(photoNameList.size==1){
                photoNameListCloud.addAll(photoNameList)
            }else if(photoNameList.size==2){
                photoNameListCloud.addAll(photoNameList)
            }else{
                photoNameListCloud.add(photoNameList[0])
                photoNameListCloud.add(photoNameList[1])
                photoNameListCloud.add(photoNameList[2])
            }
            //for(i in 0..2){
            //photoNameListCloud.add(photoNameList[i])
            //Log.d("CloudList", photoNameListCloud.toString())
            //}

            //val obj = NCMBObject("photoPath")
            //obj.put("imageName1", photoNameListCloud[0])
            //obj.put("imageName2", photoNameListCloud[1])
            //obj.put("imageName3", photoNameListCloud[2])
            //obj.put("array", photoNameListCloud)
            abc.put("userID", user.objectId.toString())
            abc.put("name", user.userName.toString())
            abc.put("array", photoNameListCloud)
            //obj.put("pathName", photoNameListCloud as List<*>)
            //obj.addUniqueToList("pathName", photoNameListCloud)
            //Log.d("[DEBUG6]", abc.objectId.toString())
            /*if (abc.objectId == null) {
                var acl = NCMBAcl()
                acl.setReadAccess(NCMBUser.getCurrentUser().objectId, true)
                acl.setWriteAccess(NCMBUser.getCurrentUser().objectId, true)
                abc.acl = acl
            }*/

            //ACL 読み込み:可 , 書き込み:可
            val acl = NCMBAcl()
            acl.publicReadAccess = true
            acl.publicWriteAccess = true

            //通信実施
            val file = NCMBFile(filename, File(path).readBytes(), acl)
            Toast.makeText(this, "データをアップロード中.そのままお待ちください", Toast.LENGTH_SHORT).show()
            file.saveInBackground { e ->
                val result: String
                if (e != null) {
                    //保存失敗
                    AlertDialog.Builder(this@MainActivity)
                        .setTitle("Notification from NIFCloud")
                        .setMessage("Error:" + e.message)
                        .setPositiveButton("OK", null)
                        .show()
                }else{
                    Log.d("RESULT", "SUCCESS")
                }
            }

            abc.saveInBackground { e ->
                if(e != null){
                    Log.d("[Error]", e.toString())
                }else{
                    Log.d("[ObjectResult]", "SUCCESS")
                    Toast.makeText(this, "アップロード完了", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        button2.setOnClickListener{
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).resolveActivity(packageManager)?.let {
                if (checkPermission()) {
                    takePicture()
                } else {
                    grantCameraPermission()
                }
            } ?: Toast.makeText(this, "カメラを扱うアプリがありません", Toast.LENGTH_LONG).show()
        }

        button4.setOnClickListener {
            Log.d("PhotoList", dbHandler?.getAllData().toString())
        }

        button6.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }

        button10.setOnClickListener {
            val intent = Intent(this, DataSelectActivity::class.java)
            startActivity(intent)
        }
    }

    private fun takePicture() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            addCategory(Intent.CATEGORY_DEFAULT)
            putExtra(MediaStore.EXTRA_OUTPUT, createSaveFileUri())
        }

        startActivityForResult(intent, RESULT_CAMERA)
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
        if (requestCode == RESULT_CAMERA && resultCode == Activity.RESULT_OK) {
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put("_data", path)
            }
            contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

            val user: Users = Users()
            user.photoName = filename.toString()
            dbHandler!!.addUser(user)

            val inputStream = FileInputStream(File(path))
            val bitmap = BitmapFactory.decodeStream(inputStream)
            imageView.setImageBitmap(bitmap)
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
