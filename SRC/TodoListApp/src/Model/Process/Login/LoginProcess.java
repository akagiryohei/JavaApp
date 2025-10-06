package Model.Process.Login;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;
import java.util.function.Consumer;

import Entity.Pair;
import Entity.UserData;
import Entity.DB.DBClientDtoPair;
import Entity.DB.DBResultType;
import Entity.DB.User;
import Entity.Dialog.ReminderList;
import Interface.Model.IDBClient;
import Interface.Model.Process.Login.ILoginProcess;
import Interface.Model.Process.Login.ILoginProcess.ResultType;

/**
  * ログイン認証機能処理クラス
  */
public class LoginProcess implements ILoginProcess
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
  public LoginProcess(IDBClient dbClient, ExecutorService dbQueue)
  {
    this.DBClient = dbClient;
    this.DBQueue = dbQueue;
  }

  /**
   * ログイン実施
   * @param email ログイン対象アカウントのメールアドレス
   * @param password ログイン対象アカウントのパスワード
   * @param isBusyChanged 処理中イベントコールバック
   * @param finished 処理完了イベントコールバック
  */
  public void Login(String email, String password, Consumer<Boolean> isBusyChanged, Consumer<Pair<ResultType, UserData>> finished)
  {
    /** 処理中フラグを立てる */
    isBusyChanged.accept(true);

    var task = new FutureTask<>(new LoginTask(this.DBClient, email, password)) {
      @Override
      protected void done() {
        Pair<ResultType, UserData> ret = null;

        try
        {
          ret = ((Pair<ResultType, UserData>)this.get());
        }
        catch (InterruptedException | ExecutionException e)
        {
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
   * ログイン認証タスク
   */
  private class LoginTask extends LoginTaskBase implements Callable
  {
    /** DB接続クライアントインスタンス */
    private IDBClient DBClient;

    /** ログイン対象のユーザID */
    private String Email;

    /** ログイン対象のパスワード */
    private String Password;

    /**
     * コンストラクタ
     * 依存性注入
     * @param dbClient DB接続クライアントインスタンス
     * @param email ログイン対象のユーザID
     * @param password ログイン対象のパスワード
     */
    public LoginTask(IDBClient dbClient, String email, String password)
    {
        this.DBClient = dbClient;
        this.Email = email;
        this.Password = password;
    }

    /**
     * 非同期処理
     * @return 処理結果（処理結果、ログインユーザ情報）
     */
    @Override
    public Pair<ResultType, UserData> call()
    {
      Pair<ResultType, UserData> ret = null;
      ResultType retResult = ResultType.Failure;
      String hashPassword = "";

      try {
        hashPassword = this.ToHashSHA256(this.Password);
        var result = this.DBClient.CanLogin(this.Email, hashPassword);

        switch (result.Value1)
        {
          case DBResultType.Success:
            if (result.Value2 != null && result.Value2.Id != "")
            {
              // アカウントが見つかり、パスワードが合致していた
              retResult = ResultType.Success;
            }
            else
            {
              // アカウントが見つからなかった、またはパスワードが合致しなかった
              retResult = ResultType.SuccessNotFoundAccount;
            }

            break;
          case DBResultType.Failure:
            // DB処理で異常が発生した（クエリ実行エラーやライブラリエラーなど）
            retResult = ResultType.Failure;
            break;
          case DBResultType.Timeout:
            // DB処理でタイムアウトが発生した
            retResult = ResultType.Timeout;
            break;
          default:
            // 列挙体のためあり得ない
            break;
        }

        ret = new Pair<ResultType, UserData>(retResult, new UserData() {{ UserId = result.Value2.Id; Email = result.Value2.Email; }});
      }
      catch (NoSuchAlgorithmException e)
      {
        ret = new Pair<ResultType, UserData>(ResultType.Failure, new UserData());
      }

      return ret;
    }
  }

  private class SignupTask extends LoginTaskBase implements Callable
  {
    /** DB接続クライアントインスタンス */
    private IDBClient DBClient;

    /** 登録対象のユーザID */
    private String Email;

    /** 登録対象のパスワード */
    private String Password;

    /** 登録対象の秘密の質問番号 */
    private String SecretTipsId;

    /** 登録対象の秘密の質問内容 */
    private String SecretPassWord;

    /**
     * コンストラクタ
     * 依存性注入
     * @param dbClient DB接続クライアントインスタンス
     * @param email ログイン対象のユーザID
     * @param password ログイン対象のパスワード
     */
    public SignupTask(IDBClient dbClient, String email, String password, String secretTipsId, String secretPassWord)
    {
        this.DBClient = dbClient;
        this.Email = email;
        this.Password = password;
        this.SecretTipsId = secretTipsId;
        this.SecretPassWord = secretPassWord;
    }

    /**
     * 非同期処理
     * @return 処理結果（処理結果、ユーザID）
     */
    @Override
    public ResultType call()
    {
      ResultType retResult = ResultType.Failure;

      try {
        User user = new User();
        user.Email = this.Email;
        user.Password = this.ToHashSHA256(this.Password);
        user.SecretTipsId = this.SecretTipsId;
        user.SecretPassword = this.ToHashSHA256(this.SecretPassWord);

        var result = this.DBClient.InsertUser(user);

        switch (result)
        {
          case DBResultType.Success:
            retResult = ResultType.Success;
            break;
          case DBResultType.Failure:
            // DB処理で異常が発生した（クエリ実行エラーやライブラリエラーなど）
            retResult = ResultType.Failure;
            break;
          case DBResultType.Timeout:
            // DB処理でタイムアウトが発生した
            retResult = ResultType.Timeout;
            break;
          default:
            // 列挙体のためあり得ない
            break;
        }
      }
      catch (NoSuchAlgorithmException e)
      {
        retResult = ResultType.Failure;
      }

      return retResult;
    }
  }

  private class GetSecretTipsTask implements Callable
  {
    /** DB接続クライアントインスタンス */
    private IDBClient DBClient;
    /** 取得する秘密の番号のユーザのEmail */
    private String Email;

    public GetSecretTipsTask(IDBClient dbClient, String email)
    {
        this.DBClient = dbClient;
        this.Email = email;
    }

    /**
     * 非同期処理
     * @return 処理結果（処理結果、ユーザID）
     */
    @Override
    public Pair<ResultType, String> call()
    {
      Pair<ResultType, String> ret = null;
      ResultType retResult = ResultType.Failure;
      DBClientDtoPair<DBResultType, Integer> result;
      DBClientDtoPair<DBResultType, String> result2 = null;

      try {
        result = this.DBClient.GetSecretId(this.Email);

        if (result.Value1 == DBResultType.Success && result.Value2 == 0)
        {
          retResult = ResultType.SuccessNotFoundAccount;
          ret = new Pair<ResultType, String>(retResult, "");
        }
        else if(result.Value1 == DBResultType.Success)
        {
          result2 = this.DBClient.GetSecret(result.Value2);
          switch (result2.Value1)
          {
            case DBResultType.Success:
              retResult = ResultType.Success;
              break;
            case DBResultType.Failure:
              // DB処理で異常が発生した（クエリ実行エラーやライブラリエラーなど）
              retResult = ResultType.Failure;
              break;
            case DBResultType.Timeout:
              // DB処理でタイムアウトが発生した
              retResult = ResultType.Timeout;
              break;
            default:
              // 列挙体のためあり得ない
              break;
          }
          ret = new Pair<ResultType, String>(retResult, result2.Value2);
        }
        else
        {
          switch (result.Value1)
          {
            case DBResultType.Success:
              // ここに到達することはあり得ない
              break;
            case DBResultType.Failure:
              // DB処理で異常が発生した（クエリ実行エラーやライブラリエラーなど）
              retResult = ResultType.Failure;
              break;
            case DBResultType.Timeout:
              // DB処理でタイムアウトが発生した
              retResult = ResultType.Timeout;
              break;
            default:
              // 列挙体のためあり得ない
              break;
          }

          ret = new Pair<ResultType, String>(retResult, "");
        }
      }
      catch (Exception e)
      {
        ret = new Pair<ResultType,String>(ResultType.Failure, "");
      }

      return ret;
    }
  }

  /**
   * 忘却者認証タスク
   */
  private class LostPassUserLoginTask extends LoginTaskBase implements Callable
  {
    /** DB接続クライアントインスタンス */
    private IDBClient DBClient;

    /** ログイン対象のユーザID */
    private String Email;

    /** ログイン対象のパスワード */
    private String Password;

    /**
     * コンストラクタ
     * 依存性注入
     * @param dbClient DB接続クライアントインスタンス
     * @param email ログイン対象のユーザID
     * @param password ログイン対象のパスワード
     */
    public LostPassUserLoginTask(IDBClient dbClient, String email, String secretPassword)
    {
        this.DBClient = dbClient;
        this.Email = email;
        this.Password = secretPassword;
    }

    /**
     * 非同期処理
     * @return 処理結果（処理結果、ユーザID）
     */
    @Override
    public Pair<ResultType, UserData> call()
    {
      Pair<ResultType, UserData> ret = null;
      ResultType retResult = ResultType.Failure;
      String hashPassword = "";

      try {
        hashPassword = this.ToHashSHA256(this.Password);
        var result = this.DBClient.CanLostPassUserLogin(this.Email, hashPassword);

        switch (result.Value1)
        {
          case DBResultType.Success:
            if (result.Value2 != null && result.Value2.Id != "")
            {
              // アカウントが見つかり、パスワードが合致していた
              retResult = ResultType.Success;
            }
            else
            {
              // アカウントが見つからなかった、またはパスワードが合致しなかった
              retResult = ResultType.SuccessNotFoundAccount;
            }
            break;
          case DBResultType.Failure:
            // DB処理で異常が発生した（クエリ実行エラーやライブラリエラーなど）
            retResult = ResultType.Failure;
            break;
          case DBResultType.Timeout:
            // DB処理でタイムアウトが発生した
            retResult = ResultType.Timeout;
            break;
          default:
            // 列挙体のためあり得ない
            break;
        }

        ret = new Pair<ResultType, UserData>(retResult, new UserData() {{ UserId = result.Value2.Id; Email = result.Value2.Email; }});
      }
      catch (NoSuchAlgorithmException e)
      {
        ret = new Pair<ResultType, UserData>(ResultType.Failure, new UserData());
      }

      return ret;
    }
  }

  private class GetReminderTask implements Callable
  {
    /** DB接続クライアントインスタンス */
    private IDBClient DBClient;

    /** ログイン対象のユーザID */
    private String UserId;

    /** ログイン時の時刻 */
    private Date ClickedDate;

    /**
     * コンストラクタ
     * 依存性注入
     * @param dbClient DB接続クライアントインスタンス
     * @param email ログイン対象のユーザID
     * @param password ログイン対象のパスワード
     */
    public GetReminderTask(IDBClient dbClient, String userId, Date clickedDate)
    {
        this.DBClient = dbClient;
        this.UserId = userId;
        this.ClickedDate = clickedDate;
    }

    /**
     * 非同期処理
     * @return 処理結果（処理結果、ユーザID）
     */
    @Override
    public Pair<ResultType, List<ReminderList>> call()
    {
      Pair<ResultType, List<ReminderList>> ret = null;
      ResultType retResult = ResultType.Failure;

      ArrayList<ReminderList> taskList = new ArrayList<ReminderList>();

      try {
        var result = this.DBClient.GetUnFinishedTask(Integer.parseInt(this.UserId));

        for(int i=0; i < result.Value2.size(); i++)
        {
          if(result.Value2.get(i).end_date == null ||
            this.ClickedDate.before(result.Value2.get(i).end_date))
          {
            ReminderList reminder = new ReminderList();
            reminder.ListName = result.Value2.get(i).list_name;
            reminder.TaskContent = result.Value2.get(i).task_text;
            reminder.EndDate = result.Value2.get(i).end_date == null ? null : result.Value2.get(i).end_date;
            taskList.add(reminder);
          }
        }

        switch (result.Value1)
        {
          case DBResultType.Success:
            // 未完了タスクが見つかった
            retResult = ResultType.Success;
            break;
          case DBResultType.Failure:
            // DB処理で異常が発生した（クエリ実行エラーやライブラリエラーなど）
            taskList.clear();
            retResult = ResultType.Failure;
            break;
          case DBResultType.Timeout:
            // DB処理でタイムアウトが発生した
            taskList.clear();
            retResult = ResultType.Timeout;
            break;
          default:
            // 列挙体のためあり得ない
            break;
        }

        ret = new Pair<ResultType, List<ReminderList>>(retResult, taskList);
      }
      catch (Exception e)
      {
        taskList.clear();
        ret = new Pair<ResultType, List<ReminderList>>(ResultType.Failure, taskList);
      }

      return ret;
    }
  }

  /**
   * ログイン認証共通処理クラス
   */
  private abstract class LoginTaskBase
  {
    /**
     * 平文をSHA-256ハッシュ値に変換する
     * @return SHA-256ハッシュ値
     * @throws NoSuchAlgorithmException ハッシュ化に失敗
     */
    protected String ToHashSHA256(String password) throws NoSuchAlgorithmException
    {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      md.update(password.getBytes());
      byte[] hashBytes = md.digest();
      String hash = Base64.getEncoder().encodeToString(hashBytes);
      return hash;
    }
  }
  
  public void Signup(String email, String password, String secretTipsId, String secretPassword, Consumer<Boolean> isBusyChanged, Consumer<ResultType> finished)
  {
    /** 処理中フラグを立てる */
    isBusyChanged.accept(true);

    var task = new FutureTask<>(new SignupTask(this.DBClient, email, password, secretTipsId, secretPassword))
    {
      @Override
      protected void done() {
        ResultType ret = null;

        try
        {
          ret = ((ResultType)this.get());
        }
        catch (InterruptedException | ExecutionException e)
        {
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

  public void GetSecretTipsList(Consumer<Boolean> isBusyChanged, Consumer<Pair<ResultType, List<String>>> finished)
  {
    /** 処理中フラグを立てる */
    isBusyChanged.accept(true);

    var task = new FutureTask<>(() -> { //akagi非同期処理をここでしてる
      return this.DBClient.getSecretTipsList();
    }) {
      @Override
      protected void done() {
          DBClientDtoPair<DBResultType, List<String>> result;
          var retResult = ResultType.Failure;

          try {
            result = this.get();
          } catch (InterruptedException | ExecutionException e) {
            // ここでCatchするときは処理キューがキャンセルされた場合になる（キャンセルの場合は処理結果を通知しない）
            isBusyChanged.accept(false);
            return;
          }

          switch (result.Value1)
          {
            case DBResultType.Success:
              if (result.Value2 != null)
              {
                // アカウントが見つかり、パスワードが合致していた
                retResult = ResultType.Success;
              }
              else
              {
                // アカウントが見つからなかった、またはパスワードが合致しなかった
                retResult = ResultType.SuccessNotFoundAccount;
              }

              break;
            case DBResultType.Failure:
              // DB処理で異常が発生した（クエリ実行エラーやライブラリエラーなど）
              retResult = ResultType.Failure;
              break;
            case DBResultType.Timeout:
              // DB処理でタイムアウトが発生した
              retResult = ResultType.Timeout;
              break;
            default:
              // 列挙体のためあり得ない
              break;
          }

          isBusyChanged.accept(false);
          finished.accept(new Pair<ResultType,List<String>>(retResult, result.Value2));
      }
    };

    /** DB接続処理をキューに追加 */
    this.DBQueue.submit(task);
  }

  public void GetUserSecretTips(String email, Consumer<Boolean> isBusyChanged, Consumer<Pair<ResultType, String>> finished)
  {
    /** 処理中フラグを立てる */
    isBusyChanged.accept(true);

    var task = new FutureTask<>(new GetSecretTipsTask(this.DBClient, email))
    {
      @Override
      protected void done() {
        Pair<ResultType, String> ret = null;

        try
        {
          ret = ((Pair<ResultType, String>)this.get());
        }
        catch (InterruptedException | ExecutionException e)
        {
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

  public void LostPassUserLogin(String email, String secretPassword, Consumer<Boolean> isBusyChanged, Consumer<Pair<ResultType, UserData>> finished)
  {
    /** 処理中フラグを立てる */
    isBusyChanged.accept(true);

    var task = new FutureTask<>(new LostPassUserLoginTask(this.DBClient, email, secretPassword)) {
      @Override
      protected void done() {
        Pair<ResultType, UserData> ret = null;

        try
        {
          ret = ((Pair<ResultType, UserData>)this.get());
        }
        catch (InterruptedException | ExecutionException e)
        {
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

  public void GetReminderList(String email, Date clickedDate, Consumer<Boolean> isBusyChanged, Consumer<Pair<ResultType, List<ReminderList>>> finished)
  {
    /** 処理中フラグを立てる */
    isBusyChanged.accept(true);

    var task = new FutureTask<>(new GetReminderTask(this.DBClient, email, clickedDate)) {
      @Override
      protected void done() {
        Pair<ResultType, List<ReminderList>> ret = null;

        try
        {
          ret = ((Pair<ResultType, List<ReminderList>>)this.get());
        }
        catch (InterruptedException | ExecutionException e)
        {
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
}