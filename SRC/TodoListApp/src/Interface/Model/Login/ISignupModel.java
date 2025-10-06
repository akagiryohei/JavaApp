package Interface.Model.Login;

import java.util.List;
import java.util.function.Consumer;
import Entity.Pair;
import Interface.Model.Process.Login.ILoginProcess.ResultType;

public interface ISignupModel
{
  /**
   * 秘密の質問一覧取得実施
   * @param isBusyChanged 処理中イベントコールバック
   * @param finished 処理完了イベントコールバック
  */
  public void GetSecretTipsList(Consumer<Boolean> isBusyChanged, Consumer<Pair<ResultType, List<String>>> finished);

  /**
   * アカウント登録実施
   * @param email 作成対象アカウントのメールアドレス
   * @param password ログイン対象アカウントのパスワード
   * @param SecretTipsId 作成対象の秘密の質問番号
   * @param SecretPassword 作成対象アカウントの秘密のパスワード
   * @param isBusyChanged 処理中イベントコールバック
  */
  public void SignupAuth(String email, String password, String SecretTipsId, String SecretPassword, Consumer<Boolean> isBusyChanged, Consumer<ResultType> finished);
}
