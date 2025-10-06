package View.Todo;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ItemListener;


import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import Entity.GanttchartTask;
import Entity.UserData;
import Entity.UserList;
import Entity.UserTask;
import Entity.Dialog.Constants;
import Entity.Dialog.GanttchartProgress;
import Interface.Controller.Todo.ITodoBoardController;
import Interface.Controller.Todo.ITodoGanttchartController;
import Interface.Controller.Todo.ITodoListController;
import Interface.View.IMainWindowView;
import Interface.View.Todo.ITodoGanttchartView;
import Interface.View.Todo.ITodoListBaseView;
import View.JPanelViewBase;
import View.Dialog.CommonDialogView;
import View.Dialog.CommonDialogView.CommonDialogType;
import View.Dialog.EditProgressRateDialogView;
import View.Dialog.Listener.EditProgressRateDialogViewListener;
import View.Todo.Board.TodoSideBoardPanel;
import View.Todo.List.TodoGanttchartContentPanel;
import View.Todo.Listener.TodoSideCommonPanelListener;
import View.Ganttchart.GanttchartPanel;
import View.Ganttchart.Listener.GanttchartPanelListener;

public class TodoGanttchartView extends JPanelViewBase implements ITodoGanttchartView, TodoSideCommonPanelListener, ItemListener, ActionListener, GanttchartPanelListener, EditProgressRateDialogViewListener{

    // 親画面のインスタンス
    public ITodoListBaseView BaseViewInstance;

    // TodoList(リスト型表示)コントローラ
    private ITodoGanttchartController Controller;

    // LeftJPanelのインスタンス
    private TodoSideCommonPanel TodoSideCommonPanel;

    // LeftJPanel（ボード表示時）のインスタンス
    private TodoSideBoardPanel TodoSideBoardPanel;

    // RightJPanelのインスタンス
    private TodoGanttchartContentPanel TodoGanttchartContentPanel;

    // 左部分の表示
    private JPanel TodoSidePanel;

    // 右部分の表示
    private JPanel TodoContentPanel;

    // ガントチャートテーブル
    private JTable GanttchartTable;

    // ダイアログスクロールビュー
    private JScrollPane GanttchartScrollPanel;

    // 共通ダイアログのインスタンス
    private CommonDialogView CommonDialogView;

    // 進捗率ダイアログのインスタンス
    private GanttchartProgress GanttchartProgress;

    private GanttchartPanel GanttchartPanelInstance;

    // リストデータ
    private List<UserList> List;

    // 選択中リストID
    private int SelectedListId;

    // 選択中リスト名
    private String SelectedListName;

    // 選択中リストのタスク
    public  List<UserTask> Task;

    // リスト編集ダイアログ
    JDialog ListDialog;

    // 進捗率編集ダイアログ
    EditProgressRateDialogView EditProgressRateDialogView;

    // 修正用テキストフィールド
    JTextField ListNameInputField;

    // MainWindowViewのインスタンス
    private IMainWindowView MainWindowViewInstance;

    // 現在年月
    private YearMonth CurrentYearMonth;

    // リスト名
    private JLabel ListName;

    // 年月選択プルダウン
    private JComboBox<YearMonth> YearMonthSelectComboBox;

    private final DateTimeFormatter YearMonthFormatter = DateTimeFormatter.ofPattern("yyyy年MM月");

    private Boolean IsUpdating;

    // 選択中タスク
    private GanttchartTask SelectedTask;

