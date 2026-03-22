package View.Todo.Listener;

import java.util.EventListener;

public interface TodoAICreateListTaskContentPanelListener extends EventListener
{

    /**
     * AIリスト・タスク案作成
     * @param userInput ユーザー入力
     */
    public void Ask(String userInput);

    /**
     * AIリスト・タスク案再生成
     * @param userInput ユーザー入力
     * @param addUserInput 追加ユーザー入力
     */
    public void ReAsk(String userInput, String addUserInput);

    /**
     * 保持しているタスクリストの修正
     * @param userDeleteTaskId ユーザが削除したタスクID
     */
    public void EditAITaskList(int userDeleteTaskId);

    /**
     * リスト名編集
     * @param userEditListName ユーザが編集したリスト名
     */
    public void EditAIList(String userEditListName);

    /**
     * リストとタスクの登録メソッド
     */
    public void AddListTask();


}
