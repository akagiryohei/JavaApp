package Interface.View.Todo;

import java.util.Date;
import java.util.List;

import Entity.UserList;
import Entity.UserTask;
import Interface.Controller.Todo.ITodoListController;

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
   * @param listId 画面の選択中リストID
   */
  public void UpdateListClicked(int listId);

  /**
   * タスク更新メソッド
   * @param taskId
   */
  public void UpdateTaskClicked(int taskId);


  /**
   * リスト編集ダイアログ閉
   */
  public void CloseListDialog();

  /**
   * タスク編集ダイアログ閉
   */
  public void CloseTaskDialog();

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
   * 期日編集ダイアログ閉
   */
  public void ClosePeriodDialog();

  /**
   * 期日登録ダイアログ
   */
  public void PeriodButtonDialog();


}
