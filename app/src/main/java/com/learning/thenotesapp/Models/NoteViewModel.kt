package com.learning.thenotesapp.Models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.learning.thenotesapp.Database.NoteDatabase
import com.learning.thenotesapp.Database.NotesRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private val repositry : NotesRepo
    val allNotes : LiveData<List<Note>>
    init {
        val dao = NoteDatabase.getDatabase(application).getNoteDao()
        repositry = NotesRepo(dao)
        allNotes = repositry.allNotes
    }

    fun deleteNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repositry.delete(note)
    }

    fun insertNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repositry.insert(note)
    }

    fun updateNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repositry.update(note)
    }
}