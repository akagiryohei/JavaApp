package View.Dialog.Listener;

import java.util.EventListener;
/**
 * タスク名更新ダイアログのイベントリスナ
 */
public interface UpdateTaskDialogViewListener extends EventListener
{
    /**
     * 編集ボタン押下時イベント
     * @param UpdateTaskName 選択中タスク名
     */
    public void UpdateTaskClicked(String UpdateTaskName);
}
