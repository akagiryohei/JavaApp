package View.Login;

import javax.swing.*;

import View.JPanelViewBase;

import java.awt.*;
import java.awt.event.*;

// 新規登録Viewクラス
public class SignupView extends JPanelViewBase implements ActionListener
{
  // ユーザ名テキストフィールドのインスタンス
  private JTextField userNameTextField;

  // パスワード文字列テキストフィールドのインスタンス
  private JPasswordField passwordTextField;

  // 登録ボタンのインスタンス
  private JButton signUpButton;

  // ヒント内容のコンボボックスの設定
  private JComboBox secretTipsBox;

  // 秘密のパスワード文字列テキストフィールドのインスタンス
  private JTextField secretPasswordField;

  // キャンセルボタンのインスタンス
  private JButton cancelButton;

  // 親画面のインスタンス
  private LoginBaseView BaseViewInstance;

  // コンストラクタ
  // baseViewInstance : 親画面のインスタンス
  public SignupView(LoginBaseView baseViewInstance)
  {
    // TODO : 親画面のインスタンスをView層で直接持つのは検討の余地あり
    this.BaseViewInstance = baseViewInstance;
    
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
    JLabel userLabel = new JLabel("ユーザ名(メールアドレス)");
    userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    userLabel.setBounds(300,176,200,66);
    userLabel.setVerticalAlignment(JLabel.CENTER); //垂直位置
    userLabel.setHorizontalAlignment(JLabel.CENTER); //水平位置
    userLabel.setOpaque(true);
    userLabel.setBackground(Color.CYAN);
    this.add(userLabel);

    this.userNameTextField = new JTextField();
    userNameTextField.setBounds(500,176,200,66);
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
    passwordTextField.setBounds(500,264,200,66);
    this.passwordTextField.setColumns(1);
    this.add(this.passwordTextField);

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
    // JComboBox secretTipsBox = new JComboBox({"学校","恋人","トラウマ"});
    String[] secretTip = {"未選択","学校","恋人","トラウマ"};
    this.secretTipsBox = new JComboBox(secretTip);
    this.secretTipsBox.setBounds(500,374,200,22);
    this.secretTipsBox.setActionCommand("SecretTipsBox");
    this.add(this.secretTipsBox);

    // 秘密のパスワード入力欄の設定
    this.secretPasswordField = new JTextField();
    this.secretPasswordField.setBounds(500,418,200,22);
    this.secretPasswordField.setColumns(1);
    this.add(this.secretPasswordField);

    // 登録ボタンの設定
    this.signUpButton = new JButton("登録");
    this.signUpButton.setActionCommand("SignUpButton");
    this.signUpButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    this.signUpButton.setBounds(300,462,200,44);
    this.add(this.signUpButton);

    // キャンセルボタンを設定
    this.cancelButton = new JButton("キャンセル");
    this.cancelButton.setActionCommand("CancelButton");
    this.cancelButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    this.cancelButton.setBounds(500,462,200,44);
    this.add(this.cancelButton);
  }

  public void Show()
  {
    // ボタン押下イベントのリスナーを登録する
    this.cancelButton.addActionListener(this);
  }

  public void Hide()
  {
    // ボタン押下イベントのリスナーを解除する
    this.cancelButton.removeActionListener(this);
  }

  // ボタンからのアクションリスナー
  public void actionPerformed(ActionEvent e)
  {
    switch (e.getActionCommand())
    {
      case "CancelButton":
        // キャンセルボタン押下イベントを受信
        this.CancelButtonClicked();
        break;
      default:
        // ロジック上あり得ない
        break;
    }
  }

  // キャンセルボタン押下時の処理
  private void CancelButtonClicked()
  {
    System.out.println("新規アカウント作成ボタンが押下された");

    // TODO: 親画面に対して子画面から表示状態を指示するのは違和感を感じる（コールバックやイベントハンドラに修正を検討）
    this.BaseViewInstance.ChangeView(LoginBaseView.ViewType.LoginView);
  }
}
