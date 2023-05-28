package com.example.memoryallocation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.memoryallocation.ui.theme.MemBlock
import com.example.memoryallocation.ui.theme.MemoryAllocationTheme

class MainActivity : ComponentActivity() {

    val memory by lazy {mutableStateOf(Memory(this))}
    val j by lazy {mutableStateOf(JobList(this))}

    var time by mutableStateOf(0)

    var throughput = HashMap<Int, Int>()
    var fragmentation = HashMap<Int, Int>()
    var timeInQueue = HashMap<Int, Int>()
    
    lateinit var navController: NavHostController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            navController = rememberNavController()
            MemoryAllocationTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {

                    NavHost(navController = navController, startDestination = "menu") {
                        composable("menu") {
                            MenuScreen(navController)
                        }
                        composable("simulation") {
                            SimulationScreen(navController, memory.value.memoryList, j.value.jobList, time)
                        }
                    }

                }
            }
        }
    }

    fun firstFitAlgorithm(isBestFit : Boolean = false, isWorstFit : Boolean = false) {

        memory.value = Memory(this)
        j.value = JobList(this)

        val memory = memory.value
        val j = j.value
        if (isBestFit) {
            memory.memoryList.sortBy { memBlock -> memBlock.size }
        } else if (isWorstFit) {
            memory.memoryList.sortByDescending { memBlock -> memBlock.size }
        }

        var partitions = memory.memoryList
        var jobQueue = j.jobList

        while (jobQueue.isNotEmpty()) {
            var job = jobQueue[0]
            var canFitInPartition = false

            for (partition in partitions) {
                if (job.size <= partition.size) {
                    canFitInPartition = true
                    if (partition.isFree()) {
                        println("job ${job.id} is allocated to partition ${partition.id}")

                        partition.job = job
                        partition.timesUsed += 1
                        partition.setFragmentation()

                        fragmentation[job.id] = partition.getFragmentation()
                        timeInQueue[job.id] = time

                        jobQueue.removeAt(0)
                        break
                    }
                }
            }
            if (canFitInPartition) {
                println("no partition currently available for job ${job.id}")

                time += 1
                throughput[time] = 0

                for (partition in partitions) {
                    if (partition.isFree())
                        continue

                    var allocatedJob = partition.job
                    allocatedJob!!.time -= 1

                    if (allocatedJob!!.time == 0) {
                        println("job ${allocatedJob.id} is being freed from partition ${partition.id} at time $time")
                        throughput[time] = throughput[time]!! + 1
                        partition.job = null
                        partition.setFragmentation()
                    } else {
                        println("job ${job.id} will never be allocated")
                        job.time -= 1
                        jobQueue.removeAt(0)
                    }
                }
            }
        }
        val remainingJobs = arrayListOf<MemBlock>()

        for (partition in partitions) {
            if (! partition.isFree()){
                remainingJobs.add(partition)
            }
        }

        while (remainingJobs.isNotEmpty()) {
            time += 1
            throughput[time] = 0

            for (partition in partitions) {

                var allocatedJob = partition.job
                allocatedJob!!.time -= 1

                if (allocatedJob!!.time == 0) {
                    println("job ${allocatedJob.id} is being freed from partition ${partition.id} at time $time")
                    throughput[time] = throughput[time]!! + 1
                    partition.job = null
                    partition.setFragmentation()
                    remainingJobs.remove(partition)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MemoryAllocationTheme {
        Greeting("Android")
    }
}