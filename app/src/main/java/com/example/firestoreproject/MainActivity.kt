package com.example.firestoreproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.firestoreproject.classes.Note
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var editTextTitle: EditText
    private lateinit var editTextDescription: EditText
    private lateinit var saveButton: Button
    private lateinit var loadButton: Button
    private lateinit var buttonUpdateTitle: Button
    private lateinit var buttonDeleteDescription: Button
    private lateinit var buttonDeleteNote: Button
    private lateinit var TextViewData:TextView
    private val db:FirebaseFirestore=FirebaseFirestore.getInstance()
    private val docRef:DocumentReference=db.collection("NoteBook").document("My first note")
    private val KEY_TITLE="title"
    private val KEY_DESCRIPTION="description"
    private lateinit var listener:ListenerRegistration


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextDescription=findViewById(R.id.edit_text_description)
        editTextTitle=findViewById(R.id.edit_text_tilte)
        saveButton=findViewById(R.id.button_save)
        loadButton=findViewById(R.id.button_load)
        buttonUpdateTitle=findViewById(R.id.button_update_title)
        buttonDeleteDescription=findViewById(R.id.button_delete_description)
        buttonDeleteNote=findViewById(R.id.button_delete_note)
        TextViewData=findViewById(R.id.text_view_data)

        saveButton.setOnClickListener{
            save()
        }

        loadButton.setOnClickListener {
            loadData()
        }

        buttonUpdateTitle.setOnClickListener {
            updateTitle()
        }

        buttonDeleteDescription.setOnClickListener {
            deletedescription()
        }

        buttonDeleteNote.setOnClickListener {
            deleteNote()
        }


    }

    private fun deletedescription() {
        val note= mutableMapOf<String,Any>()
        note.put(KEY_DESCRIPTION,FieldValue.delete())
        docRef.update(note)
    }

    private fun deleteNote(){
        docRef.delete()
    }

    //this function updates the title of a Firestore document using the value entered in the editTextTitle field. The updated title is stored in the Firestore document referenced by docRef.
    private fun updateTitle() {
        val title= editTextTitle.text.toString()
        val note = mutableMapOf<String,Any>()
        note[KEY_TITLE]=title
        docRef.set(note, SetOptions.merge())
    }


    // the "this" is used for a purpose  that the data will remove automatically when app are closed
    //onStart() method with the snapshot listener ensures that whenever the data in the Firestore document referenced by docRef changes, the TextViewData TextView is updated in real-time to reflect the latest title and description values. The listener continues to listen for changes until the Activity is stopped or destroyed.
    override fun onStart() {
        super.onStart()
       docRef.addSnapshotListener(this) { document, error ->
            error?.let {
                return@addSnapshotListener
            }
            document?.let {
                if (it.exists()){
//                    val title = it.getString(KEY_TITLE)       TODO this will be erase in 7 lecture we can use it's alternative
//                    val description = it.getString(KEY_DESCRIPTION)

            val note= it.toObject(Note::class.java)

                    TextViewData.text="Title: "+"${note?.title}\nDescription: ${note?.description}"

                }else {
                    TextViewData.text=""

                }}
            }

    }

//    override fun onStop() {
//        super.onStop()
//        listener.remove()
//    }

    private fun loadData() {
        docRef.get().
        addOnSuccessListener {
           document ->
            if (document.exists()){
           val title = document.getString(KEY_TITLE)
           val description = document.getString(KEY_DESCRIPTION)


                TextViewData.text="Title "+"$title\nDescription $description"

           }else{
                Toast.makeText(this,"The document does't exists",Toast.LENGTH_LONG).show()
            }


        }.addOnFailureListener {
            Toast.makeText(this,"Note Not  Added!",Toast.LENGTH_SHORT).show()
        }
    }

    private fun save(){
        val title=editTextTitle.text.toString()
        val description=editTextDescription.text.toString()

//        val note= mutableMapOf<String,Any>()
//        note.put(KEY_TITLE,title)     TODO this will be erase in 7 lecture we can use it's alternative
//        note.put(KEY_DESCRIPTION,description)

        val note=Note(title, description)

       docRef.set(note)
            .addOnSuccessListener {
                Toast.makeText(this,"Note Added!",Toast.LENGTH_LONG).show()
            }.addOnFailureListener{
                Toast.makeText(this,"Note Not  Added!",Toast.LENGTH_LONG).show()
            }

    }
}