package com.example.cloudexample

import android.app.Dialog
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Display
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_diary_list.*

class DiaryListActivity : AppCompatActivity() {

    var dbHandler = DatabaseHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_list)

        var diaries = dbHandler.getAllUsers()
        val adapter = DiaryListAdapter(this, diaries)
        list_view.adapter = adapter

        list_view.setOnItemClickListener { adapterView, view, position, id ->
            //val name = view.findViewById<TextView>(android.R.id.text1).text
            val diarydata = adapterView.getItemAtPosition(position) as DiaryData
            val title = diarydata.title
            val day = diarydata.day
            val photo = diarydata.photo
            //Log.d("test", name1)
            Toast.makeText(this, "$title の日記を表示します", Toast.LENGTH_SHORT).show()

            var dialog = Dialog(this)
            dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
            val customdialogView: View = layoutInflater.inflate(R.layout.custom_dialog_diary, null)
            dialog.setContentView(customdialogView)
            var imageView_dialog = customdialogView.findViewById<ImageView>(R.id.ctm_imgae_imageView)
            imageView_dialog.setImageBitmap(BitmapFactory.decodeFile(photo))
            var titleTextView_dialog = customdialogView.findViewById<TextView>(R.id.ctm_title_textView)
            titleTextView_dialog.text = title
            var dayTextView_dialog = customdialogView.findViewById<TextView>(R.id.ctm_day_textView)
            dayTextView_dialog.text = day

            var display: Display = windowManager.defaultDisplay
            dialog.show()
        }
    }
}
