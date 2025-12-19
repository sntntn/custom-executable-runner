package com.example.customexecutablerunner

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.project.Project


class CustomExecutableConfigurationFactory(
    type: ConfigurationType
) : ConfigurationFactory(type) {

    override fun createTemplateConfiguration(project: Project): RunConfiguration {
        return CustomExecutableRunConfiguration(project, this, "Custom Executable")
    }

    override fun getId(): String = "CustomExecutableFactory"
}