package View;

import javax.swing.*;

public abstract class JFrameViewBase extends JFrame
{
  //abstractを付けて強制的に関数を作成するようにしてる
  
  // 画面表示
  public abstract void Show();

  // 画面非表示
  public abstract void Hide();
}
