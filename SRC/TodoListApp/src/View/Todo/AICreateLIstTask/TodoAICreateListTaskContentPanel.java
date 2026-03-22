package View.Todo.AICreateLIstTask;

import View.JPanelViewBase;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;

import Interface.View.Todo.ITodoAICreateListTaskView;

import java.awt.event.*;
import View.Todo.Listener.TodoAICreateListTaskContentPanelListener;
import View.Todo.Listener.TodoSideBoardPanelListener;
import javax.swing.event.EventListenerList;
import Entity.Enum.LogLevel;
import java.util.List;
import Entity.AIListTask;



/**
 * AIでTODOリストを作成するパネル(右画面)
 */
public class TodoAICreateListTaskContentPanel extends JPanelViewBase implements ActionListener
{
    /** スクロールパネル */
    private JScrollPane ScrollPanel;
    /** 指示文言のテキストエリア */
    private JTextArea InstructionTextArea;
    /** タスク生成ボタン */
    private JButton CreateTaskButton;
    /** タスクの再生成ボタン */
    private JButton RefineTaskButton;
    /** 追加指示文言のテキストエリア */
    private JTextArea AddInstructionTextArea;
    /** リストタスク追加ボタン */
    private JButton AddListTaskButton;
    /** リスト名入力フィールド */
    private JTextField ListNameTextField;
    /** リスト名編集ボタン */
    private JButton EditListNameButton;
    /** タスクリストパネル */
    private JPanel TaskListPanel;
    /** タスクカウントラベル */
    private JLabel TaskCountLabel;
    /** リスト名パネル */
    private JPanel ListNamePanel;
    /** プレビューパネル（下画面のメイン）*/
    private JPanel PreviewPanel;
    /** 削除ボタン */
    private JButton[] RemoveButton;
    /** イベントリスナインスタンス */
    protected EventListenerList ListenerList;



    /**
     * コンストラクタ
     */
    public TodoAICreateListTaskContentPanel()
    {
        /**
         * setLayout:配置のルール
         */
        setLayout(null);
        this.ListenerList = new EventListenerList();
        this.InitComponents();
        this.LayoutComponents();
    }

    /**
     * 表示メソッド
     */
    public void Show()
    {
        // イベントの起動処理
        this.CreateTaskButton.addActionListener(this);
        this.RefineTaskButton.addActionListener(this);
        this.AddListTaskButton.addActionListener(this);
        this.EditListNameButton.addActionListener(this);
    }

    /**
     * 非表示メソッド
     */
    public void Hide()
    {
        // イベントの停止処理
        this.CreateTaskButton.removeActionListener(this);
        this.RefineTaskButton.removeActionListener(this);
        this.AddListTaskButton.removeActionListener(this);
        this.EditListNameButton.removeActionListener(this);
        this.Clear();
    }

