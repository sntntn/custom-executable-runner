package com.example.customexecutablerunner

import com.intellij.execution.configurations.ConfigurationType
import com.intellij.execution.configurations.ConfigurationTypeBase
import com.intellij.icons.AllIcons

class CustomExecutableConfigurationType : ConfigurationTypeBase(
    ID,
    "Custom Executable",
    "Run a custom executable",
    AllIcons.Actions.Execute
) {

    init {
        addFactory(CustomExecutableConfigurationFactory(this))
    }

    companion object {
        const val ID = "CUSTOM_EXECUTABLE_RUN_CONFIGURATION"
    }
}