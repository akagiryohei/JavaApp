package View;

import java.awt.Component;
import java.awt.Insets;
import java.awt.Point;

import javax.swing.*;

public abstract class JFrameViewBase extends JFrame
{
  //abstractを付けて強制的に関数を作成するようにしてる
  
  // 画面表示
  public abstract void Show();

  // 画面非表示
  public abstract void Hide();

  /**
    * コンポーネント座標を取得する
    * @param 取得対象コンポーネントオブジェクト
    * @return 取得対象コンポーネント表示座標
    */
  public Point GetComponentPostion(Component component)
  {
    int titleBarHeight = 0;
    Insets insets = this.getInsets();
    titleBarHeight = insets.top;
    
    int x = this.getLocation().x + component.getX();
    int y = (this.getLocation().y + titleBarHeight) + component.getY() + component.getHeight();
    Point ret = new Point(x, y);

    return ret;
  }
}
