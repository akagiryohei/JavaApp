package View.Dialog.Listener;

import java.util.EventListener;

/**
 * リマインダーダイアログのイベントリスナ
 */
public interface ReminderDialogViewListener extends EventListener
{
  /**
   * リマインダーリスト画面のOKボタン押下イベント
   * ログイン時に全面に表示されるタスク一覧画面
   */
  public void ReminderOKButtonClicked();
}