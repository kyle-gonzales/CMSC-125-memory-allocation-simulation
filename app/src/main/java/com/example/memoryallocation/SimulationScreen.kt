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
import com.example.memoryallocation.ui.theme.MemBlock

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimulationScreen(
    navController : NavHostController,
    partitions : ArrayList<MemBlock>,
    jobQueue : ArrayList<Job>,
    time : Int
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
                        .size(200.dp, 100.dp)
                        .padding(2.dp)) {
                        Column(
                            modifier = Modifier
                                .weight(.7f)
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (partition.job != null) {
                                Card(
                                    modifier = Modifier.fillMaxSize(),
                                    shape = CutCornerShape(0.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.primary
                                    )
                                ) {
                                    val job = partition.job as Job
                                    Text("Job${job.id}")
                                    Text("Time Left: ${job.time}")
                                    Text("Size: ${job.size}")
                                }
                            }
                        }
                        Row(
                            modifier = Modifier
                                .weight(.3f)
                                .fillMaxWidth()
                                .padding(4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Size: ${partition.size}", fontSize = 12.sp)
                            Text("Times Used: ${partition.timesUsed}", fontSize = 12.sp)
                        }
                    }
                }
            }
            val nextJob = jobQueue[0]
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
                    Card (
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ){
                        Column(
                            modifier = Modifier.padding(4.dp)
                        ) {
                            Text("ID: ${nextJob.id}")
                            Text("Size: ${nextJob.size}")
                        }
                    }
                }
                Column {
                    Button(modifier = Modifier.fillMaxWidth(), onClick = {  }) {
                        Text("Tick")
                    }
                }
            }
        }
    }
}