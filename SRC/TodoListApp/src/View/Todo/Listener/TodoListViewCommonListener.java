package View.Todo.Listener;

import java.util.EventListener;

/**
 * ログイン後画面基底Viewクラスリスナ
 */
public interface TodoListViewCommonListener extends EventListener
{
  /**
   * ボードボタンクリック時の処理
   */
  public void BoardButtonClicked();

  /**
   * リストボタンクリック時の処理　現在リスト画面であるかを確認する必要がある
   */
  public void ListButtonClicked();

  /**
   * ガントチャートボタンクリック時の処理
   */
  public void GanttchartButtonClicked();

  /**
   * AIリスト・タスク案作成ボタンクリック時の処理
   */
  public void AICreateListTaskButtonClicked();
}
