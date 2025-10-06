package Interface.Model.Login;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import Entity.Pair;
import Entity.UserData;
import Entity.Dialog.ReminderList;
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
  public void LoginAuth(String email, String password, Consumer<Boolean> isBusyChanged, Consumer<Pair<ResultType, UserData>> finished, Consumer<Pair<ResultType, Pair<List<ArrayList<String>>, UserData>>> reminderList);

  /**
   * ログインボタンの押下可否を判定
   * @param email メールアドレス
   * @param password パスワード
   * @return true 押下可能, false 押下不可
  */
  public Boolean GetLoginButtonPossibility(String email, String password);
}