package Controller.Login;

import View.Login.SignupView;
import Controller.ControllerBase;
import Model.Login.SignupModel;
import Entity.Enum.ViewStateEnum;

/*
 * 新規アカウント追加画面コントローラ
 */
public class SignupController extends ControllerBase
{
  // 画面表示状態
  private ViewStateEnum ViewState;

  // Viewインスタンス
  private SignupView View;

  // Modelインスタンス
  private SignupModel Model;

  // コンストラクタ
  public SignupController(SignupView view, SignupModel model)
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

  // 画面のインスタンスを取得
  public SignupView GetViewInstance()
  {
    return this.View;
  }
}
