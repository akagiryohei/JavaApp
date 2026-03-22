package View.Todo.List;

import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.EventListenerList;

import Interface.View.Todo.ITodoListView;
import Entity.UserTask;
import View.JPanelViewBase;
import View.Common.JPlaceholderTextField;
import View.Todo.Listener.TodoListContentPanelListener;
import Entity.Enum.LogLevel;

/*
 * リスト表示のメインパネル
 */
public class TodoListContentPanel extends JPanelViewBase implements ActionListener, DocumentListener {
    // タスク未完了
    private final static Boolean TASK_INCOMPLETE = false;

    // スクロールリストパネル
    private JScrollPane scrollListPane;

    // イベントリスナインスタンス
    protected EventListenerList ListenerList;

    // リストモデル
    private DefaultListModel ListModel;

    // リストリスト
    private List<UserTask> UserTasks;

    // +ボタン
    private JButton PlusButton;

    // 取得したタスク（サマリー無し）
    private List<UserTask> Task;

    // 完了タスク
    private List<UserTask> ConTask;

    // 未完了タスク
    private List<UserTask> IncTask;

    // 未完了タスクパネル
    private JPanel listPanel;

    // 完了済みタスクパネル
    private JPanel completeTaskPanel;

    // 完了タスクリストモデル
    private DefaultListModel<UserTask> ConListModel;

    // 未完了タスクリストモデル
    private DefaultListModel<UserTask> IncListModel;

    // 飛田式デリートボタン
    private JButton[] DeleteButtons;
    // 飛田式編集ボタン
    private JButton[] UpdateButtons;

    // 選択中タスクのID
    private int SelectedTaskId;

    // 期日ボタン
    private JButton PeriodButton;

    // 未完了タスク期日ボタン
    private JButton[] TaskPeriodButton;

    // 開始期日
    private Date InputStartPeriod;

    // 終了期日
    private Date InputEndPeriod;

    // タスク入力欄
    private JPlaceholderTextField NewTaskNameLabel;

    // 未完了チェックボタン
    private JCheckBox[] IncCheckBox;

    // 完了チェックボタン
    private JCheckBox[] ConCheckBox;

    // 上のパネル
    private JPanel upPanel;

    // リスト名ラベル
    private JLabel listNameLabel;

