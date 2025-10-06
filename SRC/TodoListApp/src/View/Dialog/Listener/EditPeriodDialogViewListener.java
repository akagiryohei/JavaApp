package View.Dialog.Listener;

import java.util.Date;
import java.util.EventListener;

/**
 * リマインダーダイアログのイベントリスナ
 */
public interface EditPeriodDialogViewListener extends EventListener
{
  /**
   * 編集ボタン押下時イベント
   */
  public void EditPeriodButtonClicked(Date startDate, Date endDate);

  /**
   * 期日セットボタン押下時イベント
   */
  public void SetPeriodButtonClicked(Date startDate, Date endDate);
}