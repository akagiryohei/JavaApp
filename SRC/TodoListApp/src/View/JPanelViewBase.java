package View;

import javax.swing.*;

public abstract class JPanelViewBase extends JPanel
{
  //abstractを付けて強制的に関数を作成するようにしてる
  // 画面表示
  public abstract void Show();

  // 画面非表示
  public abstract void Hide();
}
