package com.example.memoryallocation

import com.example.memoryallocation.ui.theme.MemBlock
import java.io.File

class Memory {

    lateinit var memoryList : ArrayList<MemBlock>

    init {
        val f = File("mem_list.txt")
        val blocks = f.readLines()

        for (block in blocks) {
            val elements = block.split(" ")
            val id = elements[0].toInt()
            val size = elements[1].toInt()
            memoryList.add(
                MemBlock(id, size)
            )
        }
    }
}