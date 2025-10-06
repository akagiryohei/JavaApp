package Interface.Controller.Login;

import Interface.View.Login.ILoginBaseView;

/*
 * ログイン全般コントローラインタフェース
 */
public interface ILoginBaseController
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
   * 画面Viewインスタンスを取得
   * @return 画面Viewインスタンス
   */
  public ILoginBaseView GetViewInstance();
}
