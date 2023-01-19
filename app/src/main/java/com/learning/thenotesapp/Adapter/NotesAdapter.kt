package com.learning.thenotesapp.Adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.learning.thenotesapp.Models.Note
import com.learning.thenotesapp.R
import kotlin.random.Random

class NotesAdapter(private val context: Context, private val listener: NotesClickListener) :
    RecyclerView.Adapter<NotesAdapter.NoteVH>() {

    private val noteList = ArrayList<Note>()

    inner class NoteVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val notesLayout = itemView.findViewById<CardView>(R.id.card_layout)
        val title = itemView.findViewById<TextView>(R.id.tv_title)
        val noteTv = itemView.findViewById<TextView>(R.id.tv_note)
        val date = itemView.findViewById<TextView>(R.id.tv_date)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteVH {
        return NoteVH(
            LayoutInflater.from(context).inflate(R.layout.list_items, parent, false)
        )
    }

    override fun onBindViewHolder(holder: NoteVH, position: Int) {
        val currentNote = noteList[position]
        holder.title.text = currentNote.title
        holder.noteTv.text = currentNote.note
        holder.title.isSelected = true

        holder.date.text = currentNote.date
        holder.date.isSelected = true

        holder.notesLayout.setOnClickListener {
            listener.onItemClick(noteList[holder.adapterPosition])
        }
        holder.notesLayout.setOnLongClickListener {
            listener.onLongItemClick(noteList[holder.adapterPosition], holder.notesLayout)
            true
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            holder.notesLayout.setCardBackgroundColor(
                holder.itemView.resources.getColor(
                    randomColor(),
                    null
                )
            )
        }
    }


    override fun getItemCount(): Int {
        return noteList.size
    }

    fun updateList(newList: List<Note>){
        noteList.clear()
        noteList.addAll(newList)
        notifyDataSetChanged()
    }

    fun filter(search: String){
    noteList.clear()
        for (item in noteList){
            if(item.title?.lowercase()?.contains(search.lowercase()) == true ||
                    item.note?.lowercase()?.contains(search.lowercase()) == true){
                noteList.add(item)
            }
        }
        notifyDataSetChanged()
    }
    private fun randomColor(): Int {
        val list = ArrayList<Int>()
        list.add(R.color.NoteColor1)
        list.add(R.color.NoteColor2)
        list.add(R.color.NoteColor3)
        list.add(R.color.NoteColor4)
        list.add(R.color.NoteColor5)
        list.add(R.color.NoteColor6)
        list.add(R.color.NoteColor7)
        list.add(R.color.NoteColor8)
        list.add(R.color.NoteColor9)

        val seed = System.currentTimeMillis().toInt()
        val randomIndex = Random(seed).nextInt(list.size)
        return list[randomIndex]
    }



    interface NotesClickListener {
        fun onItemClick(note: Note)
        fun onLongItemClick(note: Note, cardView: CardView)
    }


}


