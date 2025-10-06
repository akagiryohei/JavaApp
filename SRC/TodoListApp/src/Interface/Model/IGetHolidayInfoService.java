package Interface.Model;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.function.Consumer;

/*
 * GetHolidayInfoServiceのインタフェース
 */
public interface IGetHolidayInfoService
{
  /**
   * 祝日CSV読込（非同期）
   */
  public void FetchAsync();

  /**
   * 読取結果取得（非同期）
   * @param 読取結果通知コールバック（処理完了済みなら即コールバック）
   */
  public void RequestHolidayInfoAsync(Consumer<LinkedHashMap<LocalDate, String>> callback);

  /**
   * 読取結果取得（同期）
   * @return 読取結果
   */
  public LinkedHashMap<LocalDate, String> RequestHolidayInfoSync();
}
