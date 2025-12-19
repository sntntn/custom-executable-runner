package com.example.customexecutablerunner

import com.intellij.execution.ExecutionException
import com.intellij.execution.Executor
import com.intellij.execution.configurations.*
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project

class CustomExecutableRunConfiguration(
    project: Project,
    factory: ConfigurationFactory,
    name: String
) : RunConfigurationBase<Any>(project, factory, name) {

    var executablePath: String = ""
    var arguments: String = ""

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> {
        return CustomExecutableSettingsEditor()
    }

    @Throws(ExecutionException::class)
    override fun getState(
        executor: Executor,
        environment: ExecutionEnvironment
    ): RunProfileState {
        return CustomExecutableRunState(environment, this)
    }
}