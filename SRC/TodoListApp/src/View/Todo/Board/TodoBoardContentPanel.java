package View.Todo.Board;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.EventListenerList;

import View.JPanelViewBase;
import View.Todo.TodoListView;
import View.Todo.Listener.TodoBoardContentPanelListener;
import View.Todo.Listener.TodoSideCommonPanelListener;
import Entity.UserList;
import Entity.UserTask;
import Interface.View.Todo.ITodoBoardView;

import java.util.ArrayList;
import java.util.List;

/**
 * ボード型表示の右コンテンツ部分
 */
public class TodoBoardContentPanel extends JPanelViewBase implements ActionListener, MouseListener
{
    // スクロールリストパネル
    private JScrollPane ScrollListPane;

    // イベントリスナインスタンス
    protected EventListenerList ListenerList;

    // リストリスト
    private List<UserList> UserLists;

    // リストパネル
    JPanel ListPanel;

    // 追加パネル
    JPanel AddPanel;

    // タスク名入力欄
    JTextField TaskNameInputField;

    // リスト名ラベル
    JLabel ListNameLabel;

    // タスク名ラベル
    JLabel TaskNameLabel;

    // 完了タスク名ラベル
    JLabel ConTaskNameLabel;

    // 下のPanel
    JPanel downPanel;

    // 完了済みタスクパネル
    JPanel conPanel;

    // 完了済みタスクラベル
    JLabel conLabel;

    // タスク完了
    final static int TASK_COMPLETE = 1;

    // タスク未完了
    final static int TASK_INCOMPLETE = 0;

    // 削除メニュー
    private JMenuItem DeleteMenuItem;

    // 編集メニュー
    private JMenuItem UpdateMenuItem;

    // ポップアップメニュー
    private JPopupMenu Popup;

    // 選択中タイプ
    private String type;

    // 選択中ラベル
    private JLabel label;

    // 選択中アイテムのID
    private int selectItemId;

    // 選択中アイテムのテキスト
    private String selectItemText;

    // 完了タスクのチェックボックス
    private JCheckBox conCheckBox;

    // 未完了タスクのチェックボックス
    private JCheckBox inCheckBox;

    // 親画面インスタンス
    private ITodoBoardView TodoBoardViewInstance;

    // +ボタン
    private JButton[] TaskPlusButton;

    // ClientProperty用のキー（＋ボタン用）
    private final String InputFieldKey = "inputField";

    // ClientProperty用のキー（リスト用）
    private final String UserListKey = "userList";

    // ClientProperty用のキー（タスク用）
    private final String UserTaskKey = "userTask";

    // ClientProperty用のキー（タスクのタイプ）
    private final String Type = "type";

    // ClientProperty用のキー（完了タスク）
    private final String ConTaskKey = "conTask";

    // ClientProperty用のキー（未完了タスク）
    private final String InTaskKey = "inTask";

    // ClientProperty用のキー（リスト）
    private final String List = "list";

    // ClientProperty用のキー（タスク）
    private final String Task = "task";

    // ClientProperty用のキー（完了タスク）
    private final String ConTask = "conUserTask";

    // リスト名入力欄
    private JTextField ListNameInputField;

    // +ボタン
    private JButton ListPlusButton;


