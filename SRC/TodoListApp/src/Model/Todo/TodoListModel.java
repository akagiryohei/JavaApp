package Model.Todo;

import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import Entity.Pair;
import Entity.UserData;
import Entity.UserList;

import Interface.Model.ILogger;
import Interface.Model.IValidationUtil;
import Interface.Model.Process.Todo.ITodoProcess;
import Interface.Model.Process.Todo.ITodoProcess.ResultType;
import Interface.Model.Todo.ITodoListModel;
import Entity.UserTask;
import Entity.DB.InputTask;

/**
 * TodoList画面モデルクラス
 */
public class TodoListModel extends TodoBaseModel implements ITodoListModel
{

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
    public TodoListModel(ILogger logger, ITodoProcess todoProcess, UserData userData, IValidationUtil util)
    {
        this.Process = todoProcess;
        this.Logger = logger;
        this.UserData = userData;
        this.Util = util;
    }

    /**
     * ユーザータスク登録
     * @param TaskText
     * @param StartDate
     * @param EndDate
     * @param isBusyChanged 処理中イベントコールバック
     * @param finished 処理完了コールバック
     */
    public void CreateUserTask(String taskText, Date startDate, Date endDate, Consumer<Boolean> isBusyChanged, Consumer<ResultType> finished)
    {
        int getLength = this.Util.GetWideStringLength(taskText);
        if (getLength > 0 && getLength < 40)
        {

            this.Process.CreateUserTask(
                taskText,
                this.Util.IsOriginDate(startDate) ? null : startDate,//初期値と同じであれば、nullにする
                this.Util.IsOriginDate(endDate) ? null : endDate,//初期値と同じであれば、nullにする
                this.ListId,
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
     * タスク取得メソッド
     * @param listId 選択中ID
     */
    public void GetUserTask(int listId, Consumer<Boolean> isBusyChanged, Consumer<Pair<ITodoProcess.ResultType, List<UserTask>>> finished)
    {
        this.Process.GetUserTask(this.UserData.UserId, listId, (isBusy) ->
        {
            isBusyChanged.accept(isBusy);
        }, (result) -> {
            // タスク編集メソッド等に使いまわしするために保存
            this.ListId = listId;
            this.Task = result.Value2;
            finished.accept(result);
        });
    }

    /**
     * タスク取得メソッド
     * ※タスク編集時に呼び出される用
     */
    public void GetUserTask(Consumer<Boolean> isBusyChanged, Consumer<Pair<ITodoProcess.ResultType, List<UserTask>>> finished)
    {
        this.GetUserTask(this.ListId, isBusyChanged, finished);
    }

    /**
     * 期日編集メソッド
     * @param taskId
     * @param startDate
     * @param endDate
     * @param isBusyChanged
     * @param finished
     */
    public void EditPeriodDate(int taskId, Date startDate, Date endDate, Consumer<Boolean>isBusyChanged, Consumer<ResultType> finished)
    {
        this.Process.EditPeriodDate(taskId, startDate, endDate, this.UserData.UserId, (isBusy) -> {
            isBusyChanged.accept(isBusy);
        }, (result) -> {
            finished.accept(result);
        });
    }

    /**
     * ＋ボタン押下可否判定
     * @param taskText タスク入力欄の文字列
     */
    public Boolean GetPlusButtonPossibility(String taskText)
    {
        return taskText.isEmpty();
    }

}
