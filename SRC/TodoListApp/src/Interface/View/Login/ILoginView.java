package Interface.View.Login;

import java.util.ArrayList;
import java.util.List;

import Entity.UserData;
import Entity.Dialog.ReminderList;
import Interface.Controller.Login.ILoginController;
import Interface.Model.ILogger;
import View.Login.Listener.LoginViewListener;

/*
 * ログイン画面Viewインタフェース
 */
public interface ILoginView
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
   * コントローラインスタンスを設定
   * @param controller コントローラインスタンス
   */
  public void SetController(ILoginController controller);

  /**
   * ロガーインスタンスを設定
   * @param logger ロガーインスタンス
   */
  public void SetLogger(ILogger logger);
  
  /**
   * リスナ対象追加
   * @param listener 追加対象リスナインスタンス
   */
  public void AddListener(LoginViewListener listener);

  /**
   * リスナ対象削除
   * @param listener 削除対象リスナインスタンス
   */
  public void RemoveListener(LoginViewListener listener);

  /**
   * ログイン失敗ダイアログ表示
   */
  public void LoginFailure();

  /**
   * ログイン後画面に遷移
   */
  public void TransitionAfterLoginView(UserData userData);

  /**
   * リマインダーダイアログを表示
   */
  public void ReminderDialogView(List<ArrayList<String>> reminderList, UserData loginedUserData);

  /**
   * Controllerで渡す：isBusyで使う。
   * @param isDisp
   */
  public void ElementDisabled(boolean isDisabled);
  
  /**
   * ユーザ名とパスワードの入力内容監視
   * @param isDisabled ログインボタンの押下可否
  */
  public void LoginButtonDisabled(boolean isDisabled);
}