package View.Todo;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.security.auth.login.LoginContext;
import javax.swing.*;

import Entity.UserList;
import Entity.UserTask;
import Entity.Dialog.Constants;
import Interface.Controller.Todo.ITodoListController;
import Interface.View.IMainWindowView;
import Interface.View.Todo.ITodoListBaseView;
import Interface.View.Todo.ITodoListView;
// コントローラーimport必要
import View.JPanelViewBase;
import View.MainWindowView;
import View.Dialog.CommonDialogView;
import View.Dialog.CommonDialogView.CommonDialogType;
import View.Dialog.Listener.EditPeriodDialogViewListener;
import View.Dialog.EditPeriodDialogView;
import View.Todo.Board.TodoSideBoardPanel;
import View.Todo.List.*;
import View.Todo.Listener.TodoSideCommonPanelListener;
/**
 * リスト型表示の画面（左と右の部品を配置する画面）
 */
public class TodoListView extends JPanelViewBase implements ITodoListView, TodoSideCommonPanelListener, ActionListener, EditPeriodDialogViewListener
{
  // 親画面のインスタンス
  public ITodoListBaseView BaseViewInstance;

  // TodoList(リスト型表示)コントローラ
  public ITodoListController Controller;

  // LeftJPanelのインスタンス
  private TodoSideCommonPanel TodoSideCommonPanel;

  // LeftJPanel（ボード表示時）のインスタンス
  private TodoSideBoardPanel TodoSideBoardPanel;

  // RightJPanelのインスタンス
  private TodoListContentPanel TodoListContentPanel;

  // MainWindowViewのインスタンス
  private IMainWindowView MainWindowViewInstance;

  // ログイン中ユーザーのリスト
  public List<UserList> List;

  // 選択中リストID
  private int SelectedListId;

  // 選択中タスクID
  private int SelectedTaskId;

  // 選択中リスト名
  private String SelectedListName;

  // 選択中タスクテキスト
  private String SelectedTaskText;

  // 選択中リストのタスク
  public  List<UserTask> Task;

  // リスト編集ダイアログ
  JDialog ListDialog;

  // タスク編集ダイアログ
  JDialog TaskDialog;

  // 期日編集ダイアログ
  JDialog PeriodDialog;

  // 修正用テキストフィールド
  JTextField ListNameInputField;

  // 修正用テキストフィールド
  JTextField TaskNameInputField;

  // 期日修正用テキストフィールド（開始日）
  private JFormattedTextField StartDateInputField;
  // private JTextField StartDateInputField;

  // 期日修正用テキストフィールド（終了日）
  private JFormattedTextField EndDateInputField;
  // private JTextField EndDateInputField;

  // 共通ダイアログのインスタンス
  private CommonDialogView CommonDialogView;

  // 開始日
  private String StartDate;

  // 終了日
  private String EndDate;

  // 期日ダイアログインスタンス
  private EditPeriodDialogView EditPeriodDialogView;

