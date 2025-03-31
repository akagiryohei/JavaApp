package View.Login;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.function.Consumer;
import java.nio.charset.StandardCharsets;


import javax.security.auth.login.LoginContext;
import javax.swing.*;

import Controller.Login.LoginController;
import Entity.Dialog.Constants;
import View.JPanelViewBase;
import View.MainWindowView;

// ログイン画面Viewクラス
public class LoginView extends JPanelViewBase implements ActionListener
{
  // ユーザ名テキストフィールドのインスタンス
  private JTextField userNameTextField;

  // パスワード文字列テキストフィールドのインスタンス
  private JPasswordField passwordTextField;

  // 新規登録ボタンのインスタンス
  private JButton createAccountButton;

  // ログインボタンのインスタンス
  private JButton loginButton;

  // パスワード忘却者ボタンのインスタンス
  private JButton LostPassUserButton;

  // 親画面のインスタンス
  private LoginBaseView BaseViewInstance;

  // ログインコントローラー
  public LoginController controller;

  // MainWindowのインスタンス
  private MainWindowView MainWindowViewInstance;

  // コンストラクタ
  // baseViewInstance : 親画面のインスタンス
  public LoginView(LoginBaseView baseViewInstance, MainWindowView mainWindowView)
  {
    // TODO : 親画面のインスタンスは型依存ではなくinterface等に修正し依存しないようにする
    // TODO : 親画面のインスタンスをView層で直接持つのは検討の余地あり
    this.BaseViewInstance = baseViewInstance;
    this.MainWindowViewInstance = mainWindowView;


    // 一番親のLoginViewにかかってるLayoutマネージャーの向こうかが可能になる
    this.setLayout(null);

    // アプリ名ラベルの設定
    JLabel appNameLabel = new JLabel("タスク管理ツール");
    appNameLabel.setBounds(300,22,400,132);
    appNameLabel.setVerticalAlignment(JLabel.CENTER); //垂直位置
    appNameLabel.setHorizontalAlignment(JLabel.CENTER); //水平位置
    appNameLabel.setOpaque(true);
    appNameLabel.setBackground(Color.CYAN);
    this.add(appNameLabel);

    // ユーザ名入力欄の設定
    JLabel userLabel = new JLabel("ユーザ名(メールアドレス)");
    userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    userLabel.setBounds(300,176,200,66);
    userLabel.setVerticalAlignment(JLabel.CENTER); //垂直位置
    userLabel.setHorizontalAlignment(JLabel.CENTER); //水平位置
    userLabel.setOpaque(true);
    userLabel.setBackground(Color.CYAN);
    
    this.add(userLabel);

    this.userNameTextField = new JTextField();
    this.userNameTextField.setBounds(500,176,200,66);
    this.userNameTextField.setColumns(1);
    this.add(this.userNameTextField);

    // パスワード入力欄の設定
    JLabel passwordLabel = new JLabel("パスワード");
    passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    passwordLabel.setBounds(300,264,200,66);
    passwordLabel.setVerticalAlignment(JLabel.CENTER); //垂直位置
    passwordLabel.setHorizontalAlignment(JLabel.CENTER); //水平位置
    passwordLabel.setOpaque(true);
    passwordLabel.setBackground(Color.CYAN);
    this.add(passwordLabel);

    this.passwordTextField = new JPasswordField();
    this.passwordTextField.setPreferredSize(new Dimension(20, 20));
    this.passwordTextField.setBounds(500,264,200,66);
    this.passwordTextField.setColumns(1);
    this.add(this.passwordTextField);

    // ログインボタンの設定
    this.loginButton = new JButton("Login");
    this.loginButton.setActionCommand("LoginButton");
    this.loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    this.loginButton.setBounds(300,352,400,44);
    this.add(this.loginButton);

    // パスワード忘却者ボタンの設定
    this.LostPassUserButton = new JButton("パスワード忘却者へ");
    this.LostPassUserButton.setActionCommand("LostPassUserButton");
    this.LostPassUserButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    this.LostPassUserButton.setBounds(300,418,400,44);
    this.add(this.LostPassUserButton);
    

    // 新規登録ボタンの設定
    this.createAccountButton = new JButton("新規アカウント登録");
    this.createAccountButton.setActionCommand("CreateAccountButton");
    this.createAccountButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    this.createAccountButton.setBounds(300,484,400,44);

    this.add(this.createAccountButton);

    // ログイン画面にパーツを配置
  }

