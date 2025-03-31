package Model.Process.Login;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;
import java.util.function.Consumer;

import Entity.Pair;
import Model.DBClient;
import Model.DBClient.DBClientDtoPair;
import Model.DBClient.DBResultType;

/**
  * ログイン認証機能処理クラス
  */
public class LoginProcess
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

    /** 正常終了（認証NG、アカウントが見つからなかった、パスワードが合致していない） */
    SuccessNotFoundAccount,

    /** 異常終了 */
    Failure,

    /** バリデーションエラー */
    ValidationError,

    /** タイムアウト */
    Timeout,
  }

  /**
   * コンストラクタ
   * 依存性注入
   * @param dbClient DB接続クライアントインスタンス
   * @param dbQueue  DB処理キューインスタンス
  */
  public LoginProcess(DBClient dbClient, ExecutorService dbQueue)
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
  public void Login(String email, String password, Consumer<Boolean> isBusyChanged, Consumer<Pair<ResultType, String>> finished)
  {
    /** 処理中フラグを立てる */
    isBusyChanged.accept(true);

    var task = new FutureTask<>(() -> { //akagi非同期処理をここでしてる
      // TODO: 引数passwordはハッシュ化した文字列を渡す
      return this.DBClient.CanLogin(email, password);
    }) {
      @Override
      protected void done() {
          DBClientDtoPair<DBClient.DBResultType, String> result;
          var retResult = ResultType.Failure;
          var retUserId = "";

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
              if (result.Value2 != null && result.Value2 != "-1")
              {
                // アカウントが見つかり、パスワードが合致していた
                retResult = ResultType.Success;
                retUserId = result.Value2;
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
          finished.accept(new Pair<LoginProcess.ResultType,String>(retResult, retUserId));
      }
    };

    /** DB接続処理をキューに追加 */
    this.DBQueue.submit(task);
  }
}
