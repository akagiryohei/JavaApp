package Interface.View.Login;

import Interface.Controller.Login.ISignupController;
import Interface.Model.ILogger;
import View.Login.Listener.SignupViewListener;

import java.util.List;

/*
 * 新規登録Viewインタフェース
 */
public interface ISignupView
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
  public void AddListener(SignupViewListener listener);
  
  /**
   * リスナ対象削除
   * @param listener 削除対象リスナインスタンス
   */
  public void RemoveListener(SignupViewListener listener);

  /**
   * コントローラインスタンスを設定
   * @param controller コントローラインスタンス
   */
  public void SetController(ISignupController controller);
  
  /**
   * ロガーインスタンスを設定
   * @param logger ロガーインスタンス
   */
  public void SetLogger(ILogger logger);

  /**
   * ヒント内容のコンボボックスに要素を追加
   * @param secretTipsList ヒント内容一覧
   */
  public void SetSecretTipsList(List<String> secretTipsList);

  /**
   * 秘密の質問一覧取得失敗ダイアログ表示
   */
  public void GetSecretTipsListFailure();
  
  /**
   * 新規アカウント登録失敗ダイアログ表示
   */
  public void SignupFailure();

  /**
   * 画面要素を選択不可に設定
   * @param isDisabled ボタンを無効にするか
   */
  public void ElementDisabled(boolean isDisabled);

  /**
   * キャンセルボタンのみ有効に設定
   */
  public void CancelButtonOnlyEnabled();
}
