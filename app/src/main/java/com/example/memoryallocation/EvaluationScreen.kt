package com.example.memoryallocation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EvaluationScreen(
    navController: NavHostController,
    throughput : HashMap<Int, Int>,
    fragmentation : HashMap<Int, Int>,
    timeInQueue: HashMap<Int, Int>,
) {
    Scaffold(
        topBar = {
            SmallTopAppBar (
                modifier = Modifier,
                title = {  Text("Evaluation")  },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigate("menu") { popUpTo(0) }
                        }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "back arrow")
                    }
                }
            )
        }
    ) {
        Column(modifier = Modifier.padding(paddingValues = it)) {

        }
    }
}