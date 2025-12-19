package com.example.customexecutablerunner

import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import javax.swing.BoxLayout
import javax.swing.JPanel
import javax.swing.JTextField

class CustomExecutableSettingsEditor :
    SettingsEditor<CustomExecutableRunConfiguration>() {

    private val executableField = TextFieldWithBrowseButton()
    private val argsField = JTextField()

    override fun resetEditorFrom(config: CustomExecutableRunConfiguration) {
        executableField.text = config.executablePath
        argsField.text = config.arguments
    }

    override fun applyEditorTo(config: CustomExecutableRunConfiguration) {
        config.executablePath = executableField.text
        config.arguments = argsField.text
    }

    override fun createEditor(): JPanel {
        return JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            add(executableField)
            add(argsField)
        }
    }
}