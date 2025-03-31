package Model.Todo;

import java.util.List;
import java.util.function.Consumer;

import Entity.Pair;
import Entity.UserList;
import Model.Logger;
import Model.Process.Login.LoginProcess.ResultType;
import Model.Process.Todo.TodoProcess;

/**
 * TodoList画面モデルクラス
 */
public class TodoListModel
{
    /** Todoリスト処理インスタンス */
    private TodoProcess Process;

    /** ロガーインスタンス */
    private Logger Logger;

    /** ログインユーザーID */
    private String UserID;

    /**
     * コンストラクタ
     * 依存性注入
     * @param logger ロガーインスタンス
     * @param todoProcess Todo処理インスタンス
     * @param ログイン中のユーザID
     */
    public TodoListModel(Logger logger, TodoProcess todoProcess, String userID)
    {
        this.Process = todoProcess;
        this.Logger = logger;
        this.UserID = userID;
    }

    /**
     * ユーザリスト取得
     * @param isBusyChanged 処理中イベントコールバック
     * @param finished 処理完了コールバック
     */
    public void GetUserList(Consumer<Boolean> isBusyChanged, Consumer<Pair<TodoProcess.ResultType, List<UserList>>> finished)
    {
        this.Process.GetUserList(this.UserID, (isBusy) ->
        {
            isBusyChanged.accept(isBusy);
        }, (result) -> {
            finished.accept(result);
            System.out.println(result);
        });
    }

    /**
     * ユーザリスト登録
     * @param isBusyChanged 処理中イベントコールバック
     * @param finished 処理完了コールバック
     */
    public void CreateUserList(Consumer<Boolean> isBusyChanged, Consumer<TodoProcess.ResultType> finished, String listText)
    {
        this.Process.CreateUserList(listText, this.UserID, (isBusy) ->
        {
            isBusyChanged.accept(isBusy);
        }, (result) -> {
            finished.accept(result);
        });
    }
}
