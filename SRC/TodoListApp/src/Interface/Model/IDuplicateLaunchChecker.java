package Interface.Model;

/*
 * DuplicateLaunchCheckerのインタフェース
 */
public interface IDuplicateLaunchChecker
{
  /**
   * ロック取得して、多重起動ではないかを確認する
   * @return true: ロック取得成功（多重起動ではない）、false: ロック取得失敗（多重起動に該当）
   */
  public boolean TryLock();

  /**
   * ロックを解放し、関連するリソースをクローズする
   */
  public void Release();
}
 