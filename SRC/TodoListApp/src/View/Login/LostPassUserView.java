package View.Login;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.EventListenerList;

import Entity.UserData;
import Entity.Dialog.Constants;
import Entity.Enum.LogLevel;
import Interface.Controller.Login.ILostPassUserController;
import Interface.View.IMainWindowView;
import Interface.View.Login.ILostPassUserView;
import View.JPanelViewBase;
import View.Common.JPlaceholderTextField;
import View.Dialog.CommonDialogView;
import View.Dialog.CommonDialogView.CommonDialogType;
import View.Dialog.Listener.ReminderDialogViewListener;
import View.Dialog.ReminderDialogView;
import View.Login.Listener.LostPassUserViewListener;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

// 忘却者Viewクラス
public class LostPassUserView extends JPanelViewBase implements ILostPassUserView, ActionListener, DocumentListener, ReminderDialogViewListener
{
  // ヒント内容の初期値
  private final String SECLET_PASSWORD_LABEL_CONTENT = "<html><body>ヒントが欲しい人は自身のメールアドレス<br />入力しsupportボタン押下</body></html>";

  // ユーザ名テキストフィールドのインスタンス
  private JPlaceholderTextField UserNameTextField;

  // 秘密のパスワード文字列テキストフィールドのインスタンス
  private JPlaceholderTextField SecretPasswordField;

  // 秘密のパスワードのヒントラベル
  private JLabel secretPasswordLabel;
  
  // ヒント内容表示ボタン（supportボタン）
  private JButton SupportButton;
  
  // ログインボタンのインスタンス
  private JButton LoginButton;

  // タイトルへ戻るボタンのインスタンス
  private JButton TitleButton;

  // 画面内入力の値（ユーザー名、パスワード）
  public ArrayList InputList;

  // 忘却者画面コントローラー
  private ILostPassUserController Controller;

  // 共通ダイアログのインスタンス
  private CommonDialogView CommonDialogView;

  // リマインダーダイアログのインスタンス
  private ReminderDialogView ReminderDialogView;

  // イベントリスナインスタンス
  protected EventListenerList ListenerList;

  // ログイン成功したユーザID
  private UserData LoginedUserData;

  /**
   * コンストラクタ
   * @param commonDialogView CommonDialogViewのインスタンス
   */
  public LostPassUserView(CommonDialogView commonDialogView, ReminderDialogView reminderDialogView)
  {
    // イベントリスナインスタンスを初期化
    this.ListenerList = new EventListenerList();
    
    // ダイアログインスタンスを初期化
    this.CommonDialogView = commonDialogView;

    // リマインダーダイアログインスタンスを初期化
    this.ReminderDialogView = reminderDialogView;
    
    // 一番親のLoginViewにかかってるLayoutマネージャーの向こうかが可能になる
    this.setLayout(null);

    // Supportボタン横のガイダンスラベルの設定
    JLabel guidanceLabel = new JLabel("<html><body>パスワードを忘れた方へ<br />秘密のパスワード入力画面</body></html>");
    guidanceLabel.setBounds(300,22,400,88);
    guidanceLabel.setVerticalAlignment(JLabel.CENTER); //垂直位置
    guidanceLabel.setHorizontalAlignment(JLabel.CENTER); //水平位置
    guidanceLabel.setOpaque(true);
    guidanceLabel.setBackground(Color.CYAN);
    this.add(guidanceLabel);

    // ユーザ名入力ガイダンスラベルの設定
    JLabel userLabel = new JLabel("ユーザ名を入力");
    userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    userLabel.setBounds(100,176,400,66);
    userLabel.setVerticalAlignment(JLabel.CENTER); //垂直位置
    userLabel.setHorizontalAlignment(JLabel.CENTER); //水平位置
    userLabel.setOpaque(true);
    userLabel.setBackground(Color.CYAN);
    this.add(userLabel);

    // ユーザ名入力欄の設定
    this.UserNameTextField = new JPlaceholderTextField();
    this.UserNameTextField.SetPlaceholderText("入力欄（メールアドレス）");
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
    this.SecretPasswordField = new JPlaceholderTextField();
    this.SecretPasswordField.SetPlaceholderText("入力欄（秘密のパスワード）");
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
    this.LoginButton = new JButton("Try Login");
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

    // Supportボタン押下イベントのリスナを登録する
    this.SupportButton.addActionListener(this);
    this.SupportButtonDisabled(false);
    // メールアドレス入力イベントのリスナーを登録する
    this.UserNameTextField.getDocument().addDocumentListener(this);
    // 秘密のパスワード入力イベントのリスナーを登録する
    this.SecretPasswordField.getDocument().addDocumentListener(this);
    // ログインボタン押下イベントのリスナを登録する
    this.LoginButton.addActionListener(this);
    this.LoginButtonDisabled(false);
    // リマインダーダイアログのリスナを登録する
    this.ReminderDialogView.AddListener(this);
    // タイトルへ戻るボタン押下イベントのリスナーを登録する
    this.TitleButton.addActionListener(this);
    // ヒント内容の表示ラベルに初期値を設定する
    this.SetSecretInitialTips();
  }

