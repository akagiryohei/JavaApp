package Interface.View.Login;

import View.Login.Listener.LoginBaseViewListener;

/*
 * ログイン前画面基底Viewインタフェース
 */
public interface ILoginBaseView
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
   * リスナ対象追加
   * @param listener 追加対象リスナインスタンス
   */
  public void AddListener(LoginBaseViewListener listener);

  /**
   * リスナ対象削除
   * @param listener 削除対象リスナインスタンス
   */
  public void RemoveListener(LoginBaseViewListener listener);
}
