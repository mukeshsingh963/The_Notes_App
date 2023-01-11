package com.learning.thenotesapp

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.learning.thenotesapp.Models.Note
import com.learning.thenotesapp.databinding.ActivityAddNoteBinding
import java.text.SimpleDateFormat
import java.util.*

class AddNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNoteBinding

    private lateinit var note: Note
    private lateinit var oldNote: Note
    var isUpdate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            oldNote = intent.getSerializableExtra("current_note") as Note
            binding.noteTitle.setText(oldNote.title)
            binding.noteBody.setText(oldNote.note)
            isUpdate = true
        }
        catch (e:Exception){
            e.printStackTrace()
        }
        binding.checkMark.setOnClickListener{
            val title = binding.noteTitle.text.toString()
            val body = binding.noteBody.text.toString()
            if(title.isNotEmpty() || body.isNotEmpty()){
                val formatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    SimpleDateFormat("EEE, d MMM YYYY HH:MM a")
                } else {
                    TODO("VERSION.SDK_INT < N")
                }
                if (isUpdate){
                    note = Note( oldNote.id,title,body,formatter.format(Date()))
                }else{
                    note = Note( null,title,body,formatter.format(Date()))
                }
                val intent = Intent()
                intent.putExtra("current_note", note)
                setResult(Activity.RESULT_OK, intent)
                finish()
            } else{
                Toast.makeText(this@AddNoteActivity, "Please enter some text",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
        }

        binding.backArrow.setOnClickListener {
            onBackPressed()
        }
    }

}