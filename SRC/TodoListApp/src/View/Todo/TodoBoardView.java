package View.Todo;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.EventListenerList;

import Entity.UserList;
import Entity.Enum.LogLevel;
import Interface.Controller.Todo.ITodoBoardController;
import Interface.View.Todo.ITodoBoardView;
import View.JPanelViewBase;
import View.Dialog.CommonDialogView;
import View.Dialog.CommonDialogView.CommonDialogType;
import View.Dialog.EditPeriodDialogView;
import View.Dialog.UpdateListDialogView;
import View.Dialog.UpdateTaskDialogView;
import View.Dialog.Listener.UpdateListDialogViewListener;
import View.Dialog.Listener.UpdateTaskDialogViewListener;
import View.Todo.Board.TodoBoardContentPanel;
import View.Todo.Board.TodoSideBoardPanel;
import View.Todo.Listener.TodoBoardContentPanelListener;
import View.Todo.Listener.TodoListViewCommonListener;
import View.Todo.Listener.TodoSideBoardPanelListener;

import java.util.List;

/**
 * ボード型表示の画面（左と右の部品を配置する画面）
 */
public class TodoBoardView extends JPanelViewBase implements ITodoBoardView, ActionListener,
    TodoBoardContentPanelListener, UpdateListDialogViewListener, UpdateTaskDialogViewListener, TodoSideBoardPanelListener {
  // TodoList(リスト型表示)コントローラ
  private ITodoBoardController Controller;

  // 左のPanelのインスタンス
  private TodoSideBoardPanel TodoSideBoardPanel;

  // 右のPanelのインスタンス
  private TodoBoardContentPanel TodoBoardContentPanel;

  // 共通ダイアログのインスタンス
  private CommonDialogView CommonDialogView;

  // 選択中リストID
  private int SelectedListId;

  // 選択中タスクID
  private int SelectedTaskId;

  // 選択中リスト名
  private String SelectedListName;

  // 選択中タスクテキスト
  private String SelectedTaskText;

  // 修正用テキストフィールド
  private JTextField TaskNameInputField;

  // イベントリスナインスタンス
  protected EventListenerList ListenerList;

  // リスト編集ダイアログ
  private UpdateListDialogView UpdateListDialogView;

  // タスク編集ダイアログ
  private UpdateTaskDialogView UpdateTaskDialogView;

  /**
   * コンストラクタ
   * 
   * @param baseViewInstance ログイン後画面のインスタンス
   */
  public TodoBoardView(CommonDialogView commonDialogView,UpdateListDialogView updateListDialogView, UpdateTaskDialogView updateTaskDialogView) {
    this.UpdateListDialogView = updateListDialogView;
    this.UpdateTaskDialogView = updateTaskDialogView;
    // ダイアログインスタンスを初期化
    this.CommonDialogView = commonDialogView;

    // レイアウトマネージャー解除
    this.setLayout(null);
    this.setBackground(Color.RED);

    this.ListenerList = new EventListenerList();

    // 左要素作成（Panel)
    // ここでこの要素のインスタンスを渡してるから、下のTodoSideCommonPanel内でも操作が可能
    this.TodoSideBoardPanel = new TodoSideBoardPanel();
    this.TodoSideBoardPanel.setBounds(0, 0, 230, 600);

    // 右要素作成（Panel）
    this.TodoBoardContentPanel = new TodoBoardContentPanel(this);
    this.TodoBoardContentPanel.setBounds(230, 0, 769, 600);

    // 左要素追加
    this.add(this.TodoSideBoardPanel);
    // 右要素追加
    this.add(this.TodoBoardContentPanel);

  }

  /**
   * ボードボタンクリック時の処理
   */
  public void actionPerformed(ActionEvent e) {
    switch (e.getActionCommand()) {
      default:
        break;
    }
  }

  public void Show() {
    this.Controller.GetUserListAndTask();
    this.TodoSideBoardPanel.Show();
    this.TodoBoardContentPanel.AddListener(this);
    this.TodoBoardContentPanel.Show();
    this.TodoBoardContentPanel.AddListener(this);
    this.TodoSideBoardPanel.AddListener(this);
  }

  public void Hide()
  {
    this.TodoSideBoardPanel.Hide();
    this.TodoBoardContentPanel.RemoveListener(this);
    this.TodoBoardContentPanel.Hide();
    this.TodoBoardContentPanel.RemoveListener(this);
    this.TodoSideBoardPanel.RemoveListener(this);
  }
  
  /**
   * コントローラインスタンスを設定
   * 
   * @param controller コントローラインスタンス
   */
  public void SetController(ITodoBoardController controller)
  {
    this.Controller = controller;
  }

  /**
   * リスナ対象追加
   * 
   * @param listener 追加対象リスナインスタンス
   */
  public void AddListener(TodoListViewCommonListener listener) {
    this.ListenerList.add(TodoListViewCommonListener.class, listener);
  }

  /**
   * リスナ対象削除
   * 
   * @param listener 削除対象リスナインスタンス
   */
  public void RemoveListener(TodoListViewCommonListener listener) {
    this.ListenerList.remove(TodoListViewCommonListener.class, listener);
  }

  /**
   * ボードボタンクリック時の処理
   */
  public void BoardButtonClicked() {
    for (TodoListViewCommonListener listener : this.ListenerList.getListeners(TodoListViewCommonListener.class)) {
      // ボードボタン押下を通知
      listener.BoardButtonClicked();
    }
  }

  /**
   * リストボタンクリック時の処理
   */
  public void ListButtonClicked() {
    for (TodoListViewCommonListener listener : this.ListenerList.getListeners(TodoListViewCommonListener.class)) {
      // リストボタン押下を通知
      listener.ListButtonClicked();
    }
  }

  /**
   * ガントチャートボタンクリック時の処理
   */
  public void GanttchartButtonClicked() {
    for (TodoListViewCommonListener listener : this.ListenerList.getListeners(TodoListViewCommonListener.class)) {
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
   * 新規作成のリストを画面に反映
   * 
   * @param list
   */
  public void SetListTask(List<UserList> list) {
    this.TodoBoardContentPanel.SetList(list);
  }

  /**
   * リスト削除メソッド
   * 
   * @param listId 画面の選択中リストID
   */
  public void DeleteListButtonClicked(int listId) {
    this.Controller.DeleteList(listId);
  }

  /**
   * タスク削除メソッド
   * 
   * @param listId 画面の選択中リストID
   */
  public void DeleteTaskButtonClicked(int listId) {
    this.Controller.DeleteTask(listId);
  }

  /**
   * 完了タスク削除メソッド
   * 
   * @param listId 画面の選択中リストID
   */
  public void DeleteConTaskButtonClicked(int listId) {
    this.Controller.DeleteTask(listId);
  }

  // リスト名設定失敗ダイアログ表示
  public void ListLengthFailureDialog() {
    this.CommonDialogView.Show(CommonDialogType.ListLengthFailureDialog, true);
  }

  // リスト削除失敗ダイアログ表示
  public void ListDeleteFailureDialog() {
    this.CommonDialogView.Show(CommonDialogType.ListDeleteFailureDialog, true);
  }

  // リスト更新失敗ダイアログ表示
  public void ListUpdateFailureDialog() {
    this.CommonDialogView.Show(CommonDialogType.ListUpdateFailureDialog, true);
  }

  // タスク削除失敗ダイアログ表示
  public void TaskDeleteFailureDialog() {
    this.CommonDialogView.Show(CommonDialogType.TaskDeleteFailureDialog, true);
  }

  // タスク更新失敗ダイアログ表示
  public void TaskUpdateFailureDialog()
  {
    this.CommonDialogView.Show(CommonDialogType.TaskUpdateFailureDialog, true);
  }

  // タスク登録失敗ダイアログ表示
  public void TaskCreateFailureDialog() {
    this.CommonDialogView.Show(CommonDialogType.TaskCreateFailureDialog, true);
  }

  // リストおよびタスク取得失敗時ダイアログ表示
  public void GetListAndTaskFailureDialog() {
    this.CommonDialogView.Show(CommonDialogType.GetListAndTaskFailureDialog, true);
  }
  
  /**
   * リスト更新メソッド
   * 
   * @param UpdateListName 画面の編集中リスト名
   */
  public void UpdateListClicked(String UpdateListName) {
    this.UpdateListDialogView.RemoveListener(this);
    this.UpdateListDialogView.Hide();
    this.Controller.UpdateList(this.SelectedListId, UpdateListName);
  }

  /**
   * タスク更新メソッド
   * 
   * @param UpdateTaskName 画面の編集中タスク名
   */
  public void UpdateTaskClicked(String UpdateTaskName) {
    this.UpdateTaskDialogView.RemoveListener(this);
    this.UpdateTaskDialogView.Hide();
    this.Controller.UpdateTask(this.SelectedTaskId, UpdateTaskName);
  }

  /**
   * リストアップデートダイアログ
   */
  // TODO:リストとタスクどちらにも使えるようにする？
  public void UpdateListDialog(int listid, String listName) {
    this.SelectedListId = listid;
    this.SelectedListName = listName;
    this.UpdateListDialogView.AddListener(this);
    this.UpdateListDialogView.Show(this.SelectedListName);
  }

  /**
   * タスクアップデートダイアログ
   */
  public void UpdateTaskDialog(int taskId, String taskText) {
    this.SelectedTaskId = taskId;
    this.SelectedTaskText = taskText;
    this.UpdateTaskDialogView.AddListener(this);
    this.UpdateTaskDialogView.Show(this.SelectedTaskText);
  }

  /**
   * チェックボタン押下時処理
   * 
   * @param taskid    タスクID
   * @param isChecked チェック状態
   */
  public void CheckBoxClicked(int taskid, boolean isChecked) {
    this.Controller.UpdateTask(taskid, isChecked);
  }

  /**
   * タスク登録処理
   * 
   * @param listId   リストID
   * @param taskText タスクテキスト
   */
  public void CreateUserTask(int listId, String taskText) {
    this.Controller.CreateUserTask(listId, taskText);
  }

  /**
   * リスト作成メソッド
   * 
   * @param listText
   */
  public void CreateUserList(String listText) {
    this.Controller.CreateUserList(listText);
  }

  /**
   * 左側パネルの要素押下可否設定メソッド
   * @param isDisabled 押下不可にする場合true、押下可能にする場合false
   */
  public void SideElementDisabled(boolean isDisabled)
  {
    this.TodoSideBoardPanel.ElementDisabled(isDisabled);
    this.WithLogger((logger) -> {
      logger.WriteLog(LogLevel.Info, "画面要素押下可否設定" + "(" + String.valueOf(isDisabled) + ")");
    });
  }

  /**
   * 右側パネルの要素押下可否設定メソッド
   * @param isDisabled 押下不可にする場合true、押下可能にする場合false
   */
  public void LeftElementDisabled(boolean isDisabled)
  {
    this.TodoBoardContentPanel.ElementDisabled(isDisabled);
    this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "画面要素押下可否設定" + "(" + String.valueOf(isDisabled) + ")");});
  }

}
