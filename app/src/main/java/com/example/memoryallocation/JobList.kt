package com.example.memoryallocation

import android.content.Context
import androidx.lifecycle.LifecycleObserver
import java.io.BufferedReader
import java.io.InputStreamReader

class JobList (context : Context)  {

    var jobList = ArrayList<Job>()

    init {
        val inputStream = context.resources.openRawResource(R.raw.job_list)
        val reader = BufferedReader(InputStreamReader(inputStream))
        var line : String?

        val blocks = ArrayList<String>()
        reader.readLine()

        while(reader.readLine().also { line = it } != null) {
            blocks.add(line!!)
        }

        reader.close()
        inputStream.close()

        for (block in blocks) {
            val elements = block.split(Regex("\\s+"))
            val id = elements[0].toInt()
            val time = elements[1].toInt()
            val size  = elements[2].toInt()

            jobList.add(Job(id, time, size))
        }
    }

}