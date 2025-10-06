package Interface.Model.Todo;

import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import Entity.Pair;
import Entity.UserList;
import Entity.UserTask;
import Entity.DB.InputTask;
import Interface.Model.Process.Todo.ITodoProcess;
import Interface.Model.Process.Todo.ITodoProcess.ResultType;

/**
 * リスト型Todo画面Modelのインタフェース
 */
public interface ITodoListModel
{
    /**
     * ユーザリスト取得
     * @param isBusyChanged 処理中イベントコールバック
     * @param finished 処理完了コールバック
     */
    public void GetUserList(Consumer<Boolean> isBusyChanged, Consumer<Pair<ResultType, List<UserList>>> finished);

    /**
     * ユーザリスト登録
     * @param listText 画面入力リスト名
     * @param isBusyChanged 処理中イベントコールバック
     * @param finished 処理完了コールバック
     */
    public void CreateUserList(String listText, Consumer<Boolean> isBusyChanged, Consumer<ResultType> finished);

    /**
     * ユーザータスク登録
     * @param TaskText
     * @param StartDate
     * @param EndDate
     * @param isBusyChanged 処理中イベントコールバック
     * @param finished 処理完了コールバック
     */
    public void CreateUserTask(String taskText, Date startDate, Date endDate, Consumer<Boolean> isBusyChanged, Consumer<ResultType> finished);

    /**
     * ユーザリスト削除
     * @param listId 画面の選択中リストID
     * @param isBusyChanged 処理中イベントコールバック
     * @param finished 処理完了コールバック
     */
    public void DeleteList(int listId, Consumer<Boolean> isBusyChanged, Consumer<ResultType> finished);

    /**
     * リスト編集
     * @param listId 画面の選択中リストID
     * @param listName 画面入力リスト名
     * @param isBusyChanged 処理中イベントコールバック
     * @param finished 処理完了コールバック
     */
    public void UpdateList(int listId, String listName, Consumer<Boolean> isBusyChanged, Consumer<ResultType> finished);

    /**
     * タスク取得
     * @param listId 画面の選択中リストID
     * @param isBusyChanged 処理中イベントコールバック
     * @param finished 処理完了コールバック
     */
    public void GetUserTask(int listId, Consumer<Boolean> isBusyChanged, Consumer<Pair<ITodoProcess.ResultType, List<UserTask>>> finished);

        /**
     * タスク取得メソッド
     * @param listId 選択中ID
     */
    public void GetUserTask(Consumer<Boolean> isBusyChanged, Consumer<Pair<ITodoProcess.ResultType, List<UserTask>>> finished);

    /**
     * タスク編集
     * @param taskId 画面の選択中タスクID
     * @param taskText 画面の選択中タスクテキスト
     * @param isBusyChanged 処理中イベントコールバック
     * @param finished 処理完了コールバック
     */
    public void UpdateTask(int taskId, String taskText, Consumer<Boolean> isBusyChanged, Consumer<ResultType> finished);

    /**
     * タスク編集（タスク進捗度＋完了/未完了）
     * @param taskId 画面の選択中タスクID
     * @param isChecked 画面の選択中タスクの状態
     * @param isBusyChanged 処理中イベントコールバック
     * @param finished 処理完了コールバック
     */
    public void UpdateTask(int taskId, boolean isChecked, Consumer<Boolean> isBusyChanged, Consumer<ResultType> finished);

    /**
     * 期日編集メソッド
     * @param taskId
     * @param startDate
     * @param endDate
     * @param isBusyChanged
     * @param finished
     */
    public void EditPeriodDate(int taskId, Date startDate, Date endDate, Consumer<Boolean>isBusyChanged, Consumer<ResultType> finished);

    /**
     * ユーザタスク削除
     * @param taskId 画面の選択中タスクID
     * @param isBusyChanged 処理中イベントコールバック
     * @param finished 処理完了コールバック
     */
    public void DeleteTask(int taskId, Consumer<Boolean> isBusyChanged, Consumer<ResultType> finished);

    /**
     * ユーザ名取得
     * @return ログイン中のユーザ名
     */
    public String GetUserName();
}
