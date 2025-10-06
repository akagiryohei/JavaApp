package Interface.Controller.Todo;

import Interface.View.Todo.ITodoListBaseView;

/*
 * Todoリスト全般対応コントローラ
 */
public interface ITodoListBaseController
{
  /**
   * 画面表示
   */
  public void Show();

  /**
   * 画面非表示
   */
  public void Hide();

  /**
   * 画面のインスタンスを取得
   * @return Todoリスト全般画面Viewインスタンス
   */
  public ITodoListBaseView GetViewInstance();
}
