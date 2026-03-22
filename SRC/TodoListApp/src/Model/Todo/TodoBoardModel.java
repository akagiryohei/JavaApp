package Model.Todo;

import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import Entity.Pair;
import Entity.UserData;
import Entity.UserList;
import Interface.Model.ILogger;
import Interface.Model.Process.Todo.ITodoProcess;
import Interface.Model.Process.Todo.ITodoProcess.ResultType;
import Interface.Model.Todo.ITodoBoardModel;
import Interface.Model.IValidationUtil;


public class TodoBoardModel extends TodoBaseModel implements ITodoBoardModel
{
    /**
     * コンストラクタ
     * 依存性注入
     * @param logger ロガーインスタンス
     * @param todoProcess Todo処理インスタンス
     * @param ログイン中のユーザID
     */
    public TodoBoardModel(ILogger logger, ITodoProcess todoProcess, UserData userData, IValidationUtil util)
    {
        this.Process = todoProcess;
        this.Logger = logger;
        this.UserData = userData;
        this.Util = util;
    }

    /**
     * ユーザリスト取得
     * @param isBusyChanged 処理中イベントコールバック
     * @param finished 処理完了コールバック
     */
    public void GetUserListAndTask(Consumer<Boolean> isBusyChanged, Consumer<Pair<ResultType, List<UserList>>> finished)
    {
        this.Process.GetUserListAndTask(this.UserData.UserId, (isBusy) ->
        {
            isBusyChanged.accept(isBusy);
        }, (result) -> {
            finished.accept(result);
        });
    }

    /**
     * ユーザータスク登録
     * @param listId
     * @param TaskText
     * @param StartDate
     * @param EndDate
     * @param isBusyChanged 処理中イベントコールバック
     * @param finished 処理完了コールバック
     */
    public void CreateUserTask(int listId, String taskText, Date startDate, Date endDate, Consumer<Boolean> isBusyChanged, Consumer<ResultType> finished)
    {
        int getLength = this.Util.GetWideStringLength(taskText);
        if (getLength > 0 && getLength < 40)
        {
            this.Process.CreateUserTask(
                taskText,
                null,
                null,
                listId,
                this.UserData.UserId,
                (isBusy) ->
            {
                isBusyChanged.accept(isBusy);
            }, (result) -> {
                finished.accept(result);
            });
        }
        else
        {
            finished.accept(ITodoProcess.ResultType.ValidateError);
        }
    }
}
