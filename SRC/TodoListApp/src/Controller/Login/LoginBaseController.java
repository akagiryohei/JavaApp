package Controller.Login;

import Controller.ControllerBase;
import Entity.Enum.ViewStateEnum;
import Model.Login.LoginBaseModel;
import View.Login.LoginBaseView;

/*
 * ログイン全般コントローラ
 */
public class LoginBaseController extends ControllerBase
{
  // 画面表示状態
  private ViewStateEnum ViewState;

  // Viewインスタンス
  private LoginBaseView View;

  // Modelインスタンス
  private LoginBaseModel Model;

  // コンストラクタ
  public LoginBaseController(LoginBaseView view, LoginBaseModel model)
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
      this.ViewState = ViewStateEnum.Open;
      this.View.Show();
    }
  }

  // 画面非表示
  public void Hide()
  {
    if (this.ViewState != ViewStateEnum.Close)
    {
      this.ViewState = ViewStateEnum.Close;
      this.View.Hide();
    }
  }

  public LoginBaseView GetViewInstance()
  {
    return this.View;
  }
}
