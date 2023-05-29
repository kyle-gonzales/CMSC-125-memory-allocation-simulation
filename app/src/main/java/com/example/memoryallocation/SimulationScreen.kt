package com.example.memoryallocation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimulationScreen(
    navController : NavHostController,
    partitions : List<MemBlock>,
    jobQueue : List<Job>,
    time : Int,
    isEvaluationEnabled : Boolean

) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp),
    ) {
        Row {
            LazyColumn (
                Modifier
                    .weight(.6f)
                    .fillMaxWidth()){
                items(partitions) { partition ->
                    Card (modifier = Modifier
                        .size(140.dp, 70.dp)
                        .padding(2.dp)) {
                        Column(
                            modifier = Modifier
                                .weight(.6f)
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (partition.job != null) {
                                Card(
                                    modifier = Modifier.fillMaxSize(),
                                    shape = CutCornerShape(0.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.onPrimary
                                    )
                                ) {
                                    val job = partition.job as Job
                                    Column (
                                        modifier = Modifier
                                            .padding(4.dp)
                                    ) {
                                        Row (
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text("Job${job.id}", fontSize = 12.sp)
                                            Text("${job.time}", color = MaterialTheme.colorScheme.onPrimaryContainer, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                        }
                                        Text("Size: ${job.size}", fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                        Row(
                            modifier = Modifier
                                .weight(.4f)
                                .fillMaxWidth()
                                .padding(4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Size: ${partition.size}", fontSize = 12.sp)
                            Text("Uses: ${partition.timesUsed}", fontSize = 12.sp)
                        }
                    }
                }
            }
            val nextJob : Job? = if (jobQueue.isNotEmpty()) {
                jobQueue[0]
            } else {
                null
            }
            //next job
            Column(
                modifier = Modifier
                    .weight(0.4f)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Row (
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ){
                    Text("Time:")
                    Text(time.toString(), fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
                Column {
                    Text("Next Job: ")
                    if (nextJob != null) {
                        Card (
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        ){
                            Column(
                                modifier = Modifier.padding(4.dp)
                            ) {
                                Text("ID: ${nextJob.id}")
                                Text("Size: ${nextJob.size}")
                            }
                        }
                    } else {
                        Text("No Jobs Left!")
                    }
                }
                Column {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            navController.navigate("evaluation")
                        },
                        enabled = isEvaluationEnabled
                    ) {
                        Text("Evaluate")
                    }
                }
            }
        }
    }
}