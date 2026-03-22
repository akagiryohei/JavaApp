package Interface.Model.Login;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import Entity.Pair;
import Entity.UserData;
import Entity.Dialog.ReminderList;
import Interface.Model.Process.Login.ILoginProcess;
import Interface.Model.Process.Login.ILoginProcess.ResultType;

/**
  * ログイン画面Modelインタフェース
  */
public interface ILoginModel 
{
  /**
   * ログイン認証実施
   * @param email ログイン対象アカウントのメールアドレス
   * @param password ログイン対象アカウントのパスワード
   * @param isBusyChanged 処理中イベントコールバック
  */
  public void LoginAuth(String email, String password, Consumer<Boolean> isBusyChanged, Consumer<Pair<ResultType, UserData>> finished);

  /**
   * ログインボタンの押下可否を判定
   * @param email メールアドレス
   * @param password パスワード
   * @return true 押下可能, false 押下不可
  */
  public Boolean GetLoginButtonPossibility(String email, String password);

  /**
   * ログイン認証実施
   * @param userId ログイン対象アカウントのID
   * @param clickedDate ログインボタンを押下した時刻
   * @param isBusyChanged 処理中イベントコールバック
   * @param resultValue 処理完了コールバック
   */
  public void GetReminderList(String userId, Date clickedDate, Consumer<Boolean> isBusyChanged, Consumer<Pair<ILoginProcess.ResultType,List<ArrayList<String>>>> resultValue);
}