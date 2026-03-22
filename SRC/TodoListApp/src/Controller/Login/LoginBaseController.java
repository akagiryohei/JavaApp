package Controller.Login;

import Controller.ControllerBase;
import Entity.Enum.ViewStateEnum;
import Interface.Controller.Login.ILoginBaseController;
import Interface.Model.Login.ILoginBaseModel;
import Interface.View.IViewProxyUtil;
import Interface.View.Login.ILoginBaseView;

/*
 * ログイン全般コントローラ
 */
public class LoginBaseController extends ControllerBase implements ILoginBaseController
{
  // 画面表示状態
  private ViewStateEnum ViewState;

  // Viewインスタンス
  private ILoginBaseView View;

  // Modelインスタンス
  private ILoginBaseModel Model;

  //  画面ラップ処理インスタンス
  private IViewProxyUtil ViewProxyUtil;

  // コンストラクタ
  public LoginBaseController(ILoginBaseView view, ILoginBaseModel model, IViewProxyUtil viewProxyUtil)
  {
    this.ViewProxyUtil = viewProxyUtil;
    this.View = this.ViewProxyUtil.WrapView(ILoginBaseView.class, view);
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

  public ILoginBaseView GetViewInstance()
  {
    return this.ViewProxyUtil.UnwrapView(this.View);
  }
}
