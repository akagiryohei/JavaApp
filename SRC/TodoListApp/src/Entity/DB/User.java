package Entity.DB;

/**
 * ユーザー登録データ型クラス
 */
public class User {
    // メールアドレス
    public String Email;
    // パスワード
    public String Password;
    // メールアドレス
    public String SecretTipsId;
    // メールアドレス
    public String SecretPassword;
    
    public User()
    {
        this.Email = "";
        this.Password = "";
        this.SecretTipsId = "";
        this.SecretPassword = "";
    }
}
