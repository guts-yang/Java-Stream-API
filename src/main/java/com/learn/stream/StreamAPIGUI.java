package com.learn.stream;

import com.learn.stream.basic.BasicStreamOperations;
import com.learn.stream.intermediate.IntermediateStreamOperations;
import com.learn.stream.advanced.AdvancedStreamOperations;
import com.learn.stream.example.ComprehensiveExample;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Java Stream API 学习案例 GUI 界面 (优化版)
 * 提供图形化的交互界面来运行各种Stream示例
 * 
 * 优化内容:
 * 1. 修复编码兼容性问题
 * 2. 统一字体大小系统
 * 3. 改善中文字体渲染
 * 4. 优化颜色搭配和界面一致性
 */
public class StreamAPIGUI extends JFrame {
    
    // ==================== 字体系统常量 ====================
    private static final FontFamily DEFAULT_FONT_FAMILY = FontFamily.MICROSOFT_YAHEI;
    private static final FontSize TITLE_SIZE = FontSize.LARGE;
    private static final FontSize SUBTITLE_SIZE = FontSize.MEDIUM;
    private static final FontSize BODY_SIZE = FontSize.NORMAL;
    private static final FontSize SMALL_SIZE = FontSize.SMALL;
    private static final FontSize BUTTON_SIZE = FontSize.NORMAL;
    
