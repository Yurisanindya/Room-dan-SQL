package com.example.roomkotlin
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.roomkotlin.room.Note
import android.widget.TextView
import com.example.roomkotlin.databinding.ActivityMainBinding


class NoteAdapter (val notes: ArrayList<Note>, val listener: OnAdapterListener) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    class NoteViewHolder( view: View) : RecyclerView.ViewHolder(view) {

        val texttitle = view.findViewById<TextView>(R.id.text_title)
        val icon_edit = view.findViewById<ImageView>(R.id.icon_edit)
        val icon_delete = view.findViewById<ImageView>(R.id.icon_delete)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_note, parent, false)
        return NoteViewHolder(view)


    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {

        val note = notes[position]
        holder.texttitle.text = notes?.get(position)?.title
        holder.texttitle.setOnClickListener {
            listener.onClick(note) }

        holder.icon_edit.setOnClickListener {
            listener.onUpdate(note) }

        holder.icon_delete.setOnClickListener {
            listener.onDelete(note)
        } }


        override fun getItemCount() = notes.size
        fun setData(list: List<Note>) {
            notes.clear()
            notes.addAll(list)
            notifyDataSetChanged()
        }

        interface OnAdapterListener {
            fun onClick(note: Note)
            fun onUpdate(note: Note)
            fun onDelete (note: Note)

        }
    }


