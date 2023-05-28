package com.example.memoryallocation

import android.content.Context
import androidx.lifecycle.LifecycleObserver
import com.example.memoryallocation.ui.theme.MemBlock
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

class Memory (context : Context)  {

    var memoryList = ArrayList<MemBlock>()

    init {

        val inputStream = context.resources.openRawResource(R.raw.mem_list)
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
            val size = elements[1].toInt()
            memoryList.add(
                MemBlock(id, size)
            )
        }

    }
}