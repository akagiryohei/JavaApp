package View.Todo;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ItemListener;


import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.EventListenerList;

import Entity.GanttchartTask;
import Entity.UserList;
import Entity.Enum.LogLevel;
import Interface.Controller.Todo.ITodoGanttchartController;
import Interface.View.Todo.ITodoGanttchartView;
import View.JPanelViewBase;
import View.Dialog.CommonDialogView;
import View.Dialog.CommonDialogView.CommonDialogType;
import View.Dialog.EditProgressRateDialogView;
import View.Dialog.UpdateListDialogView;
import View.Dialog.Listener.EditProgressRateDialogViewListener;
import View.Dialog.Listener.UpdateListDialogViewListener;
import View.Todo.Listener.TodoListViewCommonListener;
import View.Todo.Listener.TodoSideCommonPanelListener;
import View.Ganttchart.GanttchartPanel;
import View.Ganttchart.Listener.GanttchartPanelListener;

public class TodoGanttchartView extends JPanelViewBase implements ITodoGanttchartView, TodoSideCommonPanelListener, ItemListener, ActionListener, GanttchartPanelListener, EditProgressRateDialogViewListener, UpdateListDialogViewListener
{
    // 年月フォーマッター
    private final DateTimeFormatter YearMonthFormatter = DateTimeFormatter.ofPattern("yyyy年MM月");
    // TodoList(リスト型表示)コントローラ
    private ITodoGanttchartController Controller;

    // LeftJPanelのインスタンス
    private TodoSideCommonPanel TodoSideCommonPanel;

    // 共通ダイアログのインスタンス
    private CommonDialogView CommonDialogView;

    private GanttchartPanel GanttchartPanelInstance;

    // 選択中リストID
    private int SelectedListId;

    // 選択中リスト名
    private String SelectedListName;

    // 進捗率編集ダイアログ
    private EditProgressRateDialogView EditProgressRateDialogView;

    // リスト名
    private JLabel ListName;

    // 年月選択プルダウン
    private JComboBox<YearMonth> YearMonthSelectComboBox;

    private Boolean IsUpdating;

    // 選択中タスク
    private GanttchartTask SelectedTask;

    // リスト編集ダイアログ
    private UpdateListDialogView UpdateListDialogView;

    // スクロールリストパネル
    private JScrollPane scrollListPane;

    // イベントリスナインスタンス
    protected EventListenerList ListenerList;

