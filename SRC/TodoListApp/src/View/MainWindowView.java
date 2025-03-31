package View;

import java.awt.*;
import javax.swing.*;

import Controller.Login.LoginBaseController;
import Controller.Todo.TodoListBaseController;
import DI.MainWindowDI;

public class MainWindowView extends JFrameViewBase
{
  // ウィンドウタイトル文字列定義
  private static final String WINDOW_TITLE = "Todoアプリ";

  // ログイン前画面インスタンス
  private JPanel BeforeLogin;

  // ログイン後画面インスタンス
  private JPanel AfterLogin;

  // CardLayoutオブジェクト
  private CardLayout layout;

  // MainWindowDIのインスタンス
  private MainWindowDI MainWindowDIInstance;

  // ログイン画面コントローラのインスタンス
  private LoginBaseController LoginBaseControllerInstance;

  // Todoリスト（リスト型表示の）コントローラーのインスタンス
  private TodoListBaseController TodoListBaseControllerInstance;

  // 画面表示種別
  public enum ViewType{
    // ログイン前
    BeforeLogin,

    // ログイン後
    AfterLogin,
  };

  // コンストラクタ
  public MainWindowView(MainWindowDI mainWindowDi)
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
    this.ChangeView(ViewType.BeforeLogin, "");
    // ウィンドウ表示
    this.setVisible(true);
  }

  public void Hide()
  {
    // ウィンドウ非表示
    this.setVisible(false);
  }

  public void ChangeView(ViewType type, String userID)
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
        this.getContentPane().add(this.BeforeLogin, ViewType.BeforeLogin.name());
        this.layout.show(this.getContentPane(), ViewType.BeforeLogin.name());
        this.LoginBaseControllerInstance.Show();

        if (this.AfterLogin != null)
        {
          // ログアウトボタンを実装した場合の処理
          // ログイン後画面からログイン前画面に戻るときにログイン後画面を閉じる処理を記述
          // 先にコントローラにクローズを通知（余分なイベントを受信しないため）
          this.remove(this.AfterLogin);
          this.AfterLogin = null;
          this.TodoListBaseControllerInstance = null;
        }

        break;

      case ViewType.AfterLogin:
        // 先にコントローラにクローズを通知する（余分なイベントを受信しないようにするため）
        this.LoginBaseControllerInstance.Hide();

        // ログイン後画面を設定
        this.TodoListBaseControllerInstance = this.MainWindowDIInstance.CreateTodoListBaseMVC(this, userID);
        this.AfterLogin = this.TodoListBaseControllerInstance.GetViewInstance();
        this.getContentPane().add(this.AfterLogin, ViewType.AfterLogin.name());
        this.layout.show(this.getContentPane(), ViewType.AfterLogin.name());
        this.TodoListBaseControllerInstance.Show();

        // ログイン前画面を閉じる処理
        this.remove(this.BeforeLogin);
        this.BeforeLogin = null;
        this.LoginBaseControllerInstance = null;

        break;
    }
  }
}

