package Model.Process.Todo;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;
import java.util.function.Consumer;

import Entity.Pair;
import Entity.UserList;
import Entity.DB.ListColumn;
import Model.DBClient;
import Model.DBClient.DBResultType;

/**
  * Todoリスト処理クラス
  */
public class TodoProcess
{
  /** DB接続クライアントインスタンス */
  private DBClient DBClient;
  
  /** DB処理キューインスタンス */
  private ExecutorService DBQueue;

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
   * コンストラクタ
   * 依存性注入
   * @param dbClient DB接続クライアントインスタンス
   * @param dbQueue  DB処理キューインスタンス
  */
  public TodoProcess(DBClient dbClient, ExecutorService dbQueue)
  {
    this.DBClient = dbClient;
    this.DBQueue = dbQueue;
  }

  /**
   * 指定ユーザのリスト一覧を取得
   * @param userId 指定ユーザID
   * @param isBusyChanged 処理中イベントコールバック
   * @param finished 処理完了イベントコールバック（処理結果、リスト一覧）
  */
  public void GetUserList(String userId, Consumer<Boolean> isBusyChanged, Consumer<Pair<ResultType, List<UserList>>> finished)//akagi UserList変わる
  {
    /** 処理中フラグを立てる */
    isBusyChanged.accept(true);
    var task = new FutureTask<>(new GetUserListTask(this.DBClient, userId)) {
      @Override
      protected void done() {
        Pair<ResultType, List<UserList>> ret = null;
          try {
            ret = ((Pair<ResultType, List<UserList>>)this.get());
          } catch (InterruptedException | ExecutionException e) {
            // ここでCatchするときは処理キューがキャンセルされた場合になる（キャンセルの場合は処理結果を通知しない）
            isBusyChanged.accept(false);
            return;
          }

          isBusyChanged.accept(false);
          finished.accept(ret);
      }
    };

    /** DB接続処理をキューに追加 */
    this.DBQueue.submit(task);
  }

  /**
   * ユーザタスク一覧取得タスク
   */
  private class GetUserListTask implements Callable
  {
    /** DB接続クライアントインスタンス */
    private DBClient DBClient;

    /** 取得対象のユーザID */
    private String UserId;

    /**
     * コンストラクタ
     * 依存性注入
     * @param dbClient DB接続クライアントインスタンス
     * @param userId 取得対象のユーザID
     */
    public GetUserListTask(DBClient dbClient, String userId)
    {
        this.DBClient = dbClient;
        this.UserId = userId;
    }

    /**
     * 非同期処理
     * @return 処理結果（処理結果、リスト一覧）
     */
    @Override
    public Pair<ResultType,  List<UserList>> call()//akagi UserList
    {
        Pair<ResultType, List<UserList>> result = null;//akagi Arrayは型変えるUserList
        var retResult = ResultType.Failure;
        List<UserList> retUserList = new ArrayList<UserList>();

        try
        {
            var ret = this.DBClient.GetList(Integer.parseInt(this.UserId));
            List<ListColumn> listDate = ret.Value2;

            switch (ret.Value1)
            {
                case DBResultType.Success:
                    retResult = ResultType.Success;
                    break;
                case DBResultType.Failure:
                    retResult = ResultType.Failure;
                    break;
                case DBResultType.Timeout:
                    retResult = ResultType.Timeout;
                    break;
                default:
                    // 列挙体のためあり得ない
                    break;
            }
            
            listDate.forEach(item -> {
              UserList userList = new UserList();
              userList.id = item.id;
              userList.list_name = item.list_name;
              retUserList.add(userList);
            });

            result = new Pair<TodoProcess.ResultType, List<UserList>>(retResult, retUserList);
        }
        catch (NumberFormatException e)
        {
            result = new Pair<ResultType, List<UserList>>(ResultType.ValidateError, retUserList);
        }

        return result;
    }
  }

  /**
   * 指定ユーザのリスト登録処理
   * @param listText リスト名
   * @param userId   指定ユーザID
   * @param isBusyChanged 処理中イベントコールバック
   * @param finished 処理完了イベントコールバック（処理結果）
   */
  public void CreateUserList(String listText, String userId, Consumer<Boolean> isBusyChanged, Consumer<ResultType> finished)
  {
    /** 処理中フラグを立てる */
    isBusyChanged.accept(true);

    var task = new FutureTask<>(new CreateUserListTask(this.DBClient, listText, userId)){
      @Override
      protected void done() {
      ResultType ret = null;
        try {
          ret = ((ResultType)this.get());
        } catch (InterruptedException | ExecutionException e) {
          isBusyChanged.accept(false);
          return;
        }

        isBusyChanged.accept(false);
        finished.accept(ret);
      }
    };

    /** DB接続処理をキューに追加 */
    this.DBQueue.submit(task);
  }

  /**
   * ユーザタスク登録タスク
   */
  private class CreateUserListTask implements Callable
  {
    /** DB接続クライアントインスタンス */
    private DBClient DBClient;

    /** 登録対象のユーザID */
    private String UserId;
    
    /** リスト名 */
    private String ListText;

    /** 
     * コストラクタ
     * 依存性注入
     * @param dbClient DB接続クライアントインスタンス
     * @param userId 登録対象のユーザID
     * @param ListText リスト名
     */
    public CreateUserListTask(DBClient dbClient, String userId, String listText)
    {
      this.DBClient = dbClient;
      this.UserId = userId;
      this.ListText = listText;
    }

    /**
     * 非同期処理
     * @return 処理結果（処理結果）
     */
    @Override
    public ResultType call()
    {
      ResultType result = null;
      var retResult = ResultType.Failure;

      try
      {
        var ret = this.DBClient.InsertList(this.ListText, Integer.parseInt(this.UserId));
        switch (ret) {
          case DBResultType.Success:
            retResult = ResultType.Success;
            break;
          case DBResultType.Failure:
            retResult = ResultType.Failure;
            break;
          case DBResultType.Timeout:
            retResult = ResultType.Timeout;
            break;
          default:
            break;
        }
        result = retResult;
      }
      catch (NumberFormatException e)
      {
        result = ResultType.ValidateError;
      }

      return result;
    }
  }
}
