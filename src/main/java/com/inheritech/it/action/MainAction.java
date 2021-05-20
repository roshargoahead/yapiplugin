package com.inheritech.it.action;

import com.inheritech.it.ui.MainUiClass;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiElement;
import java.util.Objects;

public class MainAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        if (Objects.isNull(project)) {
            Messages.showInfoMessage("empty project", "Tips");
            return;
        }
        // TODO: insert action logic here
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        PsiElement psiElement = e.getData(PlatformDataKeys.PSI_ELEMENT);
        if (Objects.isNull(psiElement)) {
            return;
        }
        MainUiClass mainUiClass = new MainUiClass(e.getProject(), editor, psiElement);
        if (mainUiClass.getShowUp()) {
            mainUiClass.pack();
            mainUiClass.setVisible(true);
        }
    }
}
