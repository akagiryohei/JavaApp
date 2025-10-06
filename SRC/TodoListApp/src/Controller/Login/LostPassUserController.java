package Controller.Login;

import java.util.ArrayList;

import Controller.ControllerBase;
import Entity.Enum.LogLevel;
import Entity.Enum.ViewStateEnum;
import Interface.Controller.Login.ILostPassUserController;
import Interface.Model.Login.ILostPassUserModel;
import Interface.Model.Process.Login.ILoginProcess.ResultType;
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

  public LostPassUserController(ILostPassUserView view, ILostPassUserModel model)
  {
    this.View = view;
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
    return this.View;
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
          this.View.InputContentFailureDialog();
        }
        else
        {
          // ダイアログ表示
          this.View.FailureDialog();
        }
      }
    });
  }

  // ユーザのメールアドレスから秘密の質問の番号を取得
  public void LostPassUserLoginAuth(String userName, String secretPassWord)
  {
    this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "Viewから忘却ログイン指示受信"); });

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
        this.View.TransitionAfterLoginView(result.Value2);
      }
      else
      {
        // ヒント内容のコンボボックスをもとに戻す
        this.View.SetSecretInitialTips();
        // ダイアログ表示
        this.View.LoginFailure();
      }
    });
  }
}