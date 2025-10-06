package Interface.View.Todo;

import java.util.List;

import Entity.UserList;
import Entity.UserTask;
import Interface.Controller.Todo.ITodoBoardController;

/**
 * ボード型Todo画面Viewインタフェース
 */
public interface ITodoBoardView
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
  public void SetController(ITodoBoardController controller);

  /**
   * ボードボタンクリック時の処理
   */
  public void BoardButtonClicked();

  /**
   * リストボタンクリック時の処理
   */
  public void ListButtonClicked();

  /**
   * ガントチャートボタンクリック時の処理
   */
  public void GanttchartButtonClicked();

  /**
   * 新規作成のリストを画面に反映
   * @param list
   */
  public void SetListTask(List<UserList> list);

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
   * タスク編集ダイアログ閉
   */
  public void CloseTaskDialog();

  /**
   * リスト編集ダイアログ閉
   */
  public void CloseListDialog();

  /**
   * チェックボタン押下時処理
   * @param taskid タスクID
   * @param isChecked チェック状態
   */
  public void CheckBoxClicked(int taskid, boolean isChecked);

  /**
   * タスク登録処理
   * @param listId リストID
   * @param taskText タスクテキスト
   */
  public void CreateUserTask(int listId, String taskText);

}