    /**
     * 初期化&宣言メソッド
     */
    public void InitComponents()
    {
        // 指示内容のテキストエリア(6行40文字)
        this.InstructionTextArea = new JTextArea(6,40);
        this.InstructionTextArea.setLineWrap(true); // 折り返し設定
        this.InstructionTextArea.setWrapStyleWord(true); // 単語単位で折り返し
        this.InstructionTextArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200,200,200)), // 外枠の線
            BorderFactory.createEmptyBorder(10,10,10,10) // 内側の余白
        ));

        // タスク生成ボタン
        this.CreateTaskButton = this.CreateStyleButton("AIによるタスク生成", new Color(102, 126, 234), Color.WHITE);
        this.CreateTaskButton.setActionCommand("CreateListTaskButton");

        //リスト名入力フィールド
        this.ListNameTextField = new JTextField();
        this.ListNameTextField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200,200,200)), // 外枠の線
            BorderFactory.createEmptyBorder(10,10,10,10) // 内側の余白
        ));

        // リスト名編集ボタン(これは大きさが違うため独自のスタイルで宣言する)
        // TODO:おそらくsampleのほうでは環境依存系文字を使ってるためSVGに置き換えて表示する必要性がある
        this.EditListNameButton = new JButton("適用");
        this.EditListNameButton.setBackground(new Color(102, 126, 234));
        this.EditListNameButton.setForeground(Color.WHITE);
        this.EditListNameButton.setFocusPainted(false); // フォーカスの点線を消す
        this.EditListNameButton.setBorderPainted(false);
        this.EditListNameButton.setCursor(new Cursor(Cursor.HAND_CURSOR));// ホバー時のカーソルを変更
        this.EditListNameButton.setToolTipText("リスト名を編集");
        this.EditListNameButton.setActionCommand("EditListNameButton");

        // 追加指示のテキストエリア(3行40文字)
        this.AddInstructionTextArea = new JTextArea(3,40);
        this.AddInstructionTextArea.setLineWrap(true); // 折り返し設定
        this.AddInstructionTextArea.setWrapStyleWord(true); // 単語単位で折り返し
        this.AddInstructionTextArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200,200,200)), // 外枠の線
            BorderFactory.createEmptyBorder(10,10,10,10) // 内側の余白
        ));

        // タスク再生成ボタン
        this.RefineTaskButton = this.CreateStyleButton("リストとタスクの再生成", Color.RED, Color.WHITE);
        this.RefineTaskButton.setActionCommand("RefineTaskButton");

        // リストタスク追加ボタン(TODOリスト追加ボタン)
        this.AddListTaskButton = this.CreateStyleButton("TODOリストに追加", new Color(39, 174, 96), Color.WHITE);
        this.AddListTaskButton.setActionCommand("AddListTaskButton");

        // タスクリストパネル
        this.TaskListPanel = new JPanel();
        this.TaskListPanel.setLayout(new BoxLayout(this.TaskListPanel, BoxLayout.Y_AXIS)); // タスクを縦に並べる
        this.TaskListPanel.setBackground(new Color(240,240,245));

        // タスクカウントラベル
        this.TaskCountLabel = new JLabel("0件");
        this.TaskCountLabel.setForeground(Color.WHITE);// 文字色
        this.TaskCountLabel.setOpaque(true); // 背景を有効にする
        this.TaskCountLabel.setBackground(new Color(102,126,234)); // 背景色
        this.TaskCountLabel.setBorder(BorderFactory.createEmptyBorder(4,12,4,12)); // 余白

        // リスト名パネル
        this.ListNamePanel = new JPanel(new BorderLayout(10,10));
        this.ListNamePanel.setBackground(new Color(237,233,254));
        this.ListNamePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(196,181,253), 2),//枠線の色と太さ
            BorderFactory.createEmptyBorder(15,15,15,15)// 内側の余白
        ));
        this.ListNamePanel.setVisible(false);// 初期状態では非表示

        // プレビューパネル（下画面のメイン）
        this.PreviewPanel = new JPanel(new BorderLayout(10,10));
        this.PreviewPanel.setBackground(new Color(240, 240,245));
        this.PreviewPanel.setVisible(false);// 初期状態では非表示

        // 削除ボタン
        this.RemoveButton = new JButton[0];
    }

    /**
     * コンポーネントの配置メソッド
     */
    private void LayoutComponents()
    {
        // 入力セクション
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS)); // 縦に並べる
        inputPanel.setBackground(new Color(248,249,250));// ちょい灰色の背景色
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230,230,230), 1), // 枠線の色と太さ
            BorderFactory.createEmptyBorder(20,20,20,20) // 内側の余白
        ));

        // 指示内容
        JPanel instructionPanel = this.CreateFormGroup("指示内容", new JScrollPane(this.InstructionTextArea));
        inputPanel.add(instructionPanel);
        inputPanel.add(Box.createVerticalStrut(15)); // セクション間のスペース

        // 生成ボタンパネル
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false); // 背景を透明にする
        buttonPanel.add(this.CreateTaskButton);
        inputPanel.add(buttonPanel);

        // リスト名セクション
        JLabel listNameLabel = new JLabel("リスト名");
        listNameLabel.setForeground(new Color(85,85,85));

        // リスト名入力と編集ボタンを同じ行に配置するためのパネル
        JPanel listNameInputPanel = new JPanel(new BorderLayout(10,0));
        listNameInputPanel.setOpaque(false); // 背景を透明にする
        listNameInputPanel.add(this.ListNameTextField, BorderLayout.CENTER);
        listNameInputPanel.add(this.EditListNameButton, BorderLayout.EAST);

        this.ListNamePanel.add(listNameLabel, BorderLayout.NORTH);
        this.ListNamePanel.add(Box.createVerticalStrut(8)); // ラベルと入力の間にスペースを追加
        this.ListNamePanel.add(listNameInputPanel, BorderLayout.CENTER);

        // プレビューセクションのheader
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240,240,245));
        JLabel titleLabel = new JLabel("生成されたタスク");
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(this.TaskCountLabel, BorderLayout.EAST);

        // タスクリストのscrollペイン
        JScrollPane taskScrollPane = new JScrollPane(this.TaskListPanel);
        taskScrollPane.setPreferredSize(new Dimension(0, 300)); // 高さを300pxに固定
        taskScrollPane.setBorder(BorderFactory.createLineBorder(new Color(200,200,200), 1)); // 枠線の色と太さ

        // 追加指示セクション
        JPanel refinementPanel = this.CreateFormGroup("タスクへの追加指示（任意）", new JScrollPane(this.AddInstructionTextArea));

        // ボタンパネル
        JPanel actionButtonPanel =new JPanel(new GridLayout(1,2,10,0));
        actionButtonPanel.setOpaque(false); // 背景を透明にする
        actionButtonPanel.add(this.RefineTaskButton);
        actionButtonPanel.add(this.AddListTaskButton);


        // プレビューパネルに追加する
        JPanel previewContentPanel =new JPanel();
        previewContentPanel.setLayout(new BoxLayout(previewContentPanel, BoxLayout.Y_AXIS));
        previewContentPanel.setOpaque(false); // 背景を透明にする

        // リスト名パネルを配置
        this.ListNamePanel.setAlignmentX(Component.LEFT_ALIGNMENT); // 左寄せ
        previewContentPanel.add(this.ListNamePanel);
        previewContentPanel.add(Box.createVerticalStrut(15)); // セクション間のスペース

        // タスクセクションを配置
        JPanel taskSection = new JPanel(new BorderLayout(10,10));
        taskSection.setOpaque(false); // 背景を透明にする
        taskSection.add(headerPanel, BorderLayout.NORTH);
        taskSection.add(taskScrollPane, BorderLayout.CENTER);
        previewContentPanel.add(taskSection);

        previewContentPanel.add(Box.createVerticalStrut(15));

        // 追加指示とボタンを配置
        refinementPanel.setAlignmentX(Component.LEFT_ALIGNMENT); // 左寄せ
        previewContentPanel.add(refinementPanel);
        previewContentPanel.add(Box.createVerticalStrut(10));
        actionButtonPanel.setAlignmentX(Component.LEFT_ALIGNMENT); // 左寄せ
        previewContentPanel.add(actionButtonPanel);

        this.PreviewPanel.add(previewContentPanel, BorderLayout.NORTH); // 上寄せで配置

        // メインパネルに追加
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false); // 背景を透明にする

        JLabel mainTitle = new JLabel("AIタスク自動作成", SwingConstants.CENTER);// コンポーネントを中央寄せ
        mainTitle.setBorder(BorderFactory.createEmptyBorder(0,0,20,0)); // タイトルと入力セクションのスペース

        topPanel.add(mainTitle, BorderLayout.NORTH);
        topPanel.add(inputPanel, BorderLayout.CENTER);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(topPanel);
        mainPanel.add(this.PreviewPanel);

        this.ScrollPanel = new JScrollPane(mainPanel);
        this.ScrollPanel.setBackground(Color.CYAN);
        this.ScrollPanel.setBounds(0,0,757,565);
        this.add(this.ScrollPanel);
    }

    /**
     * ボタンのスタイルとエフェクト
     */
    private JButton CreateStyleButton(String text, Color bgColor, Color fgColor)
    {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false); // フォーカスの点線を消す
        button.setBorderPainted(false);
        button.setOpaque(true); // 背景色を有効にする
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));// ホバー時のカーソルを変更

        // ホバーエフェクト
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                // ボタンが有効な場合のみ色を変える
                if (button.isEnabled())
                {
                    button.setBackground(bgColor.darker());
                }
            }
            // マウスが離れたときに元の色に戻す
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    /**
     * 指示文言入力フォーム作成メソッド
     */
    private JPanel CreateFormGroup(String labelText, JComponent component)
    {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false); // 背景を透明にする
        panel.setAlignmentX(Component.CENTER_ALIGNMENT); // パネル自体を中央揃え

        JLabel label = new JLabel(labelText);
        label.setForeground(new Color(85,85,85));
        label.setAlignmentX(Component.LEFT_ALIGNMENT); // 左寄せ

        component.setAlignmentX(Component.LEFT_ALIGNMENT); // 左寄せ
        
        panel.add(label);
        panel.add(Box.createVerticalStrut(8)); // ラベルとコンポーネントの間にスペースを追加
        panel.add(component);

        return panel;
    }

    /**
     * ボタンからのアクションリスナー
     */
    public void actionPerformed(ActionEvent e)
    {
        if (e.getActionCommand().equals("CreateListTaskButton")) {
            this.CreateListTask();
        } else if (e.getActionCommand().equals("RefineTaskButton")) {
            this.RefineListTask();
        } else if (e.getActionCommand().equals("AddListTaskButton")) {
            this.AddListTask();
        } else if (e.getActionCommand().equals("EditListNameButton")) {
            this.EditListName();
        } else {
            var command = e.getActionCommand().split("_");
            var index = Integer.parseInt(command[1]);

            switch (command[0]) {
                case "RemoveButton":
                    this.RemoveButtonClicked(index);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * リストタスク生成メソッド
     */
    public void CreateListTask()
    {
        System.out.println("リストタスク生成ボタンが押されました");
        String userInput = this.InstructionTextArea.getText().trim();
        if (userInput.isEmpty())
        {
            // TODO: ダイアログを呼ぶ
            System.out.println("指示文言が空です");
            return;
        }
        else
        {
            for (TodoAICreateListTaskContentPanelListener listener : this.ListenerList.getListeners(TodoAICreateListTaskContentPanelListener.class))
                {
                    listener.Ask(userInput);
                }
        }
    }

    /**
     * リストタスク再生成メソッド
     */
    public void RefineListTask()
    {
        System.out.println("リストタスク再生成ボタンが押されました");
        // 指示文言の取得
        String userInput = this.InstructionTextArea.getText().trim();
        // 追加文言の取得
        String addUserInput = this.AddInstructionTextArea.getText().trim();
        // 指示文言か、追加文言が空のときはError
        if (userInput.isEmpty() || addUserInput.isEmpty())
        {
            // TODO: ダイアログを呼ぶ
            System.out.println("指示文言か、追加文言が空です");
            return;
        }
        else
        {
            for (TodoAICreateListTaskContentPanelListener listener : this.ListenerList.getListeners(TodoAICreateListTaskContentPanelListener.class))
            {
                listener.ReAsk(userInput, addUserInput);
            }
        }
    }

    /**
     * リストとタスクの登録メソッド
     */
    public void AddListTask()
    {
        System.out.println("リストとタスクの登録ボタンが押されました");
        // TODO:リストとタスクの登録処理
        for (TodoAICreateListTaskContentPanelListener listener : this.ListenerList.getListeners(TodoAICreateListTaskContentPanelListener.class)) {
            listener.AddListTask();
        }
    }

    /**
     * リスト名編集メソッド(Model保持のリスト名を更新する
     */
    public void EditListName()
    {
        System.out.println("リスト名編集ボタンが押されました");
        String userEditListName = this.ListNameTextField.getText();
        // TODO:編集されたリスト名をModelに反映する処理を作成する
        for (TodoAICreateListTaskContentPanelListener listener : this.ListenerList.getListeners(TodoAICreateListTaskContentPanelListener.class)) {
            listener.EditAIList(userEditListName);
        }
    }

    /**
     * リスナ対象追加
     * @param listener 追加対象リスナインスタンス
     */
    public void AddListener(TodoSideBoardPanelListener listener)
    {
        this.ListenerList.add(TodoSideBoardPanelListener.class, listener);
    }

    /**
     * リスナ対象削除
     * @param listener 削除対象リスナインスタンス
     */
    public void RemoveListener(TodoSideBoardPanelListener listener)
    {
        this.ListenerList.remove(TodoSideBoardPanelListener.class, listener);
    }

    /**
     * リスナ対象追加（メイン画面）
     * @param listener 追加対象リスナインスタンス
     */
    public void MainAddListener(TodoAICreateListTaskContentPanelListener listener)
    {
        this.ListenerList.add(TodoAICreateListTaskContentPanelListener.class, listener);
    }

    /**
     * リスナ対象削除（メイン画面）
     * @param listener 削除対象リスナインスタンス
     */
    public void MainRemoveListener(TodoAICreateListTaskContentPanelListener listener)
    {
        this.ListenerList.remove(TodoAICreateListTaskContentPanelListener.class, listener);
    }

    /**
     * AI作成のリスト・タスク案を画面に反映
     * @param aIListTask AIが作成したリスト・タスク案
     */
    public void SetAIListTask(AIListTask aIListTask)
    {
        System.out.println("AI作成のリスト・タスク案を画面に反映します");
        // 画面のリスト・タスク案表示処理
        //リスト名のセット
        this.ListNameTextField.setText(aIListTask.listName);
        this.ListNamePanel.setVisible(true);
        this.PreviewPanel.setVisible(true);

        // タスクのセット
        this.DisplayTasks(aIListTask.tasks);
    }

    /**
     * タスクのセットメソッド
     */
    public void DisplayTasks(List<String> tasks)
    {
        // 前の削除ボタンのアクションリスナ解放
        for (int i = 0; i < this.RemoveButton.length; i++)
        {
            if (this.RemoveButton[i] != null)
            {
                this.RemoveButton[i].removeActionListener(this);
                this.RemoveButton[i] = null;
            }
        }
                // 削除ボタンの初期化
        this.RemoveButton = new JButton[tasks.size()];

        // タスクのセット
        // TODO:TaskListPanelの中身があるかどうかを確認すること
        if (this.TaskListPanel.getComponentCount() > 0) {
            this.TaskListPanel.removeAll();
        }
        this.TaskCountLabel.setText(tasks.size() + "件");

        // タスクパネルの作成
        for (int index = 0; index < tasks.size(); index++)
        {
            this.TaskListPanel.add(this.CreateTaskItem(tasks.get(index), index));
        }
        // 画面を更新する
        this.TaskListPanel.revalidate();
        this.TaskListPanel.repaint();
    }

    /**
     * タスクアイテムの作成メソッド
     * @param taskText タスク名
     * @param index タスクインデックス
     */
    public JPanel CreateTaskItem(String taskText, int index)
    {
        JPanel panel = new JPanel(new BorderLayout(10,10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200,200,200),2),
            BorderFactory.createEmptyBorder(12,12,12,12)
        ));

        // タスク内容
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        // タスク名ラベル
        JLabel taskNameLabel = new JLabel(taskText);
        taskNameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        contentPanel.add(taskNameLabel);

        // 削除ボタン
        this.RemoveButton[index] = new JButton("削除");
        this.RemoveButton[index].setBackground(new Color(231, 76, 60));
        this.RemoveButton[index].setForeground(Color.WHITE);
        this.RemoveButton[index].setFocusPainted(false);
        this.RemoveButton[index].setBorderPainted(false);
        this.RemoveButton[index].setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.RemoveButton[index].setActionCommand("RemoveButton_" +  index);
        this.RemoveButton[index].addActionListener(this);

        panel.add(contentPanel, BorderLayout.CENTER);
        panel.add(this.RemoveButton[index], BorderLayout.EAST);

        return panel;
    }

    /**
     * 削除ボタン押下時処理
     */
    public void RemoveButtonClicked(int index)
    {
        System.out.println("押下された削除ボタンのindexは" + index);
        for (TodoAICreateListTaskContentPanelListener listener : this.ListenerList.getListeners(TodoAICreateListTaskContentPanelListener.class)) {
            listener.EditAITaskList(index);
        }
    }

    /**
     * 画面の初期化（登録完了後）
     */
    public void Clear()
    {
        // 指示文言クリア
        this.InstructionTextArea.setText("");
        // リスト名入力欄クリア
        this.ListNameTextField.setText("");
        // 追加文言クリア
        this.AddInstructionTextArea.setText("");
        // タスクカウントのリセット
        this.TaskCountLabel.setText("0件");
        // 前の削除ボタンのアクションリスナ解放
        for (int i = 0; i < this.RemoveButton.length; i++)
        {
            if (this.RemoveButton[i] != null)
            {
                this.RemoveButton[i].removeActionListener(this);
                this.RemoveButton[i] = null;
            }
        }
        // タスクリストパネル内の削除
        if (this.TaskListPanel.getComponentCount() > 0) {
            this.TaskListPanel.removeAll();
        }
        // リスト名パネル非活性
        this.ListNamePanel.setVisible(false);
        // プレビューパネル非活性
        this.PreviewPanel.setVisible(false);
        // 画面を更新する
        this.TaskListPanel.revalidate();
        this.TaskListPanel.repaint();
    }

    /**
     * 非活性処理(ControllerのisBusyを使う)
     * @param isBusy 処理中フラグ
     */
    public void ElementDisabled(Boolean isBusy)
    {
        this.WithLogger((logger) -> {
            logger.WriteLog(LogLevel.Info, "画面要素押下可否設定" + "(" + String.valueOf(isBusy) + ")");
        });
        // ボタンの活性非活性
        this.CreateTaskButton.setEnabled(!isBusy);
        this.RefineTaskButton.setEnabled(!isBusy);
        this.AddListTaskButton.setEnabled(!isBusy);
        this.EditListNameButton.setEnabled(!isBusy);
        if (this.RemoveButton.length > 0) {
            for (int i = 0; i < this.RemoveButton.length; i++) {
                this.RemoveButton[i].setEnabled(!isBusy);
            }
        }
        // テキストエリアの活性非活性
        this.InstructionTextArea.setEditable(!isBusy);
        this.AddInstructionTextArea.setEditable(!isBusy);
    }

}
