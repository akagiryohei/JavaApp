package Controller.Login;

import java.util.Date;
import java.util.ArrayList;
import Controller.ControllerBase;
import Entity.Enum.LogLevel;
import Entity.Enum.ViewStateEnum;
import Interface.Controller.Login.ILostPassUserController;
import Interface.Model.Login.ILostPassUserModel;
import Interface.Model.Process.Login.ILoginProcess.ResultType;
import Interface.View.IViewProxyUtil;
import Interface.View.Login.ILostPassUserView;

/*
 * 忘却者画面コントローラ
 */
public class LostPassUserController extends ControllerBase implements ILostPassUserController
{
  // 画面表示状態
  private ViewStateEnum ViewState;

  // Viewインスタンス
  private ILostPassUserView View;

  // Modelインスタンス
  private ILostPassUserModel Model;

  /** 画面ラップ処理インスタンス */
  private IViewProxyUtil ViewProxyUtil;

  public LostPassUserController(ILostPassUserView view, ILostPassUserModel model, IViewProxyUtil viewProxyUtil)
  {
    this.ViewProxyUtil = viewProxyUtil;
    this.View = this.ViewProxyUtil.WrapView(ILostPassUserView.class, view);
    this.Model = model;

    // 画面状態を設定
    this.ViewState = ViewStateEnum.Close;
  }
  
  // 画面表示
  public void Show()
  {
    if (this.ViewState != ViewStateEnum.Open)
    {
      this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "画面オープン指示"); });
      this.ViewState = ViewStateEnum.Open;
      this.View.Show();
    }
  }

  // 画面非表示
  public void Hide()
  {
    if (this.ViewState != ViewStateEnum.Close)
    {
      this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "画面クローズ指示"); });
      this.ViewState = ViewStateEnum.Close;
      this.View.Hide();
    }
  }

  // 画面のインスタンスを取得
  public ILostPassUserView GetViewInstance()
  {
    return this.ViewProxyUtil.UnwrapView(this.View);
  }

  // ユーザのメールアドレスから秘密の質問の番号を取得
  public void GetSecretTips(String email)
  {
    this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "Viewからメールアドレスから秘密の質問の番号を取得指示受信"); });

    this.Model.GetUserSecretTips(email, (isBusy) -> 
    {
      this.View.ElementDisabled(isBusy);
    }, (result) -> {
      this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "Model層からfinishedコールバック受信"); });
      this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "ResultType=" + result.Value1.name()); });

      if (result.Value1 == ResultType.Success)
      {
        // 秘密の質問取得完了
        this.View.SetSecretTips(result.Value2);
      }
      // 秘密の質問一覧取得失敗時
      // キャンセルボタン以外のボタンを押下不可にする
      else
      {
        // ヒント内容のコンボボックスをもとに戻す
        this.View.SetSecretInitialTips();
        if (result.Value1 == ResultType.SuccessNotFoundAccount)
        {
          // ダイアログ表示
          this.View.ShowInputContentFailureDialog();
        }
        else
        {
          // ダイアログ表示
          this.View.ShowDBConnectionFailureDialog();
        }
      }
    });
  }

  // ユーザのメールアドレスから秘密の質問の番号を取得
  public void LostPassUserLoginAuth(String userName, String secretPassWord)
  {
    this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "Viewから忘却ログイン指示受信"); });
    Date clickedDate = new Date();

    this.Model.LostPassUserLoginAuth(userName, secretPassWord, (isBusy) ->
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
        }, (reminderList) -> {
          if(reminderList.Value1 == ResultType.Success)
          {
            this.View.ReminderDialogView(reminderList.Value2, result.Value2);
          }
          else
          {
            this.View.ShowLoginFailureDialog();
          }
        });
      }
      else
      {
        // ヒント内容のコンボボックスをもとに戻す
        this.View.SetSecretInitialTips();
        // ダイアログ表示
        this.View.ShowLoginFailureDialog();
      }
    });
  }

  /**
   * 入力欄のイベント情報の変化が発生した
   * @param userName ユーザ名
   */
  public void ChangedTextField(String userName)
  {
    this.View.SupportButtonDisabled(this.Model.GetSupportButtonPossibility(userName));
  }

  /**
   * 入力欄のイベント情報の変化が発生した
   * @param userName ユーザ名
   */
  public void ChangedTextField(String userName, String SecretPassword)
  {
    this.View.LoginButtonDisabled(this.Model.GetLoginButtonPossibility(userName, SecretPassword));
  }
}