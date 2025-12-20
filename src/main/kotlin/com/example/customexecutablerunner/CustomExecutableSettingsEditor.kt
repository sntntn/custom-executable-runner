package com.example.customexecutablerunner

import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.options.ConfigurationException
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import javax.swing.BoxLayout
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField

class CustomExecutableSettingsEditor :
    SettingsEditor<CustomExecutableRunConfiguration>() {

    private val executableCombo =
        ComboBox(ExecutableOption.entries.toTypedArray())

    private val executableField = TextFieldWithBrowseButton()
    private val argsField = JTextField()

    private val descriptor = FileChooserDescriptor(
        true,
        false,
        false,
        false,
        false,
        false
    ).apply {
        title = "Select Executable"
        description = "Choose an executable file"
    }

    init {
        executableCombo.addActionListener {
            updateCustomExecutableState()
        }

        executableField.addActionListener {
            val file = FileChooser.chooseFile(
                descriptor,
                null,
                null
            )
            if (file != null) {
                executableField.text = file.path
            }
        }

        executableField.textField.document.addDocumentListener(object : javax.swing.event.DocumentListener {
            override fun insertUpdate(e: javax.swing.event.DocumentEvent) = fireEditorStateChanged()
            override fun removeUpdate(e: javax.swing.event.DocumentEvent) = fireEditorStateChanged()
            override fun changedUpdate(e: javax.swing.event.DocumentEvent) = fireEditorStateChanged()
        })
    }

    override fun resetEditorFrom(config: CustomExecutableRunConfiguration) {
        executableCombo.selectedItem = config.executableOption
        executableField.text = config.executablePath
        argsField.text = config.arguments

        executableField.isEnabled =
            config.executableOption == ExecutableOption.CUSTOM
    }

    override fun applyEditorTo(config: CustomExecutableRunConfiguration) {
        val selectedOption = executableCombo.selectedItem as ExecutableOption
        if (selectedOption == ExecutableOption.CUSTOM && executableField.text.isBlank()) {
            throw ConfigurationException("Please specify the executable path for Custom option")
        }

        config.executableOption = selectedOption
        config.executablePath = executableField.text
        config.arguments = argsField.text
    }

    override fun createEditor(): JPanel {
        return JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)

            add(JLabel("Executable:"))
            add(executableCombo)

            add(JLabel(" Custom executable path - if 'Custom executable' is selected"))
            add(executableField)

            add(JLabel("Arguments:"))
            add(argsField)
        }
    }

    private fun updateCustomExecutableState() {
        val selected = executableCombo.selectedItem as ExecutableOption
        executableField.isEnabled = selected == ExecutableOption.CUSTOM
        fireEditorStateChanged()
    }

}