  /**
   * コンストラクタ
   * @param baseViewInstance 親画面のインスタンス
   * @param mainWindowViewInstance MainWindowViewのインスタンス
   */
  public TodoListView(ITodoListBaseView baseViewInstance, IMainWindowView mainWindowViewInstance, CommonDialogView commonDialogView, EditPeriodDialogView editPeriodDialogView)
  {
    // TODO : 親画面のインスタンスは型依存ではなくinterface等に修正し依存しないようにする
    // TODO : 親画面のインスタンスをView層で直接持つのは検討の余地あり
    this.BaseViewInstance = baseViewInstance;
    this.MainWindowViewInstance = mainWindowViewInstance;
    this.EditPeriodDialogView = editPeriodDialogView;

    // ダイアログインスタンスを初期化
    this.CommonDialogView = commonDialogView;

    // 一番親のLoginViewにかかってるLayoutマネージャーの向こうかが可能になる
    this.setLayout(null);
    this.setBackground(Color.BLACK);

    // 左要素作成（Panel)
    // ここでこの要素のインスタンスを渡してるから、下のTodoSideCommonPanel内でも操作が可能
    this.TodoSideCommonPanel = new TodoSideCommonPanel();
    this.TodoSideCommonPanel.setBounds(0,0,230,600);

    // 右要素作成（Panel）
    this.TodoListContentPanel = new TodoListContentPanel(this);
    this.TodoListContentPanel.setBounds(230,0,769,600);

    // 左要素追加
    this.add(this.TodoSideCommonPanel);
    // 右要素追加
    this.add(this.TodoListContentPanel);
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
   * 汎用ダイアログ
   * @param dialogName
   * @param operation
   */
  public void Dialog(String dialogName, String operation)
  {
    // JButton openDialogButton = new JButton("ダイアログを開く");
    // openDialogButton.addActionListener(e -> {
    // JDialogの作成(ownerとしてfreameを指定)
    // TODO: キャスト処理は共通ダイアログにすると削除できる
    JDialog dialog = new JDialog((JFrame)this.MainWindowViewInstance, dialogName, true);
    dialog.setSize(400,150);
    dialog.setLayout(null);
    JLabel operationLabel = new JLabel(operation);
    operationLabel.setBounds(20,20,350,70);
    dialog.add(operationLabel);
    dialog.setLocationRelativeTo(null);
    dialog.setVisible(true);
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
   * タスクアップデートダイアログ
   */
  public void UpdateTaskDialog(int taskId, String taskText)
  {
    this.SelectedTaskId = taskId;
    this.SelectedTaskText = taskText;

    // ダイアログ名
    // TODO:キャスト処理は共通ダイアログにすると削除する
    this.TaskDialog = new JDialog((JFrame)this.MainWindowViewInstance, Constants.EDIT_DIALOG, true);
    this.TaskDialog.setSize(400, 256);
    this.TaskDialog.setLayout(null);

    // 指示文言
    JLabel operationLabel = new JLabel(Constants.TASK_EDIT_OPERATION);
    operationLabel.setBounds(76,10,350,35);
    this.TaskDialog.add(operationLabel);

    // Text入力
    this.TaskNameInputField = new JTextField();
    TaskNameInputField.setText(this.SelectedTaskText);
    TaskNameInputField.setBounds(76,50,228,35);
    TaskNameInputField.setColumns(1);
    this.TaskDialog.add(TaskNameInputField);

    // 編集ボタン
    JButton editButton = new JButton("編集");
    editButton.setBounds(90,130,76,35);
    editButton.setActionCommand("EditTaskName");
    editButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    this.TaskDialog.add(editButton);
    editButton.addActionListener(this);
    this.TaskDialog.setLocationRelativeTo(null);
    this.TaskDialog.setVisible(true);
    // OKでもキャンセルでもダイアログを閉じる処理は入れるべき
  }

  /**
   * ボタンからのアクションリスナー
   * ここは特に役目無し
   * ボタンの処理はそれぞれが右、左の配置してるPanelの中でやってる
   */
  public void actionPerformed(ActionEvent e)
  {
    switch (e.getActionCommand())
    {
      case "EditListName":
        System.out.println("EditListNameボタンが押下された");
        this.UpdateListClicked(this.SelectedListId);
        break;
      case "EditTaskName":
        System.out.println("EditTaskNameボタンが押下された");
        this.UpdateTaskClicked(this.SelectedTaskId);
      case "EditPeriodDate":
        System.out.println("EditPeriodDateボタンが押下された");
        this.EditPeriodDateClicked(this.SelectedTaskId);
        break;
      //
      default:
        // ロジック上あり得ない
        break;
    }
  }

  /**
   * 表示メソッド
   */
  public void Show()
  {
    this.Controller.GetUserList();
    this.TodoSideCommonPanel.AddListener(this);
    this.TodoSideCommonPanel.Show();
    this.TodoListContentPanel.Show();
    this.EditPeriodDialogView.AddListener(this);
  }

  /**
   * 非表示メソッド
   */
  public void Hide()
  {
    this.TodoSideCommonPanel.RemoveListener(this);
    this.TodoSideCommonPanel.Hide();
    this.TodoListContentPanel.Hide();
    this.EditPeriodDialogView.RemoveListener(this);
  }
  
  /**
   * コントローラインスタンスを設定
   * @param controller コントローラインスタンス
   */
  public void SetController(ITodoListController controller)
  {
    this.Controller = controller;
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

  /**
   * タスク作成メソッド
   * @param TaskText
   * @param StartDate
   * @param EndDate
   */
  public void CreateUserTask(String TaskText, Date StartDate, Date EndDate)
  {
    this.Controller.CreateUserTask(TaskText, StartDate, EndDate);
  }

  /**
   * リスト名入力欄クリア
   */
  public void ClearListNameInput()
  {
    this.TodoSideCommonPanel.ClearListNameInputField();
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
   * タスク更新メソッド
   * @param taskId 画面の選択中タスクID
   */
  public void UpdateTaskClicked(int taskId)
  {
    String taskText = this.TaskNameInputField.getText();
    this.Controller.UpdateTask(taskId, taskText);
  }

  /**
   * 期日編集メソッド
   */
  public void EditPeriodDateClicked(int taskId)
  {
    // controllerで入力されたフォーマットがあってるか確認するバリデーションを作る
    // タスク登録ジの処理をまねること
    // 手入力でDATEを触らない
    // 渡すときはDATE型で
    Date startDate = (Date)this.StartDateInputField.getValue();
    Date endDate = (Date)this.EndDateInputField.getValue();
    this.Controller.EditPeriodDate(taskId, startDate, endDate);
  }

  /**
   * リスト編集ダイアログ閉
   */
  public void CloseListDialog()
  {
    this.ListDialog.dispose();
  }

  /**
   * タスク編集ダイアログ閉
   */
  public void CloseTaskDialog()
  {
    this.TaskDialog.dispose();
  }

  /**
   * 期日編集ダイアログ閉
   */
  public void ClosePeriodDialog()
  {
    this.PeriodDialog.dispose();
  }

  /**
   * ユーザリストクリック時の処理
   * @param listId 選択対象のリストID
   * @param listName 選択対象のリスト名
   */
  public void UserListButtonClicked(int listId, String listName)
  {
    this.Controller.GetUserTask(listId, listName);
  }

  /**
   * 新規作成のタスクを画面に反映
   * @param task
   */
  public void SetTask(List<UserTask> task, String listName)
  {
    this.Task = task;
    this.TodoListContentPanel.SetTask(task, listName);
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
   * タスク削除ボタン押下時メソッド
   * @param taskid 画面の選択中タスクID
   */
  public void DeleteTaskClicked(int taskid)
  {
    this.Controller.DeleteTask(taskid);
  }

  /**
   * チェックボタン押下時処理
   */
  public void CheckBoxClicked(int taskid, boolean isChecked)
  {
    this.Controller.UpdateTask(taskid, isChecked);
  }

  /**
   * 期日変更ダイアログ
   */
  public void PeriodButtonDialog(int taskId, String startDate, String endDate)
  {
    this.SelectedTaskId = taskId;
    this.StartDate = startDate;
    this.EndDate = endDate;

    this.EditPeriodDialogView.Show(startDate, endDate, true);
  }

  /**
   * 期日登録ダイアログ
   */
  public void PeriodButtonDialog()
  {
    this.EditPeriodDialogView.Show(this.StartDate, this.EndDate, false);
  }

  /**
   * 期日変更ダイアログの開始日を設定
   * @param startDate 開始日
   */
  public void EditPeriodButtonClicked(Date startDate, Date endDate)
  {
    this.Controller.EditPeriodDate(this.SelectedTaskId, startDate, endDate);
  }

  /**
   * 期日セットボタン押下時イベント
   */
  public void SetPeriodButtonClicked(Date startDate, Date endDate)
  {
    this.SetPeriodDate(startDate, endDate);
    this.TodoListContentPanel.SetPeriodDate(startDate, endDate);
  }

  /**
   * Dateから文字列に変換
   * @param startDate 開始日
   * @param endDate 終了日
   */
  private void SetPeriodDate(Date startDate, Date endDate)
  {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    this.StartDate = sdf.format(startDate);
    this.EndDate = sdf.format(endDate);
  }


}
