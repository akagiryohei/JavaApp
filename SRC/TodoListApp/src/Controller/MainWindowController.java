package Controller;

import Entity.Enum.ViewStateEnum;
import Interface.Controller.IMainWindowController;
import Interface.Model.IMainWindowModel;
import Interface.View.IMainWindowView;
import Interface.View.IViewProxyUtil;

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

  // 画面ラップ処理インスタンス
  private IViewProxyUtil ViewProxyUtil;

  // コンストラクタ
  public MainWindowController(IMainWindowView view, IMainWindowModel model, IViewProxyUtil viewProxyUtil)
  {
    this.ViewProxyUtil = viewProxyUtil;
    this.View = this.ViewProxyUtil.WrapView(IMainWindowView.class, view);
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

  // 致命的なエラーが発生した
  public void NotifyFatalErrorOccurred()
  {
    this.View.ShowFatalErrorDialog();
  }

  // アプリ終了
  public void TerminateApplication(boolean hasError)
  {
    this.Hide();
    System.exit(hasError ? 1 : 0);
  }
}
