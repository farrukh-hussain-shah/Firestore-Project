package com.example.firestoreproject.classes

data class Note(val title:String,val description:String){
    constructor():this("","")//no arg constructor needed
}