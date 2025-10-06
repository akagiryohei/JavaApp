package View.Login;

import javax.swing.*;
import javax.swing.event.EventListenerList;

import Entity.UserData;
import Entity.Dialog.Constants;
import Entity.Enum.LogLevel;
import Interface.Controller.Login.ILostPassUserController;
import Interface.View.IMainWindowView;
import Interface.View.Login.ILostPassUserView;
import View.JPanelViewBase;
import View.Dialog.CommonDialogView;
import View.Dialog.CommonDialogView.CommonDialogType;
import View.Login.Listener.LostPassUserViewListener;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.regex.Pattern;

// 忘却者Viewクラス
public class LostPassUserView extends JPanelViewBase implements ILostPassUserView, ActionListener
{
  // ヒント内容の初期値
  private final String SECLET_PASSWORD_LABEL_CONTENT = "<html><body>ヒントが欲しい人は自身のメールアドレスを<br />入力しsupportボタン押下</body></html>";

  // ユーザ名テキストフィールドのインスタンス
  private JTextField UserNameTextField;

  // 秘密のパスワード文字列テキストフィールドのインスタンス
  private JTextField SecretPasswordField;

  // 秘密のパスワードのヒントラベル
  private JLabel secretPasswordLabel;
  
  // ヒント内容表示ボタン（supportボタン）
  private JButton SupportButton;
  
  // ログインボタンのインスタンス
  private JButton LoginButton;

  // タイトルへ戻るボタンのインスタンス
  private JButton TitleButton;
  
  // メールアドレス正規表現
  private String RegexUser;

  // メールアドレス正規表現パターン確定
  private Pattern PatternUser;

  // 画面内入力の値（ユーザー名、パスワード）
  public ArrayList InputList;

  // 忘却者画面コントローラー
  private ILostPassUserController Controller;

  // 共通ダイアログのインスタンス
  private CommonDialogView CommonDialogView;

  // イベントリスナインスタンス
  protected EventListenerList ListenerList;

  /**
   * コンストラクタ
   * @param mainWindowViewInstance MainWindowViewのインスタンス
   */
  public LostPassUserView(IMainWindowView mainWindowViewInstance, CommonDialogView commonDialogView)
  {
    // イベントリスナインスタンスを初期化
    this.ListenerList = new EventListenerList();
    
      // メールアドレス正規表現
    this.RegexUser = "^[a-zA-Z0-9_\\-]+(\\.[a-zA-Z0-9_\\-]+)*@([a-zA-Z0-9][a-zA-Z0-9\\-]*[a-zA-Z0-9]\\.)+[a-zA-Z]{2,}$";
    this.PatternUser = Pattern.compile(this.RegexUser);//regexの文字列を正規表現パターンとして解釈、コンパイルする。Patternオブジェクトとして返す

    // ダイアログインスタンスを初期化
    this.CommonDialogView = commonDialogView;
    
    // 一番親のLoginViewにかかってるLayoutマネージャーの向こうかが可能になる
    this.setLayout(null);


    // ガイダンスラベルの設定
    JLabel guidanceLabel = new JLabel("<html><body>パスワードを忘れた方へ<br />秘密のパスワード入力画面</body></html>");
    guidanceLabel.setBounds(300,22,400,88);
    guidanceLabel.setVerticalAlignment(JLabel.CENTER); //垂直位置
    guidanceLabel.setHorizontalAlignment(JLabel.CENTER); //水平位置
    guidanceLabel.setOpaque(true);
    guidanceLabel.setBackground(Color.CYAN);
    this.add(guidanceLabel);

    // ユーザ名入力欄の設定
    JLabel userLabel = new JLabel("ユーザ名を入力");
    userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    userLabel.setBounds(100,176,400,66);
    userLabel.setVerticalAlignment(JLabel.CENTER); //垂直位置
    userLabel.setHorizontalAlignment(JLabel.CENTER); //水平位置
    userLabel.setOpaque(true);
    userLabel.setBackground(Color.CYAN);
    this.add(userLabel);

    this.UserNameTextField = new JTextField();
    this.UserNameTextField.setBounds(100,286,400,44);
    this.UserNameTextField.setColumns(1);
    this.add(this.UserNameTextField);

    // 秘密のパスワードラベルの設定
    this.secretPasswordLabel = new JLabel();
    this.secretPasswordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    this.secretPasswordLabel.setBounds(500,176,300,66);
    this.secretPasswordLabel.setVerticalAlignment(JLabel.CENTER); //垂直位置
    this.secretPasswordLabel.setHorizontalAlignment(JLabel.CENTER); //水平位置
    this.secretPasswordLabel.setOpaque(true);
    this.secretPasswordLabel.setBackground(Color.CYAN);
    this.add(secretPasswordLabel);

    // 秘密のパスワード入力欄の設定
    this.SecretPasswordField = new JTextField();
    this.SecretPasswordField.setBounds(500,286,400,44);
    this.SecretPasswordField.setColumns(1);
    this.add(this.SecretPasswordField);

    // ヒント内容表示ボタン（supportボタン）
    this.SupportButton = new JButton("support");
    this.SupportButton.setActionCommand("SupportButton");
    this.SupportButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    this.SupportButton.setBounds(800,176,100,66);
    this.add(this.SupportButton);

    // ログインボタンの設定
    this.LoginButton = new JButton("Login");
    this.LoginButton.setActionCommand("LoginButton");
    this.LoginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    this.LoginButton.setBounds(300,418,400,44);
    this.add(this.LoginButton);
    
    // タイトルへ戻るボタンを設定
    this.TitleButton = new JButton("タイトルへ戻る");
    this.TitleButton.setActionCommand("TitleButton");
    this.TitleButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    this.TitleButton.setBounds(300,484,400,44);
    this.add(this.TitleButton);
  }

