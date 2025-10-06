package View;

import java.awt.*;
import javax.swing.*;

import Entity.UserData;
import Interface.Controller.Login.ILoginBaseController;
import Interface.Controller.Todo.ITodoListBaseController;
import Interface.DI.IMainWindowDI;
import Interface.View.IMainWindowView;
import Interface.View.Login.ILoginBaseView;
import Interface.View.Todo.ITodoListBaseView;
import View.Login.Listener.LoginBaseViewListener;

public class MainWindowView extends JFrameViewBase implements IMainWindowView, LoginBaseViewListener
{
  // ウィンドウタイトル文字列定義
  private static final String WINDOW_TITLE = "Todoアプリ";

  // ログイン前画面インスタンス
  private ILoginBaseView BeforeLogin;

  // ログイン後画面インスタンス
  private ITodoListBaseView AfterLogin;

  // CardLayoutオブジェクト
  private CardLayout layout;

  // MainWindowDIのインスタンス
  private IMainWindowDI MainWindowDIInstance;

  // ログイン画面コントローラのインスタンス
  private ILoginBaseController LoginBaseControllerInstance;

  // Todoリスト（リスト型表示の）コントローラーのインスタンス
  private ITodoListBaseController TodoListBaseControllerInstance;

  // 画面表示種別
  public enum ViewType{
    // ログイン前
    BeforeLogin,

    // ログイン後
    AfterLogin,
  };

  // コンストラクタ
  public MainWindowView(IMainWindowDI mainWindowDi)
  {
    this.setTitle(MainWindowView.WINDOW_TITLE);
    this.setSize(1000, 600); // 場所指定はないから、画面のどの部分に出したいのかによって処理追加必要
    this.layout = new CardLayout();
    this.setLayout(this.layout);
    this.setLocationRelativeTo(null); // nullを入れることで自動的にディスプレイの中心に出る
    this.setResizable(false);

    this.MainWindowDIInstance = mainWindowDi;
  }

  public void Show()
  {
    // 初回は必ずログイン前画面へ遷移
    this.ChangeView(ViewType.BeforeLogin, null);
    // ウィンドウ表示
    this.setVisible(true);
  }

  public void Hide()
  {
    // ウィンドウ非表示
    this.setVisible(false);
  }

  /**
   * ログイン成功通知を受信
   * @param userId ログイン成功したユーザID
   */
  @Override
  public void SuccessfulLogin(UserData userData)
  {
    this.ChangeView(ViewType.AfterLogin, userData);
  }

  /**
   * 表示画面遷移
   * @param type 表示対象Viewを指定
   * @param userData ログイン対象ユーザ情報を入力（ログイン前画面からログイン後画面遷移時のみ設定）
   */
  private void ChangeView(ViewType type, UserData userData)
  {

    switch(type)
    {
      case ViewType.BeforeLogin:
        if (this.AfterLogin != null)
        {
          // ログアウトボタンを実装した場合の処理
          // ログイン後画面からログイン前画面に戻るときにログイン後画面を閉じる処理を記述
          // 先にコントローラにクローズを通知（余分なイベントを受信しないため）
          this.TodoListBaseControllerInstance.Hide();
        }
        
        // ログイン前画面を設定
        this.LoginBaseControllerInstance = this.MainWindowDIInstance.CreateLoginBaseMVC(this);
        this.BeforeLogin = this.LoginBaseControllerInstance.GetViewInstance();
        this.BeforeLogin.AddListener(this);
        this.getContentPane().add((JPanel)this.BeforeLogin, ViewType.BeforeLogin.name());
        this.layout.show(this.getContentPane(), ViewType.BeforeLogin.name());
        this.LoginBaseControllerInstance.Show();

        if (this.AfterLogin != null)
        {
          // ログアウトボタンを実装した場合の処理
          // ログイン後画面からログイン前画面に戻るときにログイン後画面を閉じる処理を記述
          // 先にコントローラにクローズを通知（余分なイベントを受信しないため）
          this.remove((JPanel)this.AfterLogin);
          this.AfterLogin = null;
          this.TodoListBaseControllerInstance = null;
        }

        break;

      case ViewType.AfterLogin:
        // 先にコントローラにクローズを通知する（余分なイベントを受信しないようにするため）
        this.LoginBaseControllerInstance.Hide();
        this.BeforeLogin.RemoveListener(this);

        // ログイン後画面を設定
        this.TodoListBaseControllerInstance = this.MainWindowDIInstance.CreateTodoListBaseMVC(this, userData);
        this.AfterLogin = this.TodoListBaseControllerInstance.GetViewInstance();
        this.getContentPane().add((JPanel)this.AfterLogin, ViewType.AfterLogin.name());
        this.layout.show(this.getContentPane(), ViewType.AfterLogin.name());
        this.TodoListBaseControllerInstance.Show();

        // ログイン前画面を閉じる処理
        this.remove((JPanel)this.BeforeLogin);
        this.BeforeLogin = null;
        this.LoginBaseControllerInstance = null;
        this.setVisible(true);

        break;
    }
  }
}

