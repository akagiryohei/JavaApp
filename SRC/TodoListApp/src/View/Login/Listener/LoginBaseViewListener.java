package View.Login.Listener;

import java.util.EventListener;
import Entity.UserData;

/**
 * ログイン前画面基底Viewイベントリスナ
 */
public interface LoginBaseViewListener extends EventListener
{
  /**
   * ログイン成功
   * @param userData ログインユーザ情報
   */
  public void SuccessfulLogin(UserData userData);
}
