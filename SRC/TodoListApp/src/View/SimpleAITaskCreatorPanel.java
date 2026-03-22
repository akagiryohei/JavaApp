package View;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class SimpleAITaskCreatorPanel extends JPanel {
    
    // UIコンポーネント
    private JTextArea instructionTextArea;
    private JButton generateButton;
    private JTextField listNameField;
    private JButton editListNameButton;
    private JTextArea refinementTextArea;
    private JButton refineButton;
    private JButton addToTodoButton;
    private JPanel taskListPanel;
    private JLabel taskCountLabel;
    private JPanel previewPanel;
    private JPanel loadingPanel;
    private JPanel listNamePanel;
    
    // データ
    private List<Task> generatedTasks = new ArrayList<>();
    private String generatedListName = "";
    
    public SimpleAITaskCreatorPanel() {
        /**
         * setLayout:配置のルール
         * BorderLayout:上下左右中央に分けて配置できる
         * setBorder:枠線をつける設定
         * setBackground:背景色の設定
         */
        setLayout(new BorderLayout(10, 10));//位置とサイズの管理
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(new Color(240, 240, 245));
        
        initComponents();
        layoutComponents();
    }
    
    private void initComponents() {
        // 指示内容のテキストエリア
        instructionTextArea = new JTextArea(6, 40);
        instructionTextArea.setLineWrap(true);
        instructionTextArea.setWrapStyleWord(true);
        instructionTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
        instructionTextArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 2),
            new EmptyBorder(8, 8, 8, 8)
        ));
        
        // タスク生成ボタン
        generateButton = createStyledButton("✨ AIにタスクを作成してもらう", 
            new Color(102, 126, 234), Color.WHITE);
        generateButton.addActionListener(e -> generateTasks());
        
        // リスト名の入力フィールド
        listNameField = new JTextField();
        listNameField.setFont(new Font("Arial", Font.BOLD, 16));
        listNameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 2),
            new EmptyBorder(8, 8, 8, 8)
        ));
        
        // リスト名編集ボタン
        editListNameButton = new JButton("✏️");
        editListNameButton.setFont(new Font("Arial", Font.PLAIN, 18));
        editListNameButton.setBackground(new Color(102, 126, 234));
        editListNameButton.setForeground(Color.WHITE);
        editListNameButton.setFocusPainted(false);
        editListNameButton.setBorderPainted(false);
        editListNameButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editListNameButton.setToolTipText("リスト名を編集");
        editListNameButton.addActionListener(e -> {
            listNameField.requestFocus();
            listNameField.selectAll();
        });
        
        // 追加指示のテキストエリア
        refinementTextArea = new JTextArea(3, 40);
        refinementTextArea.setLineWrap(true);
        refinementTextArea.setWrapStyleWord(true);
        refinementTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
        refinementTextArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 2),
            new EmptyBorder(8, 8, 8, 8)
        ));
        
        // タスク再生成ボタン
        refineButton = createStyledButton("🔄 タスクを再生成", 
            new Color(102, 126, 234), Color.WHITE);
        refineButton.addActionListener(e -> refineTasks());
        
        // TODOリスト追加ボタン
        addToTodoButton = createStyledButton("✅ TODOリストに追加", 
            new Color(39, 174, 96), Color.WHITE);
        addToTodoButton.addActionListener(e -> addTasksToTodoList());
        
        // タスクリストパネル
        taskListPanel = new JPanel();
        taskListPanel.setLayout(new BoxLayout(taskListPanel, BoxLayout.Y_AXIS));
        taskListPanel.setBackground(Color.WHITE);
        
        // タスクカウントラベル
        taskCountLabel = new JLabel("0件");
        taskCountLabel.setFont(new Font("Arial", Font.BOLD, 14));
        taskCountLabel.setForeground(Color.WHITE);
        taskCountLabel.setOpaque(true);
        taskCountLabel.setBackground(new Color(102, 126, 234));
        taskCountLabel.setBorder(new EmptyBorder(4, 12, 4, 12));
        
        // リスト名パネル
        listNamePanel = new JPanel(new BorderLayout(10, 10));
        listNamePanel.setBackground(new Color(237, 233, 254));
        listNamePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(196, 181, 253), 2),
            new EmptyBorder(15, 15, 15, 15)
        ));
        listNamePanel.setVisible(false);
        
        // プレビューパネル
        previewPanel = new JPanel(new BorderLayout(10, 10));
        previewPanel.setBackground(new Color(240, 240, 245));
        previewPanel.setVisible(false);
        
        // ローディングパネル
        loadingPanel = createLoadingPanel();
        loadingPanel.setVisible(false);
    }
    
    private void layoutComponents() {
        // 入力セクション
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBackground(new Color(248, 249, 250));
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        // 指示内容
        JPanel instructionPanel = createFormGroup("指示内容", new JScrollPane(instructionTextArea));
        inputPanel.add(instructionPanel);
        inputPanel.add(Box.createVerticalStrut(15));
        
        // 生成ボタン
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.add(generateButton);
        inputPanel.add(buttonPanel);
        
        // リスト名セクション
        JLabel listNameLabel = new JLabel("📝 リスト名");
        listNameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        listNameLabel.setForeground(new Color(85, 85, 85));
        
        JPanel listNameInputPanel = new JPanel(new BorderLayout(10, 0));
        listNameInputPanel.setOpaque(false);
        listNameInputPanel.add(listNameField, BorderLayout.CENTER);
        listNameInputPanel.add(editListNameButton, BorderLayout.EAST);
        
        listNamePanel.add(listNameLabel, BorderLayout.NORTH);
        listNamePanel.add(Box.createVerticalStrut(8));
        listNamePanel.add(listNameInputPanel, BorderLayout.CENTER);
        
        // プレビューセクションのヘッダー
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 240, 245));
        JLabel titleLabel = new JLabel("📋 生成されたタスク");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(taskCountLabel, BorderLayout.EAST);
        
        // タスクリストのスクロールペイン
        JScrollPane taskScrollPane = new JScrollPane(taskListPanel);
        taskScrollPane.setPreferredSize(new Dimension(0, 300));
        taskScrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        
        // 追加指示セクション
        JPanel refinementPanel = createFormGroup("タスクへの追加指示（任意）", 
            new JScrollPane(refinementTextArea));
        
        // ボタンパネル
        JPanel actionButtonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        actionButtonPanel.setOpaque(false);
        actionButtonPanel.add(refineButton);
        actionButtonPanel.add(addToTodoButton);
        
        // プレビューパネルに追加
        JPanel previewContentPanel = new JPanel();
        previewContentPanel.setLayout(new BoxLayout(previewContentPanel, BoxLayout.Y_AXIS));
        previewContentPanel.setOpaque(false);
        
        listNamePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        previewContentPanel.add(listNamePanel);
        previewContentPanel.add(Box.createVerticalStrut(15));
        
        JPanel taskSection = new JPanel(new BorderLayout(10, 10));
        taskSection.setOpaque(false);
        taskSection.add(headerPanel, BorderLayout.NORTH);
        taskSection.add(taskScrollPane, BorderLayout.CENTER);
        taskSection.setAlignmentX(Component.LEFT_ALIGNMENT);
        previewContentPanel.add(taskSection);
        
        previewContentPanel.add(Box.createVerticalStrut(15));
        refinementPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        previewContentPanel.add(refinementPanel);
        previewContentPanel.add(Box.createVerticalStrut(10));
        actionButtonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        previewContentPanel.add(actionButtonPanel);
        
        previewPanel.add(previewContentPanel, BorderLayout.NORTH);
        
        // メインパネルに追加
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        
        JLabel mainTitle = new JLabel("🤖 AI タスク自動作成", SwingConstants.CENTER);
        mainTitle.setFont(new Font("Arial", Font.BOLD, 24));
        mainTitle.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        topPanel.add(mainTitle, BorderLayout.NORTH);
        topPanel.add(inputPanel, BorderLayout.CENTER);
        
        add(topPanel, BorderLayout.NORTH);
        add(loadingPanel, BorderLayout.CENTER);
        add(previewPanel, BorderLayout.CENTER);
    }
    
    private JPanel createFormGroup(String labelText, JComponent component) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(new Color(85, 85, 85));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        component.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(label);
        panel.add(Box.createVerticalStrut(8));
        panel.add(component);
        
        return panel;
    }
    
    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(fgColor);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(250, 40));
        
        // ホバーエフェクト
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(bgColor.darker());
                }
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    private JPanel createLoadingPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 240, 245));
        
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        
        JLabel spinner = new JLabel("⏳");
        spinner.setFont(new Font("Arial", Font.PLAIN, 48));
        spinner.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel text = new JLabel("AIがタスクを生成中...");
        text.setFont(new Font("Arial", Font.BOLD, 16));
        text.setForeground(new Color(102, 126, 234));
        text.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        content.add(spinner);
        content.add(Box.createVerticalStrut(10));
        content.add(text);
        
        panel.add(content);
        return panel;
    }
    
    private String generateListName(String instruction) {
        // 指示内容からリスト名を生成（実際はAIが生成）
        String keywords = instruction.substring(0, Math.min(20, instruction.length()));
        return keywords + "のタスクリスト";
    }
    
    private void generateTasks() {
        String instruction = instructionTextArea.getText().trim();
        
        if (instruction.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "指示内容を入力してください", 
                "入力エラー", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // ローディング表示
        previewPanel.setVisible(false);
        loadingPanel.setVisible(true);
        generateButton.setEnabled(false);
        
        // 実際のアプリでは、ここでバックエンドのAI APIを非同期で呼び出します
        SwingWorker<List<Task>, Void> worker = new SwingWorker<List<Task>, Void>() {
            @Override
            protected List<Task> doInBackground() throws Exception {
                Thread.sleep(2000); // API呼び出しのシミュレーション
                return generateDemoTasks(instruction);
            }
            
            @Override
            protected void done() {
                try {
                    generatedTasks = get();
                    generatedListName = generateListName(instruction);
                    listNameField.setText(generatedListName);
                    displayTasks();
                    loadingPanel.setVisible(false);
                    listNamePanel.setVisible(true);
                    previewPanel.setVisible(true);
                    generateButton.setEnabled(true);
                } catch (Exception e) {
                    e.printStackTrace();
                    generateButton.setEnabled(true);
                }
            }
        };
        worker.execute();
    }
    
    private List<Task> generateDemoTasks(String instruction) {
        List<Task> tasks = new ArrayList<>();
        
        // デモ用のタスク生成（指示内容から推測）
        String baseInstruction = instruction.substring(0, Math.min(30, instruction.length()));
        
        tasks.add(new Task(baseInstruction + "の計画立案", "high"));
        tasks.add(new Task(baseInstruction + "の準備作業", "high"));
        tasks.add(new Task(baseInstruction + "の実施", "medium"));
        tasks.add(new Task(baseInstruction + "の進捗確認", "medium"));
        tasks.add(new Task(baseInstruction + "の振り返り", "low"));
        
        return tasks;
    }
    /**
     * 表示メソッド
     */
    private void displayTasks() {
        taskListPanel.removeAll();
        taskCountLabel.setText(generatedTasks.size() + "件");
        
        for (Task task : generatedTasks) {
            taskListPanel.add(createTaskItem(task));
            taskListPanel.add(Box.createVerticalStrut(10));
        }
        
        taskListPanel.revalidate();
        taskListPanel.repaint();
    }
    
    /**
     * タスクアイテムを作成するメソッド
     * @param task
     * @return
     */
    private JPanel createTaskItem(Task task) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 2),
            new EmptyBorder(12, 12, 12, 12)
        ));
        
        // タスク内容
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel(task.title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel priorityLabel = new JLabel(getPriorityText(task.priority));
        priorityLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        priorityLabel.setOpaque(true);
        priorityLabel.setBorder(new EmptyBorder(2, 8, 2, 8));
        priorityLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        Color priorityColor = getPriorityColor(task.priority);
        priorityLabel.setBackground(priorityColor);
        priorityLabel.setForeground(getPriorityTextColor(task.priority));
        
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(8));
        contentPanel.add(priorityLabel);
        
        // 削除ボタン
        JButton removeButton = new JButton("削除");
        removeButton.setBackground(new Color(231, 76, 60));
        removeButton.setForeground(Color.WHITE);
        removeButton.setFocusPainted(false);
        removeButton.setBorderPainted(false);
        removeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        removeButton.addActionListener(e -> {
            generatedTasks.remove(task);
            displayTasks();
        });
        
        panel.add(contentPanel, BorderLayout.CENTER);
        panel.add(removeButton, BorderLayout.EAST);
        
        return panel;
    }
    
    private void refineTasks() {
        String refinement = refinementTextArea.getText().trim();
        
        if (refinement.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "追加指示を入力してください", 
                "入力エラー", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // ローディング表示
        previewPanel.setVisible(false);
        loadingPanel.setVisible(true);
        refineButton.setEnabled(false);
        
        // 実際のアプリでは、既存のタスクと追加指示をバックエンドに送信
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                Thread.sleep(1500);
                // デモ用: タスクを改善
                for (Task task : generatedTasks) {
                    task.title = task.title + " (改善済)";
                    if (task.priority.equals("low")) {
                        task.priority = "medium";
                    }
                }
                return null;
            }
            
            @Override
            protected void done() {
                displayTasks();
                loadingPanel.setVisible(false);
                previewPanel.setVisible(true);
                refineButton.setEnabled(true);
                refinementTextArea.setText("");
            }
        };
        worker.execute();
    }
    
    private void addTasksToTodoList() {
        if (generatedTasks.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "追加するタスクがありません", 
                "エラー", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String listName = listNameField.getText().trim();
        if (listName.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "リスト名を入力してください", 
                "入力エラー", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // 実際のアプリでは、ここでバックエンドのAPIを呼び出してTODOリストに追加
        JOptionPane.showMessageDialog(this, 
            "リスト「" + listName + "」に" + generatedTasks.size() + "件のタスクを追加しました！", 
            "成功", 
            JOptionPane.INFORMATION_MESSAGE);
        
        // リセット
        generatedTasks.clear();
        instructionTextArea.setText("");
        refinementTextArea.setText("");
        listNameField.setText("");
        listNamePanel.setVisible(false);
        previewPanel.setVisible(false);
    }
    
    private String getPriorityText(String priority) {
        switch (priority) {
            case "high": return "高優先度";
            case "medium": return "中優先度";
            case "low": return "低優先度";
            default: return "優先度なし";
        }
    }
    
    private Color getPriorityColor(String priority) {
        switch (priority) {
            case "high": return new Color(255, 238, 238);
            case "medium": return new Color(255, 234, 167);
            case "low": return new Color(223, 230, 233);
            default: return Color.LIGHT_GRAY;
        }
    }
    
    private Color getPriorityTextColor(String priority) {
        switch (priority) {
            case "high": return new Color(204, 0, 0);
            case "medium": return new Color(214, 48, 49);
            case "low": return new Color(99, 110, 114);
            default: return Color.BLACK;
        }
    }
    
    // タスククラス
    private static class Task {
        String title;
        String priority;
        
        Task(String title, String priority) {
            this.title = title;
            this.priority = priority;
        }
    }
    
    // テスト用のmainメソッド
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("AI タスク自動作成 - シンプル版");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new SimpleAITaskCreatorPanel());
            frame.setSize(900, 850);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
