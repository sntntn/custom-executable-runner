package com.example.customexecutablerunner

import com.intellij.execution.ExecutionException
import com.intellij.execution.Executor
import com.intellij.execution.configurations.*
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import org.jdom.Element


class CustomExecutableRunConfiguration(
    project: Project,
    factory: ConfigurationFactory,
    name: String
) : RunConfigurationBase<Any>(project, factory, name) {

    var executableOption: ExecutableOption = ExecutableOption.RUSTC
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

    override fun writeExternal(element: Element) {
        super.writeExternal(element)
        element.setAttribute("executableOption", executableOption.name)
        element.setAttribute("executablePath", executablePath)
        element.setAttribute("arguments", arguments)
    }

    override fun readExternal(element: Element) {
        super.readExternal(element)
        executableOption =
            element.getAttributeValue("executableOption")
                ?.let { ExecutableOption.valueOf(it) }
                ?: ExecutableOption.RUSTC

        executablePath =
            element.getAttributeValue("executablePath") ?: ""

        arguments =
            element.getAttributeValue("arguments") ?: ""
    }
}