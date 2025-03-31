package Controller;

import Entity.Enum.ViewStateEnum;

/*
 * 全コントローラのベース
 */
public abstract class ControllerBase
{
  // 画面表示状態
  private ViewStateEnum ViewState;

  // 画面表示
  public abstract void Show();

  // 画面非表示
  public abstract void Hide();
}
