package com.learning.thenotesapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.SearchView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.learning.thenotesapp.Adapter.NotesAdapter
import com.learning.thenotesapp.Database.NoteDatabase
import com.learning.thenotesapp.Models.Note
import com.learning.thenotesapp.Models.NoteViewModel
import com.learning.thenotesapp.databinding.ActivityAddNoteBinding
import com.learning.thenotesapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), NotesAdapter.NotesClickListener, PopupMenu.OnMenuItemClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var database: NoteDatabase
    lateinit var notesRV: RecyclerView
    lateinit var viewModel: NoteViewModel
    lateinit var adapter: NotesAdapter
    lateinit var selectedNote: Note

    private val updateNote = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if (result.resultCode == Activity.RESULT_OK){
            val note = result.data?.getSerializableExtra("note") as? Note
            if (note != null){
                viewModel.updateNote(note)
            }
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initUI()
        viewModel = ViewModelProvider(this,
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(NoteViewModel::class.java)
        viewModel.allNotes.observe(this,{ list->
            list?.let {
                adapter.updateList(it)
            }
        })
        database = NoteDatabase.getDatabase(this)
    }

    private fun initUI(){
        binding.idRecyclerView.hasFixedSize()
        binding.idRecyclerView.layoutManager = StaggeredGridLayoutManager(2, LinearLayout.VERTICAL)
        adapter= NotesAdapter(this,this)
        binding.idRecyclerView.adapter = adapter

        val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == Activity.RESULT_OK){
                val note = result.data?.getSerializableExtra("current_note") as? Note
                if (note != null){
                    viewModel.insertNote(note)
                }
            }
        }
        binding.fbAddNote.setOnClickListener{
            val intent = Intent(this, AddNoteActivity::class.java)
            getContent.launch(intent)
        }
        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

                override fun onQueryTextChange(newText: String?): Boolean {
                if(newText != null){
                    adapter.filter(newText)
                }
                    return true
            }

        })


    }

    override fun onItemClick(note: Note) {
        val intent = Intent(this@MainActivity, AddNoteActivity::class.java)
        intent.putExtra("current_note", note)
        intent.putExtra("noteTitle", note.title)
        intent.putExtra("noteDescription", note.note)
        intent.putExtra("noteId", note.id)
        updateNote.launch(intent)
    }



    override fun onLongItemClick(note: Note, cardView: CardView) {
        selectedNote = note
        popUpDisplay(cardView)
    }

    fun popUpDisplay(cardView: CardView){
        val popUp = PopupMenu(this, cardView)
        popUp.setOnMenuItemClickListener(this@MainActivity)
        popUp.inflate(R.menu.pop_up_menu)
        popUp.show()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {

        if (item?.itemId == R.id.delete_note){
            viewModel.deleteNote(selectedNote)
            return true
        }
        return false
    }
}