  public void Hide()
  {
    this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "画面クローズ"); });

    // Supportボタン押下イベントのリスナを解除する
    this.SupportButton.removeActionListener(this);
    // メールアドレス入力イベントのリスナーを解除する
    this.UserNameTextField.getDocument().removeDocumentListener(this);
    // 秘密のパスワード入力イベントのリスナーを解除する
    this.SecretPasswordField.getDocument().removeDocumentListener(this);
    // ログインボタン押下イベントのリスナを解除する
    this.LoginButton.removeActionListener(this);
    // リマインダーダイアログのリスナを解除する
    this.ReminderDialogView.RemoveListener(this);
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
   * ユーザ名が入力されたとき
   * @param e 変化を検知した入力欄のインスタンス情報
  */
  @Override
  public void insertUpdate(DocumentEvent e) {
    this.ChangedTextField(e);
  }

  /**
   * ユーザ名が消されたとき
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

  // ヒント内容の表示ラベルに初期値を設定
  public void SetSecretInitialTips()
  {
    this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "ヒント内容の表示ラベルに初期値を設定"); });
    this.secretPasswordLabel.setText(this.SECLET_PASSWORD_LABEL_CONTENT);
  }

  // ヒント内容の表示ラベルに要素を追加
  public void SetSecretTips(String secretTips)
  {
    this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "ヒント内容の表示ラベルに要素を設定（設定値：" + secretTips + "）"); });
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
  public void ShowDBConnectionFailureDialog()
  {
    this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "DB接続失敗ダイアログ表示"); });
    this.CommonDialogView.Show(CommonDialogType.DBConnectionFailureDialog, true);
  }

  // 入力テキスト相違ダイアログ表示
  public void ShowInputContentFailureDialog()
  {
    this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "入力テキスト相違ダイアログ表示"); });
    this.CommonDialogView.Show(CommonDialogType.InputContentFailureDialog, true);
  }

  // ログイン失敗ダイアログ表示
  public void ShowLoginFailureDialog()
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
      this.SecretPasswordField.setEnabled(!isDisabled);
      this.TitleButton.setEnabled(!isDisabled);
      if(!isDisabled)
      {
        this.Controller.ChangedTextField(this.UserNameTextField.getText());
        this.Controller.ChangedTextField(this.UserNameTextField.getText(), this.SecretPasswordField.getText());
      }
      else
      {
        this.SupportButton.setEnabled(!isDisabled);
        this.LoginButton.setEnabled(!isDisabled);
      }
  }

  /**
   * ユーザ名の入力内容監視
   * @param isDisabled Supportボタンの押下可否
   */
  public void SupportButtonDisabled(boolean isDisabled)
  {
    this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "Support ボタン押下可否設定" + "(" + String.valueOf(isDisabled) + ")"); });
    this.SupportButton.setEnabled(isDisabled);
  }

  /**
   * ユーザ名の入力内容監視
   * @param isDisabled Try Loginボタンの押下可否
   */
  public void LoginButtonDisabled(boolean isDisabled)
  {
    this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "Try Login ボタン押下可否設定" + "(" + String.valueOf(isDisabled) + ")"); });
    this.LoginButton.setEnabled(isDisabled);
  }
  
  /**
   * ユーザ名の入力内容監視
   * @param e ユーザ名のインスタンス情報
  */
  private void ChangedTextField(DocumentEvent e){
    if(e.getDocument() == this.UserNameTextField.getDocument())
    {
      this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "ユーザ名の入力内容が変更されました"); });
    }
    else if(e.getDocument() == this.SecretPasswordField.getDocument())
    {
      this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "秘密のパスワードの入力内容が変更されました"); });
    }
    // Supportボタンの評価
    this.Controller.ChangedTextField(this.UserNameTextField.getText());
    // Try Loginボタンの評価
    this.Controller.ChangedTextField(this.UserNameTextField.getText(), this.SecretPasswordField.getText());
  }
}