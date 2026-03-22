package Model;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.regex.Pattern;

import Interface.Model.IValidationUtil;

public class ValidationUtil implements IValidationUtil {
    
    private final String emailRegex = "^[a-zA-Z0-9_\\-]+(\\.[a-zA-Z0-9_\\-]+)*@([a-zA-Z0-9][a-zA-Z0-9\\-]*[a-zA-Z0-9]\\.)+[a-zA-Z]{2,}$";
    private final String passWordRegex = "^[a-zA-Z0-9]+$";
    private final Date originDate = new Date(0);

    /**
     * コンストラクタ
     */
    public ValidationUtil()
    {
    }

    /**
     * emailアドレスバリデーション
     * @param email ユーザー入力のユーザー名
     * @return true:バリデーションOK/false:バリデーションNG
     */
    public boolean IsEmailValid(String email)
    {
        // メールアドレス正規表現
        Pattern patternUser = Pattern.compile(emailRegex);
        return patternUser.matcher(email).matches();
    }

    /**
     * passwordバリデーション
     * @param pass ユーザー入力のパスワード
     * @return true:バリデーションOK/false:バリデーションNG
     */
    public boolean IsPassWordValid(String pass)
    {
        // パスワード正規表現
        Pattern patternPass = Pattern.compile(passWordRegex);
        return patternPass.matcher(pass).matches();
    }

    /**
     * 文字列バイト数取得（全角文字を除く）
     * @param text
     * @return
     */
    public int GetStringLength(String text)
    {
        return text.getBytes(StandardCharsets.UTF_8).length;
    }

    /**
     * 文字列バイト数取得（全角文字を含む）
     * @param text
     * @return
     */
    public int GetWideStringLength(String text)
    {
        return text.getBytes(Charset.forName("Shift-JIS")).length;
    }

    /**
     * 文字列を指定したバイト数だけ切り取り
     * @param text
     * @param bytes
     * @return
     */
    public String GetSubstringText(String text, Integer bytes)
    {
        String retText = "";
        int textByteCnt = 0; 
        for (int i = 0; i < text.length(); i++) {

            //対象となる文字列を先頭から1文字切り出し、その文字のバイト数を調べる
            String tmpText = text.substring(i, i + 1);
            var tmpTextByte = this.GetWideStringLength(tmpText);

            //切り出した文字を変数retに追加した際のバイト数が指定バイト数より大きければ、変数retを返す
            if (textByteCnt + tmpTextByte > bytes) {
                return retText;
            } else {
                retText = retText + tmpText;
                textByteCnt = textByteCnt + tmpTextByte;
            }
        }
        return retText;
    }

    /**
     * 引数の日付型オブジェクトがorigin値かどうか
     * @param date
     * @return
     */
    public boolean IsOriginDate(Date date)
    {
        return date.getTime() == originDate.getTime();
    }
}
