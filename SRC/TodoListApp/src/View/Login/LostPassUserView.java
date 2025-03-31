package View.Login;

import javax.swing.*;

import Controller.MainWindowController;
import Entity.Dialog.Constants;
import View.JPanelViewBase;
import View.MainWindowView;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.regex.Pattern;

// 新規登録Viewクラス
public class LostPassUserView extends JPanelViewBase implements ActionListener
{
  // ユーザ名テキストフィールドのインスタンス
  private JTextField userNameTextField;

  // 秘密のパスワード文字列テキストフィールドのインスタンス
  private JTextField secretPasswordField;
  
  // ヒント内容表示ボタン（supportボタン）
  private JButton supportButton;
  
  // ログインボタンのインスタンス
  private JButton loginButton;

  // タイトルへ戻るボタンのインスタンス
  private JButton titleButton;

  // 親画面のインスタンス
  private LoginBaseView BaseViewInstance;

  // メールアドレス正規表現
  private String regexUser;

  // メールアドレス正規表現パターン確定
  private Pattern patternUser;

  // 画面内入力の値（ユーザー名、パスワード）
  public ArrayList InputList;

  // MainWindowViewのインスタンス
  private MainWindowView MainWindowViewInstance;

  // コンストラクタ
  // baseViewInstance : 親画面のインスタンス
  public LostPassUserView(LoginBaseView baseViewInstance, MainWindowView mainWindowViewInstance)
  {
      // メールアドレス正規表現
    this.regexUser = "^[a-zA-Z0-9_\\-]+(\\.[a-zA-Z0-9_\\-]+)*@([a-zA-Z0-9][a-zA-Z0-9\\-]*[a-zA-Z0-9]\\.)+[a-zA-Z]{2,}$";
    this.patternUser = Pattern.compile(regexUser);//regexの文字列を正規表現パターンとして解釈、コンパイルする。Patternオブジェクトとして返す

    // TODO : 親画面のインスタンスをView層で直接持つのは検討の余地あり
    this.BaseViewInstance = baseViewInstance;
    this.MainWindowViewInstance = mainWindowViewInstance;
    
    // 一番親のLoginViewにかかってるLayoutマネージャーの向こうかが可能になる
    this.setLayout(null);

    this.setBackground(Color.RED);

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

    this.userNameTextField = new JTextField();
    userNameTextField.setBounds(100,286,400,44);
    this.userNameTextField.setColumns(1);
    this.add(this.userNameTextField);

    // 秘密のパスワードラベルの設定
    JLabel secretPasswordLabel = new JLabel("<html><body>ヒントが欲しい人は自身のメールアドレスを<br />入力しsupportボタン押下</body></html>");
    secretPasswordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    secretPasswordLabel.setBounds(500,176,300,66);
    secretPasswordLabel.setVerticalAlignment(JLabel.CENTER); //垂直位置
    secretPasswordLabel.setHorizontalAlignment(JLabel.CENTER); //水平位置
    secretPasswordLabel.setOpaque(true);
    secretPasswordLabel.setBackground(Color.CYAN);
    this.add(secretPasswordLabel);

    // 秘密のパスワード入力欄の設定
    this.secretPasswordField = new JTextField();
    secretPasswordField.setBounds(500,286,400,44);
    this.secretPasswordField.setColumns(1);
    this.add(this.secretPasswordField);

    // ヒント内容表示ボタン（supportボタン）
    this.supportButton = new JButton("support");
    this.supportButton.setActionCommand("SupportButton");
    this.supportButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    this.supportButton.setBounds(800,176,100,66);
    this.add(this.supportButton);

    // ログインボタンの設定
    this.loginButton = new JButton("Login");
    this.loginButton.setActionCommand("LoginButton");
    this.loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    this.loginButton.setBounds(300,418,400,44);
    this.add(this.loginButton);
    
    // タイトルへ戻るボタンを設定
    this.titleButton = new JButton("タイトルへ戻る");
    this.titleButton.setActionCommand("TitleButton");
    this.titleButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    this.titleButton.setBounds(300,484,400,44);
    this.add(this.titleButton);
  }

