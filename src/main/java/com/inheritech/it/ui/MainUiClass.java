package com.inheritech.it.ui;

import com.inheritech.it.constants.AnnotationConstants;
import com.inheritech.it.constants.JavaBasicType;
import com.inheritech.it.entity.UserVO;
import com.inheritech.it.factory.ParseStringFactory;
import com.inheritech.it.util.HttpUtil;
import com.inheritech.it.util.PsiElementUtil;
import com.intellij.lang.jvm.annotation.JvmAnnotationArrayValue;
import com.intellij.lang.jvm.annotation.JvmAnnotationAttribute;
import com.intellij.lang.jvm.annotation.JvmAnnotationAttributeValue;
import com.intellij.lang.jvm.annotation.JvmAnnotationConstantValue;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiParameterList;
import com.intellij.psi.PsiType;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.javadoc.PsiDocTag;
import com.intellij.psi.javadoc.PsiDocTagValue;
import com.intellij.psi.util.PsiTreeUtil;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

public class MainUiClass extends JDialog {
    private JPanel contentPane;

    private JButton buttonOK;

    private JButton buttonCancel;

    private JButton addCategoryButton;

    private JComboBox<String> categoryComboBox;

    private JButton addApiButton;

    private JTextField apiName;

    private JComboBox<String> apiStatus;

    private JTextField apiPath;

    private JTextField methodField;

    private JButton bodyButton;

    private JButton headersButton;

    private JButton queryButton;

    private JPanel queryButtonPanel;

    private JButton addQueryParamButton;

    private JPanel queryPanel;

    private JPanel queryFieldPanel;

    private JPanel paramPanel;

    private JPanel okPanel;

    private JPanel commonPanel;

    private JPanel apiPanel;

    private JPanel pathParamPanel;

    private JPanel pathPanel;

    private JPanel bodyPanel;

    private JPanel bodyParamPanel;

    private final Map<String, Object> categoryMap = new HashMap<>();

    private boolean showUp;

    private final Map<String, Map<String, JComponent>> queryParamFieldMap = new ConcurrentHashMap<>();

    private final Map<String, Map<String, JComponent>> pathParamFieldMap = new ConcurrentHashMap<>();

    private AtomicInteger atomicInteger = new AtomicInteger(0);

