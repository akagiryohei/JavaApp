package Model;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import Interface.Model.IGetHolidayInfoService;

/**
 * 祝日情報取得サービスクラス
 */
public class GetHolidayInfoService implements IGetHolidayInfoService
{
  /** 祝日情報CSVの日付フォーマット（ゼロサプレス） */
  private final DateTimeFormatter Formatter = DateTimeFormatter.ofPattern("yyyy/M/d");

  /** 祝日情報CSVのカラムセパレータ */
  private final String Separator = ",";

  /** 祝日情報CSVのファイルパス */
  private String FilePath;

  /** 非同期処理オブジェクト */
  private CompletableFuture<LinkedHashMap<LocalDate, String>> LoadFuture;

  /** カラムインデックス列挙体 */
  private enum HolidayCsvColumn
  {
    /* 日付 */
    Date,

    /* 国民の祝日 */
    Name,
  }

  /**
   * コンストラクタ
   * @param 祝日情報CSVのファイルパス
   */
  public GetHolidayInfoService(String filePath)
  {
    this.FilePath = filePath;
  }

  /**
   * 祝日CSV読込（非同期）
   */
  public void FetchAsync()
  {
    // サブスレッドで非同期実行する
    this.LoadFuture = CompletableFuture.supplyAsync(() ->
    {
      LinkedHashMap<LocalDate, String> holidayMap = new LinkedHashMap<>();
      
      try (var lines = Files.lines(Paths.get(this.FilePath), Charset.forName("Shift_JIS")))
      {
        lines.skip(1)  // ヘッダ行を読み飛ばす
             .map(line -> line.split(this.Separator, -1)) // 全要素をカラム単位で分割する
             .filter(splitStr -> splitStr.length == HolidayCsvColumn.values().length)  // カラム数が合致する要素のみ抽出
             .forEach(splitStr -> holidayMap.put(
                 LocalDate.parse(splitStr[HolidayCsvColumn.Date.ordinal()], this.Formatter),  // 先頭カラムを日付とする
                 splitStr[HolidayCsvColumn.Name.ordinal()].trim()  // 後方カラムを祝日名とする
             ));
      }
      catch (Exception e)
      {
        // 処理失敗時は空の戻り値とする
        holidayMap.clear();
      }

      return holidayMap;
    });
  }

  /**
   * 読取結果取得（非同期）
   * @param 読取結果通知コールバック（処理完了済みなら即コールバック）
   */
  public void RequestHolidayInfoAsync(Consumer<LinkedHashMap<LocalDate, String>> callback)
  {
    if (this.LoadFuture != null)
    {
      this.LoadFuture.thenAccept(callback);
    }
    else
    {
      // 空要素をコールバック
      callback.accept(new LinkedHashMap<LocalDate, String>());
    }
  }

  /**
   * 読取結果取得（同期）
   * @return 読取結果（処理未完了の場合は空要素を戻り値とする）
   */
  public LinkedHashMap<LocalDate, String> RequestHolidayInfoSync()
  {
    return this.LoadFuture != null && this.LoadFuture.isDone() ? this.LoadFuture.join() : new LinkedHashMap<LocalDate, String>();
  }
}