  public void Show()
  {
    // supportボタン押下イベントのリスナを登録する
    this.supportButton.addActionListener(this);
    // ログインボタン押下イベントのリスナを登録する
    this.loginButton.addActionListener(this);
    // タイトルへ戻るボタン押下イベントのリスナーを登録する
    this.titleButton.addActionListener(this);
  }

  public void Hide()
  {
    // supportボタン押下イベントのリスナを解除する
    this.supportButton.removeActionListener(this);
    // ログインボタン押下イベントのリスナを解除する
    this.loginButton.removeActionListener(this);
    // タイトルへ戻るボタン押下イベントのリスナーを解除する
    this.titleButton.removeActionListener(this);
  }

  // ボタンからのアクションリスナー
  public void actionPerformed(ActionEvent e)
  {
    switch (e.getActionCommand())
    {
      case "SupportButton":
        // supportボタン押下イベントを受信
        this.SupportList();
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

  // supportボタン押下時処理
  private void SupportList()
  {
    // ユーザー名バリデーション
    // メールアドレス正規表現
    String UserNameTextField = this.userNameTextField.getText();
    boolean Judgment = this.patternUser.matcher(UserNameTextField).matches();
    String supportList = "両親の旧姓";
    String userName = "akagi@gmail.com";
    if(Judgment)
    {
      if(userName.equals(UserNameTextField))//akagi:このif文がDBへの問い合わせになってる
      {
        Dialog(supportList, Constants.DIALOG);// DB問い合わせがうまくいたとき、秘密のパスワードのヒントが収納されたテーブルから取得してくる物
      }
      else
      {
        Dialog(Constants.USER_NAME_FAILURE_OPERATION, Constants.UNDEFINED_DIALOG);
      }
    }
    else
    {
      Dialog(Constants.USER_NAME_FaILURE_VALIDATION_OPERATION, Constants.UNDEFINED_DIALOG);
    }
  }

  // ログインボタン押下時の処理
  private void LoginButtonClicked()
  {
    System.out.println("ログインボタンが押下された");

    // ユーザ名取得
    String UserNameTextField = this.userNameTextField.getText();
    String SecretPasswordField = this.secretPasswordField.getText();

    // ユーザ名バリデーション処理
    if(this.patternUser.matcher(UserNameTextField).matches())
    {
      System.out.println(UserNameTextField + "は有効なメールアドレスです");
    }
    else
    {
      System.out.println(UserNameTextField + "は無効なメールアドレスです");
    }
    System.out.println(SecretPasswordField);

    this.InputList = new ArrayList<>();
    this.InputList.add(UserNameTextField);
    this.InputList.add(SecretPasswordField);
    this.BaseViewInstance.TarnationAfterLoginView("");
    // TODO: 親画面に対して子画面から表示状態を指示するのは違和感を感じる（コールバックやイベントハンドラに修正を検討）
    // this.BaseViewInstance.ChangeView(LoginBaseView.ViewType.LoginView);
  }

  // タイトルへ戻るボタン押下時の処理
  private void TitleButtonClicked()
  {
    System.out.println("タイトルへ戻るボタンが押下された");

    // TODO: 親画面に対して子画面から表示状態を指示するのは違和感を感じる（コールバックやイベントハンドラに修正を検討）
    this.BaseViewInstance.ChangeView(LoginBaseView.ViewType.LoginView);
  }


  // 失敗ダイアログ表示
  private void Dialog(String OPERATION, String DIALOG)
  {
    JDialog dialog = new JDialog(this.MainWindowViewInstance, DIALOG, true);
    dialog.setSize(450,150);
    dialog.setLayout(null);
    JLabel operationLabel = new JLabel(OPERATION);
    operationLabel.setBounds(20,20,250,70);
    dialog.add(operationLabel);
    dialog.setLocationRelativeTo(null);
    dialog.setVisible(true);
  }

}