    // ==================== 颜色系统常量 ====================
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);      // 主色调 - 蓝色
    private static final Color SECONDARY_COLOR = new Color(52, 152, 219);    // 次要色 - 浅蓝
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);       // 成功色 - 绿色
    private static final Color WARNING_COLOR = new Color(230, 126, 34);      // 警告色 - 橙色
    private static final Color DANGER_COLOR = new Color(231, 76, 60);        // 危险色 - 红色
    private static final Color INFO_COLOR = new Color(155, 89, 182);         // 信息色 - 紫色
    private static final Color TEXT_PRIMARY = new Color(33, 37, 41);         // 主要文字色
    private static final Color TEXT_SECONDARY = new Color(108, 117, 125);    // 次要文字色
    private static final Color BACKGROUND_LIGHT = new Color(248, 249, 250);  // 浅色背景
    
    private JTextArea outputArea;
    private JProgressBar progressBar;
    private JLabel statusLabel;
    private JPanel buttonPanel;
    private JButton clearButton;
    private JButton saveButton;
    
    // 保存原始System.out用于恢复
    private final PrintStream originalOut = System.out;
    
    public StreamAPIGUI() {
        initializeGUI();
        redirectOutput();
        showWelcomeMessage();
    }
    
    // ==================== 字体系统枚举 ====================
    private enum FontFamily {
        MICROSOFT_YAHEI("微软雅黑"),
        SIMSUN("宋体"),
        SANS_SERIF("SansSerif");
        
        private final String fontName;
        
        FontFamily(String fontName) {
            this.fontName = fontName;
        }
        
        public String getFontName() {
            return fontName;
        }
    }
    
    private enum FontSize {
        EXTRA_SMALL(10, Font.PLAIN),
        SMALL(12, Font.PLAIN),
        NORMAL(14, Font.PLAIN),
        MEDIUM(16, Font.PLAIN),
        LARGE(20, Font.BOLD),
        EXTRA_LARGE(24, Font.BOLD);
        
        private final int size;
        private final int style;
        
        FontSize(int size, int style) {
            this.size = size;
            this.style = style;
        }
        
        public Font getFont(FontFamily family) {
            return new Font(family.getFontName(), style, size);
        }
        
        public int getSize() {
            return size;
        }
        
        public int getStyle() {
            return style;
        }
    }
    
    /**
     * 初始化GUI界面
     */
    private void initializeGUI() {
        setTitle("Java Stream API 学习案例 - 图形化界面");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800); // 增加窗口尺寸
        setLocationRelativeTo(null);
        
        // 设置优化后的外观
        setupLookAndFeel();
        
        // 主面板
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15)); // 增加间距
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(mainPanel);
        
        // 顶部面板 - 标题和状态
        createTopPanel();
        
        // 左侧面板 - 操作按钮
        createLeftPanel();
        
        // 中心面板 - 输出区域
        createCenterPanel();
        
        // 底部面板 - 控制按钮
        createBottomPanel();
        
        // 添加菜单栏
        createMenuBar();
    }
    
    /**
     * 设置外观和感觉
     */
    private void setupLookAndFeel() {
        try {
            // 设置系统外观
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // 优化UI默认值
            UIManager.put("Button.font", BUTTON_SIZE.getFont(DEFAULT_FONT_FAMILY));
            UIManager.put("Label.font", BODY_SIZE.getFont(DEFAULT_FONT_FAMILY));
            UIManager.put("Menu.font", BODY_SIZE.getFont(DEFAULT_FONT_FAMILY));
            UIManager.put("MenuItem.font", BODY_SIZE.getFont(DEFAULT_FONT_FAMILY));
            UIManager.put("TextArea.font", SMALL_SIZE.getFont(FontFamily.SANS_SERIF));
            UIManager.put("OptionPane.messageFont", BODY_SIZE.getFont(DEFAULT_FONT_FAMILY));
            UIManager.put("OptionPane.buttonFont", BUTTON_SIZE.getFont(DEFAULT_FONT_FAMILY));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 创建顶部面板
     */
    private void createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        // 主标题
        JLabel titleLabel = new JLabel("Java Stream API 学习案例", SwingConstants.CENTER);
        titleLabel.setFont(TITLE_SIZE.getFont(DEFAULT_FONT_FAMILY));
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        
        // 副标题
        JLabel subtitleLabel = new JLabel("通过图形化界面学习和实践Stream API", SwingConstants.CENTER);
        subtitleLabel.setFont(SUBTITLE_SIZE.getFont(DEFAULT_FONT_FAMILY));
        subtitleLabel.setForeground(TEXT_SECONDARY);
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        // 状态和进度区域
        JPanel statusPanel = new JPanel(new BorderLayout(10, 5));
        statusPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 10, 20));
        
        // 状态标签
        statusLabel = new JLabel("就绪", SwingConstants.RIGHT);
        statusLabel.setFont(SMALL_SIZE.getFont(DEFAULT_FONT_FAMILY));
        statusLabel.setForeground(SUCCESS_COLOR);
        
        // 进度条
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setString("就绪");
        progressBar.setFont(SMALL_SIZE.getFont(DEFAULT_FONT_FAMILY));
        progressBar.setForeground(SECONDARY_COLOR);
        progressBar.setBackground(BACKGROUND_LIGHT);
        
        statusPanel.add(statusLabel, BorderLayout.NORTH);
        statusPanel.add(progressBar, BorderLayout.SOUTH);
        
        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(subtitleLabel, BorderLayout.CENTER);
        topPanel.add(statusPanel, BorderLayout.SOUTH);
        
        getContentPane().add(topPanel, BorderLayout.NORTH);
    }
    
    /**
     * 创建左侧操作面板
     */
    private void createLeftPanel() {
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(new TitledBorder("选择操作"));
        buttonPanel.setPreferredSize(new Dimension(280, 0)); // 增加宽度
        
        // 创建功能按钮
        createOperationButtons();
        
        // 添加一些间距
        buttonPanel.add(Box.createVerticalStrut(25));
        
        getContentPane().add(new JScrollPane(buttonPanel), BorderLayout.WEST);
    }
    
    /**
     * 创建操作按钮
     */
    private void createOperationButtons() {
        // 基础操作按钮
        JButton basicButton = createStyledButton("🔰 基础操作示例", "学习Stream的创建、过滤、映射等基本操作", SECONDARY_COLOR);
        basicButton.addActionListener(e -> runExample("基础操作", () -> {
            updateStatus("正在运行基础操作示例...");
            BasicStreamOperations.main(new String[]{});
        }));
        
        // 中级操作按钮
        JButton intermediateButton = createStyledButton("📊 中级操作示例", "学习Optional、并行Stream、复杂收集操作", INFO_COLOR);
        intermediateButton.addActionListener(e -> runExample("中级操作", () -> {
            updateStatus("正在运行中级操作示例...");
            IntermediateStreamOperations.main(new String[]{});
        }));
        
        // 高级操作按钮
        JButton advancedButton = createStyledButton("⚡ 高级操作示例", "学习自定义Collector、性能优化、异常处理", WARNING_COLOR);
        advancedButton.addActionListener(e -> runExample("高级操作", () -> {
            updateStatus("正在运行高级操作示例...");
            AdvancedStreamOperations.main(new String[]{});
        }));
        
        // 综合示例按钮
        JButton comprehensiveButton = createStyledButton("🎯 综合应用示例", "体验完整的学生成绩管理系统", SUCCESS_COLOR);
        comprehensiveButton.addActionListener(e -> runExample("综合应用", () -> {
            updateStatus("正在运行综合应用示例...");
            ComprehensiveExample.main(new String[]{});
        }));
        
        // 运行所有按钮
        JButton allButton = createStyledButton("🚀 运行所有示例", "依次运行所有Stream API示例", DANGER_COLOR);
        allButton.addActionListener(e -> runAllExamples());
        
        // 添加按钮到面板
        buttonPanel.add(basicButton);
        buttonPanel.add(Box.createVerticalStrut(12));
        buttonPanel.add(intermediateButton);
        buttonPanel.add(Box.createVerticalStrut(12));
        buttonPanel.add(advancedButton);
        buttonPanel.add(Box.createVerticalStrut(12));
        buttonPanel.add(comprehensiveButton);
        buttonPanel.add(Box.createVerticalStrut(25));
        buttonPanel.add(allButton);
        
        // 为按钮面板设置固定宽度
        buttonPanel.add(Box.createVerticalGlue());
    }
    
    /**
     * 创建样式化按钮（优化版）
     */
    private JButton createStyledButton(String text, String description, Color color) {
        // 使用HTML来支持更好的文本格式
        String htmlText = String.format(
            "<html><div style='text-align: left; padding: 5px;'>" +
            "<b style='font-size: %dpx;'>%s</b><br>" +
            "<span style='font-size: %dpx; color: #666;'>%s</span>" +
            "</div></html>",
            BUTTON_SIZE.getSize() + 2, text,
            SMALL_SIZE.getSize(), description
        );
        
        JButton button = new JButton(htmlText);
        
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(250, 70));
        button.setPreferredSize(new Dimension(250, 70));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createRaisedBevelBorder(),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        // 优化字体设置
        button.setFont(BUTTON_SIZE.getFont(DEFAULT_FONT_FAMILY));
        
        // 鼠标悬停效果
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.brighter());
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLoweredBevelBorder(),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)
                ));
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createRaisedBevelBorder(),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)
                ));
            }
        });
        
        return button;
    }
    
    /**
     * 创建中心输出面板
     */
    private void createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(new TitledBorder("输出结果"));
        
        // 输出文本区域
        outputArea = new JTextArea();
        outputArea.setFont(SMALL_SIZE.getFont(FontFamily.SANS_SERIF));
        outputArea.setBackground(BACKGROUND_LIGHT);
        outputArea.setForeground(TEXT_PRIMARY);
        outputArea.setEditable(false);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        outputArea.setCaretColor(PRIMARY_COLOR);
        outputArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // 设置行高和边距
        outputArea.setRows(20);
        outputArea.setColumns(80);
        
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getViewport().setBackground(BACKGROUND_LIGHT);
        
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        getContentPane().add(centerPanel, BorderLayout.CENTER);
    }
    
    /**
     * 创建底部控制面板
     */
    private void createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBorder(new EmptyBorder(15, 0, 0, 0));
        
        // 清空按钮
        clearButton = new JButton("清空输出");
        clearButton.setFont(BUTTON_SIZE.getFont(DEFAULT_FONT_FAMILY));
        clearButton.setBackground(SECONDARY_COLOR);
        clearButton.setForeground(Color.WHITE);
        clearButton.setFocusPainted(false);
        clearButton.addActionListener(e -> outputArea.setText(""));
        
        // 保存按钮
        saveButton = new JButton("保存结果");
        saveButton.setFont(BUTTON_SIZE.getFont(DEFAULT_FONT_FAMILY));
        saveButton.setBackground(SUCCESS_COLOR);
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.addActionListener(e -> saveOutput());
        
        // 帮助按钮
        JButton helpButton = new JButton("帮助信息");
        helpButton.setFont(BUTTON_SIZE.getFont(DEFAULT_FONT_FAMILY));
        helpButton.setBackground(INFO_COLOR);
        helpButton.setForeground(Color.WHITE);
        helpButton.setFocusPainted(false);
        helpButton.addActionListener(e -> showHelpDialog());
        
        bottomPanel.add(helpButton);
        bottomPanel.add(clearButton);
        bottomPanel.add(saveButton);
        
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);
    }
    
    /**
     * 创建菜单栏
     */
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // 文件菜单
        JMenu fileMenu = new JMenu("文件(F)");
        fileMenu.setMnemonic('F');
        
        JMenuItem clearItem = new JMenuItem("清空输出", 'C');
        clearItem.addActionListener(e -> outputArea.setText(""));
        fileMenu.add(clearItem);
        
        fileMenu.addSeparator();
        
        JMenuItem saveItem = new JMenuItem("保存结果", 'S');
        saveItem.addActionListener(e -> saveOutput());
        fileMenu.add(saveItem);
        
        fileMenu.addSeparator();
        
        JMenuItem exitItem = new JMenuItem("退出", 'X');
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);
        
        // 运行菜单
        JMenu runMenu = new JMenu("运行(R)");
        runMenu.setMnemonic('R');
        
        runMenu.add(createMenuItem("基础操作", "basic", 'B'));
        runMenu.add(createMenuItem("中级操作", "intermediate", 'M'));
        runMenu.add(createMenuItem("高级操作", "advanced", 'A'));
        runMenu.add(createMenuItem("综合应用", "comprehensive", 'C'));
        
        runMenu.addSeparator();
        
        runMenu.add(createMenuItem("运行所有", "all", 'L'));
        
        // 帮助菜单
        JMenu helpMenu = new JMenu("帮助(H)");
        helpMenu.setMnemonic('H');
        
        JMenuItem aboutItem = new JMenuItem("关于", 'A');
        aboutItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutItem);
        
        JMenuItem helpItem = new JMenuItem("使用帮助", 'H');
        helpItem.addActionListener(e -> showHelpDialog());
        helpMenu.add(helpItem);
        
        menuBar.add(fileMenu);
        menuBar.add(runMenu);
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
    }
    
    /**
     * 创建菜单项
     */
    private JMenuItem createMenuItem(String text, String command, char mnemonic) {
        JMenuItem item = new JMenuItem(text, mnemonic);
        item.addActionListener(e -> {
            switch (command) {
                case "basic":
                    runExample("基础操作", () -> BasicStreamOperations.main(new String[]{}));
                    break;
                case "intermediate":
                    runExample("中级操作", () -> IntermediateStreamOperations.main(new String[]{}));
                    break;
                case "advanced":
                    runExample("高级操作", () -> AdvancedStreamOperations.main(new String[]{}));
                    break;
                case "comprehensive":
                    runExample("综合应用", () -> ComprehensiveExample.main(new String[]{}));
                    break;
                case "all":
                    runAllExamples();
                    break;
            }
        });
        return item;
    }
    
    /**
     * 重定向System.out到GUI（优化版）
     */
    private void redirectOutput() {
        System.setOut(new PrintStream(new ByteArrayOutputStream() {
            @Override
            public void write(byte[] buf, int off, int len) {
                String text = new String(buf, off, len);
                SwingUtilities.invokeLater(() -> {
                    outputArea.append(text);
                    outputArea.setCaretPosition(outputArea.getDocument().getLength());
                });
            }
        }));
    }
    
    /**
     * 运行示例
     */
    private void runExample(String exampleName, Runnable exampleCode) {
        // 在新线程中运行示例
        new Thread(() -> {
            try {
                // 禁用所有按钮
                setButtonsEnabled(false);
                
                // 清空输出并添加标题
                outputArea.setText("");
                printHeader(exampleName + " 示例运行结果");
                
                // 运行示例
                exampleCode.run();
                
                // 显示完成信息
                printFooter(exampleName + " 示例运行完成!");
                
            } catch (Exception e) {
                printError("运行 " + exampleName + " 示例时出错: " + e.getMessage());
                e.printStackTrace();
            } finally {
                // 恢复按钮状态
                setButtonsEnabled(true);
                updateStatus("就绪");
                progressBar.setString("就绪");
                progressBar.setValue(0);
            }
        }).start();
    }
    
    /**
     * 运行所有示例
     */
    private void runAllExamples() {
        new Thread(() -> {
            try {
                setButtonsEnabled(false);
                outputArea.setText("");
                printHeader("Stream API 学习案例 - 完整演示");
                
                String[] examples = {
                        "基础操作", "中级操作", "高级操作", "综合应用"
                };
                
                Runnable[] exampleCodes = {
                        () -> BasicStreamOperations.main(new String[]{}),
                        () -> IntermediateStreamOperations.main(new String[]{}),
                        () -> AdvancedStreamOperations.main(new String[]{}),
                        () -> ComprehensiveExample.main(new String[]{}),
                };
                
                for (int i = 0; i < examples.length; i++) {
                    updateStatus("正在运行第 " + (i+1) + "/" + examples.length + " 个示例...");
                    progressBar.setValue((i+1) * 100 / examples.length);
                    
                    printSectionSeparator(examples[i]);
                    exampleCodes[i].run();
                    
                    if (i < examples.length - 1) {
                        printPause();
                    }
                }
                
                printFooter("所有示例运行完成! 🎉");
                
            } catch (Exception e) {
                printError("运行示例时出错: " + e.getMessage());
                e.printStackTrace();
            } finally {
                setButtonsEnabled(true);
                updateStatus("就绪");
                progressBar.setString("就绪");
                progressBar.setValue(0);
            }
        }).start();
    }
    
    /**
     * 设置按钮启用状态
     */
    private void setButtonsEnabled(boolean enabled) {
        Component[] components = buttonPanel.getComponents();
        for (Component component : components) {
            if (component instanceof JButton) {
                ((JButton) component).setEnabled(enabled);
            }
        }
        clearButton.setEnabled(enabled);
        saveButton.setEnabled(enabled);
    }
    
    /**
     * 更新状态标签
     */
    private void updateStatus(String status) {
        SwingUtilities.invokeLater(() -> {
            statusLabel.setText(status);
            progressBar.setString(status);
        });
    }
    
    /**
     * 打印标题（修复编码问题）
     */
    private void printHeader(String title) {
        outputArea.append("\n" + createSeparatorLine('=', 80) + "\n");
        outputArea.append("                        " + title + "\n");
        outputArea.append("时间: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "\n");
        outputArea.append(createSeparatorLine('=', 80) + "\n\n");
    }
    
    /**
     * 打印脚注（修复编码问题）
     */
    private void printFooter(String message) {
        outputArea.append("\n" + createSeparatorLine('=', 80) + "\n");
        outputArea.append("                    ✅ " + message + "\n");
        outputArea.append(createSeparatorLine('=', 80) + "\n\n");
    }
    
    /**
     * 打印错误信息（修复编码问题）
     */
    private void printError(String message) {
        outputArea.append("\n❌ 错误: " + message + "\n");
    }
    
    /**
     * 打印分隔符（修复编码问题）
     */
    private void printSectionSeparator(String sectionName) {
        outputArea.append("\n" + createSeparatorLine('-', 80) + "\n");
        outputArea.append("                       " + sectionName + "\n");
        outputArea.append(createSeparatorLine('-', 80) + "\n\n");
    }
    
    /**
     * 打印暂停提示（修复编码问题）
     */
    private void printPause() {
        outputArea.append("\n按回车键继续下一个示例...\n");
        try {
            Thread.sleep(1000); // 稍作暂停
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * 创建分隔线（兼容Java 8+）
     */
    private String createSeparatorLine(char character, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(character);
        }
        return sb.toString();
    }
    
    /**
     * 显示欢迎信息（优化版）
     */
    private void showWelcomeMessage() {
        outputArea.setText("");
        
        StringBuilder welcome = new StringBuilder();
        welcome.append(createSeparatorLine('=', 80)).append("\n");
        welcome.append("              Java Stream API 学习案例 - GUI 界面\n");
        welcome.append(createSeparatorLine('=', 80)).append("\n");
        welcome.append("\n");
        welcome.append("  这是一个完整的Stream API学习项目，包含从基础到高级的各种示例\n");
        welcome.append("\n");
        welcome.append("  请选择左侧的按钮来运行不同的示例，或者从菜单栏选择功能\n");
        welcome.append("\n");
        welcome.append("  提示: 输出结果会显示在这个文本区域中\n");
        welcome.append("\n");
        welcome.append(createSeparatorLine('=', 80)).append("\n");
        welcome.append("  版本: v2.0 (优化版)\n");
        welcome.append("  特性: 优化编码显示、统一字体系统、改善用户体验\n");
        welcome.append(createSeparatorLine('=', 80)).append("\n");
        
        outputArea.append(welcome.toString());
    }
    
    /**
     * 保存输出结果
     */
    private void saveOutput() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("文本文件 (*.txt)", "txt"));
        fileChooser.setSelectedFile(new java.io.File("StreamAPI_Output_" + 
                new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".txt"));
        
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                java.io.OutputStreamWriter writer = new java.io.OutputStreamWriter(
                    new java.io.FileOutputStream(fileChooser.getSelectedFile()), "UTF-8");
                writer.write(outputArea.getText());
                writer.close();
                JOptionPane.showMessageDialog(this, "输出结果已保存到文件!", "保存成功", 
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "保存文件时出错: " + e.getMessage(), 
                        "保存失败", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * 显示帮助对话框（优化版）
     */
    private void showHelpDialog() {
        String helpText = "Java Stream API 学习案例 - 使用帮助\n\n" +
                "🎯 功能说明:\n" +
                "• 基础操作示例: 学习Stream的创建、过滤、映射等基本操作\n" +
                "• 中级操作示例: 学习Optional、并行Stream、复杂收集操作\n" +
                "• 高级操作示例: 学习自定义Collector、性能优化、异常处理\n" +
                "• 综合应用示例: 体验完整的学生成绩管理系统\n\n" +
                "💡 使用提示:\n" +
                "• 点击左侧按钮运行对应的示例\n" +
                "• 输出结果会显示在中心的文本区域中\n" +
                "• 可以使用\"清空输出\"按钮清除历史输出\n" +
                "• 使用\"保存结果\"功能可以将输出保存到文件\n" +
                "• 运行期间按钮会被禁用，防止重复操作\n\n" +
                "🚀 性能优化:\n" +
                "• 大数据量操作可能需要一些时间，请耐心等待\n" +
                "• 可以通过进度条查看当前运行状态\n" +
                "• 建议按照\"基础→中级→高级→综合\"的学习顺序\n\n" +
                "📚 学习建议:\n" +
                "• 仔细观察每个示例的输出结果\n" +
                "• 尝试修改示例代码，观察不同参数的效果\n" +
                "• 对比传统循环与Stream的编程方式\n" +
                "• 思考在实际项目中如何应用Stream API";
        
        JTextArea textArea = new JTextArea(helpText);
        textArea.setEditable(false);
        textArea.setBackground(getBackground());
        textArea.setFont(BODY_SIZE.getFont(DEFAULT_FONT_FAMILY));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(700, 500));
        
        JOptionPane.showMessageDialog(this, scrollPane, "使用帮助", 
                JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * 显示关于对话框（优化版）
     */
    private void showAboutDialog() {
        String aboutText = "Java Stream API 学习案例 v2.0 (优化版)\n\n" +
                "🎓 项目说明:\n" +
                "这是一个面向大学生Java课程学习的Stream API综合学习案例，\n" +
                "通过递进式的示例帮助学生掌握Java 8引入的Stream编程技术。\n\n" +
                "📋 功能特性:\n" +
                "• 完整的Stream API学习体系\n" +
                "• 图形化交互界面\n" +
                "• 实际业务场景示例\n" +
                "• 性能优化实践指导\n" +
                "• 异常处理机制\n\n" +
                "🔧 技术栈:\n" +
                "• Java 8+ Stream API\n" +
                "• Swing GUI 框架\n" +
                "• Maven 项目管理\n\n" +
                "✨ 优化内容:\n" +
                "• 修复编码兼容性问题\n" +
                "• 统一字体大小系统\n" +
                "• 改善中文字体渲染\n" +
                "• 优化颜色搭配和界面一致性\n" +
                "• 增强用户体验和可读性\n\n" +
                "👨‍💻 开发信息:\n" +
                "• 适合Java初学者到进阶开发者\n" +
                "• 包含从基础到高级的完整示例\n" +
                "• 提供实际应用场景案例\n\n" +
                "📖 学习建议:\n" +
                "建议按照\"基础操作 → 中级操作 → 高级操作 → 综合应用\"\n" +
                "的顺序进行学习，从理论到实践，全面掌握Stream API。";
        
        JTextArea textArea = new JTextArea(aboutText);
        textArea.setEditable(false);
        textArea.setBackground(getBackground());
        textArea.setFont(BODY_SIZE.getFont(DEFAULT_FONT_FAMILY));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 450));
        
        JOptionPane.showMessageDialog(this, scrollPane, "关于", 
                JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * 主方法
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StreamAPIGUI gui = new StreamAPIGUI();
            gui.setVisible(true);
        });
    }
}