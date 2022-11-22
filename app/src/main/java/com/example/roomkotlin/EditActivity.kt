package com.example.roomkotlin


import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.roomkotlin.room.Constant
import com.example.roomkotlin.room.Note
import com.lazday.kotlinroommvvm.room.NoteDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditActivity : AppCompatActivity() {

    val db by lazy { NoteDB(this) }
    private var noteId: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        setupListener()
        setupView()

    }

    fun setupView(){
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val intentType = intent.getIntExtra("intent_type", 0)
        when (intentType){
            Constant.TYPE_CREATE -> {
                val button_update = findViewById<Button>(R.id.button_update)
                button_update.visibility = View.GONE
            }
            Constant.TYPE_READ -> {
                val button_save = findViewById<Button>(R.id.button_save)
                val button_update = findViewById<Button>(R.id.button_update)
                button_save.visibility = View.GONE
                button_update.visibility = View.GONE
                getNote()
            }
            Constant.TYPE_UPDATE -> {
                val button_save = findViewById<Button>(R.id.button_save)
                button_save.visibility = View.GONE
                getNote()
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun setupListener() {
        val button_save = findViewById<Button>(R.id.button_save)
        button_save.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
            val edit_title = findViewById<EditText>(R.id.edit_title)
            val edit_note = findViewById<EditText>(R.id.edit_note)
                db.noteDao().addNote(
                Note(0, edit_title.text.toString(), edit_note.text.toString())
            )
                finish()
            }

        }
        val button_update = findViewById<Button>(R.id.button_update)
        button_update.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val edit_title = findViewById<EditText>(R.id.edit_title)
                val edit_note = findViewById<EditText>(R.id.edit_note)
                db.noteDao().updateNote(
                    Note(noteId, edit_title.text.toString(), edit_note.text.toString())
                )
                finish()
            }  }
    }
    fun getNote(){
        noteId = intent.getIntExtra("intent_id", 0)
        CoroutineScope(Dispatchers.IO).launch {
            val edit_title = findViewById<EditText>(R.id.edit_title)
            val edit_note = findViewById<EditText>(R.id.edit_note)
            val notes = db.noteDao().getNote(noteId)[0]
            edit_title.setText(notes.title)
            edit_note.setText(notes.note)
    }
}

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}