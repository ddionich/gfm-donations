package com.dionich.gofundme.payments.recurring

import com.dionich.gofundme.payments.recurring.utils.processCommands
import java.io.File

fun main(args: Array<String>) {
    val commands = if (args.isNotEmpty()) {
        File(args[0]).readLines()
    } else {
        generateSequence(::readLine).toList()
    }
    processCommands(commands)
}
