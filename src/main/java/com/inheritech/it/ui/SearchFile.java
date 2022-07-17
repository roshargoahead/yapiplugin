package com.inheritech.it.ui;

import com.inheritech.it.service.ExcelParseService;
import com.inheritech.it.service.impl.ExcelParseServiceImpl;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.ScrollType;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileTypeDescriptor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.IdeFocusManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.ui.treeStructure.Tree;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SearchFile {
    private JPanel mainPanel;

    private Map<String, Row> row_map;

    private JScrollPane fileList;
    private JTextArea searchContent;
    private JTextField excelFilePath;
    private JButton chooseExcelButton;
    private JComboBox<String> excelRowData;
    private JButton nextExcelRow;
    private JTextField sheetIndex;
    private JTextField filePathPrefix;
    private JTextField fileContentPosition;
    private JTextField matchContentColumn;
    private JButton parseExcelButton;
    private JTextField fileColumn;
    private Project project;

    private DefaultTreeModel defaultTreeModel;

    private JTree jTree;

    public SearchFile() {
        initListener();
        mockData();
    }

    private void mockData() {
        excelRowData.addItem("service-order\\src\\main\\resources\\application.properties");
        excelRowData.addItem("eruake-server\\src\\main\\resources\\application.properties");
    }

    private void initListener() {
        chooseExcelButton.addActionListener(this::chooseFile);
        this.parseExcelButton.addActionListener(this::parseFileAction);
        this.excelRowData.addActionListener(e -> this.searchFile());
        nextExcelRow.addActionListener(this::findFile);
    }

    private void findFile(ActionEvent actionEvent) {
        int itemCount = excelRowData.getItemCount();
        if (excelRowData.getSelectedIndex() + 1 >= itemCount) {
            Messages.showInfoMessage("No more data", "Tips");
            return;
        }
        excelRowData.setSelectedIndex(excelRowData.getSelectedIndex() + 1);
        searchFile();
    }

    private void parseFileAction(ActionEvent actionEvent) {
        if (!validParam()) {
            return;
        }
        parseExcel();
    }

    private boolean validParam() {
        String text = this.fileColumn.getText();
        if (StringUtils.isBlank(this.sheetIndex.getText())) {
            Messages.showInfoMessage("填写扫描结果的sheet索引，从1开始", "Tips");
            return false;
        }
        if (StringUtils.isBlank(text)) {
            Messages.showInfoMessage("填写文件名在excel中的位置的列索引", "Tips");
            return false;
        }

        return true;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    private void chooseFile(ActionEvent actionEvent) {
        FileChooser.chooseFile(createDescriptor(),
                project, null, this::setFilePath);
    }

    private void setFilePath(VirtualFile virtualFile) {
        if (virtualFile == null) {
            return;
        }
        String path = virtualFile.getPath();
        excelFilePath.setText(path);
    }

    private void parseExcel() {
        if (StringUtils.isBlank(this.excelFilePath.getText())) {
            return;
        }
        ExcelParseService excelParseService = new ExcelParseServiceImpl();
        Workbook sheets = excelParseService.parseExcel(new File(this.excelFilePath.getText()));
        Sheet sheetAt = sheets.getSheetAt(this.parseInt(this.sheetIndex));
        int lastRowNum = sheetAt.getLastRowNum();
        row_map = IntStream.of(lastRowNum).mapToObj(sheetAt::getRow)
                .collect(Collectors.toMap(this::getFileName, Function.identity(), (row1, row2) -> row1));
        row_map.keySet().forEach(this.excelRowData::addItem);
        this.excelRowData.setSelectedIndex(0);
        searchFile();
    }

    private void searchFile() {
        if (this.project == null) {
            return;
        }
        Object selectedItem = this.excelRowData.getSelectedItem();
        if (selectedItem == null) {
            return;
        }
        /*Row row = row_map.get(selectedItem);
        if (row == null) {
            return;
        }
        String cellValue = this.getCellValue(row, this.parseInt(this.fileColumn));*/
        String cellValue = selectedItem.toString().replaceAll("[/\\\\]", Matcher.quoteReplacement(File.separator));
        String[] split = cellValue.split("[/\\\\]");
        PsiFile[] filesByName = FilenameIndex.getFilesByName(this.project, split[split.length - 1], GlobalSearchScope.everythingScope(project));
        if (filesByName.length == 0) {
            return;
        }
        List<VirtualFile> findFiles = Arrays.stream(filesByName)
                .map(PsiFile::getVirtualFile)
                .filter(file -> isContains(cellValue, file))
                .collect(Collectors.toList());
        if (findFiles.isEmpty()) {
            return;
        }
        addToScroll(findFiles);
    }

    private boolean isContains(String cellValue, VirtualFile file) {
        return file.getPath().replaceAll("[/\\\\]", Matcher.quoteReplacement(File.separator))
                .contains(cellValue.replace(this.filePathPrefix.getText(), ""));
    }

    private void addToScroll(List<VirtualFile> collect) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("找到以下文件");
        collect.stream().map(this::buildTree)
                .collect(Collectors.toList())
                .forEach(root::add);
        defaultTreeModel = new DefaultTreeModel(root);
        jTree = new Tree(defaultTreeModel);
        fileList.setViewportView(jTree);
        jTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                JTree source = (Tree) e.getSource();
                TreePath selectionPath = source.getSelectionPath();
                if (selectionPath == null) {
                    return;
                }
                Object lastPathComponent = selectionPath.getLastPathComponent();
                if (lastPathComponent == null) {
                    return;
                }
                if (!(lastPathComponent instanceof DefaultMutableTreeNode)) {
                    return;
                }
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) lastPathComponent;
                Object userObject = node.getUserObject();
                if (!(userObject instanceof VirtualFile)) {
                    return;
                }
                Editor myEditor = FileEditorManager.getInstance(project).openTextEditor(new OpenFileDescriptor(project, (VirtualFile) userObject), true);
                if (myEditor == null) {
                    return;
                }
                LogicalPosition position = new LogicalPosition(10, 2);
                myEditor.getCaretModel().removeSecondaryCarets();
                myEditor.getCaretModel().moveToLogicalPosition(position);
                myEditor.getScrollingModel().scrollToCaret(ScrollType.CENTER);
                myEditor.getSelectionModel().removeSelection();
                IdeFocusManager.getGlobalInstance().requestFocus(myEditor.getContentComponent(), true);
            }
        });
    }


    private DefaultMutableTreeNode buildTree(VirtualFile virtualFile) {
        return new DefaultMutableTreeNode(virtualFile);
    }

    private String getFileName(Row row) {
        String cellValue = this.getCellValue(row, this.parseInt(fileColumn));
        if (!StringUtils.isBlank(this.filePathPrefix.getText())) {
            return cellValue.replace(this.filePathPrefix.getText(), "");
        }
        return cellValue;
    }

    private String getCellValue(Row row, int column) {
        Cell cell = row.getCell(column);
        return cell.getStringCellValue();
    }

    private int parseInt(JTextField sheetIndex) {
        String text = sheetIndex.getText();
        try {

            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            Messages.showInfoMessage(String.format("%s 需要填写数字", sheetIndex.getName()), "Tips");
        }
        return 0;
    }

    @NotNull
    private FileChooserDescriptor createDescriptor() {
        return new FileTypeDescriptor("Choose Excel File", ".xls", ".xlsx");
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
