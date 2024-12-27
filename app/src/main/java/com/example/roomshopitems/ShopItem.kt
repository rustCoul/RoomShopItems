package com.example.roomshopitems

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Entity( "item",
foreignKeys = [
    ForeignKey(
        entity = CategoryModel::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("category"),
        onDelete = ForeignKey.CASCADE
)
])
data class ShopItem(

    val name:String,
    val category: Long,
    @PrimaryKey(autoGenerate = true)
    val id:Long=0,
)

@Dao
interface ShopItemDao{
    @Insert
    suspend abstract fun addShopItem(shopItem: ShopItem)

    @Delete
    suspend  abstract fun deleteShopItem(shopItem: ShopItem)

    @Query("Select * From item Where category = :category")
    abstract fun selectAllShopItemByCategory(category: Long): Flow<List<ShopItem>>

    @Update
    suspend abstract fun updateShopItem(shopItem: ShopItem)
}
