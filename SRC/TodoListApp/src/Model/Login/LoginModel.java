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
public class LoginModel implements ILoginModel
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
  public void LoginAuth(String email, String password, Consumer<Boolean> isBusyChanged, Consumer<Pair<ResultType, UserData>> finished, Consumer<Pair<ResultType, Pair<List<ArrayList<String>>, UserData>>> reminderList)
  {
    int getLength = this.Util.GetStringLength(password);
    if (this.Util.IsEmailValid(email) && this.Util.IsPassWordValid(password) && 
        getLength > 7 && getLength < 13)
    {
      // ログインボタン押下時の時刻としてプロセスに渡すための変数
      Date clickedDate = new Date();
      this.Logger.WriteLog(LogLevel.Info, LocalDateTime.ofInstant(clickedDate.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));

      isBusyChanged.accept(true);

      this.Process.Login(email, password, (isBusy) -> {
        // Viewに処理中かどうかを通知
        System.out.println(isBusy);
      }, (result) -> {
        this.Logger.WriteLog(LogLevel.Info, "Process層からfnishedコールバック受信");
        this.Logger.WriteLog(LogLevel.Info, "ResultType=" + result.Value1.name());

        if (result.Value1 == ResultType.Success)
        {
          this.Process.GetReminderList(result.Value2.UserId, clickedDate, (isBusy) -> {
          }, (result2) -> {
            isBusyChanged.accept(false);
            if(result2.Value1 == ResultType.Success)
            {
              if(result2.Value2.size() > 0)
              {
                List<ArrayList<String>> reminderArrayList = new ArrayList<>(ReminderListToArray(result2.Value2));
                Pair<List<ArrayList<String>>, UserData> reminderPair = new Pair<List<ArrayList<String>>, UserData>(reminderArrayList, result.Value2);

                this.Logger.WriteLog(LogLevel.Info, "リマインダー情報取得に成功、リマインダーが存在した");
                reminderList.accept(new Pair<ILoginProcess.ResultType,Pair<List<ArrayList<String>>, UserData>>(result2.Value1, reminderPair));
              }
              else
              {
                this.Logger.WriteLog(LogLevel.Info, "リマインダー情報取得に成功したが、リマインダーが０件だった。");
                finished.accept(result);
              }
            }
            else
            {
              List<ArrayList<String>> reminderArrayList = new ArrayList<>(ReminderListToArray(result2.Value2));
              Pair<List<ArrayList<String>>, UserData> reminderPair = new Pair<List<ArrayList<String>>, UserData>(reminderArrayList, result.Value2);

              this.Logger.WriteLog(LogLevel.Info, "認証は正常終了したが、リマインダー情報取得で異常終了した。");
              reminderList.accept(new Pair<ILoginProcess.ResultType,Pair<List<ArrayList<String>>, UserData>>(result2.Value1, reminderPair));
            }
          });
        }
        else
        {
          isBusyChanged.accept(false);
          this.Logger.WriteLog(LogLevel.Info, "認証で異常終了だったので、リマインダー情報取得は実施せず異常終了とする");
          finished.accept(result);
        }
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
    int passwordLength = this.Util.GetStringLength(password);
    if(this.Util.IsEmailValid(email) && passwordLength > 7 && passwordLength < 13)
    {
      return true;
    }
    return false;
  }

  private List<ArrayList<String>> ReminderListToArray(List<ReminderList> reminderList)
  {
    List<ArrayList<String>> stringList = new ArrayList<>();
    SimpleDateFormat afterDateFormat = new SimpleDateFormat("yyyy年MM月dd日");

    for(int num = 0; num < reminderList.size(); num++)
    {
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