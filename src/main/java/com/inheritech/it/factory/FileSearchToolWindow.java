package com.inheritech.it.factory;

import com.inheritech.it.ui.SearchFile;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

public class FileSearchToolWindow implements ToolWindowFactory {
    private SearchFile searchFile;

    public FileSearchToolWindow() {
        initMainPanel();
    }

    private void initMainPanel() {
        searchFile = new SearchFile();
    }

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        searchFile.setProject(project);
        // 获取内容工厂的实例
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        // 获取 ToolWindow 显示的内容
        Content content = contentFactory.createContent(searchFile.getMainPanel(), "", false);
        // 设置 ToolWindow 显示的内容
        toolWindow.getContentManager().addContent(content);
    }
}
