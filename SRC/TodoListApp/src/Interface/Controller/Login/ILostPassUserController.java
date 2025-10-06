package Interface.Controller.Login;

import Interface.Model.ILogger;
import Interface.View.Login.ILostPassUserView;

/*
 * 忘却者画面コントローラインタフェース
 */
public interface ILostPassUserController
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
   * @return 忘却者画面Viewインスタンス
   */
  public ILostPassUserView GetViewInstance();
  
  /**
    * ロガーインスタンスを依存性注入する
    * @param ロガーインスタンス
    */
  public void SetLogger(ILogger logger);

  /**
   * ユーザのメールアドレスから秘密の質問の番号を取得
   * @param email 対象アカウントのメールアドレス
   */
  public void GetSecretTips(String email);

  /**
   * 忘却者ログイン認証
   * @param userName 対象アカウントのメールアドレス
   * @param secretPassWord 秘密のパスワード
   */
  public void LostPassUserLoginAuth(String userName, String secretPassWord);
}
