package View.Login;

import java.awt.*;
import javax.swing.*;

import Controller.Login.*;
import DI.Login.*;
import View.JPanelViewBase;
import View.MainWindowView;

/*
 * ログイン前画面基底Viewクラス
 */
public class LoginBaseView extends JPanelViewBase
{
  // ログイン画面のインスタンス
  private JPanel LoginView;

  // 新規登録画面のインスタンス
  private JPanel SignupView;

  // 忘却者画面のインスタンス
  private JPanel LostPassUserView;

  // CardLayoutオブジェクト
  private CardLayout layout;
  
  // 親要素のインスタンス
  private MainWindowView MainWindowViewInstance;

  // LoginBaseDIのインスタンス
  private LoginBaseDI LoginBaseDIInstance;

  // ログイン画面コントローラインスタンス
  private LoginController LoginControllerInstance;

  // 会員登録画面コントローラのインスタンス
  private SignupController SignupControllerInstance;

  // 忘却者画面コントローラーのインスタンス
  private LostPassUserController LostPassUserControllerInstance;

  // 画面表示種別
  public enum ViewType
  {
    // ログイン画面
    LoginView,

    // 新規登録画面
    SignupView,

    // 忘却者画面
    LostPassUserView,
  };

  // コンストラクタ
  public LoginBaseView(LoginBaseDI loginBaseDi, MainWindowView mainWindowViewInstance)
  {
    this.LoginBaseDIInstance = loginBaseDi;
    this.MainWindowViewInstance = mainWindowViewInstance;

    // 画面遷移にあたってCardLayoutを設定
    this.layout = new CardLayout();
    this.setLayout(this.layout);

    // ログイン前画面のインスタンスを取得
    this.LoginControllerInstance = this.LoginBaseDIInstance.CreateLoginMVC(this);
    this.LoginView = this.LoginControllerInstance.GetViewInstance();

    // CardLayoutにログイン画面を代入
    this.add(this.LoginView, ViewType.LoginView.name());
  }

  public void Show()
  {
    this.ChangeView(ViewType.LoginView);
    // TODO : LoginViewに対して画面オープンを通知する
  }

  public void Hide()
  {
    // TODO : ログイン後画面実装時にログイン画面に対してクローズを指示する
  }

  // CardLayoutで表示するViewを変更する
  public void ChangeView(ViewType type)
  {
    switch (type)
    {
      case ViewType.LoginView:
        if (this.SignupView != null)
        {
          // 新規登録画面から戻ってきた場合は破棄する
          this.remove(this.SignupView);
          this.SignupView = null;
          this.SignupControllerInstance.Hide();
          this.SignupControllerInstance = null;
        }

        if (this.LostPassUserView != null)
        {
          // 新規登録画面から戻ってきた場合は破棄する
          this.remove(this.LostPassUserView);
          this.LostPassUserView = null;
          this.LostPassUserControllerInstance.Hide();
          this.LostPassUserControllerInstance = null;
        }

        // ログイン画面表示を指示
        this.layout.show(this, ViewType.LoginView.name());
        this.LoginControllerInstance.Show();
        break;

      case ViewType.SignupView:
        // 新規登録画面のインスタンスを生成
        this.SignupControllerInstance = this.LoginBaseDIInstance.CreateSignupMVC(this);
        this.SignupView = this.SignupControllerInstance.GetViewInstance();

        // 新規登録画面表示を指示
        this.add(this.SignupView, ViewType.SignupView.name());
        this.layout.show(this, ViewType.SignupView.name());

        this.SignupControllerInstance.Show();
        break;

      case ViewType.LostPassUserView:
        // 忘却社画面のインスタンスを生成
        this.LostPassUserControllerInstance = this.LoginBaseDIInstance.CreateLostPassUserMVC(this);
        this.LostPassUserView = this.LostPassUserControllerInstance.GetViewInstance();

        // 新規登録画面表示を指示
        this.add(this.LostPassUserView, ViewType.LostPassUserView.name());
        this.layout.show(this, ViewType.LostPassUserView.name());

        this.LostPassUserControllerInstance.Show();
        break;

      default:
        // ロジック上あり得ない
        break;
    }
  }

  /*
   * MainWindowViewに対しての画面遷移指示（ログイン後画面）
   */
  public void TarnationAfterLoginView(String userId)
  {
    this.MainWindowViewInstance.ChangeView(MainWindowView.ViewType.AfterLogin, userId);
  }
}
