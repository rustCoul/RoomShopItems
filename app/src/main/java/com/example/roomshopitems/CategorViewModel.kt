package com.example.roomshopitems

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
class CategorViewModel:ViewModel() {
    private val _listOfCategories = MutableStateFlow(listOf<CategoryModel>())
    val listOfCategories = _listOfCategories.asStateFlow()

    private val _nameOfCategory = MutableStateFlow("")
    val nameOfCategory = _nameOfCategory.asStateFlow()

    private val _selectedCategory = MutableStateFlow<CategoryModel?>(null)
    val selectedCategory = _selectedCategory.asStateFlow()

    private val _isAddingCategory = MutableStateFlow(false)
    private val _isEditingCategory = MutableStateFlow(false)
    val isAddingCategory = _isAddingCategory.asStateFlow()
    val isEditingCategory = _isEditingCategory.asStateFlow()

    fun getAllCategories(){
        viewModelScope.launch(
            Dispatchers.IO
        ) {
            val value = Graph.repository.selectAllCategories()
            value.collect{ category ->
                _listOfCategories.value = category
            }
        }
    }

    fun setSelectedCategory(categoryModel: CategoryModel){
        _selectedCategory.value = categoryModel
    }

    fun startEditingCategory(categoryModel: CategoryModel){
        _selectedCategory.value = categoryModel
        _isEditingCategory.value = !_isEditingCategory.value
    }

    fun stopEditingCategory(){
        _selectedCategory.value = null
        _isEditingCategory.value = !_isEditingCategory.value
    }

    fun deleteCategory(categoryModel: CategoryModel){
        viewModelScope.launch(
            Dispatchers.IO
        ) {
            Graph.repository.deleteCategory(categoryModel)
        }
    }

    fun editCategory(categoryModel: CategoryModel){
        viewModelScope.launch(
            Dispatchers.IO
        ) {
            Graph.repository.updateCategory(categoryModel)
            getAllCategories()
            clearWrittenText()
            stopEditingCategory()
        }
    }

    fun addCategory(categoryModel: CategoryModel){
        viewModelScope.launch(
            Dispatchers.IO
        ) {
            Graph.repository.addCategory(categoryModel)
        }
    }

    fun changeAddingStatus(){
        _isAddingCategory.value = !_isAddingCategory.value
        clearWrittenText()
    }

    fun setCategoryName(categoryName: String){
        _nameOfCategory.value = categoryName
    }

    private fun clearWrittenText(){
        _nameOfCategory.value = ""
    }
}