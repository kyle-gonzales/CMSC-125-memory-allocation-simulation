package com.example.memoryallocation

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import java.text.DecimalFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EvaluationScreen(
    navController: NavHostController,
    throughput : HashMap<Int, Int>,
    fragmentation : HashMap<Int, Int>,
    timeInQueue: HashMap<Int, Int>,
    usage : ArrayList<MemBlock>
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
        val scrollState = rememberScrollState()
        Row(
            modifier = Modifier
                .padding(paddingValues = it)
                .padding(4.dp)
                .horizontalScroll(scrollState)
        ) {
            Card (
                modifier = Modifier
                    .width(150.dp)
                    .padding(4.dp)
            ) {
                Column (
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text("Waiting Time", fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(4.dp))

                    LazyColumn {
                        items(timeInQueue.keys.toList()) {job ->
                            val waitingTime = timeInQueue[job]
                            Text("Job $job = $waitingTime")
                        }
                    }
                    val average = timeInQueue.values.sum() / timeInQueue.size
                    val a = DecimalFormat("#.##").format(average).toDouble()
                    Spacer(Modifier.height(4.dp))
                    Text("Average: $a s", fontWeight = FontWeight.Bold)
                }
            }
            Card (
                modifier = Modifier
                    .width(170.dp)
                    .padding(4.dp)
            ) {
                Column (
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text("Fragmentation", fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(4.dp))

                    LazyColumn {
                        items(fragmentation.keys.toList()) {job ->
                            val internalFragmentation = fragmentation[job]
                            Text("Job $job = $internalFragmentation")
                        }
                    }
                    val average = fragmentation.values.sum() / fragmentation.size
                    val a = DecimalFormat("#.##").format(average).toDouble()
                    Spacer(Modifier.height(4.dp))
                    Text("Average: $a ", fontWeight = FontWeight.Bold)
                }
            }
            Card (
                modifier = Modifier
                    .width(200.dp)
                    .padding(4.dp)
            ) {
                Column (
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text("Usage", fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(4.dp))

                    LazyColumn {
                        items(usage) {partition ->
                            val name = partition.id
                            val size = partition.size
                            val timesUsed = partition.timesUsed
                            Text("Block $name ($size) = $timesUsed")
                        }
                    }
                }
            }

        }
    }
}