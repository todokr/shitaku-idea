package com.github.todokr.shitakuidea

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.ide.CopyPasteManager
import java.awt.datatransfer.StringSelection

class CopyPathAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val virtualFile = e.getRequiredData(CommonDataKeys.VIRTUAL_FILE)
        val project = e.getRequiredData(CommonDataKeys.PROJECT)

        val editor = e.getRequiredData(CommonDataKeys.EDITOR)

        val absolutePath = virtualFile.path
        val basePath = project.basePath ?: ""
        val pathFromRoot = absolutePath.replace("$basePath/", "")

        CopyPasteManager.getInstance().setContents(StringSelection(pathFromRoot))
    }
}