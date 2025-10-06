package View.Login.Listener;

import java.util.EventListener;

import Entity.UserData;

/**
 * 忘却者画面イベントリスナ
 */
public interface LostPassUserViewListener extends EventListener
{
  /**
   * 忘却者ログイン成功
   */
  public void SuccessfulLostPasswordLogin(UserData userData);

  /**
   * 閉じるボタン押下イベント
   */
  public void CloseButtonClicked();
}
