package Interface.View.Login;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.DocumentEvent;

import Entity.UserData;
import Entity.Enum.LogLevel;
import Interface.Controller.Login.ILostPassUserController;
import Interface.Model.ILogger;
import View.Login.Listener.LostPassUserViewListener;

/*
 * 忘却者画面Viewインタフェース
 */
public interface ILostPassUserView
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
  public void SetController(ILostPassUserController controller);
  
  /**
   * ロガーインスタンスを設定
   * @param logger ロガーインスタンス
   */
  public void SetLogger(ILogger logger);

  /**
   * リスナ対象追加
   * @param listener 追加対象リスナインスタンス
   */
  public void AddListener(LostPassUserViewListener listener);

  /**
   * リスナ対象削除
   * @param listener 削除対象リスナインスタンス
   */
  public void RemoveListener(LostPassUserViewListener listener);

  /**
   * ヒント内容のコンボボックスに初期値を設定
   */
  public void SetSecretInitialTips();

  // ヒント内容のコンボボックスに要素を追加

  /**
   * ヒント内容のコンボボックスに要素を追加
   * @param secretTips コンボボックスに設定する文字列
   */
  public void SetSecretTips(String secretTips);

  /**
   * DB接続失敗ダイアログ表示
   */
  public void ShowDBConnectionFailureDialog();

  /**
   * 入力テキスト相違ダイアログ表示
   */
  public void ShowInputContentFailureDialog();

  /**
   * ログイン失敗ダイアログ表示
   */
  public void ShowLoginFailureDialog();

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
   * ユーザ名の入力内容監視
   * @param isDisabled Supportボタンの押下可否
  */
  public void SupportButtonDisabled(boolean isDisabled);

  /**
   * ユーザ名の入力内容監視
   * @param isDisabled Try Loginボタンの押下可否
  */
  public void LoginButtonDisabled(boolean isDisabled);
}