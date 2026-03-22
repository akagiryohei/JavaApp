package View.Dialog.Listener;

import java.util.EventListener;

/**
 * 致命的エラーダイアログのイベントリスナ
 */
public interface FatalErrorDialogViewListener extends EventListener
{
  /**
   * アプリ終了ボタン押下イベント
   */
  public void TerminateApplicationButtonClicked();
}
