package Interface.View.Todo;

import View.Todo.TodoListBaseView.ViewType;

/*
 * ログイン後画面基底Viewインタフェース
 */
public interface ITodoListBaseView
{
  /**
   * 画面表示
   */
  public void Show();

  /**
   * 画面非表示
   */
  public void Hide();

  /** TODO: イベントリスナに修正するとこのインタフェースを消せる
   * View切り替えメソッド
   * @param type ViewType
   */
  public void ChangeView(ViewType type);
}