    public TodoListContentPanel() {

        this.IncTask = new ArrayList<UserTask>();
        this.ConTask = new ArrayList<UserTask>();
        this.ConListModel = new DefaultListModel<UserTask>();
        this.IncListModel = new DefaultListModel<UserTask>();

        this.setLayout(null);

        this.ListenerList = new EventListenerList();

        this.InputStartPeriod = new Date(0);
        this.InputEndPeriod = new Date(0);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        this.listNameLabel = new JLabel();

        // 上のPanel作成:サイズ指定在り
        this.upPanel = new JPanel();
        this.upPanel.setLayout(null);
        this.upPanel.setPreferredSize(new Dimension(400, 112));
        this.upPanel.setBackground(Color.LIGHT_GRAY);
        this.upPanel.setBorder(BorderFactory.createLineBorder(Color.CYAN));

        // 下のPanel作成:サイズ指定なし
        JPanel downPanel = new JPanel();
        downPanel.setLayout(new BoxLayout(downPanel, BoxLayout.Y_AXIS));
        downPanel.setBackground(Color.RED);
        downPanel.setBorder(BorderFactory.createLineBorder(Color.GREEN));

        // タスク配置用パネル
        this.listPanel = new JPanel();
        this.listPanel.setLayout(new BoxLayout(this.listPanel, BoxLayout.Y_AXIS));
        this.listPanel.setPreferredSize(new Dimension(400, 272));
        this.listPanel.setBorder(BorderFactory.createLineBorder(Color.YELLOW));

        this.UserTasks = new ArrayList<UserTask>();
        this.ListModel = new DefaultListModel();
        downPanel.add(listPanel);

        // 完了済みラベル配置用パネル
        JPanel compPanel = new JPanel();
        compPanel.setLayout(null);
        compPanel.setPreferredSize(new Dimension(400, 64));
        compPanel.setBorder(BorderFactory.createLineBorder(Color.MAGENTA));

        // 完了済みラベル
        JLabel completeLabel = new JLabel("完了済み");
        completeLabel.setVerticalAlignment(JLabel.TOP); // 垂直位置
        completeLabel.setHorizontalAlignment(JLabel.LEFT); // 水平位置
        completeLabel.setBounds(76, 16, 228, 32);
        completeLabel.setOpaque(true);
        completeLabel.setBackground(Color.CYAN);
        compPanel.add(completeLabel);

        downPanel.add(compPanel);

        // 完了済みタスク配置用パネル
        this.completeTaskPanel = new JPanel();
        this.completeTaskPanel.setLayout(new BoxLayout(this.completeTaskPanel, BoxLayout.Y_AXIS));
        this.completeTaskPanel.setPreferredSize(new Dimension(400, 272));
        this.completeTaskPanel.setBorder(BorderFactory.createLineBorder(Color.ORANGE));
        downPanel.add(completeTaskPanel);

        // 完了済みラベル配置用パネル
        JPanel newTaskPanel = new JPanel();
        newTaskPanel.setLayout(new BorderLayout());
        newTaskPanel.setPreferredSize(new Dimension(400, 64));

        // タスク名、チェックボックス、期日配置パネル
        JPanel addPanel = new JPanel();
        addPanel.setLayout(new BoxLayout(addPanel, BoxLayout.X_AXIS));
        addPanel.setPreferredSize(new Dimension(400, 32));
        addPanel.setBorder(BorderFactory.createLineBorder(Color.PINK));
        // allow horizontal expansion inside BoxLayout
        addPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));

        // +ボタン設定 aakgi:ここに画面
        this.PlusButton = new JButton("＋");
        this.PlusButton.setActionCommand("PlusButton");
        this.PlusButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.PlusButton.setPreferredSize(new Dimension(76, 35));
        // タスク入力欄
        this.NewTaskNameLabel = new JPlaceholderTextField(10);
        this.NewTaskNameLabel.SetPlaceholderText("入力内容");
        this.NewTaskNameLabel.setPreferredSize(new Dimension(608, 32));
        this.NewTaskNameLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        // 期日ボタン
        this.PeriodButton = new JButton("期日");
        this.PeriodButton.setPreferredSize(new Dimension(76, 32));
        this.PeriodButton.setActionCommand("PeriodButton");

        addPanel.add(this.PlusButton);
        addPanel.add(this.NewTaskNameLabel);
        addPanel.add(this.PeriodButton);
        // place addPanel in center so it expands horizontally
        newTaskPanel.add(addPanel, BorderLayout.CENTER);
        downPanel.add(newTaskPanel);

        mainPanel.add(upPanel);
        mainPanel.add(downPanel);

        // scrollパネルの用意
        this.scrollListPane = new JScrollPane(mainPanel);
        this.scrollListPane.setBackground(Color.CYAN);
        this.scrollListPane.setBounds(0, 0, 757, 565);
        this.add(scrollListPane);

        this.DeleteButtons = new JButton[0];

    }

    public void Show() {
        this.PlusButton.addActionListener(this);
        this.PeriodButton.addActionListener(this);
        this.PlusButtonDisabled(true);
        this.NewTaskNameLabel.getDocument().addDocumentListener(this);
    }

    public void Hide() {
        this.PlusButton.removeActionListener(this);
        this.PeriodButton.removeActionListener(this);
        this.NewTaskNameLabel.getDocument().removeDocumentListener(this);
        this.Clear();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("PlusButton")) {
            // このボタンは画面に１つしか存在しない。
            this.PlusButtonClicked();
        } else if (e.getActionCommand().equals("PeriodButton")) {
            this.PeriodButtonClicked();
        } else {
            // 以下のボタンは複数存在するためindex値によって参照
            var command = e.getActionCommand().split("_");
            var index = Integer.parseInt(command[1]);

            switch (command[0]) {
                case "DeleteButton":
                    this.DeleteButtonClicked(index);
                    break;
                case "UpdateButton":
                    this.UpdateButtonClicked(index);
                    break;
                case "IncCheckBox":
                    this.IncCheckBoxClicked(index);
                    break;
                case "ConCheckBox":
                    this.ConCheckBoxClicked(index);
                    break;
                case "PeriodButton":
                    this.EditPeriodButtonClicked(index);
                    break;
                default:
                    // ロジック上あり得ない
                    break;
            }
        }
    }

    /**
     * タスク登録処理
     */
    public void PlusButtonClicked() {
        String taskText = this.NewTaskNameLabel.getText();
        for (TodoListContentPanelListener listener : this.ListenerList.getListeners(TodoListContentPanelListener.class)) {
            listener.CreateUserTask(taskText, this.InputStartPeriod, this.InputEndPeriod);
        }
    }

    /**
     * 期日登録ボタン
     */
    public void PeriodButtonClicked() {
        for (TodoListContentPanelListener listener : this.ListenerList.getListeners(TodoListContentPanelListener.class)) {
            listener.PeriodButtonDialog();
        }
    }

    /**
     * 新規作成のタスクを画面に反映
     * 
     * @param task
     */
    public void SetTask(List<UserTask> task, String listName) {
        this.Task = task;
        for (int i = 0; i < this.DeleteButtons.length; i++) {
            if (this.DeleteButtons[i] != null)
            {
                this.DeleteButtons[i].removeActionListener(this);
                this.DeleteButtons[i] = null;
            }
        }

        this.SortTask(this.Task);
        this.ConListModel.clear();
        this.IncListModel.clear();
        this.listPanel.removeAll();
        this.completeTaskPanel.removeAll();
        this.InputStartPeriod = new Date(0);
        this.InputEndPeriod = new Date(0);
        this.NewTaskNameLabel.setText("");
        // リスト名ラベルセット
        this.SetTaskLabel(listName);
        // 未完了のリスト作成
        this.CreateIncTaskList();
        // 完了のリスト作成
        this.CreateConTaskList();
        // 変化通知が飛ばない対策（自身も再レイアウト）
        this.revalidate();
        this.repaint();
    }

    /**
     * 画面の初期化
     */
    public void Clear()
    {
        // リスト名ラベルクリア
        this.listNameLabel.setText("");
        //削除ボタンのリスト解放
        for (int i = 0; i < this.DeleteButtons.length; i++) {
            if (this.DeleteButtons[i] != null)
            {
                this.DeleteButtons[i].removeActionListener(this);
                this.DeleteButtons[i] = null;
            }
        }
        // 完了タスクのリストをクリアする
        this.ConListModel.clear();
        // 未完了タスクのリストをクリアする
        this.IncListModel.clear();
        // リストパネルをクリアする
        this.listPanel.removeAll();
        // 完了済みタスクパネルをクリアする
        this.completeTaskPanel.removeAll();
        // タスクテキスト入力欄クリア
        this.NewTaskNameLabel.setText("");
        // 画面を更新する
        this.revalidate();
        this.repaint();
    }

    /**
     * リスト名ラベルセットメソッド
     */
    public void SetTaskLabel(String listName) {
        // リスト名ラベル
        this.listNameLabel.setText(listName);
        this.listNameLabel.setVerticalAlignment(JLabel.TOP); // 垂直位置
        this.listNameLabel.setHorizontalAlignment(JLabel.LEFT); // 水平位置
        this.listNameLabel.setBounds(76, 32, 228, 32);
        this.listNameLabel.setOpaque(true);
        this.listNameLabel.setBackground(Color.CYAN);
        this.upPanel.add(this.listNameLabel);
    }

    /**
     * 取得タスクを完了と未完了に分ける
     * 
     * @param task
     */
    public void SortTask(List<UserTask> task) {
        this.IncTask.clear();
        this.ConTask.clear();
        task.forEach(taskItem -> {
            if (taskItem.taskStatus == TASK_INCOMPLETE) {
                this.IncTask.add(taskItem);
            } else {
                this.ConTask.add(taskItem);
            }
        });
    }

    /**
     * 未完了のリスト表示作成
     */
    public void CreateIncTaskList() {
        this.IncCheckBox = new JCheckBox[this.IncTask.size()];
        this.DeleteButtons = new JButton[this.IncTask.size()];
        this.UpdateButtons = new JButton[this.IncTask.size()];
        this.TaskPeriodButton = new JButton[this.IncTask.size()];
        for (int i = 0; i < this.IncTask.size(); i++) {
            // タスク名、チェックボックス、期日配置パネル
            JPanel taskPanel = new JPanel();
            taskPanel.setLayout(new BoxLayout(taskPanel, BoxLayout.X_AXIS));
            taskPanel.setPreferredSize(new Dimension(400, 32));
            // チェックボックス作成
            this.IncCheckBox[i] = new JCheckBox();
            this.IncCheckBox[i].setPreferredSize(new Dimension(76, 32));
            this.IncCheckBox[i].setSelected(false);
            this.IncCheckBox[i].setActionCommand("IncCheckBox_" + i);
            this.IncCheckBox[i].addActionListener(this);
            // タスク名表示欄
            JLabel taskNameLabel = new JLabel(this.IncTask.get(i).taskText);
            taskNameLabel.setPreferredSize(new Dimension(608, 32));
            // 期日ボタン
            this.TaskPeriodButton[i] = new JButton(
                    "開始" + this.IncTask.get(i).startDate + " ： 終了" + this.IncTask.get(i).endDate);
            this.TaskPeriodButton[i].setPreferredSize(new Dimension(170, 32));
            this.TaskPeriodButton[i].setActionCommand("PeriodButton_" + i);
            this.TaskPeriodButton[i].addActionListener(this);
            // 削除ボタン
            this.DeleteButtons[i] = new JButton("削除");
            this.DeleteButtons[i].setPreferredSize(new Dimension(76, 32));
            this.DeleteButtons[i].setActionCommand("DeleteButton_" + i);
            this.DeleteButtons[i].addActionListener(this);
            this.UpdateButtons[i] = new JButton("更新");
            this.UpdateButtons[i].setPreferredSize(new Dimension(76, 32));
            this.UpdateButtons[i].setActionCommand("UpdateButton_" + i);
            this.UpdateButtons[i].addActionListener(this);

            taskPanel.add(this.IncCheckBox[i]);
            taskPanel.add(taskNameLabel);
            taskPanel.add(this.TaskPeriodButton[i]);
            taskPanel.add(this.DeleteButtons[i]);
            taskPanel.add(this.UpdateButtons[i]);
            this.listPanel.add(taskPanel);
        }
    }

    /**
     * 完了のリスト表示作成
     */
    public void CreateConTaskList() {
        this.ConCheckBox = new JCheckBox[this.ConTask.size()];
        for (int i = 0; i < this.ConTask.size(); i++) {
            // タスク名、チェックボックス、期日配置パネル
            JPanel taskPanel = new JPanel();
            taskPanel.setLayout(new BoxLayout(taskPanel, BoxLayout.X_AXIS));
            taskPanel.setPreferredSize(new Dimension(400, 32));
            // チェックボックス作成
            this.ConCheckBox[i] = new JCheckBox();
            this.ConCheckBox[i].setPreferredSize(new Dimension(76, 32));
            this.ConCheckBox[i].setSelected(true);
            this.ConCheckBox[i].setActionCommand("ConCheckBox_" + i);
            this.ConCheckBox[i].addActionListener(this);
            // タスク名表示欄
            JLabel taskNameLabel = new JLabel(this.ConTask.get(i).taskText);
            taskNameLabel.setPreferredSize(new Dimension(684, 32));
            taskPanel.add(this.ConCheckBox[i]);
            taskPanel.add(taskNameLabel);
            completeTaskPanel.add(taskPanel);
        }
    }

    /**
     * デリートボタン押下時処理
     */
    public void DeleteButtonClicked(int index) {
        System.out.println(index + "目のボタンが押された");
        // 押下されたタスクのリストが必要となる
        for (TodoListContentPanelListener listener : this.ListenerList.getListeners(TodoListContentPanelListener.class)) {
            listener.DeleteTaskClicked(this.IncTask.get(index).id);
        }
    }

    /**
     * 編集ボタン押下時処理
     */
    public void UpdateButtonClicked(int index) {
        System.out.println(index + "更新ボタンが押下された");
        for (TodoListContentPanelListener listener : this.ListenerList.getListeners(TodoListContentPanelListener.class)) {
            listener.UpdateTaskDialog(this.IncTask.get(index).id, this.IncTask.get(index).taskText);
        }
    }

    /**
     * チェックボタン押下時処理
     */
    public void IncCheckBoxClicked(int index) {
        System.out.println(index + "チェックボタンが押下された");
        for (TodoListContentPanelListener listener : this.ListenerList.getListeners(TodoListContentPanelListener.class)) {
            listener.CheckBoxClicked(this.IncTask.get(index).id, this.IncCheckBox[index].isSelected());
        }
    }

    /**
     * チェックボタン押下時処理
     */
    public void ConCheckBoxClicked(int index) {
        System.out.println(index + "チェックボタンが押下された");
        for (TodoListContentPanelListener listener : this.ListenerList.getListeners(TodoListContentPanelListener.class)) {
            listener.CheckBoxClicked(this.ConTask.get(index).id, this.ConCheckBox[index].isSelected());
        }
    }

    /**
     * 期日押下時処理
     */
    public void EditPeriodButtonClicked(int index) {
        System.out.println(index + "期日が押下された");
        for (TodoListContentPanelListener listener : this.ListenerList.getListeners(TodoListContentPanelListener.class)) {
            listener.PeriodButtonDialog(this.IncTask.get(index).id, this.IncTask.get(index).startDate, this.IncTask.get(index).endDate);
        }
    }

    /**
     * 期日セットメソッド
     */
    public void SetPeriodDate(Date startDate, Date endDate) {
        this.InputStartPeriod = startDate;
        this.InputEndPeriod = endDate;
    }

    /**
     * タスク入力欄に入力されたとき
     * 
     * @param e
     */
    @Override
    public void insertUpdate(DocumentEvent e) {
        this.ChangeTextField(e);
    }

    /**
     * タスク入力欄から削除されたとき
     * 
     * @param e
     */
    @Override
    public void removeUpdate(DocumentEvent e) {
        this.ChangeTextField(e);
    }

    /**
     * タスク入力欄の監視
     * 
     * @param isDisabled 無効化フラグ
     */
    public void PlusButtonDisabled(boolean isDisabled) {
        this.WithLogger((logger) -> {
            logger.WriteLog(LogLevel.Info, "＋ボタン押下可否設定" + "(" + String.valueOf(!isDisabled) + ")");
        });
        this.PlusButton.setEnabled(!isDisabled);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
    }

    /**
     * タスク入力欄の監視
     */
    public void ChangeTextField(DocumentEvent e) {
        if (e.getDocument() == this.NewTaskNameLabel.getDocument()) {
            this.WithLogger((logger) -> {
                logger.WriteLog(LogLevel.Info, "タスク入力欄の変化検知");
            });
            for (TodoListContentPanelListener listener : this.ListenerList.getListeners(TodoListContentPanelListener.class)) {
                listener.ChangeTextField(this.NewTaskNameLabel.getText());
            }
        } else {
            this.WithLogger((logger) -> {
                logger.WriteLog(LogLevel.Warning, "タスク入力欄以外の変化検知");
            });
        }
    }

    /**
     * Controllerで渡す：isBusyで使う
     * 
     * @param isBusy 処理中かどうか
     */
    public void ElementDisabled(boolean isDisabled) {
        this.WithLogger((logger) -> {
            logger.WriteLog(LogLevel.Info, "画面要素押下可否設定" + "(" + String.valueOf(isDisabled) + ")");
        });
        this.PlusButton.setEnabled(isDisabled);
        this.PeriodButton.setEnabled(isDisabled);
        if (this.TaskPeriodButton.length > 0) {
            for (int i = 0; i < this.TaskPeriodButton.length; i++) {
                this.TaskPeriodButton[i].setEnabled(isDisabled);
            }
        }
        this.NewTaskNameLabel.setEnabled(isDisabled);
        if (this.IncCheckBox.length > 0) {
            for (int i = 0; i < this.IncCheckBox.length; i++) {
                this.IncCheckBox[i].setEnabled(isDisabled);
            }
        }
        if (this.ConCheckBox.length > 0) {
            for (int i = 0; i < this.ConCheckBox.length; i++) {
                this.ConCheckBox[i].setEnabled(isDisabled);
            }
        }
    }

    /**
     * リスナ対象追加
     * @param listener 追加するリスナ
     */
    public void AddListener(TodoListContentPanelListener listener)
    {
        this.ListenerList.add(TodoListContentPanelListener.class, listener);
    }

    /**
     * リスナ対象削除
     * @param listener 削除するリスナ
     */
    public void RemoveListener(TodoListContentPanelListener listener)
    {
        this.ListenerList.remove(TodoListContentPanelListener.class, listener);
    }
}
