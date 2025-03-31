package Model.Login;

import java.util.function.Consumer;

import Entity.Pair;
import Model.Logger;
import Model.ValidationUtil;
import Model.Process.Login.LoginProcess;
import Model.Process.Login.LoginProcess.ResultType;

/**
  * ログイン画面モデルクラス
  */
public class LoginModel
{
  /** ログイン処理インスタンス */
  private LoginProcess Process;

  /** ロガーインスタンス */
  private Logger Logger;

  /** validationインスタンス */
  private ValidationUtil Util;

  /**
   * コンストラクタ
   * 依存性注入
   * @param logger ロガーインスタンス
   * @param loginProcess ログイン処理インスタンス
  */
  public LoginModel(Logger logger, LoginProcess loginProcess, ValidationUtil util)
  {
    this.Process = loginProcess;
    this.Logger = logger;
    this.Util = util;
  }

  /**
   * ログイン認証実施
   * @param email ログイン対象アカウントのメールアドレス
   * @param password ログイン対象アカウントのパスワード
   * @param isBusyChanged 処理中イベントコールバック
  */
  public void LoginAuth(String email, String password, Consumer<Boolean> isBusyChanged, Consumer<Pair<ResultType, String>> finished)
  {
    // validationUtilInstance.IsEmailValid
    int getLength = this.Util.GetStringLength(password);
    if (this.Util.IsEmailValid(email) && this.Util.IsPassWordValid(password) && 
        getLength > 7 && getLength < 13)
    {
      this.Process.Login(email, password, (isBusy) -> {
        // Viewに処理中かどうかを通知
        isBusyChanged.accept(isBusy);
        System.out.println(isBusy);
      }, (result) -> {
        finished.accept(result);
      });
    }
    else
    {
      finished.accept(new Pair<LoginProcess.ResultType,String>(LoginProcess.ResultType.ValidationError, ""));
    }
  }
}
