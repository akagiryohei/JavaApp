package Controller.Login;

import Controller.ControllerBase;
import Entity.Enum.LogLevel;
import Entity.Enum.ViewStateEnum;
import Interface.Controller.Login.ILoginController;
import Interface.Model.Login.ILoginModel;
import Interface.Model.Process.Login.ILoginProcess.ResultType;
import Interface.View.IViewProxyUtil;
import Interface.View.Login.ILoginView;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.function.Consumer;


/*
 * ログイン画面コントローラー
 */
public class LoginController extends ControllerBase implements ILoginController
{
  /** 画面表示状態 */
  private ViewStateEnum ViewState;

  /** Viewインスタンス */
  private ILoginView View;

  /** Modelインスタンス */
  private ILoginModel Model;

  /** 画面ラップ処理インスタンス */
  private IViewProxyUtil ViewProxyUtil;

  /** ログイン中ユーザーID */
  private String LoginUserId;

  /**
   * コンストラクタ
   * 依存性注入
   * @param view ログイン画面Viewインスタンス
   * @param model ログイン画面Modelインスタンス
   * @param viewProxyUtil 画面ラップ処理インスタンス
   */
  public LoginController(ILoginView view, ILoginModel model, IViewProxyUtil viewProxyUtil)
  {
    this.ViewProxyUtil = viewProxyUtil;
    this.View = this.ViewProxyUtil.WrapView(ILoginView.class, view); 
    this.Model = model;

    // 画面状態を設定
    this.ViewState = ViewStateEnum.Close;
  }

  /**
   * 画面表示
   */
  public void Show()
  {
    if (this.ViewState != ViewStateEnum.Open)
    {
      this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "画面オープン指示"); });
      this.ViewState = ViewStateEnum.Open;
      this.View.Show();
    }
  }

  /**
   * 画面非表示
   */
  public void Hide()
  {
    if (this.ViewState != ViewStateEnum.Close)
    {
      this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "画面クローズ指示"); });
      this.ViewState = ViewStateEnum.Close;
      this.View.Hide();
    }
  }

  /**
   * 画面のインスタンスを取得
   * @return ログイン画面Viewインスタンス
   */
  public ILoginView GetViewInstance()
  {
    return this.ViewProxyUtil.UnwrapView(this.View);
  }

  public void LoginAuth(String userName, String passWord)
  {
    this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "Viewからログイン認証指示受信"); });

    // ログインボタン押下時の時刻としてプロセスに渡すための変数
    Date clickedDate = new Date();
    this.Logger.WriteLog(LogLevel.Info, LocalDateTime.ofInstant(clickedDate.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));

    this.Model.LoginAuth(userName, passWord, (isBusy) ->
    {
      // 処理中状態変化
      this.View.ElementDisabled(isBusy);
    }, (result) -> {
      this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "Model層からfinishedコールバック受信"); });
      this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "ResultType=" + result.Value1.name()); });

      // ログイン認証処理完了
      if (result.Value1 == ResultType.Success)
      {
        this.Model.GetReminderList(result.Value2.UserId, clickedDate, (isBusy) ->
        {
          // 処理中状態変化
          this.View.ElementDisabled(isBusy);
        }, (reminderResult) -> {
          if(result.Value1 == ResultType.Success)
          {
            this.View.ReminderDialogView(reminderResult.Value2, result.Value2);
          }
          else
          {
            // ダイアログ表示
            this.View.LoginFailure();
          }
        });
      }
      else
      {
        // ダイアログ表示
        this.View.LoginFailure();
      }
    });
  }

  /**
   * 入力欄のイベント情報の変化が発生した
   * @param userName ユーザ名
   * @param password メールアドレス
   */
  public void ChangedTextField(String userName, String password)
  {
    this.View.LoginButtonDisabled(!this.Model.GetLoginButtonPossibility(userName, password));
  }
}