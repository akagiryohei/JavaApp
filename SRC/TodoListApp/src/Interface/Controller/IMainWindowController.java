package Interface.Controller;

/*
 * 画面全般コントローラインタフェース
 */
public interface IMainWindowController
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
   * 致命的なエラーが発生した
   */
  public void NotifyFatalErrorOccurred();

  /**
   * アプリ終了
   */
  public void TerminateApplication(boolean hasError);
}
