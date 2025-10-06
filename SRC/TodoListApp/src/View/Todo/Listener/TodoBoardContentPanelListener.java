package View.Todo.Listener;

import java.util.EventListener;

/**
 * ボード型表示のコンテンツパネルリスナ
 */
public interface TodoBoardContentPanelListener extends EventListener
{
    /**
     * リスト削除メソッド
     * @param listId 画面の選択中リストID
     */
    public void DeleteListButtonClicked(int listId);

    /**
     * タスク削除メソッド
     * @param listId 画面の選択中リストID
     */
    public void DeleteTaskButtonClicked(int listId);

    /**
     * 完了タスク削除メソッド
     * @param listId 画面の選択中リストID
     */
    public void DeleteConTaskButtonClicked(int listId);

    /**
     * リストアップデートダイアログ
     * @param selectId リストID
     * @param selectName リスト名
     */
    public void UpdateListDialog(int selectId, String selectName);

    /**
     * タスクアップデートダイアログ(完了タスクと未完了を兼任する)
     * @param selectId リストID
     * @param selectName リスト名
     */
    public void UpdateTaskDialog(int selectId, String selectName);

    /**
     * タスク追加用＋ボタン押下時処理
     * @param listText リスト名
     */
    public void CreateUserList(String listText);

}
