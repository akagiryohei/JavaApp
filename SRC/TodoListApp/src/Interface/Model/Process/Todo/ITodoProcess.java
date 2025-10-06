package Interface.Model.Process.Todo;

import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import Entity.Pair;
import Entity.UserList;
import Entity.UserTask;
import Entity.DB.InputTask;

/**
  * Todoリスト処理インタフェース
  */
public interface ITodoProcess
{
  /** 処理結果定義 */
  public enum ResultType
  {
    /** 正常終了（認証OK） */
    Success,

    /** 入力値不正（ユーザIDの変換に失敗した） */
    ValidateError,

    /** 異常終了 */
    Failure,

    /** タイムアウト */
    Timeout,
  }

  /**
   * 指定ユーザのリスト一覧を取得
   * @param userId 指定ユーザID
   * @param isBusyChanged 処理中イベントコールバック
   * @param finished 処理完了イベントコールバック（処理結果、リスト一覧）
  */
  public void GetUserList(String userId, Consumer<Boolean> isBusyChanged, Consumer<Pair<ResultType, List<UserList>>> finished);

  /**
   * 指定ユーザのリスト登録処理
   * @param listText リスト名
   * @param userId   指定ユーザID
   * @param isBusyChanged 処理中イベントコールバック
   * @param finished 処理完了イベントコールバック（処理結果）
   */
  public void CreateUserList(String listText, String userId, Consumer<Boolean> isBusyChanged, Consumer<ResultType> finished);

  /**
   * 指定ユーザーのタスク登録処理
   * @param taskText
   * @param startDate
   * @param endDate
   * @param listId
   */
  public void CreateUserTask(String taskText, Date startDate, Date endDate, int listId, String userID, Consumer<Boolean> isBusyChanged, Consumer<ResultType> finished);


  /**
   * 指定ユーザのリスト削除処理
   * @param listId 画面の選択中リストID
   * @param userId   指定ユーザID
   * @param isBusyChanged 処理中イベントコールバック
   * @param finished 処理完了イベントコールバック（処理結果）
   */
  public void DeleteList(int listId, String userId, Consumer<Boolean> isBusyChanged, Consumer<ResultType> finished);

  /**
   * 指定リストのリスト名編集処理
   * @param listId 対象リストID
   * @param listName 対象リスト名
   * @param isBusyChanged 処理中イベントコールバック
   * @param finished 処理完了イベントコールバック（処理結果）
   */
  public void UpdateList(int listId, String listName, Consumer<Boolean> isBusyChanged, Consumer<ResultType> finished);

  /**
   * 指定のユーザーおよびリストのタスク一覧を取得
   * @param userId 対象ユーザーID
   * @param listId 他仕様リストID
   * @param isBusyChanged 処理中イベントコールバック
   * @param finished 処理完了イベントコールバック（処理結果）
   */
  public void GetUserTask(String userId, int listId, Consumer<Boolean> isBusyChanged, Consumer<Pair<ResultType, List<UserTask>>> finished);

      /**
   * 指定タスクのタスク名編集処理
   * @param inputTask
   * @param isBusyChanged
   * @param finished
   */
  public void UpdateTask(int taskId, String taskText, String userID, Consumer<Boolean> isBusyChanged, Consumer<ResultType> finished);

  /**
   * 指定タスクのタスク状態編集処理
   * @param taskId
   * @param progressRate
   * @param userID
   * @param isBusyChanged
   * @param finished
   */
  public void UpdateTask(int taskId, int progressRate, String userID, Consumer<Boolean> isBusyChanged, Consumer<ResultType> finished);

    /**
   * 指定ユーザーのタスク削除処理
   * @param taskId 画面の選択中タスクID
   * @param userId 指定ユーザID
   * @param isBusyChanged 処理中イベントコールバック
   * @param finished 処理完了イベントコールバック（処理結果）
   */
  public void DeleteTask(int taskId, String userId, Consumer<Boolean> isBusyChanged, Consumer<ResultType> finished);

    /**
   * 期日編集メソッド
   */
  public void EditPeriodDate(int taskId, Date startDate, Date endDate, String userId, Consumer<Boolean> isBusyChanged, Consumer<ResultType> finished);

  /**
   * 指定ユーザのリスト一覧を取得
   * @param userId 指定ユーザID
   * @param isBusyChanged 処理中イベントコールバック
   * @param finished 処理完了イベントコールバック（処理結果、リスト一覧）
  */
  public void GetUserListAndTask(String userId, Consumer<Boolean> isBusyChanged, Consumer<Pair<ResultType, List<UserList>>> finished);

}
