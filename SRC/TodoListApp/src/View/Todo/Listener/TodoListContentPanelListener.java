package View.Todo.Listener;

import java.util.Date;
import java.util.EventListener;

/**
 * TODOリストの内容表示パネルのイベントリスナ
 */
public interface TodoListContentPanelListener extends EventListener
{
    /**
     * タスク作成メソッド
     * @param TaskText
     * @param StartDate
     * @param EndDate
     */
    public void CreateUserTask(String TaskText, Date StartDate, Date EndDate);

    /**
     * 期日登録ダイアログ
     */
    public void PeriodButtonDialog();

    /**
     * タスク削除ボタン押下時メソッド
     * @param taskid 画面の選択中タスクID
     */
    public void DeleteTaskClicked(int taskid);

    /**
     * タスクアップデートダイアログ
     */
    public void UpdateTaskDialog(int taskId, String taskText);

    /**
     * チェックボタン押下時処理
     */
    public void CheckBoxClicked(int taskid, boolean isChecked);

    /**
     * 期日変更ダイアログ
     */
    public void PeriodButtonDialog(int taskId, String startDate, String endDate);

    /**
     * タスク入力欄の監視(cotrollerメソッド呼び出し)
     */
    public void ChangeTextField(String taskText);

}
