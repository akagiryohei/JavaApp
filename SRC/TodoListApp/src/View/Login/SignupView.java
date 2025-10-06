package View.Login;

import javax.swing.*;
import javax.swing.event.EventListenerList;

import Controller.Login.SignupController;
import Entity.Dialog.Constants;
import Entity.Enum.LogLevel;
import Interface.Controller.Login.ISignupController;
import Interface.View.IMainWindowView;
import Interface.View.Login.ISignupView;
import View.JPanelViewBase;
import View.Dialog.CommonDialogView;
import View.Dialog.CommonDialogView.CommonDialogType;
import View.Login.Listener.SignupViewListener;

import java.awt.*;
import java.awt.event.*;

import java.util.List;

// 新規登録Viewクラス
public class SignupView extends JPanelViewBase implements ISignupView, ActionListener
{
  // ユーザ名テキストフィールドのインスタンス
  private JTextField UserNameTextField;

  // パスワード文字列テキストフィールドのインスタンス
  private JPasswordField PasswordTextField;

  // 登録ボタンのインスタンス
  private JButton SignUpButton;

  // ヒント内容のコンボボックスの設定
  private JComboBox SecretTipsBox;

  // 秘密のパスワード文字列テキストフィールドのインスタンス
  private JTextField SecretPasswordField;

  // 最後の選択した秘密のパスワードの入力文字列
  private String LastInputSecretPassword;

  // キャンセルボタンのインスタンス
  private JButton CancelButton;

  // 新規登録画面コントローラー
  private ISignupController Controller;

  // イベントリスナインスタンス
  protected EventListenerList ListenerList;

  // 共通ダイアログのインスタンス
  private CommonDialogView CommonDialogView;

  /**
   * コンストラクタ
   */
  public SignupView(IMainWindowView mainWindowView, CommonDialogView commonDialogView)
  {
    // イベントリスナインスタンスを初期化
    this.ListenerList = new EventListenerList();

    // ダイアログインスタンスを初期化
    this.CommonDialogView = commonDialogView;
    
    // 一番親のLoginViewにかかってるLayoutマネージャーの向こうかが可能になる
    this.setLayout(null);

    // ガイダンスラベルの設定
    JLabel guidanceLabel = new JLabel("アカウント登録しましょう");
    guidanceLabel.setBounds(300,22,400,132);
    guidanceLabel.setVerticalAlignment(JLabel.CENTER); //垂直位置
    guidanceLabel.setHorizontalAlignment(JLabel.CENTER); //水平位置
    guidanceLabel.setOpaque(true);
    guidanceLabel.setBackground(Color.CYAN);
    this.add(guidanceLabel);

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

    // パスワードの注意書きラベルの設定
    JLabel passwordAttentionLabel = new JLabel("※文字数8文字以上半角英数字のみ");
    passwordAttentionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    passwordAttentionLabel.setBounds(500,330,200,22);
    passwordAttentionLabel.setForeground(Color.RED);
    this.add(passwordAttentionLabel);

    // 秘密のパスワードラベルの設定
    JLabel secretPasswordLabel = new JLabel("秘密のパスワード");
    secretPasswordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    secretPasswordLabel.setBounds(300,374,200,66);
    secretPasswordLabel.setVerticalAlignment(JLabel.CENTER); //垂直位置
    secretPasswordLabel.setHorizontalAlignment(JLabel.CENTER); //水平位置
    secretPasswordLabel.setOpaque(true);
    secretPasswordLabel.setBackground(Color.CYAN);
    this.add(secretPasswordLabel);

    // ヒント内容のコンボボックスの設定
    this.SecretTipsBox = new JComboBox();
    this.SecretTipsBox.setBounds(500,374,200,22);
    this.SecretTipsBox.setActionCommand("SecretTipsBox");
    this.add(this.SecretTipsBox);

    // 秘密のパスワード入力欄の設定
    this.SecretPasswordField = new JTextField();
    this.SecretPasswordField.setBounds(500,418,200,22);
    this.SecretPasswordField.setColumns(1);
    this.LastInputSecretPassword = "";
    this.add(this.SecretPasswordField);

    // 登録ボタンの設定
    this.SignUpButton = new JButton("登録");
    this.SignUpButton.setActionCommand("SignUpButton");
    this.SignUpButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    this.SignUpButton.setBounds(300,462,200,44);
    this.add(this.SignUpButton);

    // キャンセルボタンを設定
    this.CancelButton = new JButton("キャンセル");
    this.CancelButton.setActionCommand("CancelButton");
    this.CancelButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    this.CancelButton.setBounds(500,462,200,44);
    this.add(this.CancelButton);
  }

