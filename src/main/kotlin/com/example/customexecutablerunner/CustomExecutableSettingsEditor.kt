package com.example.customexecutablerunner

import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.ui.ComboBox
import javax.swing.BoxLayout
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField

class CustomExecutableSettingsEditor :
    SettingsEditor<CustomExecutableRunConfiguration>() {

    private val executableCombo =
        ComboBox(ExecutableOption.entries.toTypedArray())

    private val argsField = JTextField()

    private val selectedExecutableLabel =
        JLabel().apply { isVisible = false }

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

    private lateinit var previousOption: ExecutableOption

    init {
        executableCombo.addActionListener {
            val selected = executableCombo.selectedItem as ExecutableOption

            if (selected == ExecutableOption.CUSTOM) {
                val file = FileChooser.chooseFile(descriptor, null, null)

                if (file != null) {
                    selectedExecutableLabel.text = "Selected executable: ${file.path}"
                    selectedExecutableLabel.isVisible = true
                } else {
                    executableCombo.selectedItem = previousOption
                    return@addActionListener
                }
            } else {
                selectedExecutableLabel.isVisible = false
            }

            previousOption = selected
            fireEditorStateChanged()
        }
    }

    override fun resetEditorFrom(config: CustomExecutableRunConfiguration) {
        executableCombo.selectedItem = config.executableOption
        argsField.text = config.arguments

        if (config.executableOption == ExecutableOption.CUSTOM &&
            config.executablePath.isNotBlank()
        ) {
            selectedExecutableLabel.text = "Selected executable: ${config.executablePath}"
            selectedExecutableLabel.isVisible = true
        } else {
            selectedExecutableLabel.isVisible = false
        }

        previousOption = config.executableOption
    }

    override fun applyEditorTo(config: CustomExecutableRunConfiguration) {
        val selected = executableCombo.selectedItem as ExecutableOption

        config.executableOption = selected
        config.arguments = argsField.text

        if (selected == ExecutableOption.CUSTOM) {
            config.executablePath =
                selectedExecutableLabel.text.removePrefix("Selected executable: ")
        } else {
            config.executablePath = ""
        }
    }

    override fun createEditor(): JPanel {
        return JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)

            add(JLabel("Executable:"))
            add(executableCombo)

            add(selectedExecutableLabel)

            add(JLabel("Arguments:"))
            add(argsField)
        }
    }
}