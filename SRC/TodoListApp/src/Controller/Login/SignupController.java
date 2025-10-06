package Controller.Login;

import Controller.ControllerBase;
import Entity.Enum.LogLevel;
import Entity.Enum.ViewStateEnum;
import Interface.Controller.Login.ISignupController;
import Interface.Model.Login.ISignupModel;
import Interface.Model.Process.Login.ILoginProcess.ResultType;
import Interface.View.Login.ISignupView;

/*
 * 新規アカウント追加画面コントローラ
 */
public class SignupController extends ControllerBase implements ISignupController
{
  // 画面表示状態
  private ViewStateEnum ViewState;

  // Viewインスタンス
  private ISignupView View;

  // Modelインスタンス
  private ISignupModel Model;

  // コンストラクタ
  public SignupController(ISignupView view, ISignupModel model)
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
  public ISignupView GetViewInstance()
  {
    return this.View;
  }

  public void SignupAuth(String userName, String passWord, String secretTipsId, String secretPassWord)
  {
    this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "Viewから新規アカウント登録指示受信"); });

    this.Model.SignupAuth(userName, passWord, secretTipsId, secretPassWord, (isBusy) ->
    {
      // 処理中状態変化
      this.View.ElementDisabled(isBusy);
    }, (result) -> {
      this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "Model層からfinishedコールバック受信"); });
      this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "ResultType=" + result.name()); });

      // 新規アカウント登録処理完了
      if (result == ResultType.Success)
      {
        //this.View.TransitionAfterLoginView(result.Value2);
        System.out.println("アカウント登録に成功しました");
      }
      else
      {
        // ダイアログ表示
        this.View.SignupFailure();
      }
    });
  }
  
  public void GetSecretTipsList()
  {
    this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "Viewから秘密の一覧取得指示受信"); });

    this.Model.GetSecretTipsList((isBusy) -> 
    {
      this.View.ElementDisabled(isBusy);
    }, (result) -> {
      this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "Model層からfnishedコールバック受信"); });
      this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "ResultType=" + result.Value1.name()); });

      if (result.Value1 == ResultType.Success)
      {
        // 秘密の質問一覧取得完了
        this.View.SetSecretTipsList(result.Value2);
      }
      // 秘密の質問一覧取得失敗時
      // キャンセルボタン以外のボタンを押下不可にする
      else
      {
        this.View.CancelButtonOnlyEnabled();
        this.View.GetSecretTipsListFailure();
      }
    });
  }
}