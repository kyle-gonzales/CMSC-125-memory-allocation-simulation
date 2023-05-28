package com.example.memoryallocation.ui.theme

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

}