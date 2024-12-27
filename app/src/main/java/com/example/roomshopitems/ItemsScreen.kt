package com.example.roomshopitems


import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemsScreen(
    itemViewModel: ItemViewModel,
    navHostController: NavHostController,
    categoryViewModel: CategorViewModel
) {
    val category by categoryViewModel.selectedCategory.collectAsState()

    Log.e("Category", category.toString())

    Scaffold(
        Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Items") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navHostController.navigate(ScreensRoute.CATEGORIES_SCREEN)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = colorResource(falu_red),
                onClick = {
                    itemViewModel.changeAddingStatus()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "",
                    tint = colorResource(white)
                )
            }
        },
        content = {innerPadding ->
            MainContent(Modifier.padding(innerPadding), itemViewModel, category!!.id)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(
    modifier: Modifier,
    itemViewModel: ItemViewModel,
    categoryId: Long
) {
    val itemList by itemViewModel.listOfItems.collectAsState()
    val isAdding by itemViewModel.isAddingItem.collectAsState()
    val isEditing by itemViewModel.isEditingItem.collectAsState()
    val selectedItem by itemViewModel.selectedItem.collectAsState()

    LaunchedEffect(itemList.size) {
        itemViewModel.getItemsByCategory(categoryId)
    }

    if (isAdding) {
        ItemAddDialog(itemViewModel, categoryId)
    }
    else if(isEditing){
        ItemEditDialog(itemViewModel, selectedItem!!)
    }

    LazyColumn(
        modifier = modifier
    ) {
        items(itemList, key = { item -> item.id }) { item ->
            val dismissState = rememberSwipeToDismissBoxState(
                confirmValueChange = {
                    if (it == SwipeToDismissBoxValue.StartToEnd) {
                        itemViewModel.deleteItem(item)
                        true
                    } else {
                        false
                    }
                }
            )
            SwipeToDismissBox(
                state = dismissState,
                backgroundContent = {}
            ) {
                ItemsItems(
                    itemViewModel,
                    itemModel = item,
                    onEditClicked = { selectedItem ->
                        itemViewModel.setSelectedItem(selectedItem)
                        itemViewModel.startEditingItem(selectedItem)
                    }
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemAddDialog(
    itemViewModel: ItemViewModel,
    categoryId: Long
) {
    val itemName by itemViewModel.nameOfItem.collectAsState()
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
            itemViewModel.changeAddingStatus()
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
                label = { Text("Item name") },
                value = itemName,
                onValueChange = {
                    itemViewModel.setItemName(it)
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
                        if(itemName.isNotBlank()){
                            itemViewModel.addItem(
                                ShopItem(
                                    id = 0L,
                                    name = itemName,
                                    category = categoryId
                                )
                            )
                            itemViewModel.changeAddingStatus()
                        }
                        else {
                            Toast.makeText(context, "Item name can't be empty", Toast.LENGTH_LONG).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(Color.Transparent)
                ) {
                    Text(
                        text = "Add new item",
                        color = colorResource(white)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemEditDialog(
    itemViewModel: ItemViewModel,
    itemModel: ShopItem
) {
    val itemName by itemViewModel.nameOfItem.collectAsState()
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
            itemViewModel.stopEditingItem()
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
                label = { Text("Item name") },
                value = itemName,
                onValueChange = {
                    itemViewModel.setItemName(it)
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
                        if(itemName.isNotBlank()){

                            itemViewModel.editItem(
                                itemModel.copy(name = itemName)
                            )

                        }
                        else {
                            Toast.makeText(context, "Item name can't be empty", Toast.LENGTH_LONG).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(Color.Transparent)
                ) {
                    Text(
                        text = "Test",
                        color = colorResource(white)
                    )
                }
            }
        }
    }
}


@Composable
fun ItemsItems(
    itemViewModel: ItemViewModel,
    itemModel: ShopItem,
    onEditClicked:(itemModel: ShopItem) -> Unit
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(8.dp)
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
                text = itemModel.name,
                color = colorResource(white)
            )
            IconButton(
                onClick = {
                    itemViewModel.setSelectedItem(itemModel)
                    onEditClicked(itemModel)
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