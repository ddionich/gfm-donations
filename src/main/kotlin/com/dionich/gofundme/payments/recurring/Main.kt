package com.dionich.gofundme.payments.recurring

import com.dionich.gofundme.payments.recurring.di.DI
import com.dionich.gofundme.payments.recurring.di.initializeDependencies
import com.dionich.gofundme.payments.recurring.utils.CommandProcessor
import java.io.File

fun main(args: Array<String>) {
    initializeDependencies()
    val commands = if (args.isNotEmpty()) {
        File(args[0]).readLines()
    } else {
        generateSequence(::readLine).toList()
    }
    DI.resolve(CommandProcessor::class.java).processCommands(commands)
}
