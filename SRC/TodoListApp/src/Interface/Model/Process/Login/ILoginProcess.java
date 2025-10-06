package Interface.Model.Process.Login;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import Entity.Pair;
import Entity.UserData;
import Entity.Dialog.ReminderList;

/**
  * ログイン認証機能インタフェース
  */
public interface ILoginProcess
{
  /** 処理結果定義 */
  public enum ResultType
  {
    /** 正常終了（認証OK） */
    Success,

    /** 正常終了（認証NG、アカウントが見つからなかった、パスワードが合致していない） */
    SuccessNotFoundAccount,

    /** 異常終了 */
    Failure,

    /** バリデーションエラー */
    ValidationError,

    /** タイムアウト */
    Timeout,
  }

  /**
   * ログイン実施
   * @param email ログイン対象アカウントのメールアドレス
   * @param password ログイン対象アカウントのパスワード
   * @param isBusyChanged 処理中イベントコールバック
   * @param finished 処理完了イベントコールバック
  */
  public void Login(String email, String password, Consumer<Boolean> isBusyChanged, Consumer<Pair<ResultType, UserData>> finished);

  /**
   * 新規アカウント登録
   * @param email 新規アカウントとして登録するメールアドレス
   * @param password 新規アカウントとして登録するパスワード
   * @param secretTipsId 秘密のパスワードヒントID
   * @param secretPassword 秘密のパスワード
   * @param isBusyChanged 処理中イベントコールバック
   * @param finished 処理完了イベントコールバック
  */
  public void Signup(String email, String password, String secretTipsId, String secretPassword, Consumer<Boolean> isBusyChanged, Consumer<ResultType> finished);

  /**
   * 秘密のパスワードのヒント一覧取得
   * @param isBusyChanged 処理中イベントコールバック
   * @param finished 処理完了イベントコールバック（処理結果、ヒント一覧）
  */
  public void GetSecretTipsList(Consumer<Boolean> isBusyChanged, Consumer<Pair<ResultType, List<String>>> finished);

  /**
   * 対象ユーザの秘密のパスワードのヒントを取得
   * @param email 対象ユーザのメールアドレス
   * @param isBusyChanged 処理中イベントコールバック
   * @param finished 処理完了イベントコールバック（処理結果、ヒント）
  */
  public void GetUserSecretTips(String email, Consumer<Boolean> isBusyChanged, Consumer<Pair<ResultType, String>> finished);

  /**
   * 忘却者ログイン実施
   * @param email ユーザのメールアドレス
   * @param secretPassword 秘密のパスワード
   * @param isBusyChanged 処理中イベントコールバック
   * @param finished 処理完了イベントコールバック（処理結果、ログイン対象ユーザ情報）
  */
  public void LostPassUserLogin(String email, String secretPassword, Consumer<Boolean> isBusyChanged, Consumer<Pair<ResultType, UserData>> finished);

  /**
   * リマインダーリスト取得
   * @param email ログイン対象アカウントのメールアドレス
   * @param isBusyChanged 処理中イベントコールバック
   * @param finished 処理完了イベントコールバック
  */
  public void GetReminderList(String email, Date clickedDate, Consumer<Boolean> isBusyChanged, Consumer<Pair<ResultType, List<ReminderList>>> finished);
}