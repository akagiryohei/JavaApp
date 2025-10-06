package Interface.Controller.Login;

import Interface.Model.ILogger;
import Interface.View.Login.ISignupView;

/*
 * 新規アカウント追加画面コントローラインタフェース
 */
public interface ISignupController
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
   * @return 新規アカウント追加画面Viewインスタンス
   */
  public ISignupView GetViewInstance();
  
  /**
    * ロガーインスタンスを依存性注入する
    * @param ロガーインスタンス
    */
  public void SetLogger(ILogger logger);

  /**
   * 新規アカウント登録
   * @param 新規追加アカウントのメールアドレス
   * @param 新規追加アカウントのパスワード
   * @param 新規追加アカウントの秘密のパスワードID
   * @param 新規追加アカウントの秘密のパスワード
   */
  public void SignupAuth(String userName, String passWord, String secretTipsId, String secretPassWord);

  /**
   * 秘密の一覧取得
   */
  public void GetSecretTipsList();
}
