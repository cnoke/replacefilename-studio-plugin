package com.cnoke.changefile.view

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.openapi.vfs.VirtualFile
import java.awt.event.KeyEvent
import java.util.*
import javax.swing.*
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener


class EditDialog(private val event: AnActionEvent) : DialogWrapper(event.project) {

    @JvmField
    var contentPane: JPanel? = null
    @JvmField
    var search: JTextField? = null
    @JvmField
    var replace: JTextField? = null
    @JvmField
    var mapperList: JList<String>? = null
    @JvmField
    var scrollPane: JScrollPane? = null
    private val externalFiles: MutableList<VirtualFile> = ArrayList()
    private val showTexts : MutableList<String> = ArrayList()
    private val projectFile: VirtualFile? = CommonDataKeys.VIRTUAL_FILE.getData(event.dataContext)

    init {
        init()
        initUI()
    }

    private fun initUI() {
        event.project?.projectFile
        contentPane!!.registerKeyboardAction(
            { dispose() },
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
            JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
        )

        search!!.document.addDocumentListener(object : DocumentListener{

            override fun changedUpdate(e: DocumentEvent?) {

            }

            override fun insertUpdate(e: DocumentEvent?) {
                initFile(search!!.text)
            }

            override fun removeUpdate(e: DocumentEvent?) {
                initFile(search!!.text)
            }

        })
    }

    private fun initFile(searchText : String){
        if (projectFile != null && !searchText.isNullOrEmpty()) {
            externalFiles.clear()
            showTexts.clear()
            findFile(searchText)
            mapperList!!.setListData(showTexts.toTypedArray())
        }else{
            externalFiles.clear()
            showTexts.clear()
            mapperList!!.setListData(showTexts.toTypedArray())
        }
    }

    private fun findFile(searchText : String){
        projectFile?.let {
            VfsUtilCore.iterateChildrenRecursively(it, { filter->
                !filterFolder(filter)
            }, { content->
                if(content.name.contains(searchText)){
                    externalFiles.add(content)
                    showTexts.add( "${content.path} → ${content.name}")
                }
                true
            })
        }
    }

    private fun filterFolder(virtualFile: VirtualFile) : Boolean{
        return virtualFile.path.contains(".git")
    }

    override fun doOKAction() {
        if(search!!.text.isNullOrEmpty() || replace!!.text.isNullOrEmpty()){
            Messages.showMessageDialog("请输入搜索值和替换值","",null)
            return
        }
        if (externalFiles.isNotEmpty()) {
            for (externalFile in externalFiles) {
                kotlin.runCatching {

                    val name = externalFile.name
                    externalFile.rename(this, name.replace(search!!.text,replace!!.text))
                }
            }
        }
        Messages.showInfoMessage("替换成功!", "")
        dispose()
    }

    override fun getPreferredFocusedComponent(): JComponent? {
        return search
    }

    override fun createCenterPanel(): JComponent? {
        title = "工具"
        setOKButtonText("替换")
        setCancelButtonText("取消")
        scrollPane!!.viewport.view = mapperList
        return contentPane
    }

}