    public TodoGanttchartView(CommonDialogView commonDialogView, EditProgressRateDialogView editProgressRateDialogView, UpdateListDialogView updateListDialogView)
    {
        // ダイアログインスタンスを初期化
        this.CommonDialogView = commonDialogView;
        var panel = new JPanel();
        panel.setBackground(Color.pink);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        this.GanttchartPanelInstance = new GanttchartPanel();
        panel.add(this.GanttchartPanelInstance);
        this.scrollListPane = new JScrollPane(panel);
        this.scrollListPane.setBackground(Color.CYAN);
        this.scrollListPane.setBounds(230,200,757,365);
        this.EditProgressRateDialogView = editProgressRateDialogView;
        this.UpdateListDialogView = updateListDialogView;

        this.ListenerList = new EventListenerList();

        // 現在年月を取得
        this.IsUpdating = false;

        this.setLayout(null);
        this.setBackground(Color.pink);

        // 左要素作成（Panel)
        this.TodoSideCommonPanel = new TodoSideCommonPanel();
        this.TodoSideCommonPanel.setBounds(0,0,230,600);

        // リスト名ラベル
        this.ListName = new JLabel();
        this.ListName.setBounds(304,32,228,32);
        this.ListName.setBorder(BorderFactory.createLineBorder(Color.CYAN));
        this.ListName.setBackground(Color.white);
        this.ListName.setOpaque(true);
        this.add(ListName);

        // 年月選択プルダウン
        this.YearMonthSelectComboBox = new JComboBox<YearMonth>();
        this.YearMonthSelectComboBox.setBounds(304,96,120,32);
        this.YearMonthSelectComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof YearMonth) {
                    setText(((YearMonth) value).format(YearMonthFormatter));
                }
                return this;
            }
        });
        this.add(this.YearMonthSelectComboBox);
        // 左要素追加
        this.add(this.TodoSideCommonPanel);
        // 右要素追加
        this.add(this.scrollListPane);
    }

    /**
     * コントローラインスタンスを設定
     * @param controller コントローラインスタンス
     */
    public void SetController(ITodoGanttchartController controller)
    {
      this.Controller = controller;
    }

    /**
     * ボタンのアクションリスナー
     */
    public void actionPerformed(ActionEvent e)
    {
      switch (e.getActionCommand())
      {
        default:
          break;
      }
    }

    /**
     * コンボボックスからのイベント
     * @param e イベントオブジェクト
     */
    public void itemStateChanged(ItemEvent e)
    {
        if (e.getStateChange() == ItemEvent.SELECTED && !this.IsUpdating)
        {
            ItemSelectable source = e.getItemSelectable();

            if (source == this.YearMonthSelectComboBox)
            {
                YearMonth selectedYearMonth = (YearMonth)e.getItem();
                this.Controller.GetUserTask(this.TodoSideCommonPanel.GetSelectedListId(), this.ListName.getText(), selectedYearMonth);
            }
        }
    }

    public void Show()
    {
      this.Controller.GetUserList();
      this.TodoSideCommonPanel.AddListener(this);
      this.TodoSideCommonPanel.Show();
      this.YearMonthSelectComboBox.addItemListener(this);
      this.GanttchartPanelInstance.AddListener(this);
      this.GanttchartPanelInstance.Show();
      this.EditProgressRateDialogView.AddListener(this);
    }

    public void Hide()
    {
      // あとで処理追加
      this.GanttchartPanelInstance.RemoveListener(this);
      this.GanttchartPanelInstance.Hide();
      this.YearMonthSelectComboBox.removeItemListener(this);
      this.TodoSideCommonPanel.RemoveListener(this);
      this.TodoSideCommonPanel.Hide();
      this.EditProgressRateDialogView.RemoveListener(this);
    }

    /**
     * リスナ対象追加
     * @param listener 追加対象リスナインスタンス
     */
    public void AddListener(TodoListViewCommonListener listener)
    {
      this.ListenerList.add(TodoListViewCommonListener.class, listener);
    }

    /**
     * リスナ対象削除
     * @param listener 削除対象リスナインスタンス
     */
    public void RemoveListener(TodoListViewCommonListener listener)
    {
      this.ListenerList.remove(TodoListViewCommonListener.class, listener);
    }

    /**
     * ユーザリストクリック時の処理
     * @param listId 選択対象のリストID
     * @param listName 選択対象のリスト名
     */
    public void UserListButtonClicked(int listId, String listName)
    {
      this.Controller.GetUserTask(listId, listName, YearMonth.now());
    }

    /**
     * ボードボタンクリック時の処理
     */
    public void BoardButtonClicked()
    {
      for (TodoListViewCommonListener listener : this.ListenerList.getListeners(TodoListViewCommonListener.class))
      {
        // ボードボタン押下を通知
        listener.BoardButtonClicked();
      }
    }

    /**
     * リストボタンクリック時の処理　現在リスト画面であるかを確認する必要がある
     */
    public void ListButtonClicked()
    {
      for (TodoListViewCommonListener listener : this.ListenerList.getListeners(TodoListViewCommonListener.class))
      {
        // リストボタン押下を通知
        listener.ListButtonClicked();
      }
    }

    /**
     * ガントチャートボタンクリック時の処理
     */
    public void GanttchartButtonClicked()
    {
      for (TodoListViewCommonListener listener : this.ListenerList.getListeners(TodoListViewCommonListener.class))
      {
        // ガントチャートボタン押下を通知
        listener.GanttchartButtonClicked();
      }
    }

    /**
     * AIリスト・タスク案作成画面ボタンクリック時の処理
     */
    public void AICreateListTaskButtonClicked() {
        for (TodoListViewCommonListener listener : this.ListenerList.getListeners(TodoListViewCommonListener.class)) {
        // AIリスト・タスク案作成ボタン押下を通知
        listener.AICreateListTaskButtonClicked();
        }
    }

    /**
     * タスク進捗率ボタンが押下処理
     */
    @Override
    public void OnTaskProgressRateClicked(GanttchartTask selectedTask) {
      // 進捗率ダイアログを表示
      this.ProgressRateDialog(selectedTask);
    }

    /**
     * 新規作成のリストを画面に反映
     * @param list
     */
    public void SetList(List<UserList> list)
    {
      this.TodoSideCommonPanel.SetList(list);
    }

    /**
     * リスト作成メソッド
     * @param listText
     */
    public void CreateUserList(String listText)
    {
      this.Controller.CreateUserList(listText);
    }

    // リスト名設定失敗ダイアログ表示
    public void ListLengthFailureDialog()
    {
      this.CommonDialogView.Show(CommonDialogType.ListLengthFailureDialog, true);
    }
    // リスト削除失敗ダイアログ表示
    public void ListDeleteFailureDialog()
    {
      this.CommonDialogView.Show(CommonDialogType.ListDeleteFailureDialog, true);
    }

    // リスト更新失敗ダイアログ表示
    public void ListUpdateFailureDialog()
    {
      this.CommonDialogView.Show(CommonDialogType.ListUpdateFailureDialog, true);
    }

    // タスク削除失敗ダイアログ表示
    public void TaskDeleteFailureDialog()
    {
      this.CommonDialogView.Show(CommonDialogType.TaskDeleteFailureDialog, true);
    }

    // リスト情報取得失敗ダイアログ表示
    public void GetListFailureDialog()
    {
      this.CommonDialogView.Show(CommonDialogType.GetUserListFailureDialog, true);
    }

    // タスク情報取得失敗ダイアログ表示
    public void GetTaskFailureDialog()
    {
      this.CommonDialogView.Show(CommonDialogType.GetTaskFailureDialog, true);
    }

    // タスク更新失敗ダイアログ表示
    public void UpdateTaskFailureDialog()
    {
      this.CommonDialogView.Show(CommonDialogType.UpdateTaskFailureDialog, true);
    }

  /**
   * リストアップデートダイアログ
   */
  public void UpdateListDialog(int listid, String listName)
  {
    this.SelectedListId = listid;
    this.SelectedListName = listName;
    this.UpdateListDialogView.AddListener(this);
    this.UpdateListDialogView.Show(this.SelectedListName);
  }

  /**
   * リスト名入力欄クリア
   */
  public void ClearListNameInput()
  {
    this.TodoSideCommonPanel.ClearListNameInputField();
  }

  /**
   * ログイン中ユーザー名を設定
   * @param userName 画面表示するユーザ名
   */
  public void SetUserName(String userName)
  {
    this.TodoSideCommonPanel.SetUserName(userName);
  }


  /**
   * リスト削除メソッド
   * @param listId 画面の選択中リストID
   */
  public void DeleteListButtonClicked(int listId)
  {
    this.Controller.DeleteList(listId);
  }

  /**
   * リスト更新メソッド
   * @param listId 画面の選択中リストID
   */
  public void UpdateListClicked(String UpdateListName)
  {
    this.UpdateListDialogView.RemoveListener(this);
    this.UpdateListDialogView.Hide();
    this.Controller.UpdateList(this.SelectedListId, UpdateListName);
  }

  /**
   * 新規作成のタスクを画面に反映
   * @param task 選択中リストのタスク一覧
   * @param listName 選択中リスト名
   * @param activeYearMonth 選択中年月
   * @param today 本日の日付
   */
  public void SetTask(List<GanttchartTask> userTask, String listName, YearMonth activeYearMonth, LocalDate today)
  {
    // これでfor文を回して値を渡していく
    this.ListName.setText(listName);
    this.IsUpdating = true;
    this.YearMonthSelectComboBox.removeAllItems();
    if (activeYearMonth != null)
    {
      this.GetYearMonthList().forEach(ym -> {
        this.YearMonthSelectComboBox.addItem(ym);
      });
    }
    this.YearMonthSelectComboBox.setSelectedItem(activeYearMonth);
    this.IsUpdating = false;
    this.GanttchartPanelInstance.RenderGanttchartGrid(activeYearMonth, today, userTask);
    //変化通知が飛ばない対策（自身も再レイアウト）
    this.revalidate();
    this.repaint();
  }

  /**
   * ガントチャートパネルの本日表示を更新する
   * @param today 本日の日付
   */
  public void RefreshTodayCell(LocalDate today)
  {
    this.GanttchartPanelInstance.RefreshTodayCell(today);
  }
  
  /**
   * 前後２年分の年月取得メソッド
   */
  public List<YearMonth> GetYearMonthList()
  {
    YearMonth now = YearMonth.now();
    YearMonth start = now.minusYears(2);
    YearMonth end = now.plusYears(2);

    List<YearMonth> yearMonthList = new ArrayList<>();
    YearMonth current = start;
    while (!current.isAfter(end)) {
        yearMonthList.add(current);
        current = current.plusMonths(1);
    }
    return yearMonthList;
  }

  /**
   * 進捗率更新ダイアログ表示
   * @param selectedTask 選択中タスク
   */
  public void ProgressRateDialog(GanttchartTask selectedTask)
  {
    this.SelectedTask = selectedTask;
    this.EditProgressRateDialogView.Show(this.SelectedTask.ProgressRate);
  }

  /*
   * OKボタンクリック時処理
   */
  public void OkButtonClicked(int progressRate) {
    this.Controller.UpdateTask(this.SelectedTask.TaskID, progressRate, this.SelectedTask.ListID, this.ListName.getText(), (YearMonth)this.YearMonthSelectComboBox.getSelectedItem());
    this.EditProgressRateDialogView.Hide();
    this.SelectedTask = null;
  }

  /*
   * キャンセルボタンクリック時処理
   */
  public void CancelButtonClicked() {
    this.EditProgressRateDialogView.Hide();
    this.SelectedTask = null;
  }

  /**
   * 左側パネルの要素押下可否設定メソッド
   * @param isDisabled 押下不可にする場合true、押下可能にする場合false
   */
  public void SideElementDisabled(boolean isDisabled)
  {
    this.TodoSideCommonPanel.ElementDisabled(isDisabled);
    this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "画面要素押下可否設定" + "(" + String.valueOf(isDisabled) + ")");});
  }

  /**
   * 右側パネルの要素押下可否設定メソッド
   * @param isDisabled 押下不可にする場合true、押下可能にする場合false
   */
  public void LeftElementDisabled(boolean isDisabled)
  {
    this.YearMonthSelectComboBox.setEnabled(!isDisabled);
    this.GanttchartPanelInstance.setEnabled(!isDisabled);
    this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "画面要素押下可否設定" + "(" + String.valueOf(isDisabled) + ")");});
  }

}