package com.example.memoryallocation

import com.example.memoryallocation.Job

class MemBlock (
    val id : Int,
    val size : Int,
    var job : Job? = null,
) {
    private var fragmentation : Int = getFragmentation()
    public var timesUsed = 0
    fun isFree() : Boolean {
        return job == null
    }

    fun setFragmentation() {
        if (job == null) {
            fragmentation = 0
            return
        }
        fragmentation = size - job!!.size
    }

    fun getFragmentation() : Int {
        setFragmentation()
        return fragmentation
    }

    fun copy(newJob : Job?, newTimesUsed : Int? = null): MemBlock {
        val c = MemBlock(id, size, newJob)
        newTimesUsed?.let { c.timesUsed = it }
        c.setFragmentation()
        return c
    }

}