package Model;

import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class ValidationUtil {
    
    private final String emailRegex = "^[a-zA-Z0-9_\\-]+(\\.[a-zA-Z0-9_\\-]+)*@([a-zA-Z0-9][a-zA-Z0-9\\-]*[a-zA-Z0-9]\\.)+[a-zA-Z]{2,}$";
    private final String passWordRegex = "^[a-zA-Z0-9]+$";
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
     * 文字列バイト数取得
     * @param text
     * @return
     */
    public int GetStringLength(String text)
    {
        return text.getBytes(StandardCharsets.UTF_8).length;
    }
}
