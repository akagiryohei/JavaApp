package Interface.View.Todo;

import java.util.List;

import Entity.UserList;
import Interface.Controller.Todo.ITodoBoardController;
import Interface.Model.ILogger;
import View.Todo.Listener.TodoListViewCommonListener;

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
   * タスク更新失敗ダイアログ
   */
  public void TaskUpdateFailureDialog();

  /**
   * タスク登録失敗ダイアログ
   */
  public void TaskCreateFailureDialog();

  /**
   * リストおよびタスク取得失敗時ダイアログ表示
   */
  public void GetListAndTaskFailureDialog();

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
