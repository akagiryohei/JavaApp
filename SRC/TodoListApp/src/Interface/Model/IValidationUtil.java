package Interface.Model;

import java.util.Date;

/*
 * ValidationUtilのインタフェース
 */
public interface IValidationUtil
{
  /**
    * emailアドレスバリデーション
    * @param email ユーザー入力のユーザー名
    * @return true:バリデーションOK/false:バリデーションNG
    */
  public boolean IsEmailValid(String email);

  /**
   * passwordバリデーション
   * @param pass ユーザー入力のパスワード
   * @return true:バリデーションOK/false:バリデーションNG
   */
  public boolean IsPassWordValid(String pass);

  /**
   * 文字列バイト数取得
   * @param text
   * @return
   */
  public int GetStringLength(String text);

    /**
   * 引数の日付型オブジェクトがorigin値かどうか
   * @param date
   * @return
   */
  public boolean IsOriginDate(Date date);
}
