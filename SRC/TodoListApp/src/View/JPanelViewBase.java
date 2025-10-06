package View;

import java.awt.Component;
import java.awt.Container;
import java.awt.Insets;
import java.awt.Point;
import java.util.Optional;
import java.util.function.Consumer;

import javax.swing.*;

import Interface.Model.ILogger;

public abstract class JPanelViewBase extends JPanel
{
  /**
    * ロガーインスタンス
    */
  protected ILogger Logger;

  //abstractを付けて強制的に関数を作成するようにしてる
  // 画面表示
  public abstract void Show();

  // 画面非表示
  public abstract void Hide();

  /**
    * ロガーインスタンスが設定されていたらロギングする
    * @param action ロガーインスタンスが設定されていたらインスタンスをコールバック
    */
  protected void WithLogger(Consumer<ILogger> action)
  {
    Optional.ofNullable(this.Logger).ifPresent(action);
  }

  /**
    * ロガーインスタンスを依存性注入する
    * @param ロガーインスタンス
    */
  public void SetLogger(ILogger logger)
  {
    this.Logger = logger;
  }

  /**
    * コンポーネント座標を取得する
    * @param 取得対象コンポーネントオブジェクト
    * @return 取得対象コンポーネント表示座標
    */
  public Point GetComponentPostion(Component component)
  {
    int titleBarHeight = 0;

    // 親要素を走査しJFrameインスタンスを取得
    Container parent = this.getTopLevelAncestor();

    if (parent instanceof JFrame)
    {
      JFrame frame = (JFrame) parent;
      Insets insets = frame.getInsets();

      // JFrameインスタンスを確認できた場合は、ウィンドウタイトルバー分のオフセット値を取得する
      titleBarHeight = insets.top;
    }

    int x = this.getLocation().x + component.getX();
    int y = (this.getLocation().y + titleBarHeight) + component.getY() + component.getHeight();
    Point ret = new Point(x, y);
    
    return ret;
  }
}
