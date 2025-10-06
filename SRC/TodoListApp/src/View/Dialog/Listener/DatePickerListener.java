package View.Dialog.Listener;

import java.time.LocalDate;
import java.util.EventListener;

/**
 * 日付選択ピッカーのイベントリスナ
 */
public interface DatePickerListener extends EventListener
{
  /**
   * 日付選択イベント
   * @param selectedDate 選択された日付
   */
  public void SelectedDate(LocalDate selectedDate);
}
