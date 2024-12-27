package com.example.roomshopitems


import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.roomshopitems.R.color.auburn
import com.example.roomshopitems.R.color.black
import com.example.roomshopitems.R.color.black_bean
import com.example.roomshopitems.R.color.falu_red
import com.example.roomshopitems.R.color.rosewood
import com.example.roomshopitems.R.color.white

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CategoryScreen(
    categoryViewModel: CategorViewModel,
    navHostController: NavHostController,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Categories") }
            )
        },
        containerColor = colorResource(white),
        content = { innerPadding ->
            MainContent(Modifier.padding(innerPadding), categoryViewModel, navHostController)

        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = colorResource(falu_red),
                onClick = {
                    categoryViewModel.changeAddingStatus()
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "",
                    tint = colorResource(white)
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(
    modifier: Modifier,
    categoryViewModel: CategorViewModel,
    navHostController: NavHostController,
) {
    val categoryList by categoryViewModel.listOfCategories.collectAsState()
    val isAdding by categoryViewModel.isAddingCategory.collectAsState()
    val isEditing by categoryViewModel.isEditingCategory.collectAsState()
    val selectedCategory = categoryViewModel.selectedCategory.collectAsState()
    // val dummyList = listOf(Dummy.dummy1, Dummy.dummy2, Dummy.dummy3, Dummy.dummy4)

    LaunchedEffect(
        categoryList.size
    ) {
        categoryViewModel.getAllCategories()
    }

    if (isAdding) {
        CategoryAddDialog(categoryViewModel)
    } else if (isEditing && selectedCategory.value != null) {
        CategoryEditDialog(categoryViewModel, selectedCategory.value!!)
    }

    LazyColumn(
        modifier = modifier
    ) {
        items(categoryList, key = { category -> category.id }) { category ->
            val dismissState = rememberSwipeToDismissBoxState(
                confirmValueChange = {
                    if (it == SwipeToDismissBoxValue.StartToEnd) {
                        categoryViewModel.deleteCategory(category)
                        true
                    }
                    else {
                        false
                    }
                }
            )
            SwipeToDismissBox(
                state = dismissState,
                backgroundContent = {}
            ) {
                CategoryItems(
                    categoryViewModel,
                    categoryModel = category,
                    navHostController,
                    onEditClicked = { selectedCategory ->
                        categoryViewModel.startEditingCategory(selectedCategory)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryAddDialog(
    categoryViewModel: CategorViewModel,
) {
    val categoryName by categoryViewModel.nameOfCategory.collectAsState()
    val context = LocalContext.current

    BasicAlertDialog(
        modifier = Modifier
            .background(
                brush = Brush.linearGradient(
                    listOf(
                        colorResource(auburn),
                        colorResource(black_bean)
                    )
                ),
                shape = RoundedCornerShape(20.dp)
            )
            .height(150.dp)
            .width(420.dp)
        ,
        onDismissRequest = {
            categoryViewModel.changeAddingStatus()
        }
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(ButtonDefaults.shape),
                label = { Text("Category name") },
                value = categoryName,
                onValueChange = {
                    categoryViewModel.setCategoryName(it)
                }
            )
            Spacer(Modifier.padding(vertical = 10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    modifier = Modifier
                        .background(
                            brush = Brush.run {
                                linearGradient(
                                    listOf(
                                        colorResource(black_bean),
                                        colorResource(auburn)
                                    )
                                )
                            },
                            shape = ButtonDefaults.shape
                        )
                        .border(
                            1.dp,
                            color = colorResource(black),
                            shape = ButtonDefaults.shape
                        ),
                    onClick = {
                        if(categoryName.isNotBlank()){
                            categoryViewModel.addCategory(
                                CategoryModel(
                                    id = 0L,
                                    name = categoryName
                                )
                            )
                            categoryViewModel.changeAddingStatus()
                        }
                        else {
                            Toast.makeText(context, "Category name can't be empty", Toast.LENGTH_LONG).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(Color.Transparent)
                ) {
                    Text(
                        text = "Add new category",
                        color = colorResource(white)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryEditDialog(
    categoryViewModel: CategorViewModel,
    categoryModel: CategoryModel,
) {
    val categoryName by categoryViewModel.nameOfCategory.collectAsState()
    val context = LocalContext.current

    BasicAlertDialog(
        modifier = Modifier
            .background(
                brush = Brush.linearGradient(
                    listOf(
                        colorResource(auburn),
                        colorResource(black_bean)
                    )
                )
                ,
                shape = RoundedCornerShape(20.dp)
            )
            .height(150.dp)
            .width(420.dp),
        onDismissRequest = {
            categoryViewModel.stopEditingCategory()
        }
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(ButtonDefaults.shape),
                label = { Text("Edit category name") },
                value = categoryName,
                onValueChange = {
                    categoryViewModel.setCategoryName(it)
                }
            )
            Spacer(Modifier.padding(vertical = 10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    modifier = Modifier
                        .background(
                            brush = Brush.run {
                                linearGradient(
                                    listOf(
                                        colorResource(black_bean),
                                        colorResource(auburn)
                                    )
                                )
                            },
                            shape = ButtonDefaults.shape
                        )
                        .border(
                            1.dp,
                            color = colorResource(black),
                            shape = ButtonDefaults.shape
                        ),
                    onClick = {
                        if(categoryName.isNotBlank()){
                            categoryViewModel.editCategory(
                                categoryModel.copy(name = categoryName)
                            )
                        }
                        else{
                            Toast.makeText(context, "Category name can't be empty!", Toast.LENGTH_LONG).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(Color.Transparent)
                ) {
                    Text("Save")
                }
            }
        }
    }
}

@Composable
fun CategoryItems(
    categoryViewModel: CategorViewModel,
    categoryModel: CategoryModel,
    navHostController: NavHostController,
    onEditClicked: (CategoryModel) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(8.dp)
            .clickable {
                categoryViewModel.setSelectedCategory(categoryModel)
                navHostController.navigate(ScreensRoute.ITEMS_SCREEN)
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        listOf(
                            colorResource(black_bean),
                            colorResource(auburn)
                        )
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.padding(10.dp).weight(10f),
                text = categoryModel.name,
                color = colorResource(white)
            )
            IconButton(
                onClick = {
                    onEditClicked(categoryModel)
                },
                modifier = Modifier
                    .background(
                        brush = Brush.linearGradient(
                            listOf(
                                colorResource(falu_red),
                                colorResource(rosewood)
                            )
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = " ",
                    tint = Color.White
                )
            }
        }
    }
}