  public void Show()
  {
    this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "画面オープン"); });

    // ボタン押下イベントのリスナーを登録する
    this.CancelButton.addActionListener(this);
    this.SignUpButton.addActionListener(this);
    this.SecretTipsBox.addActionListener(this);
    this.LastInputSecretPassword = "";
    this.Controller.GetSecretTipsList();
  }

  public void Hide()
  {
    this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "画面クローズ"); });

    // ボタン押下イベントのリスナーを解除する
    this.CancelButton.removeActionListener(this);
    this.SignUpButton.removeActionListener(this);
    this.SecretTipsBox.removeActionListener(this);
  }

  /**
   * コントローラインスタンスを設定
   * @param controller コントローラインスタンス
   */
  public void SetController(ISignupController controller)
  {
    this.Controller = controller;
  }

  /**
   * リスナ対象追加
   * @param listener 追加対象リスナインスタンス
   */
  public void AddListener(SignupViewListener listener)
  {
    this.ListenerList.add(SignupViewListener.class, listener);
  }

  /**
   * リスナ対象削除
   * @param listener 削除対象リスナインスタンス
   */
  public void RemoveListener(SignupViewListener listener)
  {
    this.ListenerList.remove(SignupViewListener.class, listener);
  }

  /**
   * ボタンからのアクションリスナー
   */
  public void actionPerformed(ActionEvent e)
  {
    switch (e.getActionCommand())
    {
      case "SignUpButton":
        // 登録ボタン押下イベントを受信
        this.SignupButtonClicked();
      break;
      case "CancelButton":
        // キャンセルボタン押下イベントを受信
        this.CancelButtonClicked();
        break;
      case "SecretTipsBox":
        this.SecretTipsBoxChanged();
        break;
      default:
        // ロジック上あり得ない
        break;
    }
  }

  /**
   * 秘密のパスワードの選択リストが変更されたとき
   */
  private void SecretTipsBoxChanged()
  {
    String changedValue = (String) this.SecretTipsBox.getSelectedItem();
    if(this.LastInputSecretPassword != changedValue)
    {
      this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "秘密のパスワードの選択リストが変更された"); });
      this.SecretPasswordField.setText("");
      this.LastInputSecretPassword = changedValue;
    }
  }

  // ログインボタンクリック時の処理
  private void SignupButtonClicked()
  {
    this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "新規登録ボタンが押下された"); });

    String userNameTextField = this.UserNameTextField.getText();
    String passwordTextField = new String(this.PasswordTextField.getPassword());
    String secretTipsBox = Integer.toString(this.SecretTipsBox.getSelectedIndex());
    String secretPasswordField = this.SecretPasswordField.getText();
    
    // ユーザ名バリデーション処理
    this.Controller.SignupAuth(userNameTextField, passwordTextField, secretTipsBox, secretPasswordField);
  }

  /**
   * キャンセルボタン押下時の処理
   */
  private void CancelButtonClicked()
  {
    this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "キャンセルボタンが押下された"); });
    System.out.println("新規アカウント作成ボタンが押下された");
    
    this.SuccessfulSignupOrCloseButtonClicked();
  }

  /**
   * リスナに対して画面クローズを指示
   */
  private void SuccessfulSignupOrCloseButtonClicked()
  {
    this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "サインアップ成功またはキャンセルボタン押下を通知"); });

    for (SignupViewListener listener : this.ListenerList.getListeners(SignupViewListener.class))
    {
      // 画面クローズを通知
      listener.SuccessfulSignupOrCloseButtonClicked();
    }
  }

  // ヒント内容のコンボボックスに要素を追加
  public void SetSecretTipsList(List<String> secretTipsList)
  {
    this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "ヒント内容をコンボボックスに設定"); });

    this.SecretTipsBox.addItem("未選択");
    this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "コンボボックスに「" + "未選択" + "」を設定"); });
    for (String item : secretTipsList)
    {
      this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "コンボボックスに「" + item + "」を設定"); });
      this.SecretTipsBox.addItem(item);
    }
  }

  // 秘密の質問一覧取得失敗ダイアログ表示
  public void GetSecretTipsListFailure()
  {
    this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "秘密の質問一覧取得失敗ダイアログ表示"); });
    this.CommonDialogView.Show(CommonDialogType.DBConnectionFailureDialog, true);
  }

  // 新規アカウント登録失敗ダイアログ表示
  public void SignupFailure()
  {
    this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "新規アカウント登録失敗ダイアログ表示"); });
    this.CommonDialogView.Show(CommonDialogType.SiginuPFailureDialog, true);
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
      this.SecretTipsBox.setEnabled(!isDisabled);
      this.SecretPasswordField.setEnabled(!isDisabled);
      this.SignUpButton.setEnabled(!isDisabled);
      this.CancelButton.setEnabled(!isDisabled);
  }

  /**
   * Controllerで渡す：isBusyで使う。
   */
  public void CancelButtonOnlyEnabled()
  {
    this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "キャンセルボタンのみ押下可能に設定"); });
    this.UserNameTextField.setEnabled(false);
    this.PasswordTextField.setEnabled(false);
    this.SecretTipsBox.setEnabled(false);
    this.SecretPasswordField.setEnabled(false);
    this.SignUpButton.setEnabled(false);
    this.CancelButton.setEnabled(true);
  }
}