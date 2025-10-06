package View.Login.Listener;

import java.util.EventListener;

import Entity.UserData;

/**
 * ログイン画面イベントリスナ
 */
public interface LoginViewListener extends EventListener
{
  /**
   * ログイン成功
   */
  public void SuccessfulPasswordLogin(UserData userData);

  /**
   * 新規アカウント登録ボタン押下イベント
   */
  public void CreateAccountButtonClicked();

  /**
   * パスワード忘却者へボタン押下イベント
   */
  public void LostPassUserButtonClicked();
}