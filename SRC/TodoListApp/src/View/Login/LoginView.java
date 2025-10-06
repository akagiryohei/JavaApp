package View.Login;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.function.Consumer;
import java.nio.charset.StandardCharsets;

import javax.security.auth.login.LoginContext;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.EventListenerList;

import Controller.Login.LoginController;
import Entity.UserData;
import Entity.Dialog.Constants;
import Entity.Dialog.ReminderList;
import Entity.Enum.LogLevel;
import Interface.Controller.Login.ILoginController;
import Interface.View.IMainWindowView;
import Interface.View.Login.ILoginView;
import View.JPanelViewBase;
import View.Login.Listener.LoginViewListener;
import View.MainWindowView;
import View.Dialog.CommonDialogView;
import View.Dialog.CommonDialogView.CommonDialogType;
import View.Dialog.ReminderDialogView;
import View.Dialog.Listener.ReminderDialogViewListener;

// ログイン画面Viewクラス
public class LoginView extends JPanelViewBase implements ILoginView, ReminderDialogViewListener, ActionListener, DocumentListener
{
  // ユーザ名テキストフィールドのインスタンス
  private JTextField UserNameTextField;

  // パスワード文字列テキストフィールドのインスタンス
  private JPasswordField PasswordTextField;

  // 新規登録ボタンのインスタンス
  private JButton CreateAccountButton;

  // ログインボタンのインスタンス
  private JButton LoginButton;

  // パスワード忘却者ボタンのインスタンス
  private JButton LostPassUserButton;

  // ログインコントローラー
  private ILoginController Controller;

  // イベントリスナインスタンス
  protected EventListenerList ListenerList;

  // 共通ダイアログのインスタンス
  private CommonDialogView CommonDialogView;

  // リマインダーダイアログのインスタンス
  private ReminderDialogView ReminderDialogView;

  // ログイン成功したユーザID
  private UserData LoginedUserData;

  // コンストラクタ
  // commonDialogView : 共通ダイアログインスタンス
  // reminderDialogView : リマインダーダイアログインスタンス
  public LoginView(CommonDialogView commonDialogView, ReminderDialogView reminderDialogView)
  {
    // イベントリスナインスタンスを初期化
    this.ListenerList = new EventListenerList();
    
    this.CommonDialogView = commonDialogView;
    this.ReminderDialogView = reminderDialogView;

    this.LoginedUserData = null;

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
    JLabel userLabel = new JLabel("<html><body><center>ユーザ名<br />(メールアドレス)</center></html></body>");
    userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    userLabel.setBounds(300,176,200,66);
    userLabel.setVerticalAlignment(JLabel.CENTER); //垂直位置
    userLabel.setHorizontalAlignment(JLabel.CENTER); //水平位置
    userLabel.setOpaque(true);
    userLabel.setBackground(Color.CYAN);
    
    this.add(userLabel);

    this.UserNameTextField = new JTextField();
    this.UserNameTextField.setBounds(500,176,200,66);
    this.UserNameTextField.setColumns(1);
    this.add(this.UserNameTextField);

    // パスワード入力欄の設定
    JLabel passwordLabel = new JLabel("パスワード");
    passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    passwordLabel.setBounds(300,264,200,66);
    passwordLabel.setVerticalAlignment(JLabel.CENTER); //垂直位置
    passwordLabel.setHorizontalAlignment(JLabel.CENTER); //水平位置
    passwordLabel.setOpaque(true);
    passwordLabel.setBackground(Color.CYAN);
    this.add(passwordLabel);

    this.PasswordTextField = new JPasswordField();
    this.PasswordTextField.setPreferredSize(new Dimension(20, 20));
    this.PasswordTextField.setBounds(500,264,200,66);
    this.PasswordTextField.setColumns(1);
    this.add(this.PasswordTextField);

    // ログインボタンの設定
    this.LoginButton = new JButton("Login");
    this.LoginButton.setActionCommand("LoginButton");
    this.LoginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    this.LoginButton.setBounds(300,352,400,44);
    this.add(this.LoginButton);

    // パスワード忘却者ボタンの設定
    this.LostPassUserButton = new JButton("パスワード忘却者へ");
    this.LostPassUserButton.setActionCommand("LostPassUserButton");
    this.LostPassUserButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    this.LostPassUserButton.setBounds(300,418,400,44);
    this.add(this.LostPassUserButton);
    

    // 新規登録ボタンの設定
    this.CreateAccountButton = new JButton("新規アカウント登録");
    this.CreateAccountButton.setActionCommand("CreateAccountButton");
    this.CreateAccountButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    this.CreateAccountButton.setBounds(300,484,400,44);

    this.add(this.CreateAccountButton);

    // ログイン画面にパーツを配置
  }

  /**
   * ボタンからのアクションリスナー
   */
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

  /**
   * ユーザ名とパスワードが入力されたとき
   * @param e 変化を検知した入力欄のインスタンス情報
  */
  @Override
  public void insertUpdate(DocumentEvent e) {
    this.ChangedTextField(e);
  }

  /**
   * ユーザ名とパスワードが消されたとき
   * @param e 変化を検知した入力欄のインスタンス情報
  */
  @Override
  public void removeUpdate(DocumentEvent e) {
    this.ChangedTextField(e);
  }

  /**
   * リスナーの仕様上必要
   * @param e 変化を検知した入力欄のインスタンス情報
  */
  @Override
  public void changedUpdate(DocumentEvent e) {
    //何もしない
  }

