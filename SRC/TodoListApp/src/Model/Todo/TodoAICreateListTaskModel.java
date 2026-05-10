package Model.Todo;

import java.util.List;
import java.util.function.Consumer;

import Entity.AIListTask;
import Entity.Pair;
import Entity.UserData;
import Interface.Model.ILogger;
import Interface.Model.Process.Todo.ITodoProcess;
import Interface.Model.Process.Todo.ITodoProcess.ResultType;
import Interface.Model.Todo.ITodoAICreateListTaskModel;



public class TodoAICreateListTaskModel extends TodoBaseModel implements ITodoAICreateListTaskModel
{
    /** タスクデータの一次保存 */
    private AIListTask AIListTask;
    /**
     * コンストラクタ
     * 依存性注入
     * @param logger ロガーインスタンス
     * @param todoProcess Todo処理インスタンス
     * @param ログイン中のユーザID
     */
    public TodoAICreateListTaskModel(ILogger logger, ITodoProcess todoProcess, UserData userData)
    {
        this.Process = todoProcess;
        this.Logger = logger;
        this.UserData = userData;
    }

    /**
     * AIリスト・タスク作成
     * @param userInput ユーザ入力
     * @param isBusyChanged 処理中イベントコールバック
     * @param finished 処理完了コールバック
     */
    public void Ask(String userInput, Consumer<Boolean> isBusyChanged, Consumer<Pair<ResultType, AIListTask>> finished)
    {
        this.Process.Ask(userInput, (isBusy) ->
        {
            isBusyChanged.accept(isBusy);
        }, (result) -> {
            // AIからの結果を一時保存
            this.AIListTask = result.Value2;
            System.out.println("リスト案"+ this.AIListTask.listName);
            System.out.println("タスク案"+ this.AIListTask.tasks);
            finished.accept(result);
        });
    }

    /**
     * AIリスト・タスク再生成
     * @param userInput ユーザ入力
     * @param addUserInput 追加ユーザ入力
     * @param isBusyChanged 処理中イベントコールバック
     * @param finished 処理完了コールバック
     */
    public void ReAsk(String userInput, String addUserInput, Consumer<Boolean> isBusyChanged, Consumer<Pair<ResultType, AIListTask>> finished)
    {
        this.Process.ReAsk(userInput, addUserInput, (isBusy) ->
        {
            System.out.println("model" + isBusy);
            isBusyChanged.accept(isBusy);
        }, (result) -> {
            // AIからの結果を一時保存
            this.AIListTask = result.Value2;
            finished.accept(result);
        });
    }

    /**
     * ユーザーリスト登録
     * @param isBusyChanged 処理中イベントコールバック
     * @param finished 処理完了コールバック
     */
    public void CreateUserList(Consumer<Boolean> isBusyChanged, Consumer<ResultType> finished)
    {
        this.Process.CreateUserList(this.AIListTask.listName, this.UserData.UserId, (isBusy) ->
        {
            isBusyChanged.accept(isBusy);
        }, (result) -> {
            // 自動採番IDは不要なので、ResultTypeだけ返す
            // 自動採番IDはModelで保持
            this.GeneratedId = result.Value2;
            finished.accept(result.Value1);
        });
    }

    /**
     * タスクの登録
     * @param isBusyChanged 処理中イベントコールバック
     * @param finished 処理完了コールバック
     */
    public void CreateUserTask(Consumer<Boolean> isBusyChanged, Consumer<ResultType> finished)
    {
        List<String> taskTexts = this.AIListTask.tasks;
        this.Process.CreateUserTask(taskTexts, null, null, this.GeneratedId, this.UserData.UserId, (isBusy) ->
        {
            isBusyChanged.accept(isBusy);
        }, (result) -> {
            finished.accept(result);
        });
    }

    /**
     * 保持しているリストの修正
     * @param userEditListName ユーザが編集したリスト名
     */
    public ResultType EditAIList(String userEditListName)
    {
        try
        {
            this.AIListTask.listName = userEditListName;
            System.out.println(this.AIListTask.listName);
            return ResultType.Success;
        }
        catch (Exception e)
        {
            System.out.println(e);
            return ResultType.Failure;
        }
    }

    /**
     * 保持してるタスクリストの修正
     * @param userDeleteTaskId ユーザが削除したタスクID
     */
    public ResultType EditAITaskList(int userDeleteTaskId)
    {
        try
        {
            this.AIListTask.tasks.remove(userDeleteTaskId);
            return ResultType.Success;
        } catch (Exception e)
        {
            System.out.println(e);
            return ResultType.Failure;
        }
    }

    /**
     * 一時保存データの取得
     * @return 一時保存データ
     */
    public AIListTask GetAIListTask()
    {
        return this.AIListTask;
    }

    /**
     * ユーザリスト削除
     * @param isBusyChanged 処理中イベントコールバック
     * @param finished 処理完了コールバック
     */
    public void DeleteList(Consumer<Boolean> isBusyChanged, Consumer<ResultType> finished)
    {
        this.Process.DeleteList(this.GeneratedId, this.UserData.UserId, (isBusy) ->
        {
            isBusyChanged.accept(isBusy);
        }, (result) -> {
            finished.accept(result);
        });
    }


}
