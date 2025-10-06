package View.Login.Listener;

import java.util.EventListener;

/**
 * 新規アカウント登録画面イベントリスナ
 */
public interface SignupViewListener extends EventListener
{
  /**
   * 新規アカウント登録または閉じるボタン押下イベント（画面クローズ）
   */
  public void SuccessfulSignupOrCloseButtonClicked();
}
