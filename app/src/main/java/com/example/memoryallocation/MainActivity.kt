package com.example.memoryallocation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.memoryallocation.ui.theme.MemoryAllocationTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.DecimalFormat

class MainActivity : ComponentActivity() {

    var partitions = mutableStateListOf<MemBlock>()
    var jobQueue = mutableStateListOf<Job>()

    var isEvaluationEnabled by mutableStateOf(false)
//    val memory by lazy { mutableStateListOf(Memory(this).memoryList) }
//    val j by lazy { mutableStateListOf(JobList(this).jobList) }


    var time by mutableStateOf(0)

    var tp = HashMap<Int, Int>()
    var jobsCompleted by mutableStateOf(0)
    var fragmentation = HashMap<Int, Int>()
    var timeInQueue = HashMap<Int, Int>()
    var usage = ArrayList<MemBlock>()

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
                            SimulationScreen(navController, partitions, jobQueue, time, if (time == 0) 0.0 else DecimalFormat("#.##").format((jobsCompleted.toDouble() / time.toDouble())).toDouble(), isEvaluationEnabled)
                        }
                        composable("evaluation") {
                            EvaluationScreen(navController, tp, fragmentation, timeInQueue, usage)
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

    private suspend fun firstFitAlgorithm(isBestFit : Boolean = false, isWorstFit : Boolean = false) {

        initializeVariables()

        if (isBestFit) {
            partitions.sortBy { memBlock -> memBlock.size }
        } else if (isWorstFit) {
            partitions.sortByDescending { memBlock -> memBlock.size }
        }
        delay(1000)

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

                        fragmentation[job.id] = partitions[i].getFragmentation()
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
                    tp[time] = 0
                    delay(1000)

                    for (i in partitions.indices) {
                        val partition = partitions[i]
                        if (partition.isFree())
                            continue

                        var allocatedJob = partition.job!!
                        allocatedJob = updatePartition(partition, allocatedJob.copy(time = allocatedJob.time - 1)).job!!

                        if (allocatedJob.time == 0) {
                            println("job ${allocatedJob.id} is being freed from partition ${partition.id} at time $time")

                            tp[time] = tp[time]!! + 1
                            jobsCompleted ++

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
            tp[time] = 0
            delay(1000)
            for (i in partitions.indices) {
                if (partitions[i].isFree())
                    continue
                val allocatedJob = updatePartition(partitions[i], partitions[i].job!!.copy(time = partitions[i].job!!.time - 1)).job!!

                if (allocatedJob.time == 0) {
                    println("job ${allocatedJob.id} is being freed from partition ${partitions[i].id} at time $time")
                    tp[time] = tp[time]!! + 1
                    jobsCompleted ++

                    updatePartition(partitions[i], job = null)
                }
            }
        }
        isEvaluationEnabled = true
        usage.addAll(partitions.sortedBy { partition -> partition.timesUsed })
    }

    private fun initializeVariables() {
        partitions.clear()
        partitions.addAll(Memory(this).memoryList)
        jobQueue.clear()
        jobQueue.addAll(JobList(this).jobList)

        time = 0
        jobsCompleted = 0
        isEvaluationEnabled = false
        usage.clear()
    }
}
