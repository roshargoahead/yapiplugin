package com.inheritech.it.action;

import com.inheritech.it.ui.MainUiClass;
import com.inheritech.it.util.PsiElementUtil;
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import java.util.Objects;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

/**
 * @desc: s
 * @author: ShuQiaoShuang
 * @date: 2021-04-30 16:36:58
 */
public class GenerateApiAction extends PsiElementBaseIntentionAction {
    @Override
    public void invoke(@NotNull Project project, Editor editor, @NotNull PsiElement psiElement) throws IncorrectOperationException {
        MainUiClass mainUiClass = new MainUiClass(project, editor, psiElement);
        if (mainUiClass.getShowUp()) {
            mainUiClass.pack();
            mainUiClass.setVisible(true);
        }
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, @NotNull PsiElement psiElement) {
        if (Objects.nonNull(PsiElementUtil.getMethodAnnotation(PsiTreeUtil.getParentOfType(psiElement, PsiMethod.class)))) {
            return true;
        }
        return false;
    }

    @Override
    public boolean startInWriteAction() {
        return false;
    }

    @Nls(capitalization = Nls.Capitalization.Sentence)
    @NotNull
    @Override
    public String getFamilyName() {
        return "Create api To YaPi";
    }

    @Nls(capitalization = Nls.Capitalization.Sentence)
    @NotNull
    @Override
    public String getText() {
        return "Create api To YaPi";
    }
}