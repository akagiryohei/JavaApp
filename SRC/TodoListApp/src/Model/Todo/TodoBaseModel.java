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

    /** Todoリスト処理インスタンス */
    protected ITodoProcess Process;

    /** ログインユーザーID */
    protected UserData UserData;

    /** validationインスタンス */
    protected IValidationUtil Util;

    /** ロガーインスタンス */
    protected ILogger Logger;


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
    
}
