package com.example.memoryallocation

import java.io.File

class JobList {

    lateinit var jobList : ArrayList<Job>

    init {
        val f = File("job_list.txt")
        val blocks = f.readLines()

        for (block in blocks) {
            val elements = block.split(" ")
            val id = elements[0].toInt()
            val time = elements[1].toInt()
            val size  = elements[2].toInt()

            jobList.add(Job(id, time, size))
        }
    }

}