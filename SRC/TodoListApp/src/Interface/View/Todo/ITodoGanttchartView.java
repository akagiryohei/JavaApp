package Interface.View.Todo;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import Entity.GanttchartTask;
import Entity.UserList;
import Entity.UserTask;
import Interface.Controller.Todo.ITodoGanttchartController;
import Interface.Model.ILogger;
import View.Todo.Listener.TodoListViewCommonListener;

/**
 * ガントチャート型Todo画面Viewインタフェース
 */
public interface ITodoGanttchartView
{
  /**
   * コントローラインスタンスを設定
   * @param controller コントローラインスタンス
   */
  public void SetController(ITodoGanttchartController controller);

  /**
   * 画面表示
   */
  public void Show();

  /**
   * 画面非表示
   */
  public void Hide();

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
   * 新規作成のリストを画面に反映
   * @param list
   */
  public void SetList(List<UserList> list);

  /**
   * リスト作成メソッド
   * @param listText
   */
  public void CreateUserList(String listText);

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
   * タスク削除失敗ダイアログ
   */
  public void TaskDeleteFailureDialog();

  /**
   * リスト情報取得失敗ダイアログ表示
   */
  public void GetListFailureDialog();

  /**
   * タスク情報取得失敗ダイアログ表示
   */
  public void GetTaskFailureDialog();

  /**
   * タスク更新失敗ダイアログ表示
   */
  public void UpdateTaskFailureDialog();

  /**
   * リスト名入力欄クリア
   */
  public void ClearListNameInput();

  /**
   * 新規作成のタスクを画面に反映
   * @param task 選択中リストのタスク一覧
   * @param listName 選択中リスト名
   * @param activeYearMonth 選択中年月
   * @param today 今日の日付
   */
  public void SetTask(List<GanttchartTask> userTask, String listName, YearMonth activeYearMonth, LocalDate today);
  
  /**
   * ガントチャートパネルの本日表示を更新する
   * @param today 本日の日付
   */
  public void RefreshTodayCell(LocalDate today);

  /**
   * ログイン中ユーザー名を設定
   * @param userName 画面表示するユーザ名
   */
  public void SetUserName(String userName);

  /**
   * リスト更新メソッド
   * @param listId 画面の選択中リストID
   */
  public void UpdateListClicked(String UpdateListName);

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