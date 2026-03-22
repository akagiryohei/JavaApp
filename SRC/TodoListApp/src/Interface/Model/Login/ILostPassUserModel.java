package Interface.Model.Login;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import Entity.Pair;
import Entity.UserData;
import Interface.Model.Process.Login.ILoginProcess;
import Interface.Model.Process.Login.ILoginProcess.ResultType;

/**
  * 忘却者画面Modelインタフェース
  */
public interface ILostPassUserModel
{
  /**
   * 指定アカウントの秘密の質問取得
   * @param email 対象アカウントのメールアドレス
   * @param isBusyChanged 処理中イベントコールバック
   * @param finished 処理完了イベントコールバック
  */
  public void GetUserSecretTips(String email, Consumer<Boolean> isBusyChanged, Consumer<Pair<ResultType, String>> finished);

  /**
   * Supportボタンの押下可否を判定
   * @param email メールアドレス
   * @return true 押下可能, false 押下不可
  */
  public Boolean GetSupportButtonPossibility(String email);

  /**
   * Try Loginボタンの押下可否を判定
   * @param email メールアドレス
   * @return true 押下可能, false 押下不可
  */
  public Boolean GetLoginButtonPossibility(String email, String SecretPassword);
  
  /**
   * 忘却者ログイン認証
   * @param email 対象アカウントのメールアドレス
   * @param secretPassword 対象アカウントの秘密のパスワード
   * @param isBusyChanged 処理中イベントコールバック
   * @param finished 処理完了イベントコールバック
  */
  public void LostPassUserLoginAuth(String email, String secretPassword, Consumer<Boolean> isBusyChanged, Consumer<Pair<ResultType, UserData>> finished);

  /**
   * Reminderリスト取得
   * @param userId 対象アカウントのユーザーID
   * @param clickedDate 実行時の日付
   * @param isBusyChanged 処理中イベントコールバック
   * @param resultValue 処理完了イベントコールバック
  */
  public void GetReminderList(String userId, Date clickedDate, Consumer<Boolean> isBusyChanged, Consumer<Pair<ILoginProcess.ResultType,List<ArrayList<String>>>> resultValue);
}