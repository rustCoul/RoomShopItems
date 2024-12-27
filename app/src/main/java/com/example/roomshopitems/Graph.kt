package com.example.roomshopitems

import android.content.Context
import androidx.room.Room


object Graph{
    private lateinit var database: ShopDatabase

    val repository by lazy {
        Repository(
            categoryDao = database.getCategoryDao(),
            itemDao = database.getItemDao()
        )
    }

    fun provide(context: Context){
        this.database = Room.databaseBuilder(context, ShopDatabase::class.java, "ShopDatabase.db").build()
    }
}