package Interface.Controller.Login;

import Interface.Model.ILogger;
import Interface.View.Login.ILoginView;

/*
 * ログイン画面コントローラーインタフェース
 */
public interface ILoginController
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
   * 画面のインスタンスを取得
   * @return ログイン画面Viewインスタンス
   */
  public ILoginView GetViewInstance();

  /**
    * ロガーインスタンスを依存性注入する
    * @param ロガーインスタンス
    */
  public void SetLogger(ILogger logger);

  /**
   * ログイン認証
   * @param userName ログイン対象のメールアドレス
   * @param password ログイン対象のパスワード（平文）
   */
  public void LoginAuth(String userName, String passWord);

  /**
   * 入力欄のイベント情報の変化が発生した
   * @param userName ユーザ名
   * @param password メールアドレス
   */
  public void ChangedTextField(String userName, String password);
}