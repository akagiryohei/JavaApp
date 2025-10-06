package View.Todo;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import Controller.Todo.TodoBoardController;
import Entity.UserList;
import Entity.UserTask;
import Entity.Dialog.Constants;
import Interface.Controller.Todo.ITodoBoardController;
import Interface.View.IMainWindowView;
import Interface.View.Todo.ITodoBoardView;
import Interface.View.Todo.ITodoListBaseView;
import View.JPanelViewBase;
import View.Dialog.CommonDialogView;
import View.Dialog.CommonDialogView.CommonDialogType;
import View.Dialog.EditPeriodDialogView;
import View.Todo.Board.TodoBoardContentPanel;
import View.Todo.Board.TodoSideBoardPanel;
import View.Todo.List.*;
import View.Todo.Listener.TodoBoardContentPanelListener;
import View.Todo.Listener.TodoSideCommonPanelListener;

import java.util.List;



/**
 * ボード型表示の画面（左と右の部品を配置する画面）
 */
public class TodoBoardView extends JPanelViewBase implements ITodoBoardView, ActionListener, TodoBoardContentPanelListener
{
    // 親画面のインスタンス
    public ITodoListBaseView BaseViewInstance;

    // TodoList(リスト型表示)コントローラ
    private ITodoBoardController Controller;

    // 左のPanelのインスタンス
    private TodoSideBoardPanel TodoSideBoardPanel;

    // 右のPanelのインスタンス
    private TodoBoardContentPanel TodoBoardContentPanel;

    // ログイン中ユーザーのリスト
    public List<UserList> List;

    // ログイン中ユーザーのタスク
    public List<UserTask> Task;

    // 共通ダイアログのインスタンス
    private CommonDialogView CommonDialogView;

    // MainWindowViewのインスタンス
    private IMainWindowView MainWindowViewInstance;

    // 期日ダイアログインスタンス
    private EditPeriodDialogView EditPeriodDialogView;

    // 選択中リストID
    private int SelectedListId;

    // 選択中タスクID
    private int SelectedTaskId;

    // 選択中リスト名
    private String SelectedListName;

    // 選択中タスクテキスト
    private String SelectedTaskText;

    // リスト編集ダイアログ
    JDialog ListDialog;

    // 修正用テキストフィールド
    JTextField ListNameInputField;

    // タスク編集ダイアログ
    JDialog TaskDialog;

    // 修正用テキストフィールド
    JTextField TaskNameInputField;


    /**
     * コンストラクタ
     * @param baseViewInstance ログイン後画面のインスタンス
     */
    public TodoBoardView(ITodoListBaseView baseViewInstance,  IMainWindowView mainWindowViewInstance, CommonDialogView commonDialogView, EditPeriodDialogView editPeriodDialogView)
    {
      // 親画面インスタンスを保持
      this.BaseViewInstance = baseViewInstance;
      this.MainWindowViewInstance = mainWindowViewInstance;
      // ダイアログインスタンスを初期化
      this.CommonDialogView = commonDialogView;
      this.EditPeriodDialogView = editPeriodDialogView;

      // レイアウトマネージャー解除
      this.setLayout(null);
      this.setBackground(Color.RED);

      // 左要素作成（Panel)
      // ここでこの要素のインスタンスを渡してるから、下のTodoSideCommonPanel内でも操作が可能
      this.TodoSideBoardPanel = new TodoSideBoardPanel(this);
      this.TodoSideBoardPanel.setBounds(0,0,230,600);

      // 右要素作成（Panel）
      this.TodoBoardContentPanel = new TodoBoardContentPanel(this);
      this.TodoBoardContentPanel.setBounds(230,0,769,600);
      
      // 左要素追加
      this.add(this.TodoSideBoardPanel);
      // 右要素追加
      this.add(this.TodoBoardContentPanel);

    }

    /**
     * ボードボタンクリック時の処理
     */
    public void actionPerformed(ActionEvent e)
    {
      switch (e.getActionCommand())
      {
        case "EditListName":
          System.out.println("EditListNameボタンが押された");
          this.UpdateListClicked(this.SelectedListId);
          break;
        case "EditTaskName":
          System.out.println("EditTaskNameボタンが押された");
          this.UpdateTaskClicked(this.SelectedTaskId);
          break;
        default:
          break;
      }
    }

    public void Show()
    {
      this.Controller.GetUserListAndTask();
      this.TodoSideBoardPanel.Show();
      this.TodoBoardContentPanel.AddListener(this);
      this.TodoBoardContentPanel.Show();
    }

    public void Hide()
    {
      this.TodoSideBoardPanel.Hide();
    }
    //ここでやはりメソッドを作る必要がある？（リストやガントチャートに移動するときに必要）
    //baseViewInstanceを使用して、上のTodoListBaseViewのところで宣言してるchengeViewを呼んでくる必要がある。

    /**
     * コントローラインスタンスを設定
     * @param controller コントローラインスタンス
     */
    public void SetController(ITodoBoardController controller)
    {
      this.Controller = controller;
    }

    /**
     * ボードボタンクリック時の処理
     */
    public void BoardButtonClicked()
    {
      // TODO: イベントリスナに置換する
      this.BaseViewInstance.ChangeView(TodoListBaseView.ViewType.TodoBoardView);
    }

    /**
     * リストボタンクリック時の処理
     */
    public void ListButtonClicked()
    {
      // TODO: イベントリスナに置換する
      this.BaseViewInstance.ChangeView(TodoListBaseView.ViewType.TodoListView);
    }

    /**
     * ガントチャートボタンクリック時の処理
     */
    public void GanttchartButtonClicked()
    {
      // TODO: イベントリスナに置換する
      this.BaseViewInstance.ChangeView(TodoListBaseView.ViewType.TodoGanttchartView);
    }

    /**
     * 新規作成のリストを画面に反映
     * @param list
     */
    public void SetListTask(List<UserList> list)
    {
      this.List = list;
      // TODO:task処理も記載
      this.TodoBoardContentPanel.SetList(list);
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
     * タスク削除メソッド
     * @param listId 画面の選択中リストID
     */
    public void DeleteTaskButtonClicked(int listId)
    {
      this.Controller.DeleteTask(listId);
    }

    /**
     * 完了タスク削除メソッド
     * @param listId 画面の選択中リストID
     */
    public void DeleteConTaskButtonClicked(int listId)
    {
        this.Controller.DeleteTask(listId);
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
      //OKでもキャンセルでもダイアログを閉じる処理は入れるべき
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
   * チェックボタン押下時処理
   * @param taskid タスクID
   * @param isChecked チェック状態
   */
  public void CheckBoxClicked(int taskid, boolean isChecked)
  {
    this.Controller.UpdateTask(taskid, isChecked);
  }

  /**
   * タスク登録処理
   * @param listId リストID
   * @param taskText タスクテキスト
   */
  public void CreateUserTask(int listId, String taskText)
  {
    this.Controller.CreateUserTask(listId, taskText);
  }

  /**
   * リスト作成メソッド
   * @param listText
   */
  public void CreateUserList(String listText)
  {
    this.Controller.CreateUserList(listText);
  }

}
