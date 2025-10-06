package Interface.Model;

import Entity.Enum.LogLevel;

/*
 * Loggerのインタフェース
 */
public interface ILogger
{
  /**
   * ログ出力
   * @param level ログレベル
   * @param content ログ出力する文字列
   */
  public void WriteLog(LogLevel level, String content);
}
