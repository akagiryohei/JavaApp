package Controller;

import java.util.Optional;
import java.util.function.Consumer;

import Entity.Enum.ViewStateEnum;
import Interface.Model.ILogger;

/*
 * 全コントローラのベース
 */
public abstract class ControllerBase
{
  // 画面表示状態
  private ViewStateEnum ViewState;

  /**
    * ロガーインスタンス
    */
  protected ILogger Logger;

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

  // 画面表示
  public abstract void Show();

  // 画面非表示
  public abstract void Hide();
}
