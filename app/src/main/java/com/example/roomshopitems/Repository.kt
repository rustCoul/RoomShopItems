package com.example.roomshopitems
import kotlinx.coroutines.flow.Flow
class Repository(
    private val categoryDao: CategoryModelDao,
    private val itemDao: ShopItemDao
){

    suspend fun addCategory(categoryModel: CategoryModel){
        categoryDao.addcategory(categoryModel)
    }
    suspend fun deleteCategory(categoryModel: CategoryModel){
        categoryDao.deletecategory(categoryModel)
    }
    suspend fun updateCategory(categoryModel: CategoryModel){
        categoryDao.updatecategory(categoryModel)
    }
    suspend fun selectAllCategories() : Flow<List<CategoryModel>> {
        return categoryDao.selectAllcategory()
    }


    suspend fun addItem(itemModel: ShopItem){
        itemDao.addShopItem(itemModel)
    }
    suspend fun deleteItem(itemModel: ShopItem){
        itemDao.deleteShopItem(itemModel)
    }
    suspend fun updateItem(itemModel: ShopItem){
        itemDao.updateShopItem(itemModel)
    }
    suspend fun selectItemsByCategory(category: Long): Flow<List<ShopItem>>{
        return  itemDao.selectAllShopItemByCategory(category = category)
    }
}