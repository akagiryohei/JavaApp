package Model.Login;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import Entity.Pair;
import Entity.UserData;
import Entity.Dialog.ReminderList;
import Entity.Enum.LogLevel;
import Interface.Model.ILogger;
import Interface.Model.IValidationUtil;
import Interface.Model.Login.ILoginModel;
import Interface.Model.Process.Login.ILoginProcess;
import Interface.Model.Process.Login.ILoginProcess.ResultType;

/**
  * ログイン画面モデルクラス
  */
public class LoginModel extends LoginModelBase implements ILoginModel
{
  /**
   * コンストラクタ
   * 依存性注入
   * @param logger ロガーインスタンス
   * @param loginProcess ログイン処理インスタンス
  */
  public LoginModel(ILogger logger, ILoginProcess loginProcess, IValidationUtil util)
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
  public void LoginAuth(String email, String password, Consumer<Boolean> isBusyChanged, Consumer<Pair<ResultType, UserData>> finished)
  {
    int getLength = this.Util.GetWideStringLength(password);
    if (this.Util.IsEmailValid(email) && this.Util.IsPassWordValid(password) && 
        getLength > MIN_PASSWORD_LENGTH && getLength < MAX_PASSWORD_LENGTH)
    {
      isBusyChanged.accept(true);

      this.Process.Login(email, password, (isBusy) -> {
        // Viewに処理中かどうかを通知
        System.out.println(isBusy);
      }, (result) -> {
        this.Logger.WriteLog(LogLevel.Info, "Process層からfnishedコールバック受信");
        this.Logger.WriteLog(LogLevel.Info, "ResultType=" + result.Value1.name());

        if (result.Value1 == ResultType.Success)
        {
          this.Logger.WriteLog(LogLevel.Info, "ログイン認証が成功しました");
        }
        else
        {
          this.Logger.WriteLog(LogLevel.Info, "ログイン認証に失敗したため、ログイン出来ませんでした");
        }

        isBusyChanged.accept(false);
        finished.accept(result);
      });
    }
    else
    {
      isBusyChanged.accept(false);
      this.Logger.WriteLog(LogLevel.Info, "入力されたメールアドレスがフォーマット異常（"+ email + "）");
      finished.accept(new Pair<ILoginProcess.ResultType, UserData>(ILoginProcess.ResultType.ValidationError, new UserData()));
    }
  }

  /**
   * ログインボタンの押下可否を判定
   * @param email メールアドレス
   * @param password パスワード
   * @return true 押下可能, false 押下不可
  */
  public Boolean GetLoginButtonPossibility(String email, String password)
  {
    int passwordLength = this.Util.GetWideStringLength(password);
    if(this.Util.IsEmailValid(email) && passwordLength > 7 && passwordLength < 13)
    {
      return true;
    }
    return false;
  }

  private List<ArrayList<String>> ReminderListToArray(List<ReminderList> reminderList)
  {
    List<ArrayList<String>> stringList = new ArrayList<>();
    
    for(int num = 0; num < reminderList.size(); num++)
    {
      reminderList.get(num).ListName.replace("\n", "").replace("\r", "");
      reminderList.get(num).TaskContent.replace("\n", "").replace("\r", "");

      if(this.Util.GetWideStringLength(reminderList.get(num).ListName) > 20)
      {
        this.Logger.WriteLog(LogLevel.Info, "リマインダーリスト配列の " + (num + 1) + " 番目のリスト名が文字列超過により除外されました");
        continue;
      }
      if(this.Util.GetWideStringLength(reminderList.get(num).TaskContent) > 40)
      {
        this.Logger.WriteLog(LogLevel.Info, "リマインダーリスト配列の " + (num + 1) + " 番目のタスク名が文字列超過により除外されました");
        continue;
      }

      ArrayList<String> stringData = new ArrayList<>();
      stringData.add(reminderList.get(num).ListName);
      stringData.add(reminderList.get(num).TaskContent);
      if(reminderList.get(num).EndDate != null)
      {
        stringData.add(afterDateFormat.format(reminderList.get(num).EndDate));
      }
      else
      {
        stringData.add("");
      }
      stringList.add(stringData);
    }

    return stringList;
  }
}