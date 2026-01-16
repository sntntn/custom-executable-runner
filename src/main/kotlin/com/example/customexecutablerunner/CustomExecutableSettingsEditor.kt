package com.example.customexecutablerunner

import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.TextBrowseFolderListener
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.RawCommandLineEditor
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField

class CustomExecutableSettingsEditor :
    SettingsEditor<CustomExecutableRunConfiguration>() {

    private val executableCombo =
        ComboBox(ExecutableOption.entries.toTypedArray())

    private val argsField = RawCommandLineEditor()

    val customExecutableDescriptor = FileChooserDescriptorFactory.createSingleFileDescriptor().apply {
        title = "Select Executable"
        description = "Choose an executable file"
    }

    private val customExecutableField = TextFieldWithBrowseButton().apply {
        isVisible = false

        addBrowseFolderListener(TextBrowseFolderListener(customExecutableDescriptor))
    }

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

    private var isResetting = false

    private lateinit var previousOption: ExecutableOption

    init {
        executableCombo.addActionListener {
            if (isResetting) return@addActionListener
            val selected = executableCombo.selectedItem as ExecutableOption

            if (selected == ExecutableOption.CUSTOM) {
                customExecutableField.isVisible = true

                val file = FileChooser.chooseFile(customExecutableDescriptor, null, null)

                if (file != null) {
                    customExecutableField.text = file.path
                } else {
                    isResetting = true
                    try {
                        executableCombo.selectedItem = previousOption
                        customExecutableField.isVisible = previousOption == ExecutableOption.CUSTOM
                    } finally {
                        isResetting = false
                    }
                }

            } else {
                customExecutableField.isVisible = false
            }

            previousOption = selected
            fireEditorStateChanged()
        }
    }

    override fun resetEditorFrom(config: CustomExecutableRunConfiguration) {
        isResetting = true
        try {
            executableCombo.selectedItem = config.executableOption
            argsField.text = config.arguments

            if (config.executableOption == ExecutableOption.CUSTOM &&
                config.executablePath.isNotBlank()
            ) {
                customExecutableField.text = config.executablePath
                customExecutableField.isVisible = true
            } else {
                customExecutableField.isVisible = false
            }
            previousOption = config.executableOption
        } finally {
            isResetting = false
        }
    }

    override fun applyEditorTo(config: CustomExecutableRunConfiguration) {
        val selected = executableCombo.selectedItem as ExecutableOption

        config.executableOption = selected
        config.arguments = argsField.text

        config.executablePath = if (selected == ExecutableOption.CUSTOM) {
            customExecutableField.text
        } else {
            ""
        }
    }

    override fun createEditor(): JPanel {
        return JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)

            add(leftAligned(JLabel("Executable:")))
            add(executableCombo)

            add(customExecutableField)

            add(Box.createVerticalStrut(8))

            add(leftAligned(JLabel("Arguments:")))
            add(argsField)
        }
    }

    private fun leftAligned(component: JComponent): JPanel =
        JPanel(java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0)).apply {
            isOpaque = false
            add(component)
        }
}