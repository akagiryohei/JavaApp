package View.Login;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.EventListenerList;

import Entity.UserData;
import Interface.Controller.Login.ILoginController;
import Interface.Controller.Login.ILostPassUserController;
import Interface.Controller.Login.ISignupController;
import Interface.DI.Login.ILoginBaseDI;
import Interface.View.Login.ILoginBaseView;
import Interface.View.Login.ILoginView;
import Interface.View.Login.ILostPassUserView;
import Interface.View.Login.ISignupView;
import View.JPanelViewBase;
import View.Login.Listener.LoginBaseViewListener;
import View.Login.Listener.LoginViewListener;
import View.Login.Listener.LostPassUserViewListener;
import View.Login.Listener.SignupViewListener;

/*
 * ログイン前画面基底Viewクラス
 */
public class LoginBaseView extends JPanelViewBase implements ILoginBaseView, LoginViewListener, LostPassUserViewListener, SignupViewListener
{
  // ログイン画面のインスタンス
  private ILoginView LoginView;

  // 新規登録画面のインスタンス
  private ISignupView SignupView;

  // 忘却者画面のインスタンス
  private ILostPassUserView LostPassUserView;

  // CardLayoutオブジェクト
  private CardLayout Layout;

  // LoginBaseDIのインスタンス
  private ILoginBaseDI LoginBaseDIInstance;

  // ログイン画面コントローラインスタンス
  private ILoginController LoginControllerInstance;

  // 会員登録画面コントローラのインスタンス
  private ISignupController SignupControllerInstance;

  // 忘却者画面コントローラーのインスタンス
  private ILostPassUserController LostPassUserControllerInstance;
  
  // イベントリスナインスタンス
  protected EventListenerList ListenerList;

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

  /**
   * コストラクタ
   * @param loginBaseDi ログインベースDI
   */
  public LoginBaseView(ILoginBaseDI loginBaseDi)
  {
    this.LoginBaseDIInstance = loginBaseDi;

    // イベントリスナインスタンスを初期化
    this.ListenerList = new EventListenerList();

    // 画面遷移にあたってCardLayoutを設定
    this.Layout = new CardLayout();
    this.setLayout(this.Layout);

    // ログイン前画面のインスタンスを取得
    this.LoginControllerInstance = this.LoginBaseDIInstance.CreateLoginMVC();
    this.LoginView = this.LoginControllerInstance.GetViewInstance();

    // CardLayoutにログイン画面を代入
    this.add((JPanel)this.LoginView, ViewType.LoginView.name());
  }

  public void Show()
  {
    this.LoginView.AddListener(this);
    this.ChangeView(ViewType.LoginView);
    // TODO : LoginViewに対して画面オープンを通知する
  }

  public void Hide()
  {
    // TODO : ログイン後画面実装時にログイン画面に対してクローズを指示する
    this.LoginView.RemoveListener(this);
  }

  /**
   * リスナ対象追加
   * @param listener 追加対象リスナインスタンス
   */
  public void AddListener(LoginBaseViewListener listener)
  {
    this.ListenerList.add(LoginBaseViewListener.class, listener);
  }

  /**
   * リスナ対象削除
   * @param listener 削除対象リスナインスタンス
   */
  public void RemoveListener(LoginBaseViewListener listener)
  {
    this.ListenerList.remove(LoginBaseViewListener.class, listener);
  }

  /**
   * 新規アカウント登録画面で閉じるボタン押下通知受信時の処理
   */
  @Override
  public void CloseButtonClicked()
  {
    this.ChangeView(ViewType.LoginView);
  }

  /**
   * 新規アカウント登録画面で閉じるボタン押下通知受信時の処理
   */
  @Override
  public void SuccessfulSignupOrCloseButtonClicked()
  {
    this.ChangeView(ViewType.LoginView);
  }

  /**
   * ログイン画面で新規アカウント登録ボタンを押下通知受信時の処理
   */
  @Override
  public void CreateAccountButtonClicked()
  {
    this.ChangeView(ViewType.SignupView);
  }

  /**
   * ログイン画面でパスワード忘却者へボタンを押下通知受信時の処理
   */
  @Override
  public void LostPassUserButtonClicked()
  {
    this.ChangeView(ViewType.LostPassUserView);
  }

  /**
   * ログイン画面でログインに成功した通知受信時の処理
   * @param userId ログイン成功したユーザID
   */
  @Override
  public void SuccessfulPasswordLogin(UserData userData)
  {
    this.NotifySuccessfulLogin(userData);
  }
  
  /**
   * 忘却者画面でログインに成功した通知受信時の処理
   * @param userId ログイン成功したユーザID
   */
  @Override
  public void SuccessfulLostPasswordLogin(UserData userData)
  {
    this.NotifySuccessfulLogin(userData);
  }

  /**
   * View変更用メソッド
   * @param type ViewType
   */
  private void ChangeView(ViewType type)
  {
    switch (type)
    {
      case ViewType.LoginView:
        if (this.SignupView != null)
        {
          // 新規登録画面から戻ってきた場合は破棄する
          this.remove((JPanel)this.SignupView);
          this.SignupView.RemoveListener(this);
          this.SignupView = null;
          this.SignupControllerInstance.Hide();
          this.SignupControllerInstance = null;
        }

        if (this.LostPassUserView != null)
        {
          // 忘却者画面から戻ってきた場合は破棄する
          this.remove((JPanel)this.LostPassUserView);
          this.LostPassUserView.RemoveListener(this);
          this.LostPassUserView = null;
          this.LostPassUserControllerInstance.Hide();
          this.LostPassUserControllerInstance = null;
        }

        // ログイン画面表示を指示
        this.Layout.show(this, ViewType.LoginView.name());
        this.LoginControllerInstance.Show();
        break;

      case ViewType.SignupView:
        // 新規登録画面のインスタンスを生成
        this.SignupControllerInstance = this.LoginBaseDIInstance.CreateSignupMVC();
        this.SignupView = this.SignupControllerInstance.GetViewInstance();
        this.SignupView.AddListener(this);
        
        // 新規登録画面表示を指示
        this.add((JPanel)this.SignupView, ViewType.SignupView.name());
        this.Layout.show(this, ViewType.SignupView.name());

        this.SignupControllerInstance.Show();
        break;

      case ViewType.LostPassUserView:
        // 忘却者画面のインスタンスを生成
        this.LostPassUserControllerInstance = this.LoginBaseDIInstance.CreateLostPassUserMVC();
        this.LostPassUserView = this.LostPassUserControllerInstance.GetViewInstance();
        this.LostPassUserView.AddListener(this);

        // 忘却者画面画面表示を指示
        this.add((JPanel)this.LostPassUserView, ViewType.LostPassUserView.name());
        this.Layout.show(this, ViewType.LostPassUserView.name());

        this.LostPassUserControllerInstance.Show();
        break;

      default:
        // ロジック上あり得ない
        break;
    }
  }

  /**
   * ログイン成功を通知
   * @param userData ログイン成功したユーザ情報
   */
  private void NotifySuccessfulLogin(UserData userData)
  {
    for (LoginBaseViewListener listener : this.ListenerList.getListeners(LoginBaseViewListener.class))
    {
      // ログイン成功を通知
      listener.SuccessfulLogin(userData);
    }
  }
}
