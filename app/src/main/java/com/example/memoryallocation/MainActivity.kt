package com.example.memoryallocation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.memoryallocation.ui.theme.MemoryAllocationTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    var partitions = mutableStateListOf<MemBlock>()
    var jobQueue = mutableStateListOf<Job>()
//    val memory by lazy { mutableStateListOf(Memory(this).memoryList) }
//    val j by lazy { mutableStateListOf(JobList(this).jobList) }


    var time by mutableStateOf(0)

    var throughput = HashMap<Int, Int>()
    var fragmentation = HashMap<Int, Int>()
    var timeInQueue = HashMap<Int, Int>()
    
    lateinit var navController: NavHostController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val m = Memory(this)
        val j = JobList(this)

        partitions.addAll(m.memoryList)
        jobQueue.addAll(j.jobList)

        setContent {
            navController = rememberNavController()
            MemoryAllocationTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {

                    NavHost(navController = navController, startDestination = "menu") {
                        composable("menu") {
                            MenuScreen(
                                navController,
                                onFirst = {
                                    lifecycleScope.launch {
                                        firstFitAlgorithm()
                                    }
                                },
                                onBest = {
                                    lifecycleScope.launch {
                                        firstFitAlgorithm(isBestFit = true)
                                    }
                                },
                                onWorst = {
                                    lifecycleScope.launch {
                                        firstFitAlgorithm(isWorstFit = true)
                                    }
                                }
                            )
                        }
                        composable("simulation") {
                            SimulationScreen(navController, partitions, jobQueue, time)
                        }
                    }
                }
            }
        }
    }

    private fun updatePartition(
        partition : MemBlock,
        job : Job? = partition.job,
        timesUsed : Int = partition.timesUsed,
    ) : MemBlock{
        val index = partitions.indexOf(partition)
        val v = partitions[index]
        partitions[index] = v.copy(job, timesUsed)
        return partitions[index]
    }

    private fun updateJob(
        job : Job,
        time : Int
    ) : Job{
        val index = jobQueue.indexOf(job)
        jobQueue[index] = jobQueue[index].copy(time = time)
        return jobQueue[index]
    }

    private suspend fun firstFitAlgorithm(isBestFit : Boolean = false, isWorstFit : Boolean = false) {

        partitions.clear()
        partitions.addAll(Memory(this).memoryList)
        jobQueue.clear()
        jobQueue.addAll(JobList(this).jobList)

        time = 0

        if (isBestFit) {
            partitions.sortBy { memBlock -> memBlock.size }
        } else if (isWorstFit) {
            partitions.sortByDescending { memBlock -> memBlock.size }
        }

        while (jobQueue.isNotEmpty()) {
            var job = jobQueue[0]
            var canFitInPartition = false
            var found = false


            for (i in partitions.indices) {
                val partition = partitions[i]
                if (job.size <= partition.size) {
                    canFitInPartition = true
                    if (partition.isFree()) {
                        println("job ${job.id} is allocated to partition ${partition.id}")

                        updatePartition(partition, job, partition.timesUsed + 1)
//                        partition.job = job
//                        partition.timesUsed += 1
//                        partition.setFragmentation()

                        fragmentation[job.id] = partition.getFragmentation()
                        timeInQueue[job.id] = time

                        jobQueue.removeAt(0)
                        found = true
                        break
                    }
                }
            }
            if (!found) {
                if (canFitInPartition) {
                    println("no partition currently available for job ${job.id}")

                    time += 1
                    throughput[time] = 0
                    delay(1000)

                    for (i in partitions.indices) {
                        val partition = partitions[i]
                        if (partition.isFree())
                            continue

                        var allocatedJob = partition.job!!
                        allocatedJob = updatePartition(partition, allocatedJob.copy(time = allocatedJob.time - 1)).job!!

                        if (allocatedJob.time == 0) {
                            println("job ${allocatedJob.id} is being freed from partition ${partition.id} at time $time")

                            throughput[time] = throughput[time]!! + 1

                            updatePartition(partitions[i], job = null)
                        }
                    }
                } else {
                    println("job ${job.id} will never be allocated")

                    job.time = -1
                    jobQueue.removeAt(0)
                }
            }
        }

        while (partitions.any{ ! it.isFree() }) {

            time++
            throughput[time] = 0
            delay(1000)
            for (i in partitions.indices) {
                if (partitions[i].isFree())
                    continue
                val allocatedJob = updatePartition(partitions[i], partitions[i].job!!.copy(time = partitions[i].job!!.time - 1)).job!!

                if (allocatedJob.time == 0) {
                    println("job ${allocatedJob.id} is being freed from partition ${partitions[i].id} at time $time")
                    throughput[time] = throughput[time]!! + 1

                    updatePartition(partitions[i], job = null)

                }

            }
        }
    }
}
