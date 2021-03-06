package dev.shota.decompiler.window.menu.edit

import dev.shota.decompiler.window.menu.Menu
import dev.shota.decompiler.window.menu.edit.items.*
import java.awt.event.KeyEvent
import javax.swing.ButtonGroup
import javax.swing.JSeparator

class Edit : Menu("edit", KeyEvent.VK_E) {

    companion object {
        val group = ButtonGroup()
    }

    init {
        add(Copy())
        add(SelectAll())
        add(Find())
        add(JSeparator())
        add(Java())
        add(Bytecode())
    }

}