package Interface.View;

import Interface.Controller.IMainWindowController;

/**
 * MainWindowViewのインタフェース
 */
public interface IMainWindowView
{
  /**
   * 画面表示
   */
  public void Show();

  /**
   * 画面非表示
   */
  public void Hide();

  /**
   * コントローラインスタンスを設定
   * @param controller コントローラインスタンス
   */
  public void SetController(IMainWindowController controller);
  
  /**
   * 致命的エラーダイアログ表示指示
   */
  public void ShowFatalErrorDialog();
}
