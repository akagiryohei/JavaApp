package Controller;

import Entity.Enum.ViewStateEnum;
import Interface.Controller.IMainWindowController;
import Interface.Model.IMainWindowModel;
import Interface.View.IMainWindowView;

/*
 * 画面全般コントローラ
 */
public class MainWindowController extends ControllerBase implements IMainWindowController
{
  // 画面表示状態
  private ViewStateEnum ViewState;

  // Viewインスタンス
  private IMainWindowView View;

  // Modelインスタンス
  private IMainWindowModel Model;

  // コンストラクタ
  public MainWindowController(IMainWindowView view, IMainWindowModel model)
  {
    this.View = view;
    this.Model = model;

    // 画面状態を設定
    this.ViewState = ViewStateEnum.Close; // ControllerBaseにてフィールド宣言をしてるためthis.使用可能
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
}
