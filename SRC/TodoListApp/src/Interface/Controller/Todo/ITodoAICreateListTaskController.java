package Interface.Controller.Todo;

import Entity.AIListTask;
import Interface.Model.ILogger;
import Interface.View.Todo.ITodoAICreateListTaskView;

/**
 * Todoリスト（AI作成リスト・タスク案型表示）コントローラ
 */
public interface ITodoAICreateListTaskController
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
     * @return Todoリスト（AI作成リスト・タスク案型表示）画面Viewインスタンス
     */
    public ITodoAICreateListTaskView GetViewInstance();

    /**
     * ロガーインスタンスを依存性注入する
     * @param ロガーインスタンス
     */
    public void SetLogger(ILogger logger);

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
     * リスト名の編集
     * @param userEditListName ユーザが編集したリスト名
     */
    public void EditAIList(String userEditListName);

    /**
     * リストとタスクの登録メソッド
     */
    public void AddListTask();

    /**
     * リスト削除メソッド
     */
    public void DeleteList();


}
