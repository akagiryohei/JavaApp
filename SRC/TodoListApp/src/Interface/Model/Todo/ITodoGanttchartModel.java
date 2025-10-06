package Interface.Model.Todo;

import java.time.YearMonth;
import java.util.List;
import java.util.function.Consumer;

import Entity.GanttchartTask;
import Entity.Pair;
import Entity.UserList;
import Entity.UserTask;
import Interface.Model.Process.Todo.ITodoProcess;
import Interface.Model.Process.Todo.ITodoProcess.ResultType;

/**
 * ガントチャート型Todo画面Modelのインタフェース
 */
public interface ITodoGanttchartModel
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
     * タスク取得メソッド
     * @param listId 選択中ID
     * @param activeYearMonth 選択中年月
     */
    public void GetUserTask(int listId, YearMonth activeYearMonth, Consumer<Boolean> isBusyChanged, Consumer<Pair<ITodoProcess.ResultType, List<GanttchartTask>>> finished);

    /**
     * ユーザ名取得
     * @return ログイン中のユーザ名
     */
    public String GetUserName();

    /**
     * タスク編集（タスク進捗度＋完了/未完了）
     * @param taskId 画面の選択中タスクID
     * @param progress 画面の選択中タスクの進捗率
     * @param isBusyChanged 処理中イベントコールバック
     * @param finished 処理完了コールバック
     */
    public void UpdateTask(int taskId, int progress, Consumer<Boolean> isBusyChanged, Consumer<ResultType> finished);

}
