package Controller.Login;

import View.Login.LoginView;
import Controller.ControllerBase;
import Model.Login.LoginModel;
import Model.Process.Login.LoginProcess.ResultType;
import Entity.Enum.ViewStateEnum;
import java.util.function.Consumer;


/*
 * ログイン画面コントローラー
 */
public class LoginController extends ControllerBase
{
  /** 画面表示状態 */
  private ViewStateEnum ViewState;

  /** Viewインスタンス */
  private LoginView View;

  /** Modelインスタンス */
  private LoginModel Model;

  /** ログイン中ユーザーID */
  private String LoginUserId;

  /**
   * コンストラクタ
   * 依存性注入
   * @param view ログイン画面Viewインスタンス
   * @param model ログイン画面Modelインスタンス
   */
  public LoginController(LoginView view, LoginModel model)
  {
    this.View = view;
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
      this.ViewState = ViewStateEnum.Close;
      this.View.Hide();
    }
  }

  /**
   * 画面のインスタンスを取得
   * @return ログイン画面Viewインスタンス
   */
  public LoginView GetViewInstance()
  {
    return this.View;
  }

  public void LoginAuth(String userName, String passWord)
  {
    this.Model.LoginAuth(userName, passWord, (isBusy) ->
    {
      // 処理中状態変化
      this.View.ElementDisabled(isBusy);
    }, (result) -> {
      // ログイン認証処理完了
      if (result.Value1 == ResultType.Success)
      {
        this.View.TransitionAfterLoginView(result.Value2);
      }
      else
      {
        // ダイアログ表示
        this.View.LoginFailure();
      }
    });
  }
}
