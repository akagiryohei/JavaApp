package View.Dialog.Listener;

import java.util.EventListener;

/**
 * リスト名更新ダイアログのイベントリスナ
 */
public interface UpdateListDialogViewListener extends EventListener
{
    /**
     * 編集ボタン押下時イベント
     * @param UpdateListName 選択中リスト名
     */
    public void UpdateListClicked(String UpdateListName);
}
