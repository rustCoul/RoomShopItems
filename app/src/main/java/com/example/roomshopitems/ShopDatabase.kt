package com.example.roomshopitems

import androidx.room.Database
import androidx.room.RoomDatabase
@Database(
    entities = [CategoryModel::class, ShopItem::class],
    exportSchema = false,
    version = 1
)
abstract class ShopDatabase:RoomDatabase() {
    abstract fun getItemDao():ShopItemDao
    abstract fun getCategoryDao():CategoryModelDao
}