    public TodoGanttchartView(ITodoListBaseView baseViewInstance, IMainWindowView mainWindowViewInstance, CommonDialogView commonDialogView, EditProgressRateDialogView editProgressRateDialogView)
    {
        this.BaseViewInstance = baseViewInstance;

        // ダイアログインスタンスを初期化
        this.CommonDialogView = commonDialogView;
        this.GanttchartPanelInstance = new GanttchartPanel();
        this.GanttchartPanelInstance.SetLocation(230, 200);
        this.MainWindowViewInstance = mainWindowViewInstance;
        this.EditProgressRateDialogView = editProgressRateDialogView;

        // 現在年月を取得
        this.CurrentYearMonth = YearMonth.now();
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
                  // TODO: viewのメンバ変数を見たいけど
                    setText(((YearMonth) value).format(YearMonthFormatter));
                }
                return this;
            }
        });
        this.add(this.YearMonthSelectComboBox);
        // 左要素追加
        this.add(this.TodoSideCommonPanel);
        // 右要素追加
        this.add(this.GanttchartPanelInstance);
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
        case "EditListName":
          System.out.println("EditListNameボタンが押下された");
          this.UpdateListClicked(this.SelectedListId);
          break;
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
      this.BaseViewInstance.ChangeView(TodoListBaseView.ViewType.TodoBoardView);
    }

    /**
     * リストボタンクリック時の処理　現在リスト画面であるかを確認する必要がある
     */
    public void ListButtonClicked()
    {
      this.BaseViewInstance.ChangeView(TodoListBaseView.ViewType.TodoListView);
    }

    /**
     * ガントチャートボタンクリック時の処理
     */
    public void GanttchartButtonClicked()
    {
      this.BaseViewInstance.ChangeView(TodoListBaseView.ViewType.TodoGanttchartView);
    }

    /**
     * リマインダーダイアログを表示
     */
    public void ReminderDialogView(List<ArrayList<String>> reminderList, UserData loginedUserData)
    {
      //this.GanttchartProgress.Show(progress, true);
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
      this.List = list;
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

  /**
   * リストアップデートダイアログ
   */
  // TODO:リストとタスクどちらにも使えるようにする？
  public void UpdateListDialog(int listid, String listName)
  {
    this.SelectedListId = listid;
    this.SelectedListName = listName;

    // ダイアログ名
    // TODO: キャスト処理は共通ダイアログにすると削除できる
    this.ListDialog = new JDialog((JFrame)this.MainWindowViewInstance, Constants.EDIT_DIALOG, true);
    this.ListDialog.setSize(400,256);
    this.ListDialog.setLayout(null);

    // 指示文言
    JLabel operationLabel = new JLabel(Constants.TASK_EDIT_OPERATION);
    operationLabel.setBounds(76,10,350,35);
    this.ListDialog.add(operationLabel);

    // Text入力
    this.ListNameInputField = new JTextField();
    ListNameInputField.setText(this.SelectedListName);
    ListNameInputField.setBounds(76,50,228,35);
    ListNameInputField.setColumns(1);
    this.ListDialog.add(ListNameInputField);

    // 編集ボタン
    JButton editButton = new JButton("編集");
    editButton.setBounds(90,130,76,35);
    editButton.setActionCommand("EditListName");
    editButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    this.ListDialog.add(editButton);
    editButton.addActionListener(this);
    this.ListDialog.setLocationRelativeTo(null);
    this.ListDialog.setVisible(true);
    // OKでもキャンセルでもダイアログを閉じる処理は入れるべき
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
  public void UpdateListClicked(int listId)
  {
    String listName = this.ListNameInputField.getText();
    this.Controller.UpdateList(listId, listName);
  }

  /**
   * リスト編集ダイアログ閉
   */
  public void CloseListDialog()
  {
    this.ListDialog.dispose();
  }

  /**
   * 新規作成のタスクを画面に反映
   * @param task 選択中リストのタスク一覧
   * @param listName 選択中リスト名
   * @param activeYearMonth 選択中年月
   */
  public void SetTask(List<GanttchartTask> userTask, String listName, YearMonth activeYearMonth)
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
    this.GanttchartPanelInstance.RenderGanttchartGrid(activeYearMonth, userTask);
    //変化通知が飛ばない対策（自身も再レイアウト）
    this.revalidate();
    this.repaint();
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

}