    /**
     * コンストラクタ
     */
    public TodoBoardContentPanel(ITodoBoardView todoBoardViewInstance)
    {
        // 親画面インスタンスの保持
        this.TodoBoardViewInstance = todoBoardViewInstance;
        // イベントリスナインスタンスを初期化
        this.ListenerList = new EventListenerList();
        // 親画面インスタンスの保持
        this.setLayout(null);
        // ユーザリストの初期化
        this.UserLists = new ArrayList<UserList>();
        this.ListPanel = new JPanel();
        this.AddPanel = new JPanel();
        this.TaskNameInputField = new JTextField(10);
        this.ListNameLabel = new JLabel("リスト名");
        this.TaskPlusButton = new JButton[0];
        // 全てを包括していれるPanel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // 上のPanel作成:サイズ指定在り
        JPanel upPanel = new JPanel();
        upPanel.setLayout(null);
        // リスト名入力欄設定ListNameInputField
        this.ListNameInputField = new JTextField();
        this.ListNameInputField.setBounds(76,48,228,35);
        this.ListNameInputField.setColumns(1);
        upPanel.add(this.ListNameInputField);

        // +ボタン設定
        this.ListPlusButton = new JButton("＋");
        this.ListPlusButton.setActionCommand("ListPlusButton");
        this.ListPlusButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.ListPlusButton.setBounds(304,48,76,35);
        upPanel.add(this.ListPlusButton);

        // 縦横だけを指定する方法
        upPanel.setPreferredSize(new Dimension(760,160));
        upPanel.setBackground(Color.LIGHT_GRAY);


        /**  完了済みタスクの仮処理 */
        // 下のPanel作成:サイズ指定なし
        this.downPanel = new JPanel();
        this.downPanel.setLayout(new BoxLayout(this.downPanel, BoxLayout.X_AXIS));
        this.downPanel.setBackground(Color.RED);

        // 完了タスクパネルとラベルの初期化
        this.conPanel = new JPanel();
        this.conLabel = new JLabel();

        mainPanel.add(upPanel);
        mainPanel.add(this.downPanel);

        // scrollパネルの用意
        this.ScrollListPane = new JScrollPane(mainPanel);
        this.ScrollListPane.setBackground(Color.CYAN);
        this.ScrollListPane.setBounds(0,0,757,565);
        this.add(ScrollListPane);

        // リスト用とタスク用を作る必要がある？もしくはIDとかフラグを渡して、判別用メソッドを作る？
        this.Popup = new JPopupMenu();
        this.DeleteMenuItem = new JMenuItem();
        this.UpdateMenuItem = new JMenuItem();
        this.DeleteMenuItem.setActionCommand("DeleteButton");
        this.UpdateMenuItem.setActionCommand("UpdateButton");
        this.Popup.add(this.DeleteMenuItem);
        this.Popup.add(this.UpdateMenuItem);
    }
    public void Show()
    {
        this.ListNameLabel.addMouseListener(this);
        this.DeleteMenuItem.addActionListener(this);
        this.UpdateMenuItem.addActionListener(this);
        this.ListPlusButton.addActionListener(this);
    }
    public void Hide()
    {
        this.ListNameLabel.removeMouseListener(this);
        this.DeleteMenuItem.removeActionListener(this);
        this.UpdateMenuItem.removeActionListener(this);
        this.ListPlusButton.removeActionListener(this);
    }

    /**
     * リスナ対象追加
     * @param listener 追加対象リスナインスタンス
     */
    public void AddListener(TodoBoardContentPanelListener listener)
    {
        this.ListenerList.add(TodoBoardContentPanelListener.class, listener);
    }

    /**
     * リスナ対象削除
     * @param listener 削除対象リスナインスタンス
     */
    public void RemoveListener(TodoBoardContentPanelListener listener)
    {
        this.ListenerList.remove(TodoBoardContentPanelListener.class, listener);
    }

