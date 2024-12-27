package com.example.roomshopitems

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ItemViewModel:ViewModel() {
    private val _listOfItems = MutableStateFlow(listOf<ShopItem>())
    val listOfItems = _listOfItems.asStateFlow()

    private val _nameOfItem = MutableStateFlow("")
    val nameOfItem = _nameOfItem.asStateFlow()

    private val _isAddingItem = MutableStateFlow(false)
    private val _isEditingItem = MutableStateFlow(false)
    val isAddingItem = _isAddingItem.asStateFlow()
    val isEditingItem = _isEditingItem.asStateFlow()

    private val _selectedItem = MutableStateFlow<ShopItem?>(null)
    val selectedItem = _selectedItem.asStateFlow()

    fun getItemsByCategory(categoryId: Long){
        viewModelScope.launch(
            Dispatchers.IO
        ) {
            val value = Graph.repository.selectItemsByCategory(categoryId)
            value.collect{ item ->
                _listOfItems.value = item
            }
        }
    }

    fun changeAddingStatus(){
        _isAddingItem.value = !_isAddingItem.value
        clearWrittenText()
    }

    fun startEditingItem(itemModel: ShopItem){
        _isEditingItem.value = !_isEditingItem.value
    }

    fun stopEditingItem(){
        _selectedItem.value = null
        _isEditingItem.value = !_isEditingItem.value
    }

    fun setSelectedItem(itemModel: ShopItem){
        _selectedItem.value = itemModel
    }

    fun addItem(itemModel: ShopItem){
        viewModelScope.launch(
            Dispatchers.IO
        ) {
            Graph.repository.addItem(itemModel)
        }
    }

    fun editItem(itemModel: ShopItem){
        viewModelScope.launch(
            Dispatchers.IO
        ) {
            Graph.repository.updateItem(itemModel)
            clearWrittenText()
            stopEditingItem()
        }
    }

    fun setItemName(itemName: String){
        _nameOfItem.value = itemName
    }

    private fun clearWrittenText(){
        _nameOfItem.value = ""
    }

    fun deleteItem(itemModel: ShopItem) {
        viewModelScope.launch(
            Dispatchers.IO
        ) {
            Graph.repository.deleteItem(itemModel)
        }
    }
}