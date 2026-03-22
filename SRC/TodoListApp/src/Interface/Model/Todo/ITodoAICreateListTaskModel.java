package Interface.Model.Todo;

import java.util.function.Consumer;

import Entity.AIListTask;
import Entity.Pair;
import Interface.Model.Process.Todo.ITodoProcess.ResultType;

/**
 * Todoリスト（AI作成リスト・タスク案型表示）Modelインターフェース
 */
public interface ITodoAICreateListTaskModel
{
    /**
     * ユーザ名取得
     * @return ログイン中のユーザ名
     */
    public String GetUserName();

    /**
     * AIリスト・タスク作成
     * @param userInput ユーザ入力
     * @param isBusyChanged 処理中イベントコールバック
     * @param finished 処理完了コールバック
     */
    public void Ask(String userInput, Consumer<Boolean> isBusyChanged, Consumer<Pair<ResultType, AIListTask>> finished);

    /**
     * AIリスト・タスク再生成
     * @param userInput ユーザ入力
     * @param addUserInput 追加ユーザ入力
     * @param isBusyChanged 処理中イベントコールバック
     * @param finished 処理完了コールバック
     */
    public void ReAsk(String userInput, String addUserInput, Consumer<Boolean> isBusyChanged, Consumer<Pair<ResultType, AIListTask>> finished);
    
    /**
     * 保持しているリストの修正
     * @param userEditListName ユーザが編集したリスト名
     */
    public ResultType EditAIList(String userEditListName);

    /**
     * 保持してるタスクリストの修正
     * @param userDeleteTaskId ユーザが削除したタスクID
     */
    public ResultType EditAITaskList(int userDeleteTaskId);

    /**
     * 一時保存データの取得
     * @return 一時保存データ
     */
    public AIListTask GetAIListTask();

    /**
     * リスト・タスクの登録
     * @param taskTexts タスクテキスト
     * @param listId リストID
     * @param isBusyChanged 処理中イベントコールバック
     * @param finished 処理完了コールバック
     */
    public void CreateUserTask(Consumer<Boolean> isBusyChanged, Consumer<ResultType> finished);

    /**
     * ユーザーリスト登録
     * @param isBusyChanged 処理中イベントコールバック
     * @param finished 処理完了コールバック
     */
    public void CreateUserList(Consumer<Boolean> isBusyChanged, Consumer<ResultType> finished);

    /**
     * ユーザリスト削除
     * @param listId 画面の選択中リストID
     * @param isBusyChanged 処理中イベントコールバック
     * @param finished 処理完了コールバック
     */
    public void DeleteList(Consumer<Boolean> isBusyChanged, Consumer<ResultType> finished);
}
