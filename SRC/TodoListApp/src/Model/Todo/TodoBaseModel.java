package Model.Todo;

import java.util.List;
import java.util.function.Consumer;

import Entity.Pair;
import Entity.UserData;
import Entity.UserList;
import Interface.Model.ILogger;
import Interface.Model.IValidationUtil;
import Interface.Model.Process.Todo.ITodoProcess;
import Interface.Model.Process.Todo.ITodoProcess.ResultType;

public abstract class TodoBaseModel {

    /** リスト文字数下限 */
    protected final int ListNameMinLength = 0;

    /** リスト文字数上限 */
    protected final int ListNameMaxLength = 30;

    /** Todoリスト処理インスタンス */
    protected ITodoProcess Process;

    /** ログインユーザーID */
    protected UserData UserData;

    /** validationインスタンス */
    protected IValidationUtil Util;

    /** ロガーインスタンス */
    protected ILogger Logger;

    /** リスト自動採番ID */
    protected int GeneratedId;


    /**
     * ユーザリスト取得
     * @param isBusyChanged 処理中イベントコールバック
     * @param finished 処理完了コールバック
     */
    public void GetUserList(Consumer<Boolean> isBusyChanged, Consumer<Pair<ResultType, List<UserList>>> finished)
    {
        this.Process.GetUserList(this.UserData.UserId, (isBusy) ->
        {
            isBusyChanged.accept(isBusy);
        }, (result) -> {
            finished.accept(result);
        });
    }

    /**
     * ユーザリスト登録
     * @param isBusyChanged 処理中イベントコールバック
     * @param finished 処理完了コールバック
     */
    public void CreateUserList(String listText, Consumer<Boolean> isBusyChanged, Consumer<ResultType> finished)
    {
        int getLength = this.Util.GetWideStringLength(listText);
        if (getLength > this.ListNameMinLength && getLength <= this.ListNameMaxLength)
        {
            this.Process.CreateUserList(listText, this.UserData.UserId, (isBusy) ->
            {
                isBusyChanged.accept(isBusy);
            }, (result) -> {
                // 自動採番IDは不要なので、ResultTypeだけ返す
                // 自動採番IDはModelで保持
                this.GeneratedId = result.Value2;
                finished.accept(result.Value1);
            });
        }
        else
        {
            finished.accept(ITodoProcess.ResultType.ValidateError);
        }
    }

    /**
     * ユーザリスト削除
     * @param listId 画面の選択中リストID
     * @param isBusyChanged 処理中イベントコールバック
     * @param finished 処理完了コールバック
     */
    public void DeleteList(int listId, Consumer<Boolean> isBusyChanged, Consumer<ResultType> finished)
    {
        this.Process.DeleteList(listId, this.UserData.UserId, (isBusy) ->
        {
            isBusyChanged.accept(isBusy);
        }, (result) -> {
            finished.accept(result);
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
     * ユーザ名取得
     * @return ログイン中のユーザ名
     */
    public String GetUserName()
    {
        return this.UserData.Email;
    }

    /**
     * リスト編集
     */
    public void UpdateList(int listId, String listName, Consumer<Boolean> isBusyChanged, Consumer<ResultType> finished)
    {
        this.Process.UpdateList(listId, listName, (isBusy) -> {
            System.out.println(isBusy);
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

}
