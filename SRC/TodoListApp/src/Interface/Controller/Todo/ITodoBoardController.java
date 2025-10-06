package Interface.Controller.Todo;

import Interface.View.Todo.ITodoBoardView;

/*
 * Todoリスト（ボード型表示）コントローラインタフェース
 */
public interface ITodoBoardController
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
   * @return Todoリスト（ボード型表示）画面Viewインスタンス
   */
  public ITodoBoardView GetViewInstance();

  /**
   * ユーザリスト＋タスク取得（ボード画面用）
   */
  public void GetUserListAndTask();

  /**
   * リスト削除メソッド
   * @param listId 画面選択リストID
   */
  public void DeleteList(int listId);

  /**
   * タスク削除メソッド
   * @param listId 画面選択リストID
   */
  public void DeleteTask(int listId);

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
   * タスク登録処理
   * @param listId リストID
   * @param taskText タスクテキスト
   */
  public void CreateUserTask(int listId, String taskText);

    /**
     * ユーザリスト登録
     * @param listText 画面入力リスト名
     */
    public void CreateUserList(String listText);

}