    /**
     * ボタンからのアクションリスナー
     */
    public void actionPerformed(ActionEvent e)
    {
        Object src = e.getSource();
        if (src instanceof JCheckBox) {
            JCheckBox checkBox = (JCheckBox) src;
            String type = (String) checkBox.getClientProperty(Type);
            if (ConTaskKey.equals(type)) {
                UserTask conUserTask = (UserTask) checkBox.getClientProperty(ConTask);
                if (conUserTask != null) {
                    int taskId = conUserTask.id;
                    this.ConTaskCheckBoxClicked(taskId);
                }
                // 変更用のメソッドを呼ぶ
            }
            else if (InTaskKey.equals(type)) {
                // 変更用のメソッドを呼ぶ
                UserTask inUserTask = (UserTask) checkBox.getClientProperty(UserTaskKey);
                if (inUserTask != null) {
                    int taskId = inUserTask.id;
                    this.InTaskCheckBoxClicked(taskId);
                }
            }
        }
        else if (e.getActionCommand().equals("DeleteButton"))
        {
            // 画面に1つしか存在しないボタン
            this.DeleteButtonClicked();
        }
        else if (e.getActionCommand().equals("UpdateButton"))
        {
            // 画面に1つしか存在しないボタン
            this.UpdateButtonClicked();
        }
        else if (e.getActionCommand().equals("ListPlusButton"))
        {
            this.ListPlusButtonClicked();
        }
        else
        {
            // 以下のボタンは複数存在するためindex値によって参照
            var command = e.getActionCommand().split("_");
            // TODO:command[1]が数字以外の可能性もあるため、例外処理を入れる
            var taskId = Integer.parseInt(command[1]);
            switch (command[0])
            {
                case "TaskPlusButton":
                    JButton btn = (JButton) e.getSource();
                    JTextField inputField = (JTextField) btn.getClientProperty(InputFieldKey);
                    String taskText = inputField.getText();
                    this.TaskPlusButtonClicked(taskId, taskText);
                    break;
                default:
                    // ロジック上あり得ない
                    break;
            }
        }
    }

    public void mouseReleased(MouseEvent e){
        showPopup(e);
    }

    public void mousePressed(MouseEvent e){
        showPopup(e);
    }

