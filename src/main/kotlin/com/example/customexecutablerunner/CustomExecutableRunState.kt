package com.example.customexecutablerunner

import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.filters.TextConsoleBuilderFactory
import com.intellij.execution.process.OSProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.ui.ConsoleViewContentType

class CustomExecutableRunState(
    environment: ExecutionEnvironment,
    private val config: CustomExecutableRunConfiguration
) : CommandLineState(environment) {

    override fun startProcess(): OSProcessHandler {
        val executablePath = ExecutableResolver.resolve(
            config.executableOption,
            config.executablePath
        )

        val cmd = GeneralCommandLine().apply {
            exePath = executablePath
            parametersList.addParametersString(config.arguments)
        }

        val handler = OSProcessHandler(cmd)
        val console = TextConsoleBuilderFactory.getInstance()
            .createBuilder(environment.project)
            .console

        console.attachToProcess(handler)
        console.print("Running: ${cmd.commandLineString}\n", ConsoleViewContentType.NORMAL_OUTPUT)

        return handler
    }
}