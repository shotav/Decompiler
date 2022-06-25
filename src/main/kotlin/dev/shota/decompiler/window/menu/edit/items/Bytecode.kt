package dev.shota.decompiler.window.menu.edit.items

import dev.shota.decompiler.java.Disassembler
import dev.shota.decompiler.window.container.Code
import dev.shota.decompiler.window.container.Container
import dev.shota.decompiler.window.menu.edit.Edit
import dev.shota.decompiler.window.menu.view.items.Language
import dev.shota.decompiler.window.sidebar.Type
import javafx.application.Platform
import java.awt.Toolkit
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyEvent
import javax.swing.JRadioButtonMenuItem
import javax.swing.KeyStroke

class Bytecode : JRadioButtonMenuItem(), ActionListener {

    init {
        Edit.group.add(this)
        isEnabled = false
        Container.selectionModel.selectedItemProperty().addListener { _, _, item ->
            if (item == null) {
                isEnabled = false
                Edit.group.clearSelection()
                return@addListener
            }

            val code = item as Code
            if (code.entry.type == Type.CLASS) {
                isEnabled = true
                isSelected = code.type == Code.CodeType.BYTECODE
            } else {
                isEnabled = false
                Edit.group.clearSelection()
            }
        }

        val translation = Language.get("edit.bytecode")
        text = translation.value
        translation.addListener { _, _, value -> text = value }
        mnemonic = KeyEvent.VK_B
        accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_B, Toolkit.getDefaultToolkit().menuShortcutKeyMaskEx)
        addActionListener(::actionPerformed)
    }

    override fun actionPerformed(e: ActionEvent?) {
        Platform.runLater {
            val code = Container.selectionModel.selectedItem as Code
            code.type = Code.CodeType.BYTECODE
            code.codeArea.clear()
            code.codeArea.replaceText(Disassembler(code.data).code)
        }
    }

}