  public void Show()
  {
    this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "画面オープン"); });

    // supportボタン押下イベントのリスナを登録する
    this.SupportButton.addActionListener(this);
    // ログインボタン押下イベントのリスナを登録する
    this.LoginButton.addActionListener(this);
    // タイトルへ戻るボタン押下イベントのリスナーを登録する
    this.TitleButton.addActionListener(this);
    this.SetSecretInitialTips();
  }

  public void Hide()
  {
    this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "画面クローズ"); });

    // supportボタン押下イベントのリスナを解除する
    this.SupportButton.removeActionListener(this);
    // ログインボタン押下イベントのリスナを解除する
    this.LoginButton.removeActionListener(this);
    // タイトルへ戻るボタン押下イベントのリスナーを解除する
    this.TitleButton.removeActionListener(this);
  }

  /**
   * コントローラインスタンスを設定
   * @param controller コントローラインスタンス
   */
  public void SetController(ILostPassUserController controller)
  {
    this.Controller = controller;
  }

  /**
   * リスナ対象追加
   * @param listener 追加対象リスナインスタンス
   */
  public void AddListener(LostPassUserViewListener listener)
  {
    this.ListenerList.add(LostPassUserViewListener.class, listener);
  }

  /**
   * リスナ対象削除
   * @param listener 削除対象リスナインスタンス
   */
  public void RemoveListener(LostPassUserViewListener listener)
  {
    this.ListenerList.remove(LostPassUserViewListener.class, listener);
  }

  /**
   * ボタンからのアクションリスナー
   */
  public void actionPerformed(ActionEvent e)
  {
    switch (e.getActionCommand())
    {
      case "SupportButton":
        // supportボタン押下イベントを受信
        this.SupportButtonClicked();
        break;
      case "LoginButton":
        // ログインボタン押下イベントを受信
        this.LoginButtonClicked();
        break;
      case "TitleButton":
        // タイトルへボタン押下イベントを受信
        this.TitleButtonClicked();
        break;
      default:
        // ロジック上あり得ない
        break;
    }
  }

  /**
   * ログインボタン押下時の処理
   */
  private void LoginButtonClicked()
  {
    this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "ログインボタンが押下された。"); });
    System.out.println("ログインボタンが押下された");

    // ユーザ名取得
    String userNameTextField = this.UserNameTextField.getText();
    String secretPasswordField = this.SecretPasswordField.getText();
    this.Controller.LostPassUserLoginAuth(userNameTextField, secretPasswordField);
  }

  /**
   * タイトルへ戻るボタン押下時の処理
   */
  private void TitleButtonClicked()
  {
    this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "タイトルへ戻るボタンが押下された"); });
    System.out.println("タイトルへ戻るボタンが押下された");

    for (LostPassUserViewListener listener : this.ListenerList.getListeners(LostPassUserViewListener.class))
    {
      // タイトルへ戻るボタンを押下したことを通知
      listener.CloseButtonClicked();
    }
  }

  // ヒント内容のコンボボックスに初期値を設定
  public void SetSecretInitialTips()
  {
    this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "ヒント内容のコンボボックスに初期値を設定"); });
    this.secretPasswordLabel.setText(this.SECLET_PASSWORD_LABEL_CONTENT);
  }

  // ヒント内容のコンボボックスに要素を追加
  public void SetSecretTips(String secretTips)
  {
    this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "ヒント内容のコンボボックスに要素を設定（設定値：" + secretTips + "）"); });
    this.secretPasswordLabel.setText(secretTips);
  }

  // ログインボタンクリック時の処理
  private void SupportButtonClicked()
  {
    this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "Supportボタンが押下された"); });
    String userNameTextField = this.UserNameTextField.getText();

    // ユーザ名バリデーション処理
    this.Controller.GetSecretTips(userNameTextField);
  }

  // DB接続失敗ダイアログ表示
  public void FailureDialog()
  {
    this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "DB接続失敗ダイアログ表示"); });
    this.CommonDialogView.Show(CommonDialogType.DBConnectionFailureDialog, true);
  }

  // 入力テキスト相違ダイアログ表示
  public void InputContentFailureDialog()
  {
    this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "入力テキスト相違ダイアログ表示"); });
    this.CommonDialogView.Show(CommonDialogType.InputContentFailureDialog, true);
  }

  // ログイン失敗ダイアログ表示
  public void LoginFailure()
  {
    this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "ログイン失敗ダイアログ表示"); });
    this.CommonDialogView.Show(CommonDialogType.LoginFailureDialog, true);
  }

  // ログイン後画面に遷移
  public void TransitionAfterLoginView(UserData userData)
  {
    this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "ログイン後画面に遷移を通知"); });

    for (LostPassUserViewListener listener : this.ListenerList.getListeners(LostPassUserViewListener.class))
    {
      // 忘却者ログインに成功したことを通知
      listener.SuccessfulLostPasswordLogin(userData);
    }
  }
  
  /**
   * Controllerで渡す：isBusyで使う。
   * @param isDisp
   */
  public void ElementDisabled(boolean isDisabled)
  {
      this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "画面要素押下可否設定" + "(" + String.valueOf(isDisabled) + ")"); });
      this.UserNameTextField.setEnabled(!isDisabled);
      this.SecretPasswordField.setEnabled(!isDisabled);
      this.SupportButton.setEnabled(!isDisabled);
      this.LoginButton.setEnabled(!isDisabled);
      this.TitleButton.setEnabled(!isDisabled);
  }
}