    public MainUiClass(Project project, Editor editor, PsiElement psiElement) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        // 居中
        setLocationRelativeTo(null);
        // 设置下拉框可输入
        categoryComboBox.setEditable(true);

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        // 添加下拉框输入框模糊匹配的监听器
        this.setSearchable();
        // 获取全部分类
        this.getAllCategory();
        categoryComboBox.setSelectedIndex(-1);
        // 添加接口方法
//        addApiButton.addActionListener(e -> addApi(project, editor, psiElement));
        addApi(project, editor, psiElement);
        // 添加分类
        addCategoryButton.addActionListener(e -> addCategory());
        // 默认不显示分类下拉
        categoryComboBox.hidePopup();
        // query参数设置
        queryButton.addActionListener(this::showQueryParam);
        // body参数
        bodyButton.addActionListener(this::showBodyParam);
        //header参数
        headersButton.addActionListener(this::showHeaders);
        this.getQueryParam(psiElement);
        // 添加query参数按钮监听
        addQueryParamButton.addActionListener(e -> {
                    this.addParamPanel(queryFieldPanel, this.getEmptyPanel(), new GridLayout(queryParamFieldMap.size(), 1));
                }
        );
        apiStatus.addItem("未完成");
        apiStatus.addItem("已完成");
        apiStatus.setSelectedIndex(0);
        this.setSize();
        contentPane.updateUI();
    }

    private void getQueryParam(PsiElement psiElement) {
        PsiMethod psiMethod = PsiTreeUtil.getParentOfType(psiElement, PsiMethod.class);
        List<Map<String, Object>> list = PsiElementUtil.getMethodParamAnnotation(psiMethod);
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        for (Map<String, Object> map : list) {
            this.addParamPanel(queryFieldPanel, getPanel(map, psiMethod), new GridLayout(queryParamFieldMap.size(), 1));
        }
    }

    private void showHeaders(ActionEvent actionEvent) {
        // 获取方法的参数注解
        queryPanel.setVisible(false);
        bodyPanel.setVisible(false);
        contentPane.updateUI();
    }

    private JPanel getEmptyPanel() {
        // 创建布局
        JPanel jPanel = new JPanel(new GridLayout(1, 5));

        // 创建输入框
        JTextField nameTextField = new JTextField();
        nameTextField.setText("参数名称");
        this.setFont(nameTextField);
        // 添加placeholder处理
        nameTextField.addFocusListener(getListener("参数名称"));
        // 添加字体输入监听
        nameTextField.addKeyListener(this.getKeyListener());

        // 是否必选下拉框
        JComboBox<String> requiredComboBox = new ComboBox<>();
        requiredComboBox.addItem("必须");
        requiredComboBox.addItem("非必须");

        // 示例输入框
        JTextField exampleTextField = new JTextField();
        exampleTextField.setText("参数示例");
        this.setFont(exampleTextField);
        // 添加placeholder处理
        exampleTextField.addFocusListener(getListener("参数示例"));
        // 添加字体输入监听
        exampleTextField.addKeyListener(this.getKeyListener());

        // 备注输入框
        JTextField descTextField = new JTextField();
        descTextField.setText("备注");
        this.setFont(descTextField);
        descTextField.addFocusListener(getListener("备注"));
        // 添加placeholder处理
        descTextField.addFocusListener(getListener("备注"));
        // 添加字体输入监听
        descTextField.addKeyListener(this.getKeyListener());

        // 创建删除按钮
        JButton jButton = new JButton("del", new ImageIcon("com/inheritech/it/images/deleteIcon.png"));
        jButton.setName(String.valueOf(atomicInteger.incrementAndGet()));
        jButton.addActionListener(this::deleteQueryParam);
        addComponent(jPanel, nameTextField, requiredComboBox, exampleTextField, descTextField, jButton);
        return jPanel;

    }

    @NotNull
    private KeyAdapter getKeyListener() {
        return new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                JTextField source = (JTextField) e.getSource();
                source.setFont(new Font("宋体", Font.PLAIN, source.getFont().getSize()));
            }
        };
    }

    private void setFont(JTextField nameTextField) {
        Font font = new Font("宋体", Font.ITALIC, nameTextField.getFont().getSize());
        nameTextField.setFont(font);
    }

    @NotNull
    private FocusAdapter getListener(String param) {
        return new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                JTextField source = (JTextField) e.getSource();
                String name = source.getFont().getName();
                boolean italic = source.getFont().isItalic();
                if (param.equals(source.getText()) && "宋体".equals(name) && italic) {
                    source.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                JTextField source = (JTextField) e.getSource();
                if (StringUtils.isEmpty(source.getText())) {
                    setFont(source);
                    source.setText(param);
                }
            }
        };
    }

    private static final String PANEL = "panel";

    private static final String NAME = "nameTextField";

    private static final String REQUIRED = "requiredComboBox";

    private static final String EXAMPLE = "exampleTextField";

    private static final String DESC = "descTextField";

    private void addComponent(JPanel jPanel, JTextField nameTextField, JComboBox<String> requiredComboBox, JTextField exampleTextField, JTextField descTextField, JButton jButton) {
        // 添加组件
        jPanel.add(nameTextField);
        jPanel.add(requiredComboBox);
        jPanel.add(exampleTextField);
        jPanel.add(descTextField);
        jPanel.add(jButton);
        jPanel.setVisible(true);

        HashMap<String, JComponent> componetMap = new HashMap<>();
        componetMap.put(PANEL, jPanel);
        componetMap.put(NAME, nameTextField);
        componetMap.put(REQUIRED, requiredComboBox);
        componetMap.put(EXAMPLE, exampleTextField);
        componetMap.put(DESC, descTextField);
        queryParamFieldMap.put(jButton.getName(), componetMap);
    }

    private void setSearchable() {
        JTextField editorComponent = (JTextField) categoryComboBox.getEditor().getEditorComponent();
        editorComponent.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                char ch = e.getKeyChar();
                if (ch == KeyEvent.CHAR_UNDEFINED || (Character.isISOControl(ch) && (((int) ch) != 8)) || ch == KeyEvent.VK_DELETE) {
                    return;
                }
                JTextField component = (JTextField) e.getComponent();
                String str = component.getText();
                System.out.println(str);
                categoryComboBox.removeAllItems();
                categoryComboBox.setSelectedItem(str);
                if (str.length() == 0) {
                    for (String s : categoryMap.keySet()) {
                        categoryComboBox.addItem(s);
                    }
                    if (categoryComboBox.isPopupVisible()) {
                        categoryComboBox.hidePopup();
                    }
                    return;
                }
                Set<String> strings = categoryMap.keySet();
                for (String categoryName : strings) {
                    if (categoryName.contains(str)) {
                        categoryComboBox.addItem(categoryName);
                    }
                }
                if (!categoryComboBox.isPopupVisible()) {
                    categoryComboBox.showPopup();
                }
            }
        });
    }

    private void showBodyParam(ActionEvent actionEvent) {
        queryPanel.setVisible(false);
        bodyPanel.setVisible(true);
        contentPane.updateUI();
    }

    private void showQueryParam(ActionEvent actionEvent) {
        // 获取方法的参数注解
        queryPanel.setVisible(true);
        bodyPanel.setVisible(false);
        contentPane.updateUI();
    }

    private JPanel getPanel(Map<String, Object> map, PsiMethod psiMethod) {
        // 创建布局
        JPanel jPanel = new JPanel(new GridLayout(1, 5));
        // 创建输入框
        JTextField nameTextField = new JTextField();
        String name = map.get("name").toString();
        nameTextField.setText(name);
        // 是否必选下拉框
        JComboBox<String> requiredComboBox = new ComboBox<>();
        requiredComboBox.addItem("必须");
        requiredComboBox.addItem("非必须");
        // 示例输入框
        JTextField exampleTextField = new JTextField();
        // 备注输入框
        JTextField descTextField = new JTextField();
        String paramComment = this.getDocComment(psiMethod, name);
        descTextField.setText(paramComment);
        // 创建删除按钮
        JButton jButton = new JButton("del", new ImageIcon("com/inheritech/it/images/deleteIcon.png"));
        jButton.setName(String.valueOf(atomicInteger.incrementAndGet()));
        jButton.addActionListener(this::deleteQueryParam);
        // 添加组件
        addComponent(jPanel, nameTextField, requiredComboBox, exampleTextField, descTextField, jButton);
        return jPanel;
    }

    private void addParamPanel(JPanel sourcePanel, JPanel jPanel, LayoutManager layoutManager) {
        sourcePanel.setLayout(layoutManager);
        sourcePanel.add(jPanel);
        sourcePanel.updateUI();
        // 更新父panel的大小
        this.setSize();
    }

    private String getDocComment(PsiMethod psiMethod, String name) {
        PsiDocComment docComment = psiMethod.getDocComment();
        if (Objects.nonNull(docComment)) {
            for (PsiDocTag tag : docComment.getTags()) {
                String tagName = tag.getNameElement().getText();
                PsiDocTagValue valueElement = tag.getValueElement();
                if (valueElement != null) {
                    PsiElement context = valueElement.getContext();
                    if (Objects.nonNull(context)) {
                        String contextName = context.getText();
                        if (StringUtils.isNotEmpty(contextName) && contextName.contains(name)) {
                            return contextName.replace(tagName, "").replace(valueElement.getText(), "").replace("\n", "").replace("*",
                                    "").replaceAll(" ", "");
                        }
                    }

                }
            }
        }
        return "";
    }

    private void setSize() {
        Dimension commonSize = commonPanel.getSize();
        Dimension paramSize = queryPanel.getSize();
        Dimension okSize = okPanel.getSize();
        Dimension pathSize = pathPanel.getSize();
        this.setSize(750,
                commonSize.height + paramSize.height + okSize.height + pathSize.height + 200);
    }

    private void deleteQueryParam(ActionEvent actionEvent) {

        JButton jButton = (JButton) actionEvent.getSource();
        String name = jButton.getName();
        queryParamFieldMap.remove(name);
        queryFieldPanel.removeAll();
        queryFieldPanel.updateUI();
        for (String key : queryParamFieldMap.keySet()) {
            queryFieldPanel.add(queryParamFieldMap.get(key).get("panel"));
        }
    }


    private void addApi(Project project, Editor editor, @NotNull PsiElement psiElement) {
        // 获取当前方法
        PsiMethod psiMethod = PsiTreeUtil.getParentOfType(psiElement, PsiMethod.class);
        PsiClass psiClass = PsiTreeUtil.getParentOfType(psiMethod, PsiClass.class);
        if (Objects.isNull(psiMethod) || Objects.isNull(psiClass)) {
            Messages.showInfoMessage("failed to get method info", "Tips");
            return;
        }
        this.setApiName(psiMethod);
        String path = "";
        // 找注解
        PsiAnnotation classAnnotation = PsiElementUtil.getClassAnnotation(psiClass);
        if (Objects.nonNull(classAnnotation)) {
            path += getPath(classAnnotation);
        }
        PsiAnnotation methodAnnotation = PsiElementUtil.getMethodAnnotation(psiMethod);
        if (Objects.nonNull(methodAnnotation)) {
            String annotationName = methodAnnotation.getQualifiedName();
            if (StringUtils.isEmpty(annotationName)) {
                Messages.showInfoMessage("get annotation name failed", "Tips");
                return;
            }
            String substring = annotationName.substring(annotationName.lastIndexOf(".") + 1);
            methodField.setText(substring.replace("Mapping", "").toUpperCase());
            path += getPath(methodAnnotation);
            if (StringUtils.isEmpty(path)) {
                Messages.showInfoMessage("cannot get method path", "Tips");
            }
        }
        apiPath.setText(path);
        this.addPathParam(path, psiMethod);
        this.setPathPanel();
        contentPane.updateUI();
    }

    private void setPathPanel() {
        if (pathParamFieldMap.size() > 0) {
            pathPanel.setVisible(true);
            pathParamPanel.setLayout(new GridLayout(pathParamFieldMap.size(), 1));
            for (String key : pathParamFieldMap.keySet()) {
                pathParamPanel.add(pathParamFieldMap.get(key).get(PANEL));
            }
        } else {
            pathPanel.setVisible(false);
        }
    }

    private void addPathParam(String path, PsiMethod psiMethod) {
        String[] split = path.split("/");
        List<String> list = new ArrayList<>();
        for (String s : split) {
            if (s.startsWith("{") && s.endsWith("}") && s.length() > 2) {
                String pathParam = s.replace("{", "").replace("}", "");
                list.add(pathParam);
            }
        }
        PsiParameterList parameterList = psiMethod.getParameterList();

        for (String paramName : list) {
            JPanel jPanel = new JPanel(new GridLayout(1, 3));

            // 参数输入框
            JTextField paramField = new JTextField();
            paramField.setText(paramName);
            paramField.setEnabled(false);

            // 示例输入框
            JTextField exampleField = new JTextField();

            //备注输入框
            JTextField descField = new JTextField();
            this.getPathParamDoc(psiMethod, parameterList, paramName);

            Map<String, JComponent> pathParamMap = new HashMap<>(16);
            pathParamMap.put(PANEL, jPanel);
            pathParamMap.put(NAME, paramField);
            pathParamMap.put(DESC, descField);
            pathParamFieldMap.put(paramName, pathParamMap);
            jPanel.add(paramField);
            jPanel.add(exampleField);
            jPanel.add(descField);
        }
    }

    private String getPathParamDoc(PsiMethod psiMethod, PsiParameterList parameterList, String paramName) {
        for (PsiParameter parameter : parameterList.getParameters()) {
            PsiType type = parameter.getType();
            String presentableText = type.getPresentableText();

            if (JavaBasicType.TYPE_SET.contains(presentableText) && parameter.hasAnnotation(AnnotationConstants.PATH_VARIABLE)) {
                // 基础数据 从方法注释获取参数注释
                return this.getDocComment(psiMethod, paramName);
            } else if (parameter.getAnnotations().length <= 0) {
                // 非基础数据
                PsiClass resolve = ((PsiClassReferenceType) type).resolve();
                if (resolve == null) {
                    return "";
                }
                PsiField[] allFields = resolve.getAllFields();
                for (PsiField allField : allFields) {
                    String name = allField.getName();
                    if (paramName.endsWith(name)) {
                        PsiDocComment docComment = allField.getDocComment();
                        if (Objects.nonNull(docComment)) {
                            return docComment.getText().replaceAll("/", "").replaceAll("\\*", "").replaceAll("\n",
                                    "").replaceAll(" ", "");
                        }
                    }
                }
            }
        }
        return "";
    }

    private String getPath(PsiAnnotation classAnnotation) {
        String path = "";
        for (JvmAnnotationAttribute attribute : classAnnotation.getAttributes()) {
            String attributeName = attribute.getAttributeName();
            if ("path".equals(attributeName) || "value".equals(attributeName)) {
                JvmAnnotationAttributeValue attributeValue = attribute.getAttributeValue();
                if (attributeValue instanceof JvmAnnotationArrayValue) {
                    List<JvmAnnotationAttributeValue> values = ((JvmAnnotationArrayValue) attributeValue).getValues();
                    JvmAnnotationConstantValue constantValue = (JvmAnnotationConstantValue) values.get(0);
                    Object obj = constantValue.getConstantValue();
                    if (Objects.nonNull(obj)) {
                        String s = String.valueOf(obj);
                        path += s.startsWith("/") ? s : ("/" + s);
                    }
                } else if (attributeValue instanceof JvmAnnotationConstantValue) {
                    Object obj = ((JvmAnnotationConstantValue) attributeValue).getConstantValue();
                    if (Objects.nonNull(obj)) {
                        String s = String.valueOf(obj);
                        path += s.startsWith("/") ? s : ("/" + s);
                    }
                }

            }
        }
        return path;
    }

    private void setApiName(PsiMethod psiMethod) {
        // 方法注释作为接口名，没注释，需要手动填写接口
        PsiDocComment docComment = psiMethod.getDocComment();
        if (Objects.nonNull(docComment)) {
            String text = docComment.getText();
            if (!StringUtils.isEmpty(text)) {
                String[] split = text.split("\n");
                if (split.length < 1) {

                } else if (split.length == 1) {
                    apiName.setText(split[0].replaceAll("\\*", "").replaceAll(" ", ""));
                } else {
                    apiName.setText(split[1].replaceAll("\\*", "").replaceAll(" ", ""));
                }
            }
        }
    }

    private void addCategory() {
        AddCategory addCategory = new AddCategory();
        addCategory.pack();
        addCategory.setVisible(true);
    }

    public void getAllCategory() {
        if (StringUtils.isEmpty(UserVO.getInstance().getCookie())) {
            // 登录
            String cookie = HttpUtil.login();
            if (StringUtils.isEmpty(cookie)) {
                return;
            }
        }
        HashMap<String, Object> param = new HashMap<>();
        param.put("project_id", "104");
        String body = HttpUtil.doGet("https://api.inheritech.top/api/interface/list_menu", param);
        if (StringUtils.isEmpty(body)) {
            int option = Messages.showDialog("当前项目：104下未查询到接口分类，是否创建", "提示", new String[]{"创建", "取消"}, 1, null);
            if (option != 0) {
                showUp = false;
            } else {
                showUp = true;
            }
            return;
        }
        showUp = true;
        Map<String, Object> parse = ParseStringFactory.parse(body, Map.class);
        Object data = parse.get("data");
        if (data instanceof List) {
            List<Map<String, Object>> categoryList = (List<Map<String, Object>>) data;
            for (Map<String, Object> map : categoryList) {
                String name = map.get("name").toString();
                categoryComboBox.addItem(name);
                categoryMap.put(name, map);
            }
        }
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public boolean getShowUp() {
        return showUp;
    }
}
