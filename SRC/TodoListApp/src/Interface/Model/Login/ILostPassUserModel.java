package Interface.Model.Login;

import java.util.function.Consumer;
import Entity.Pair;
import Entity.UserData;
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
   * 忘却者ログイン認証
   * @param email 対象アカウントのメールアドレス
   * @param secretPassword 対象アカウントの秘密のパスワード
   * @param isBusyChanged 処理中イベントコールバック
   * @param finished 処理完了イベントコールバック
  */
  public void LostPassUserLoginAuth(String email, String secretPassword, Consumer<Boolean> isBusyChanged, Consumer<Pair<ResultType, UserData>> finished);
}
