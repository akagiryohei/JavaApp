package Model.Login;
import java.util.List;
import java.util.function.Consumer;
import Entity.Pair;
import Entity.UserData;
import Entity.Enum.LogLevel;
import Interface.Model.ILogger;
import Interface.Model.IValidationUtil;
import Interface.Model.Login.ILostPassUserModel;
import Interface.Model.Process.Login.ILoginProcess;
import Interface.Model.Process.Login.ILoginProcess.ResultType;
  
public class LostPassUserModel implements ILostPassUserModel
{
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
  public LostPassUserModel(ILogger logger, ILoginProcess loginProcess, IValidationUtil util)
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
  public void GetUserSecretTips(String email, Consumer<Boolean> isBusyChanged, Consumer<Pair<ResultType, String>> finished)
  {
    this.Process.GetUserSecretTips(email, (isBusy) -> {
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
   * @param secretPassword 作成対象アカウントの秘密のパスワード
   * @param isBusyChanged 処理中イベントコールバック
  */

  public void LostPassUserLoginAuth(String email, String secretPassword, Consumer<Boolean> isBusyChanged, Consumer<Pair<ResultType, UserData>> finished)
  {
    // validationUtilInstance.IsEmailValid
    int getLength = this.Util.GetStringLength(secretPassword);
    if (this.Util.IsEmailValid(email) && getLength <= 20)
    {
      this.Process.LostPassUserLogin(email, secretPassword, (isBusy) -> {
        // Viewに処理中かどうかを通知
        isBusyChanged.accept(isBusy);
        System.out.println(isBusy);
      }, (result) -> {
        this.Logger.WriteLog(LogLevel.Info, "Process層からfnishedコールバック受信");
        this.Logger.WriteLog(LogLevel.Info, "ResultType=" + result.Value1.name());
        finished.accept(result);
      });
    }
    else
    {
      this.Logger.WriteLog(LogLevel.Info, "入力されたメールアドレスがフォーマット異常（"+ email + "）");
      finished.accept(new Pair<ResultType, UserData>(ResultType.ValidationError, new UserData()));
    }
  }
}
