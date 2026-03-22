package Interface.View.Todo;

import java.util.Date;
import java.util.List;

import Entity.UserList;
import Entity.UserTask;
import Interface.Controller.Todo.ITodoListController;
import Interface.Model.ILogger;
import View.Todo.Listener.TodoListViewCommonListener;

/*
 * リスト型表示Todo画面Viewインタフェース
 */
public interface ITodoListView
{
  /**
   * 画面表示
   */
  public void Show();

  /**
   * 画面非表示
   */
  public void Hide();

  /**
   * コントローラインスタンスを設定
   * @param controller コントローラインスタンス
   */
  public void SetController(ITodoListController controller);

  /**
   * リスナ対象追加
   * @param listener 追加対象リスナインスタンス
   */
  public void AddListener(TodoListViewCommonListener listener);

  /**
   * リスナ対象削除
   * @param listener 削除対象リスナインスタンス
   */
  public void RemoveListener(TodoListViewCommonListener listener);

  /**
   * リスト名設定失敗ダイアログ
   */
  public void ListLengthFailureDialog();

  /**
   * リスト削除失敗ダイアログ
   */
  public void ListDeleteFailureDialog();

  /**
   * リスト更新失敗ダイアログ
   */
  public void ListUpdateFailureDialog();

  /**
   * 新規作成のリストを画面に反映
   * @param list
   */
  public void SetList(List<UserList> list);

  /**
   * タスク作成メソッド
   * @param TaskText
   * @param StartDate
   * @param EndDate
   */
  public void CreateUserTask(String TaskText, Date StartDate, Date EndDate);

  /**
   * リスト名入力欄クリア
   */
  public void ClearListNameInput();

  /**
   * リスト更新メソッド
   * @param UpdateListName 画面の編集中リスト名
   */
  public void UpdateListClicked(String UpdateListName);

  /**
   * 新規作成のタスクを画面に反映
   * @param task
   */
  public void SetTask(List<UserTask> task, String listName);

  /**
   * ログイン中ユーザー名を設定
   * @param userName 画面表示するユーザ名
   */
  public void SetUserName(String userName);

  /**
   * タスク削除ボタン押下時メソッド
   * @param taskid 画面の選択中タスクID
   */
  public void DeleteTaskClicked(int taskid);

  /**
   * タスクアップデートダイアログ
   */
  public void UpdateTaskDialog(int taskId, String taskText);

  /**
   * タスク削除失敗ダイアログ
   */
  public void TaskDeleteFailureDialog();

  /**
   * タスク更新失敗ダイアログ
   */
  public void TaskUpdateFailureDialog();


  /**
   * チェックボタン押下時処理
   */
  public void CheckBoxClicked(int taskid, boolean isChecked);

  /**
   * 期日変更ダイアログ
   */
  public void PeriodButtonDialog(int taskid, String startDate, String endDate);

  /**
   * 期日編集メソッド
   */
  public void EditPeriodDateClicked(int taskId);

  /**
   * 期日登録ダイアログ
   */
  public void PeriodButtonDialog();

  /**
   * タスク入力欄の監視
   */
  public void ChangeTextField(boolean isEmpty);

  /**
   * タスク入力欄の監視(cotrollerメソッド呼び出し)
   */
  public void ChangeTextField(String taskText);

  /**
   * タスク取得失敗ダイアログ表示
   */
  public void GetTaskFailureDialog();

  /**
   * リスト情報取得失敗時ダイアログ表示
   */
  public void ShowGetUserListFailureDialog();

  /**
   * 左側パネルの要素押下可否設定メソッド
   * @param isDisabled 押下不可にする場合true、押下可能にする場合false
   */
  public void SideElementDisabled(boolean isDisabled);

  /**
   * 右側パネルの要素押下可否設定メソッド
   * @param isDisabled 押下不可にする場合true、押下可能にする場合false
   */
  public void LeftElementDisabled(boolean isDisabled);

  /**
    * ロガーインスタンスを依存性注入する
    * @param ロガーインスタンス
    */
  public void SetLogger(ILogger logger);

}
