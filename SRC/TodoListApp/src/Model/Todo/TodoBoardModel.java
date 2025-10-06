package Model.Todo;

import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import Entity.UserTask;
import Entity.Pair;
import Entity.UserData;
import Entity.UserList;
import Interface.Model.ILogger;
import Interface.Model.Process.Todo.ITodoProcess;
import Interface.Model.Process.Todo.ITodoProcess.ResultType;
import Interface.Model.Todo.ITodoBoardModel;
import Interface.Model.IValidationUtil;


public class TodoBoardModel extends TodoBaseModel implements ITodoBoardModel {

    /** 選択中リストのタスク */
    private List<UserTask> Task;

    /** 選択中リストID */
    private int ListId;

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
            //TODO:ログ出力
        });
    }

    /**
     * ユーザタスク削除
     * @param taskId 画面の選択中タスクID
     * @param isBusyChanged 処理中イベントコールバック
     * @param finished 処理完了コールバック
     */
    public void DeleteTask(int taskId, Consumer<Boolean> isBusyChanged, Consumer<ResultType> finished)
    {
        this.Process.DeleteTask(taskId, this.UserData.UserId, (isBusy) ->
        {
            isBusyChanged.accept(isBusy);
        }, (result) -> {
            finished.accept(result);
        });
    }

    /**
     * リスト編集
     */
    public void UpdateList(int listId, String listName, Consumer<Boolean> isBusyChanged, Consumer<ResultType> finished)
    {
        this.Process.UpdateList(listId, listName, (isBusy) -> {
            isBusyChanged.accept(isBusy);
        }, (result) -> {
            finished.accept(result);
        });
    }

    /**
     * タスク編集
     * @param taskId 画面の選択中タスクID
     * @param taskText 画面の選択中タスクテキスト
     * @param isBusyChanged 処理中イベントコールバック
     * @param finished 処理完了コールバック
     */
    public void UpdateTask(int taskId, String taskText, Consumer<Boolean> isBusyChanged, Consumer<ResultType> finished)
    {
        this.Process.UpdateTask(taskId, taskText, this.UserData.UserId, (isBusy) -> {
            isBusyChanged.accept(isBusy);
        }, (result) -> {
            finished.accept(result);
        });
    }

    /**
     * タスク編集（タスク進捗度＋完了/未完了）
     * @param taskId 画面の選択中タスクID
     * @param isChecked 画面の選択中タスクの状態
     * @param isBusyChanged 処理中イベントコールバック
     * @param finished 処理完了コールバック
     */
    public void UpdateTask(int taskId, boolean isChecked, Consumer<Boolean> isBusyChanged, Consumer<ResultType> finished)
    {
        this.Process.UpdateTask(taskId, isChecked ? 100 : 0, this.UserData.UserId, (isBusy) -> {
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
        int getLength = this.Util.GetStringLength(taskText);
        if (getLength > 0 && getLength < 40)
        {
            this.Process.CreateUserTask(
                taskText,
                // this.Util.IsOriginDate(startDate) ? null : startDate,//初期値と同じであれば、nullにする// nullが来てるからここで落ちる
                // this.Util.IsOriginDate(endDate) ? null : endDate,//初期値と同じであれば、nullにする
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

    /**
     * ユーザリスト登録
     * @param listText 画面入力リスト名
     * @param isBusyChanged 処理中イベントコールバック
     * @param finished 処理完了コールバック
     */
    public void CreateUserList(String listText, Consumer<Boolean> isBusyChanged, Consumer<ResultType> finished)
    {
        int getLength = this.Util.GetStringLength(listText);
        if (getLength > 0 && getLength <= 30 )
        {
            this.Process.CreateUserList(listText, this.UserData.UserId, (isBusy) ->
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
