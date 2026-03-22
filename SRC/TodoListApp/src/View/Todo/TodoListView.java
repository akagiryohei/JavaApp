package View.Todo;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.*;
import javax.swing.event.EventListenerList;

import Entity.UserList;
import Entity.UserTask;
import Interface.Controller.Todo.ITodoListController;
import Interface.View.Todo.ITodoListView;
// コントローラーimport必要
import View.JPanelViewBase;
import View.Dialog.CommonDialogView.CommonDialogType;
import View.Dialog.Listener.*;
import View.Dialog.*;
import View.Todo.List.*;
import View.Todo.Listener.TodoListContentPanelListener;
import View.Todo.Listener.TodoListViewCommonListener;
import View.Todo.Listener.TodoSideCommonPanelListener;
import Entity.Enum.LogLevel;

/**
 * リスト型表示の画面（左と右の部品を配置する画面）
 */
public class TodoListView extends JPanelViewBase implements ITodoListView, TodoSideCommonPanelListener, ActionListener, EditPeriodDialogViewListener, UpdateListDialogViewListener, UpdateTaskDialogViewListener, TodoListContentPanelListener
{
  // TodoList(リスト型表示)コントローラ
  public ITodoListController Controller;

  // LeftJPanelのインスタンス
  private TodoSideCommonPanel TodoSideCommonPanel;

  // RightJPanelのインスタンス
  private TodoListContentPanel TodoListContentPanel;

  // 選択中リストID
  private int SelectedListId;

  // 選択中タスクID
  private int SelectedTaskId;

  // 選択中リスト名
  private String SelectedListName;

  // 選択中タスクテキスト
  private String SelectedTaskText;

  // 期日修正用テキストフィールド（開始日）
  private JFormattedTextField StartDateInputField;

  // 期日修正用テキストフィールド（終了日）
  private JFormattedTextField EndDateInputField;

  // 共通ダイアログのインスタンス
  private CommonDialogView CommonDialogView;

  // 開始日
  private String StartDate;

  // 終了日
  private String EndDate;

  // 期日ダイアログインスタンス
  private EditPeriodDialogView EditPeriodDialogView;

  // リスト編集ダイアログ
  private UpdateListDialogView UpdateListDialogView;

  // タスク編集ダイアログ
  private UpdateTaskDialogView UpdateTaskDialogView;

  // イベントリスナインスタンス
  protected EventListenerList ListenerList;

  /**
   * コンストラクタ
   * @param baseViewInstance 親画面のインスタンス
   */
  public TodoListView(CommonDialogView commonDialogView, EditPeriodDialogView editPeriodDialogView, UpdateListDialogView updateListDialogView, UpdateTaskDialogView updateTaskDialogView)
  {
    this.EditPeriodDialogView = editPeriodDialogView;
    this.UpdateListDialogView = updateListDialogView;
    this.UpdateTaskDialogView = updateTaskDialogView;

    this.ListenerList = new EventListenerList();

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
    this.TodoListContentPanel = new TodoListContentPanel();
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

  /**
   * タスク取得失敗ダイアログ表示
   */
  public void GetTaskFailureDialog()
  {
    this.CommonDialogView.Show(CommonDialogType.GetTaskFailureDialog, true);
  }

  // タスク削除失敗ダイアログ表示
  public void TaskDeleteFailureDialog()
  {
    this.CommonDialogView.Show(CommonDialogType.TaskDeleteFailureDialog, true);
  }

  // タスク更新失敗ダイアログ表示
  public void TaskUpdateFailureDialog()
  {
    this.CommonDialogView.Show(CommonDialogType.TaskUpdateFailureDialog, true);
  }

  /**
   * タスク登録失敗ダイアログ表示
   */
  public void TaskCreateFailureDialog()
  {
    this.CommonDialogView.Show(CommonDialogType.TaskCreateFailureDialog, true);
  }

  /**
   * リストアップデートダイアログ
   */
  // TODO:リストとタスクどちらにも使えるようにする？
  public void UpdateListDialog(int listid, String listName)
  {
    this.SelectedListId = listid;
    this.SelectedListName = listName;
    this.UpdateListDialogView.AddListener(this);
    this.UpdateListDialogView.Show(this.SelectedListName);
  }

  /**
   * タスクアップデートダイアログ
   */
  public void UpdateTaskDialog(int taskId, String taskText)
  {
    this.SelectedTaskId = taskId;
    this.SelectedTaskText = taskText;
    this.UpdateTaskDialogView.AddListener(this);
    this.UpdateTaskDialogView.Show(this.SelectedTaskText);
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
    this.TodoListContentPanel.AddListener(this);
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
    this.TodoListContentPanel.RemoveListener(this);
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
   * @param UpdateListName 画面の編集中リスト名
   */
  public void UpdateListClicked(String UpdateListName)
  {
    this.UpdateListDialogView.RemoveListener(this);
    this.UpdateListDialogView.Hide();
    this.Controller.UpdateList(this.SelectedListId, UpdateListName);
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
    this.TodoListContentPanel.ElementDisabled(isDisabled);
    this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "画面要素押下可否設定" + "(" + String.valueOf(isDisabled) + ")");});
  }

  /**
   * タスク更新メソッド
   * @param  UpdateTaskName 画面の編集中タスク名
   */
  public void UpdateTaskClicked(String UpdateTaskName)
  {
    this.UpdateTaskDialogView.RemoveListener(this);
    this.UpdateTaskDialogView.Hide();
    this.Controller.UpdateTask(this.SelectedTaskId, UpdateTaskName);
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
   * リスト情報取得失敗時ダイアログ表示
   */
  public void ShowGetUserListFailureDialog()
  {
    this.CommonDialogView.Show(CommonDialogType.GetUserListFailureDialog, true);
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

  /**
   * タスク入力欄の監視
   */
  public void ChangeTextField(boolean isEmpty)
  {
    this.TodoListContentPanel.PlusButtonDisabled(isEmpty);
  }

  /**
   * タスク入力欄の監視(cotrollerメソッド呼び出し)
   */
  public void ChangeTextField(String taskText)
  {
    this.Controller.ChangeTextField(taskText);
  }
}
