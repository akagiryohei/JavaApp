package View.Ganttchart.Listener;

import java.util.EventListener;

import Entity.GanttchartTask;

/**
 * ガントチャートパネルのイベントリスナ
 */
public interface GanttchartPanelListener extends EventListener
{
  /**
   * タスク進捗率ボタンが押下された
   * @param selectedTask 選択されたタスクインスタンス
   */
  public void OnTaskProgressRateClicked(GanttchartTask selectedTask);
}
