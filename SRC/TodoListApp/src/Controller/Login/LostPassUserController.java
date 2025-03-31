package Controller.Login;

import java.util.ArrayList;

import Controller.ControllerBase;
import Entity.Enum.ViewStateEnum;
import Model.Login.LostPassUserModel;
import View.Login.LostPassUserView;

/*
 * 忘却者画面コントローラ
 */
public class LostPassUserController extends ControllerBase
{
  // 画面表示状態
  private ViewStateEnum ViewState;

  // Viewインスタンス
  private LostPassUserView View;

  // Modelインスタンス
  private LostPassUserModel Model;

  public LostPassUserController(LostPassUserView view, LostPassUserModel model)
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
  public LostPassUserView GetViewInstance()
  {
    return this.View;
  }

  // ユーザ名照合用メソッド
  public ArrayList UserNameDBSelect(String userName)
  {
    ArrayList supportPullDown = new ArrayList<>();
    String DBUserName = "akagi";
    if(userName == DBUserName)
    {
      // support用のDBにアクセスしてプルダウンの中身を取得する
      return supportPullDown;
    }else
    {
      return supportPullDown;
    }
  }
}