  public void Show()
  {
    this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "画面オープン"); });

    // ボタン押下イベントのリスナーを登録する
    this.LoginButton.addActionListener(this);
    this.CreateAccountButton.addActionListener(this);
    this.LostPassUserButton.addActionListener(this);
    this.ReminderDialogView.AddListener(this);
    this.UserNameTextField.getDocument().addDocumentListener(this);
    this.PasswordTextField.getDocument().addDocumentListener(this);
    this.LoginButtonDisabled(true);
  }

  public void Hide()
  {
    this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "画面クローズ"); });

    // ボタン押下イベントのリスナーを解除する
    this.LoginButton.removeActionListener(this);
    this.CreateAccountButton.removeActionListener(this);
    this.LostPassUserButton.removeActionListener(this);
    this.UserNameTextField.getDocument().removeDocumentListener(this);
    this.PasswordTextField.getDocument().removeDocumentListener(this);
  }

  /**
   * コントローラインスタンスを設定
   * @param controller コントローラインスタンス
   */
  public void SetController(ILoginController controller)
  {
    this.Controller = controller;
  }

  /**
   * リスナ対象追加
   * @param listener 追加対象リスナインスタンス
   */
  public void AddListener(LoginViewListener listener)
  {
    this.ListenerList.add(LoginViewListener.class, listener);
  }

  /**
   * リスナ対象削除
   * @param listener 削除対象リスナインスタンス
   */
  public void RemoveListener(LoginViewListener listener)
  {
    this.ListenerList.remove(LoginViewListener.class, listener);
  }

  /**
   * ログインボタンクリック時の処理
   */
  private void LoginButtonClicked()
  {
    this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "ログインボタンが押下された。"); });
    String userNameTextField = this.UserNameTextField.getText();
    String passwordTextField = new String(this.PasswordTextField.getPassword());
    
    // ユーザ名バリデーション処理
    this.Controller.LoginAuth(userNameTextField, passwordTextField);
  }

  /**
   * 新規登録ボタンクリック時の処理
   */
  private void CreateAccountButtonClicked()
  {
    this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "新規登録ボタンが押下された"); });
    System.out.println("新規アカウント作成ボタンが押下された");

    for (LoginViewListener listener : this.ListenerList.getListeners(LoginViewListener.class))
    {
      // 新規登録ボタン押下を通知
      listener.CreateAccountButtonClicked();
    }
  }

  /**
   * パスワード忘却者へボタンクリック時の処理
   */
  private void LostPassUserButtonClicked()
  {
    this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "パスワード忘却者ボタンが押下された"); });
    System.out.println("パスワード忘却者へボタンが押下された");

    for (LoginViewListener listener : this.ListenerList.getListeners(LoginViewListener.class))
    {
      // パスワード忘却者へボタン押下を通知
      listener.LostPassUserButtonClicked();
    }
  }

  /**
   * ログイン失敗ダイアログ表示
   */
  public void LoginFailure()
  {
    this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "ログイン失敗ダイアログ表示"); });
    this.CommonDialogView.Show(CommonDialogType.LoginFailureDialog, true);
  }

  /**
   * ログイン後画面に遷移
   */
  public void TransitionAfterLoginView(UserData userData)
  {
    this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "ログイン成功を指示" + "（ログインID：" + userData.UserId + "）"); });
    
    for (LoginViewListener listener : this.ListenerList.getListeners(LoginViewListener.class))
    {
      // ログイン成功を通知
      listener.SuccessfulPasswordLogin(userData);
    }
  }

  /**
   * リマインダーダイアログを表示
   */
  public void ReminderDialogView(List<ArrayList<String>> reminderList, UserData loginedUserData)
  {
    this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "リマインダーダイアログ表示"); });
    this.LoginedUserData = loginedUserData;
    this.ReminderDialogView.Show(reminderList, true);
  }
  
  /**
   * リマインダーリスト画面のOKボタン押下イベント
   * ログイン時に全面に表示されるタスク一覧画面
   */
  public void ReminderOKButtonClicked()
  {
    this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "リマインダーダイアログでOKボタンが押下された"); });
    this.TransitionAfterLoginView(this.LoginedUserData);
  }

  /**
   * Controllerで渡す：isBusyで使う。
   * @param isDisp
   */
  public void ElementDisabled(boolean isDisabled)
  {
      this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "画面要素押下可否設定" + "(" + String.valueOf(isDisabled) + ")"); });
      this.UserNameTextField.setEnabled(!isDisabled);
      this.PasswordTextField.setEnabled(!isDisabled);
      this.LoginButton.setEnabled(!isDisabled);
      this.LostPassUserButton.setEnabled(!isDisabled);
      this.CreateAccountButton.setEnabled(!isDisabled);
  }

  /**
   * ユーザ名とパスワードの入力内容監視
   * @param isDisabled ログインボタンの押下可否
  */
  public void LoginButtonDisabled(boolean isDisabled)
  {
    this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "ログインボタン押下可否設定" + "(" + String.valueOf(!isDisabled) + ")"); });
    this.LoginButton.setEnabled(!isDisabled);
  }
  
  /**
   * ユーザ名とパスワードの入力内容監視
   * @param e ユーザ名とパスワードのインスタンス情報
  */
  private void ChangedTextField(DocumentEvent e){
    if(e.getDocument() == this.UserNameTextField.getDocument())
    {
      this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "ユーザ名の入力内容が変更されました"); });
    }
    else if(e.getDocument() == this.PasswordTextField.getDocument()){
      this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "パスワードの入力内容が変更されました"); });
    }
    this.Controller.ChangedTextField(this.UserNameTextField.getText(), new String(this.PasswordTextField.getPassword()));
  }
}