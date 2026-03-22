package View.Todo.Listener;

import java.util.EventListener;

/**
 * リスト・ガントチャート共通設定のイベントリスナ
 */
public interface TodoSideCommonPanelListener extends EventListener
{
  /**
   * ユーザリストクリック時の処理
   * @param listId 選択対象のリストID
   * @param listName 選択対象のリスト名
   */
  public void UserListButtonClicked(int listId, String listName);

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
 * AIリスト・タスク案作成ボタンクリック時の処理
 */
  public void AICreateListTaskButtonClicked();

  /**
   * リスト作成メソッド
   * @param listText
   */
  public void CreateUserList(String listText);

  /**
   * リスト削除メソッド
   * @param listId 画面の選択中リストID
   */
  public void DeleteListButtonClicked(int listId);

  /**
   * リストアップデートダイアログ
   * @param listid リストID
   * @param listName リスト名
   */
  public void UpdateListDialog(int listid, String listName);
}
