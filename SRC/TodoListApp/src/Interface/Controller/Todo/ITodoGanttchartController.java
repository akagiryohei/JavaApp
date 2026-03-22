package Interface.Controller.Todo;

import java.time.YearMonth;

import Interface.Model.ILogger;
import Interface.View.Todo.ITodoGanttchartView;

/*
 * Todoリスト（ガントチャート）コントローラ
 */
public interface ITodoGanttchartController
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
   * @return Todoリスト（ガントチャート型表示）画面Viewインスタンス
   */
  public ITodoGanttchartView GetViewInstance();

    /**
   * ユーザリスト取得
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
   * リスト名入力欄クリア
   */
  public void ClearListNameInput();

  /**
   * リスト削除メソッド
   * @param listId 画面選択リストID
   */
  public void DeleteList(int listId);

  /**
   * リスト編集メソッド
   * @param listId 画面の選択中リストID
   * @param listName 編集予定のリスト名
   */
  public void UpdateList(int listId, String listName);

  /**
   * タスク取得メソッド
   * @param listId 選択中リストID
   * @param listName 選択中リスト名
   * @param activeYearMonth 選択中年月
   */
  public void GetUserTask(int listId, String listName, YearMonth activeYearMonth);

  /**
   * タスク編集（タスク進捗度＋完了/未完了）
   * @param taskId 画面の選択中タスクID
   * @param progress 画面の選択中タスクの進捗率
   */
  public void UpdateTask(int taskId, int progress, int listId, String listName, YearMonth activeYearMonth);

  /**
  * ロガーインスタンスを依存性注入する
  * @param ロガーインスタンス
  */
  public void SetLogger(ILogger logger);

}