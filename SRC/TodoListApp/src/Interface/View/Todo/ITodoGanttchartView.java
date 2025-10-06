package Interface.View.Todo;
import java.time.YearMonth;
import java.util.List;

import Entity.GanttchartTask;
import Entity.UserList;
import Entity.UserTask;
import Interface.Controller.Todo.ITodoGanttchartController;

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
   * リスト名入力欄クリア
   */
  public void ClearListNameInput();

  /**
   * リスト編集ダイアログ閉
   */
  public void CloseListDialog();

  /**
   * 新規作成のタスクを画面に反映
   * @param task 選択中リストのタスク一覧
   * @param listName 選択中リスト名
   * @param activeYearMonth 選択中年月
   */
  public void SetTask(List<GanttchartTask> userTask, String listName, YearMonth activeYearMonth);

  /**
   * ログイン中ユーザー名を設定
   * @param userName 画面表示するユーザ名
   */
  public void SetUserName(String userName);




}