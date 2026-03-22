package View.Todo.Listener;

import java.util.EventListener;

/**
 * ボード画面とAIリスト・タスク作成画面の共通設定のイベントリスナ
 */
public interface TodoSideBoardPanelListener extends EventListener
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
     * AIリスト・タスク案作成画面ボタンクリック時の処理
     */
    public void AICreateListTaskButtonClicked();


}
