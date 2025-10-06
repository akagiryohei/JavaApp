package Model.Process.Todo;

import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.function.Consumer;

import com.mysql.cj.x.protobuf.MysqlxCrud.Update;
import Entity.Pair;
import Entity.UserList;
import Entity.UserTask;
import Entity.DB.ListColumn;
import Interface.Model.IDBClient;
import Interface.Model.Process.Todo.ITodoProcess;
import Interface.Model.Process.Todo.ITodoProcess.ResultType;
import Entity.DB.DBResultType;
import Entity.DB.InputTask;
import Entity.DB.TaskColumn;

/**
  * Todoリスト処理クラス
  */
public class TodoProcess implements ITodoProcess
{
  /** DB接続クライアントインスタンス */
  private IDBClient DBClient;
  
  /** DB処理キューインスタンス */
  private ExecutorService DBQueue;
  
  /**
   * コンストラクタ
   * 依存性注入
   * @param dbClient DB接続クライアントインスタンス
   * @param dbQueue  DB処理キューインスタンス
  */
  public TodoProcess(IDBClient dbClient, ExecutorService dbQueue)
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
   * ユーザリスト一覧取得タスク
   */
  private class GetUserListTask implements Callable
  {
    /** DB接続クライアントインスタンス */
    private IDBClient DBClient;

    /** 取得対象のユーザID */
    private String UserId;

    /**
     * コンストラクタ
     * 依存性注入
     * @param dbClient DB接続クライアントインスタンス
     * @param userId 取得対象のユーザID
     */
    public GetUserListTask(IDBClient dbClient, String userId)
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
              userList.listName = item.list_name;
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

