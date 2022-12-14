package com.example.roomkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.ButtonBarLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.roomkotlin.room.Constant
import com.example.roomkotlin.room.Note
import com.lazday.kotlinroommvvm.room.NoteDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {
    val db by lazy { NoteDB(this) }
    lateinit var noteAdapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupListener()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {

      noteAdapter = NoteAdapter(arrayListOf(), object : NoteAdapter.OnAdapterListener{
          override fun onClick(note: Note) {
              intentEdit(note.id, Constant.TYPE_READ)
          }

          override fun onUpdate(note: Note) {
              intentEdit(note.id, Constant.TYPE_UPDATE)
          }

          override fun onDelete(note: Note) {
              CoroutineScope(Dispatchers.IO).launch {
                  db.noteDao().deleteNote(note)
                  loadNote()
              }
          }

      })
        val list_note = findViewById<RecyclerView>(R.id.list_note)
        list_note.apply{
          layoutManager = LinearLayoutManager(applicationContext)
            adapter = noteAdapter
      }
    }

    override fun onStart() {
        super.onStart()
        loadNote()

    }
    fun loadNote(){
        CoroutineScope(Dispatchers.IO).launch {
            val edit_title = findViewById<EditText>(R.id.edit_title)
            val edit_note = findViewById<EditText>(R.id.edit_note)
            val notes = db.noteDao().getNotes()
            Log.d("MainActivity", "dbResponse: $notes")
            withContext(Dispatchers.Main) {
                noteAdapter.setData(notes)
            }
        }
    }
    fun setupListener() {
        val button_create = findViewById<Button>(R.id.button_create)
        button_create.setOnClickListener {
           intentEdit(0, Constant.TYPE_CREATE)


        }
    }
    fun intentEdit(noteId:Int, intentType:Int){
        startActivity(
            Intent(applicationContext, EditActivity::class.java)
                .putExtra("intent_id", noteId)
                .putExtra("intent_type", intentType)
        )
    }
}
