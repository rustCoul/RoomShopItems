package com.example.roomshopitems

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Entity("category")
data class CategoryModel(
    @PrimaryKey(true)
    val id:Long=0,

    val name:String
)

@Dao
 interface CategoryModelDao{
    @Insert
     abstract fun addcategory(categoryModel: CategoryModel)

    @Delete
     abstract fun deletecategory(categoryModel: CategoryModel)

    @Query("SELECT * FROM item")
     abstract fun selectAllcategory(): Flow<List<CategoryModel>>

    @Update
     abstract fun updatecategory(categoryModel: CategoryModel)
}