    var task = new FutureTask<>(new CreateUserListTask(this.DBClient, userId, listText)){
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
   * ユーザリスト登録タスク
   */
  private class CreateUserListTask implements Callable
  {
    /** DB接続クライアントインスタンス */
    private IDBClient DBClient;

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
    public CreateUserListTask(IDBClient dbClient, String userId, String listText)
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

  /**
   * 指定ユーザーのタスク登録処理
   * @param taskText
   * @param startDate
   * @param endDate
   * @param listId
   */
  public void CreateUserTask(String taskText, Date startDate, Date endDate, int listId, String userID, Consumer<Boolean> isBusyChanged, Consumer<ResultType> finished)
  {
    /** 処理中フラグを立てる */
    isBusyChanged.accept(true);

    var task = new FutureTask<>(new CreateUserTaskTask(this.DBClient, taskText, startDate, endDate, listId, userID)) {
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
   * ユーザータスク登録タスク
   */
  private class CreateUserTaskTask implements Callable
  {
    /** DB接続クライアントインスタンス */
    private IDBClient DBClient;

    /** タスク名 */
    private String TaskText;

    /** 開始期日*/
    private Date StartDate;

    /** 終了期日 */
    private Date EndDate;

    /** リストID */
    private int ListId;

    /** 登録対象のユーザID */
    private String UserId;
    
    public CreateUserTaskTask(IDBClient dbClient, String taskText, Date startDate, Date endDate, int listId, String userID)
    {
      this.DBClient = dbClient;
      this.TaskText = taskText;
      this.StartDate = startDate;
      this.EndDate = endDate;
      this.ListId = listId;
      this.UserId = userID;
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
        InputTask inputTask = new InputTask();
        inputTask.TaskText = this.TaskText;
        inputTask.StartDate = this.StartDate == null ? null : new java.sql.Date(this.StartDate.getTime());
        inputTask.EndDate = this.EndDate == null ? null : new java.sql.Date(this.EndDate.getTime());
        inputTask.ListId = this.ListId;
        inputTask.UserId = Integer.parseInt(this.UserId);

        var ret = this.DBClient.CreateTask(inputTask);
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



  /**
   * 指定ユーザのリスト削除処理
   * @param listId 画面の選択中リストID
   * @param userId   指定ユーザID
   * @param isBusyChanged 処理中イベントコールバック
   * @param finished 処理完了イベントコールバック（処理結果）
   */
  public void DeleteList(int listId, String userId, Consumer<Boolean> isBusyChanged, Consumer<ResultType> finished)
  {
    /** 処理中フラグを立てる */
    isBusyChanged.accept(true);
    var task = new FutureTask<>(new DeleteListTask(this.DBClient, userId, listId)){
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
   * ユーザーリスト削除タスク
   */
  private class DeleteListTask implements Callable
  {
    /** DB接続クライアントインスタンス */
    private IDBClient DBClient;

    /** 削除対象のユーザID */
    private String UserId;

    /** 削除対象のリストID */
    private int ListId;

    /**
     * コンストラクタ
     * 依存性注入
     * @param dbClient DB接続クライアントインスタンス
     * @param userId 削除対象のユーザID
     * @param listId 削除対象のリストID
     */
    public DeleteListTask(IDBClient dbClient, String userId, int listId)
    {
      this.DBClient = dbClient;
      this.UserId = userId;
      this.ListId = listId;
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
        var ret = this.DBClient.DeleteList(this.ListId, Integer.parseInt(this.UserId));
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

  /**
   * 指定リストのリスト名編集処理
   * @param listId
   * @param listName
   * @param isBusyChanged
   * @param finished
   */
  public void UpdateList(int listId, String listName, Consumer<Boolean> isBusyChanged, Consumer<ResultType> finished)
  {
    /** 処理中フラグを立てる */
    isBusyChanged.accept(true);

    var task = new FutureTask<>(new UpdateListTask(this.DBClient, listId, listName)){
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
   * リスト名編集タスク
   */
  private class UpdateListTask implements Callable
  {
    /** DB接続クライアントインスタンス */
    private IDBClient DBClient;
    /** 編集対象のリストID */
    private int ListId;
    /** 編集対象のリスト名 */
    private String ListName;

    /**
     * コンストラクタ
     * 依存性注入
     * @param dbClient DB接続クライアントインスタンス
     * @param listId 編集対象のリストID
     * @param listName 編集対象のリスト名
     */
    public UpdateListTask(IDBClient dbClient, int listId, String listName)
    {
      this.DBClient = dbClient;
      this.ListId = listId;
      this.ListName = listName;
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
        var ret = this.DBClient.UpdateList(this.ListName, this.ListId);
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

  /**
   * 指定のユーザーおよびリストのタスク一覧を取得
   * 
   */
  public void GetUserTask(String userId, int listId, Consumer<Boolean> isBusyChanged, Consumer<Pair<ResultType, List<UserTask>>> finished){
    /** 処理中フラグを立てる */
    isBusyChanged.accept(true);
    
    var task = new FutureTask<>(new GetUserTaskTask(this.DBClient, userId, listId))
    {
      @Override
      protected void done()
      {
        Pair<ResultType, List<UserTask>> ret = null;
        try {
          ret = ((Pair<ResultType, List<UserTask>>)this.get());
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
   * ユーザータスク取得タスク
   */
  private class GetUserTaskTask implements Callable
  {
    /** DB接続クライアントインスタンス */
    private IDBClient DBClient;

    /** 取得対象のユーザID */
    private String UserId;

    /** 取得対象のリストID */
    private int ListId;

    /**
     * コンストラクタ
     * 依存性注入
     * @param dbClient DB接続クライアントインスタンス
     * @param userId 取得対象のユーザID
     * @param listId 取得対象のリストID
     */
    public GetUserTaskTask(IDBClient dbClient, String userId, int listId)
    {
      this.DBClient = dbClient;
      this.UserId = userId;
      this.ListId = listId;
    }

    /**
     * 非同期処理
     * @return 処理結果（処理結果、リスト一覧）
     */
    @Override
    public Pair<ResultType, List<UserTask>> call()
    {
      Pair<ResultType, List<UserTask>> result = null;
      var retResult = ResultType.Failure;
      List<UserTask> retUserTask = new ArrayList<UserTask>();

      try
      {
        var ret = this.DBClient.GetTask(Integer.parseInt(this.UserId), this.ListId);
        List<TaskColumn> taskDate = ret.Value2;

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

        /** DB取得のDateを指定フォーマットに変換する処理 */
        taskDate.forEach(item -> {
          UserTask userTask = new UserTask();
          userTask.id = item.id;
          userTask.taskText = item.task_text;
          userTask.list_id = item.list_id;
          SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
          String strDate = item.start_date == null ? "" : dateFormat.format(item.start_date);
          userTask.startDate = strDate;
          strDate = item.end_date == null ? "" :dateFormat.format(item.end_date);
          userTask.endDate = strDate;
          userTask.taskStatus = item.task_status;
          userTask.progressRate = item.progress_rate;
          retUserTask.add(userTask);
        });

        result = new Pair<TodoProcess.ResultType, List<UserTask>>(retResult, retUserTask);
      }
      catch (NumberFormatException e)
      {
        result = new Pair<ResultType, List<UserTask>>(ResultType.ValidateError, retUserTask);
      }

      return result;
    }
  }

    /**
   * 指定タスクのタスク名編集処理
   * @param inputTask
   * @param isBusyChanged
   * @param finished
   */
  public void UpdateTask(int taskId, String taskText, String userID, Consumer<Boolean> isBusyChanged, Consumer<ResultType> finished){
    /** 処理中フラグを立てる */
    isBusyChanged.accept(true);

    var task = new FutureTask<>(new UpdateTaskTask(this.DBClient, taskId, taskText, userID)){
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
   * タスク名編集タスク
   */
  private class UpdateTaskTask implements Callable
  {
    /** DB接続クライアントインスタンス */
    private IDBClient DBClient;

    /** タスクID */
    private int TaskId;
    /** タスク名 */
    private String TaskText;
    /** ユーザーID */
    private String UserId;
    /** タスク状態 */
    private int ProgressRate;
    /** タスクの編集フラグ */
    private Boolean IsEditProgressRate;

    /**
     * コンストラクタ
     * 依存性注入
     * @param dbClient DB接続クライアントインスタンス
     * @param inputTask 選択中タスクの情報
     */
    public UpdateTaskTask(IDBClient dbClient, int taskId, String taskText, String userID)
    {
      this.DBClient = dbClient;
      this.TaskId = taskId;
      this.TaskText = taskText;
      this.UserId = userID;
      this.ProgressRate = 0;
      this.IsEditProgressRate = false;
    }

    public UpdateTaskTask(IDBClient dbClient, int taskId, int progressRate, String userID)
    {
      this.DBClient = dbClient;
      this.TaskId = taskId;
      this.ProgressRate = progressRate;
      this.UserId = userID;
      this.IsEditProgressRate = true;
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
      var ret = DBResultType.Failure;
      int taskPercent = 0;
      try
      {
        if (this.IsEditProgressRate == false)
        {
          ret = this.DBClient.UpdateTask(this.TaskId, this.TaskText, Integer.parseInt(this.UserId));
        }
        else
        {
          ret = this.DBClient.UpdateTask(this.TaskId, Integer.parseInt(this.UserId), this.ProgressRate);
        }
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

  /**
   * 指定タスクのタスク状態編集処理
   * @param taskId
   * @param progressRate
   * @param userID
   * @param isBusyChanged
   * @param finished
   */
  public void UpdateTask(int taskId, int progressRate, String userID, Consumer<Boolean> isBusyChanged, Consumer<ResultType> finished)
  {
    var task = new FutureTask<>(new UpdateTaskTask(this.DBClient, taskId, progressRate, userID))
    {
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
   * 指定ユーザーのタスク削除処理
   * @param taskId 画面の選択中タスクID
   * @param userId 指定ユーザID
   * @param isBusyChanged 処理中イベントコールバック
   * @param finished 処理完了イベントコールバック（処理結果）
   */
  public void DeleteTask(int taskId, String userId, Consumer<Boolean> isBusyChanged, Consumer<ResultType> finished)
  {
    /** 処理中フラグを立てる */
    isBusyChanged.accept(true);
    var task = new FutureTask<>(new DeleteTaskTask(this.DBClient, userId, taskId)){
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
   * ユーザータスク削除タスク
   */
  private class DeleteTaskTask implements Callable
  {
    /** DB接続クライアントインスタンス */
    private IDBClient DBClient;

    /** 削除対象のユーザID */
    private String UserId;

    /** 削除対象のタスクID */
    private int TaskId;

    /**
     * コンストラクタ
     * 依存性注入
     * @param dbClient DB接続クライアントインスタンス
     * @param userId 削除対象のユーザID
     * @param taskId 削除対象のタスクID
     */
    public DeleteTaskTask(IDBClient dbClient, String userId, int taskId)
    {
      this.DBClient = dbClient;
      this.UserId = userId;
      this.TaskId = taskId;
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
        var ret = this.DBClient.DeleteTask(this.TaskId, Integer.parseInt(this.UserId));
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

  /**
   * 期日編集メソッド
   */
  public void EditPeriodDate(int taskId, Date startDate, Date endDate, String userId, Consumer<Boolean> isBusyChanged, Consumer<ResultType> finished)
  {
    /** 処理中フラグを立てる */
    var task = new FutureTask<>(new EditPeriodDateTask(this.DBClient, taskId, startDate, endDate, userId)){
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
        finished.accept(ret);      }
    };
    /** DB接続処理をキューに追加 */
    this.DBQueue.submit(task);
  }

  /**
   * ユーザータスク期日編集タスク
   */
  private class EditPeriodDateTask implements Callable
  {
    /** DB接続クライアントインスタンス */
    private IDBClient DBClient;

    /** 削除対象のユーザID */
    private String UserId;

    /** 削除対象のタスクID */
    private int TaskId;

    /** 開始日 */
    private Date StartDate;

    /** 終了日 */
    private Date EndDate;

    /**
     * コンストラクタ
     * 依存性注入
     */
    public EditPeriodDateTask(IDBClient dbClient, int taskId, Date startDate, Date endDate, String userId)
    {
      this.DBClient = dbClient;
      this.UserId = userId;
      this.TaskId = taskId;
      this.StartDate = startDate;
      this.EndDate = endDate;
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
        InputTask inputTask = new InputTask();
        inputTask.StartDate = this.StartDate == null ? null : new java.sql.Date(this.StartDate.getTime());
        inputTask.EndDate = this.EndDate == null ? null : new java.sql.Date(this.EndDate.getTime());
        inputTask.UserId = Integer.parseInt(this.UserId);
        inputTask.TaskId = this.TaskId;


        var ret = this.DBClient.EditPeriodDate(inputTask);
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

  /**
   * 指定ユーザのリスト一覧を取得
   * @param userId 指定ユーザID
   * @param isBusyChanged 処理中イベントコールバック
   * @param finished 処理完了イベントコールバック（処理結果、リスト一覧）
  */
  public void GetUserListAndTask(String userId, Consumer<Boolean> isBusyChanged, Consumer<Pair<ResultType, List<UserList>>> finished)
  {
    /** 処理中フラグを立てる */
    isBusyChanged.accept(true);
    var task = new FutureTask<>(new GetUserListAndTaskTask(this.DBClient, userId)) {
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
   * ユーザリスト一覧取得タスク
   */
  private class GetUserListAndTaskTask implements Callable
  {
    /** DB接続クライアントインスタンス */
    private IDBClient DBClient;

    /** 取得対象のユーザID */
    private String UserId;

    /**
     * コンストラクタ
     * 依存性注入
     * @param dbClient DB接続クライアントインスタンス
     * @param userId 取得対象のユーザID
     */
    public GetUserListAndTaskTask(IDBClient dbClient, String userId)
    {
        this.DBClient = dbClient;
        this.UserId = userId;
    }

    /**
     * 非同期処理
     * @return 処理結果（処理結果、リスト一覧）
     */
    @Override
    public Pair<ResultType,  List<UserList>> call()
    {
        Pair<ResultType, List<UserList>> result = null;
        var retResult = ResultType.Failure;
        List<UserList> retUserList = new ArrayList<UserList>();
        List<UserTask> retUserTask = new ArrayList<UserTask>();

        try
        {
            var ret = this.DBClient.GetListTask(Integer.parseInt(this.UserId));
            List<ListColumn> listData = ret.Value2;

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
            for (ListColumn item : listData) {
              UserList userList = new UserList();
              userList.id = item.id;
              userList.listName = item.list_name;
              for (TaskColumn taskItem : item.taskList) {
                UserTask userTask = new UserTask();
                userTask.id = taskItem.id;
                userTask.taskText = taskItem.task_text;
                userTask.startDate = taskItem.start_date == null ? "" : new SimpleDateFormat("yyyy-MM-dd").format(taskItem.start_date);
                userTask.endDate = taskItem.end_date == null ? "" : new SimpleDateFormat("yyyy-MM-dd").format(taskItem.end_date);
                userTask.taskStatus = taskItem.task_status;
                userTask.list_id = item.id;
                userList.tasks.add(userTask);
              }
              retUserList.add(userList);
            }
            result = new Pair<ResultType, List<UserList>>(retResult, retUserList);
        }
        catch (NumberFormatException e)
        {
            result = new Pair<ResultType, List<UserList>>(ResultType.ValidateError, retUserList);
        }

        return result;
    }
  }
}

