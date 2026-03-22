package Model.Login;
import java.util.List;
import java.util.function.Consumer;
import Entity.Pair;
import Entity.Enum.LogLevel;
import Interface.Model.ILogger;
import Interface.Model.IValidationUtil;
import Interface.Model.Login.ISignupModel;
import Interface.Model.Process.Login.ILoginProcess;
import Interface.Model.Process.Login.ILoginProcess.ResultType;

public class SignupModel implements ISignupModel
{
  /** パスワードのバイト数の定数（下限）*/
  private final Integer MIN_PASSWORD_LENGTH = 7;

  /** パスワードのバイト数の定数（上限）*/
  private final Integer MAX_PASSWORD_LENGTH = 13;

  /** ログイン処理インスタンス */
  private ILoginProcess Process;

  /** ロガーインスタンス */
  private ILogger Logger;

  /** validationインスタンス */
  private IValidationUtil Util;

  /**
   * コンストラクタ
   * 依存性注入
   * @param logger ロガーインスタンス
   * @param loginProcess 新規登録処理インスタンス
  */

  public SignupModel(ILogger logger, ILoginProcess loginProcess, IValidationUtil util)
  {
    this.Process = loginProcess;
    this.Logger = logger;
    this.Util = util;
  }

  /**
   * 秘密の質問一覧取得実施
   * @param isBusyChanged 処理中イベントコールバック
   * @param finished 処理完了イベントコールバック
  */

  /** 秘密の質問一覧 **/
  public void GetSecretTipsList(Consumer<Boolean> isBusyChanged, Consumer<Pair<ResultType, List<String>>> finished)
  {
    this.Process.GetSecretTipsList((isBusy) -> {
      isBusyChanged.accept(isBusy);
    }, (result) -> {
      this.Logger.WriteLog(LogLevel.Info, "Process層からfnishedコールバック受信");
      this.Logger.WriteLog(LogLevel.Info, "ResultType=" + result.Value1.name());
      finished.accept(result);
    });
  }

  /**
   * アカウント登録実施
   * @param email 作成対象アカウントのメールアドレス
   * @param password ログイン対象アカウントのパスワード
   * @param SecretTipsId 作成対象の秘密の質問番号
   * @param SecretPassword 作成対象アカウントの秘密のパスワード
   * @param isBusyChanged 処理中イベントコールバック
  */

  public void SignupAuth(String email, String password, String SecretTipsId, String SecretPassword, Consumer<Boolean> isBusyChanged, Consumer<ResultType> finished)
  {
    int getLength = this.Util.GetWideStringLength(password);
    if (this.Util.IsEmailValid(email) && this.Util.IsPassWordValid(password) && 
        getLength > MIN_PASSWORD_LENGTH && getLength < MAX_PASSWORD_LENGTH)
    {
      this.Process.Signup(email, password, SecretTipsId, SecretPassword, (isBusy) -> {
        // Viewに処理中かどうかを通知
        isBusyChanged.accept(isBusy);
        System.out.println(isBusy);
      }, (result) -> {
        this.Logger.WriteLog(LogLevel.Info, "Process層からfnishedコールバック受信");
        this.Logger.WriteLog(LogLevel.Info, "ResultType=" + result);
        finished.accept(result);
      });
    }
    else
    {
      this.Logger.WriteLog(LogLevel.Info, "入力されたメールアドレスがフォーマット異常（"+ email + "）");
      finished.accept(ILoginProcess.ResultType.ValidationError);
    }
  }
}
