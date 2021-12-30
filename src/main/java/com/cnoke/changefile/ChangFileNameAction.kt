package com.cnoke.changefile

import com.cnoke.changefile.view.EditDialog
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class ChangFileNameAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) = EditDialog(e).show()
}