package Interface.Controller.Todo;

import java.util.Date;

import Interface.View.Todo.ITodoListView;

/*
 * Todoリスト（リスト型表示）コントローラインタフェース
 */
public interface ITodoListController
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
   * 画面のインスタンスを取得
   * @return Todoリスト（リスト型表示）画面Viewインスタンス
   */
  public ITodoListView GetViewInstance();

  /**
   * ユーザリスト取得
   * @param isBusyChanged 処理中イベントコールバック
   * @param finished 処理完了コールバック
   */
  public void GetUserList();

  /**
   * ユーザリスト登録
   * @param listText 画面入力リスト名
   * @param isBusyChanged 処理中イベントコールバック
   * @param finished 処理完了コールバック
   */
  public void CreateUserList(String listText);

  /**
   * タスク登録処理
   * @param taskText
   * @param startDate
   * @param endDate
   */
  public void CreateUserTask(String taskText, Date startDate, Date endDate);

  /**
   * リスト削除メソッド
   * @param listId 画面選択リストID
   * @param isBusyChanged 処理中イベントコールバック
   * @param finished 処理完了コールバック
   */
  public void DeleteList(int listId);

  /**
   * リスト編集メソッド
   * @param listId 画面の選択中リストID
   * @param listName 編集予定のリスト名
   */
  public void UpdateList(int listId, String listName);

  /**
   * タスク編集メソッド
   * @param taskId 画面の選択中タスクID
   * @param taskText 編集予定のタスク名
   */
  public void UpdateTask(int taskId, String taskText);

  /**
   * タスク編集メソッド（タスク進捗度＋完了/未完了）
   */
  public void UpdateTask(int taskid, boolean isChecked);

  /**
   * 期日編集メソッド
   */
  public void EditPeriodDate(int taskId, Date startDate, Date endDate);

  /**
   * タスク取得メソッド
   * @param listId
   */
  public void GetUserTask(int listId, String listName);

    /**
     * タスク削除メソッド
     */
    public void DeleteTask(int taskId);
}
