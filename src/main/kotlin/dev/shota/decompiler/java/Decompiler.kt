package dev.shota.decompiler.java

import org.jetbrains.java.decompiler.main.Fernflower
import org.jetbrains.java.decompiler.main.extern.IBytecodeProvider
import org.jetbrains.java.decompiler.main.extern.IFernflowerLogger
import org.jetbrains.java.decompiler.main.extern.IFernflowerPreferences
import org.jetbrains.java.decompiler.main.extern.IResultSaver
import org.jetbrains.java.decompiler.struct.ContextUnit
import org.jetbrains.java.decompiler.struct.StructClass
import org.jetbrains.java.decompiler.struct.StructContext
import org.jetbrains.java.decompiler.struct.lazy.LazyLoader
import org.jetbrains.java.decompiler.util.DataInputFullStream
import java.io.File
import java.util.jar.Manifest

class Decompiler(private val data: ByteArray) : IBytecodeProvider, IResultSaver, IFernflowerLogger() {

    lateinit var code: String

    init {
        val fernflower = Fernflower(this, this, IFernflowerPreferences.DEFAULTS, this)
        val file = File("null.class")
        fernflower.addSource(file)

        val structContextField = fernflower.javaClass.getDeclaredField("structContext")
        structContextField.isAccessible = true
        val structContext = structContextField.get(fernflower) as StructContext

        val loaderField = structContext.javaClass.getDeclaredField("loader")
        loaderField.isAccessible = true
        val loader = loaderField.get(structContext) as LazyLoader
        loader.addClassLink(file.name, LazyLoader.Link(file.absolutePath, null))

        val structClass = StructClass.create(DataInputFullStream(data), true, loader)
        val contextUnit = ContextUnit(ContextUnit.TYPE_FOLDER, null, file.absolutePath, true, this, fernflower)
        contextUnit.addClass(structClass, file.name)

        fernflower.decompileContext()
    }

    override fun getBytecode(externalPath: String?, internalPath: String?): ByteArray {
        return data
    }

    override fun saveClassFile(path: String?, qualifiedName: String?, entryName: String?, content: String?, mapping: IntArray?) {
        code = content!!.trimEnd()
    }

    override fun saveFolder(path: String?) {}
    override fun copyFile(source: String?, path: String?, entryName: String?) {}
    override fun createArchive(path: String?, archiveName: String?, manifest: Manifest?) {}
    override fun saveDirEntry(path: String?, archiveName: String?, entryName: String?) {}
    override fun copyEntry(source: String?, path: String?, archiveName: String?, entry: String?) {}
    override fun saveClassEntry(path: String?, archiveName: String?, qualifiedName: String?, entryName: String?, content: String?) {}
    override fun closeArchive(path: String?, archiveName: String?) {}
    override fun writeMessage(message: String?, severity: Severity?) {}
    override fun writeMessage(message: String?, severity: Severity?, t: Throwable?) {}

}