    /**
     * リストクリック時イベント + タスククリック時イベント
     */
    public void mouseClicked(MouseEvent e){
    }

    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}
    private void showPopup(MouseEvent e){
        if (e.isPopupTrigger()) {
            if (e.getSource() instanceof JLabel) {
                this.label = (JLabel) e.getSource();
                this.type = (String) label.getClientProperty(Type);
                switch (type)
                {
                    case List:
                        UserList userListId = (UserList) label.getClientProperty(UserListKey);
                        if (userListId != null)
                        {
                            // 選択中アイテムのIDを設定
                            // ここでlistIdが取得できます
                            this.selectItemId = userListId.id;
                            this.selectItemText = userListId.listName;
                            this.DeleteMenuItem.setText(userListId.listName + "を削除");
                            this.UpdateMenuItem.setText(userListId.listName + "を更新");
                        }
                        break;
                    case Task:
                        UserTask userTaskId = (UserTask) label.getClientProperty(UserTaskKey);
                        if (userTaskId != null)
                        {
                            // 選択中アイテムのIDを設定
                            // ここでtaskIdが取得できます
                            this.selectItemId = userTaskId.id;
                            this.selectItemText = userTaskId.taskText;
                            this.DeleteMenuItem.setText(userTaskId.taskText + "を削除");
                            this.UpdateMenuItem.setText(userTaskId.taskText + "を更新");
                        }
                        break;
                    case ConTaskKey:
                        UserTask conUserTaskId = (UserTask) label.getClientProperty(ConTask);
                        if (conUserTaskId != null)
                        {
                            // 選択中アイテムのIDを設定
                            // ここでconTaskIdが取得できます
                            this.selectItemId = conUserTaskId.id;
                            this.selectItemText = conUserTaskId.taskText;
                            this.DeleteMenuItem.setText(conUserTaskId.taskText + "を削除");
                            this.UpdateMenuItem.setText(conUserTaskId.taskText + "を更新");
                        }
                        break;
                    default:
                        return; // 不明なタイプの場合は何もしない
                }
            }
            Component comp = e.getComponent();
            this.Popup.show(comp, e.getX(), e.getY());
        }
    }

    /**
     * リストを設定
     * @param list ユーザリスト
     */
    public void SetList(List<UserList> list)
    {
        for(int i = 0; i < this.TaskPlusButton.length; i++) {
            this.TaskPlusButton[i].removeActionListener(this);
            this.TaskPlusButton[i].putClientProperty(InputFieldKey, null);
            this.TaskPlusButton[i] = null;
        }
        this.ListNameInputField.setText("");
        this.TaskPlusButton = new JButton[list.size()];
        try {
            this.UserLists.clear();
            this.downPanel.removeAll();
            list.forEach(listItem -> {
                this.UserLists.add(listItem.Clone());
            });
            // 完了タスクを先に構築する
            this.setCompletedTask(this.UserLists);

            // ここで一気にリストを作り上げる(未完了タスク)
            for(int i = 0; i < this.UserLists.size(); i++) {
                // TODO:完了済みタスクを一覧表示するための処理は後で作る
                this.ListPanel = new JPanel();
                this.ListPanel.setLayout(new BoxLayout(this.ListPanel, BoxLayout.Y_AXIS));
                this.ListPanel.setPreferredSize(new Dimension(228, 416));
                this.ListPanel.setAlignmentY(Component.TOP_ALIGNMENT);

                this.AddPanel = new JPanel();
                this.AddPanel.setLayout(new BoxLayout(this.AddPanel, BoxLayout.X_AXIS));
                this.AddPanel.setMaximumSize(new Dimension(228,50));
                this.AddPanel.setPreferredSize(new Dimension(228,50));
                this.AddPanel.setBorder(BorderFactory.createLineBorder(Color.CYAN));
                // タスク名入力欄
                this.TaskNameInputField = new JTextField(10);
                this.TaskNameInputField.setMaximumSize(new Dimension(228, 50));
                this.TaskNameInputField.setPreferredSize(new Dimension(228, 50));
                // +ボタン
                this.TaskPlusButton[i] = new JButton("＋");
                this.TaskPlusButton[i].setMaximumSize(new Dimension(76,50));
                this.TaskPlusButton[i].setPreferredSize(new Dimension(76,50));
                this.TaskPlusButton[i].setActionCommand("TaskPlusButton_" + this.UserLists.get(i).id);
                this.TaskPlusButton[i].putClientProperty(InputFieldKey, this.TaskNameInputField);
                this.TaskPlusButton[i].addActionListener(this);
                this.AddPanel.add(this.TaskNameInputField);
                this.AddPanel.add(this.TaskPlusButton[i]);

                // リスト名ラベル
                this.ListNameLabel = new JLabel(this.UserLists.get(i).listName);
                this.ListNameLabel.putClientProperty(Type, List);
                this.ListNameLabel.putClientProperty(UserListKey, this.UserLists.get(i));
                this.ListNameLabel.setMaximumSize(new Dimension(228, 50));
                this.ListNameLabel.setPreferredSize(new Dimension(228, 50));
                this.ListNameLabel.setBorder(BorderFactory.createLineBorder(Color.GREEN));
                this.ListNameLabel.addMouseListener(this);
                this.ListPanel.add(this.ListNameLabel);
                this.ListPanel.add(this.AddPanel);
                // タスクを設定
                this.setTask(this.UserLists.get(i).tasks);
                this.downPanel.add(this.ListPanel);
            }
            // 再描画
            this.ScrollListPane.revalidate();
            this.ScrollListPane.repaint();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * タスクを設定
     */
    public void setTask(List<UserTask> taskList)
    {
        // レイアウトが崩れる可能性大：機能が充実した後に直します。
        // タスクの回数分だけ回ってるのかを確認する
        for(int x = 0; x < taskList.size(); x++)
        {
            if (taskList.get(x).taskStatus == this.TASK_INCOMPLETE) {
                // タスクが未完了のもののみを表示する
                // タスク名、チェックボックス配置パネル
                JPanel taskPanel = new JPanel();
                taskPanel.setPreferredSize(new Dimension(228,50));
                taskPanel.setLayout(new BoxLayout(taskPanel, BoxLayout.X_AXIS));
                taskPanel.setBorder(BorderFactory.createLineBorder(Color.YELLOW));
                // タスク名表示欄
                this.TaskNameLabel = new JLabel(taskList.get(x).taskText);
                this.TaskNameLabel.putClientProperty(Type, Task);
                this.TaskNameLabel.putClientProperty(UserTaskKey, taskList.get(x));
                this.TaskNameLabel.setMaximumSize(new Dimension(228, 50));
                this.TaskNameLabel.setPreferredSize(new Dimension(228, 50));
                this.TaskNameLabel.setBorder(BorderFactory.createLineBorder(Color.RED));
                this.TaskNameLabel.addMouseListener(this);
                // チェックボックス作成
                this.inCheckBox = new JCheckBox();
                this.inCheckBox.setPreferredSize(new Dimension(76,50));
                this.inCheckBox.setMaximumSize(new Dimension(76, 50));
                this.inCheckBox.setSelected(false);
                this.inCheckBox.putClientProperty(Type, InTaskKey);
                this.inCheckBox.putClientProperty(UserTaskKey, taskList.get(x));
                this.inCheckBox.addActionListener(this);
                taskPanel.add(this.TaskNameLabel);
                taskPanel.add(this.inCheckBox);
                this.ListPanel.add(taskPanel);
            }
        }
    }

    /**
     * 完了タスクを設定
     */
    public void setCompletedTask(List<UserList> list)
    {
        try {
            this.conPanel.removeAll();
            this.conPanel.setLayout(new BoxLayout(this.conPanel, BoxLayout.Y_AXIS));
            this.conPanel.setPreferredSize(new Dimension(228, 416));
            this.conPanel.setAlignmentY(Component.TOP_ALIGNMENT);
            this.conLabel.setText("完了タスク");
            this.conLabel.setMaximumSize(new Dimension(228, 100));
            this.conLabel.setPreferredSize(new Dimension(228, 100));
            this.conLabel.setBorder(BorderFactory.createLineBorder(Color.BLUE));
            this.conPanel.add(this.conLabel);
            // タスク名、チェックボックス配置パネル
            for(int x = 0; x < list.size(); x++)
            {
                for(int y = 0; y < list.get(x).tasks.size(); y++)
                {
                    // 全タスクで完了済みのタスクのみを一括表示する
                    if (list.get(x).tasks.get(y).taskStatus == this.TASK_COMPLETE)
                    {
                        JPanel conTaskPanel = new JPanel();
                        conTaskPanel.setMaximumSize(new Dimension(228,50));
                        conTaskPanel.setPreferredSize(new Dimension(228,50));
                        conTaskPanel.setLayout(new BoxLayout(conTaskPanel, BoxLayout.X_AXIS));
                        conTaskPanel.setBorder(BorderFactory.createLineBorder(Color.YELLOW));
                        // タスク名表示欄
                        this.ConTaskNameLabel = new JLabel(list.get(x).tasks.get(y).taskText);
                        this.ConTaskNameLabel.putClientProperty(Type, ConTaskKey);
                        this.ConTaskNameLabel.putClientProperty(ConTask, list.get(x).tasks.get(y));
                        this.ConTaskNameLabel.setMaximumSize(new Dimension(228, 50));
                        this.ConTaskNameLabel.setPreferredSize(new Dimension(228, 50));
                        this.ConTaskNameLabel.setBorder(BorderFactory.createLineBorder(Color.PINK));
                        this.ConTaskNameLabel.addMouseListener(this);
                        // チェックボックス作成
                        this.conCheckBox = new JCheckBox();
                        this.conCheckBox.setPreferredSize(new Dimension(76,50));
                        this.conCheckBox.setSelected(true);
                        this.conCheckBox.putClientProperty(Type, ConTaskKey);
                        this.conCheckBox.putClientProperty(ConTask, list.get(x).tasks.get(y));
                        this.conCheckBox.addActionListener(this);
                        conTaskPanel.add(this.ConTaskNameLabel);
                        conTaskPanel.add(this.conCheckBox);
                        this.conPanel.add(conTaskPanel);
                    }
                }
            }
            this.conPanel.revalidate();
            this.conPanel.repaint();
            this.downPanel.add(this.conPanel);
        } catch (Exception e) {
            System.out.println("完了タスクの設定に失敗しました: " + e.getMessage());
        }
    }

    /**
     * デリートボタン押下時処理
     */
    private void DeleteButtonClicked()
    {
        // 選択中アイテムのタイプによって処理を分ける
        switch (this.type)
        {
            case List:
                // リスト削除処理
                for (TodoBoardContentPanelListener listener : this.ListenerList.getListeners(TodoBoardContentPanelListener.class))
                {
                    listener.DeleteListButtonClicked(this.selectItemId);
                }
                break;
            case Task:
                // タスク削除処理
                for (TodoBoardContentPanelListener listener : this.ListenerList.getListeners(TodoBoardContentPanelListener.class))
                {
                    listener.DeleteTaskButtonClicked(this.selectItemId);
                }
                break;
            case ConTaskKey:
                // 完了タスク削除処理
                for (TodoBoardContentPanelListener listener : this.ListenerList.getListeners(TodoBoardContentPanelListener.class))
                {
                    listener.DeleteConTaskButtonClicked(this.selectItemId);
                }
                break;
            default:
                return; // 不明なタイプの場合は何もしない
        }
    }

    /**
     * アップデートボタン押下時処理
     */
    private void UpdateButtonClicked()
    {
        // 選択中アイテムのタイプによって処理を分ける
        switch (this.type)
        {
            case List:
                // リスト更新処理
                for (TodoBoardContentPanelListener listener : this.ListenerList.getListeners(TodoBoardContentPanelListener.class))
                {
                    listener.UpdateListDialog(this.selectItemId, this.selectItemText);
                }
                break;
            case Task:
                // タスク更新処理
                for (TodoBoardContentPanelListener listener : this.ListenerList.getListeners(TodoBoardContentPanelListener.class))
                {
                    listener.UpdateTaskDialog(this.selectItemId, this.selectItemText);
                }
                break;
            case ConTaskKey:
                // 完了タスク更新処理
                for (TodoBoardContentPanelListener listener : this.ListenerList.getListeners(TodoBoardContentPanelListener.class))
                {
                    listener.UpdateTaskDialog(this.selectItemId, this.selectItemText);
                }
                break;
            default:
                return; // 不明なタイプの場合は何もしない
        }
    }

    /**
     * チェックボックス押下時処理(完了タスク)
     * @param taskId タスクID
     */
    private void ConTaskCheckBoxClicked(int taskId)
    {
        System.out.println("完了タスクのチェックボタンが押下された");
        this.TodoBoardViewInstance.CheckBoxClicked(taskId, false);
    }

    /**
     * チェックボックス押下時処理(未完了タスク)
     * @param taskId タスクID
     */
    private void InTaskCheckBoxClicked(int taskId)
    {
        System.out.println("未完了タスクのチェックボタンが押下された");
        this.TodoBoardViewInstance.CheckBoxClicked(taskId, true);
    }

    /**
     * タスク追加用＋ボタン押下時処理
     * @param listId リストID
     * @param taskText タスクテキスト
     */
    private void TaskPlusButtonClicked(int listId, String taskText)
    {
        System.out.println("＋ボタンが押下された");
        this.TodoBoardViewInstance.CreateUserTask(listId, taskText);
    }

    /**
     * リスト追加用＋ボタン押下時処理
     */
    private void ListPlusButtonClicked()
    {
        String listText = this.ListNameInputField.getText();
        System.out.println(listText);
        for (TodoBoardContentPanelListener listener : this.ListenerList.getListeners(TodoBoardContentPanelListener.class))
        {
            listener.CreateUserList(listText);
        }
    }
}