  // ボタンからのアクションリスナー
  public void actionPerformed(ActionEvent e)
  {
    switch (e.getActionCommand())
    {
      case "LoginButton":
        // ログインボタン押下イベントを受信
        this.LoginButtonClicked();
        break;
      case "CreateAccountButton":
        // 新規登録ボタン押下イベントを受信
        this.CreateAccountButtonClicked();
        break;
      case "LostPassUserButton":
        // パスワード忘却者へボタン押下イベントを受信
        this.LostPassUserButtonClicked();
        break;
      default:
        // ロジック上あり得ない
        break;
    }
  }

  public void Show()
  {
    // ボタン押下イベントのリスナーを登録する
    this.loginButton.addActionListener(this);
    this.createAccountButton.addActionListener(this);
    this.LostPassUserButton.addActionListener(this);
  }

  public void Hide()
  {
    // ボタン押下イベントのリスナーを解除する
    this.loginButton.removeActionListener(this);
    this.createAccountButton.removeActionListener(this);
    this.LostPassUserButton.removeActionListener(this);
  }

  // ログインボタンクリック時の処理
  private void LoginButtonClicked()
  {
    String userNameTextField = this.userNameTextField.getText();
    String passwordTextField = new String(this.passwordTextField.getPassword());
    
    // ユーザ名バリデーション処理
    this.controller.LoginAuth(userNameTextField, passwordTextField);
  }

  // 新規登録ボタンクリック時の処理
  private void CreateAccountButtonClicked()
  {
    System.out.println("新規アカウント作成ボタンが押下された");

    // TODO: 親画面に対して子画面から表示状態を指示するのは違和感を感じる（コールバックやイベントハンドラに修正を検討）
    this.BaseViewInstance.ChangeView(LoginBaseView.ViewType.SignupView);
  }

  // 新規登録ボタンクリック時の処理
  private void LostPassUserButtonClicked()
  {
    System.out.println("パスワード忘却者へボタンが押下された");

    // TODO: 親画面に対して子画面から表示状態を指示するのは違和感を感じる（コールバックやイベントハンドラに修正を検討）
    this.BaseViewInstance.ChangeView(LoginBaseView.ViewType.LostPassUserView);
  }

  // ログイン失敗ダイアログ表示
  public void LoginFailure()
  {
    JDialog dialog = new JDialog(this.MainWindowViewInstance, Constants.LOGIN_FAILURE_DIALOG, true);
    dialog.setSize(250,150);
    dialog.setLayout(null);
    JLabel operationLabel = new JLabel(Constants.LOGIN_FAILURE_OPERATION);
    operationLabel.setBounds(20,20,100,70);
    dialog.add(operationLabel);
    dialog.setLocationRelativeTo(null);
    dialog.setVisible(true);
  }

  public void TransitionAfterLoginView(String userId)
  {
    this.BaseViewInstance.TarnationAfterLoginView(userId);
  }

  /**
   * Controllerで渡す：isBusyで使う。
   * @param isDisp
   */
  public void ElementDisabled(boolean isDisabled)
  {
      this.userNameTextField.setEnabled(!isDisabled);
      this.passwordTextField.setEnabled(!isDisabled);
      this.loginButton.setEnabled(!isDisabled);
      this.LostPassUserButton.setEnabled(!isDisabled);
      this.createAccountButton.setEnabled(!isDisabled);
  }
}
