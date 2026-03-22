package Model;
import java.lang.String;
import java.util.Date;
import Entity.Enum.LogLevel;
import Interface.Model.ILogger;

import java.text.SimpleDateFormat;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Logger implements ILogger
{
  String Dir;
  private File unNumberedLogFile;
  private File checkFile;
  private File logFile;
  private long maxFileSize;
  private final SimpleDateFormat unNumberedLogFileFormater = new SimpleDateFormat("yyyyMMdd");
  private final SimpleDateFormat writeLogFormater = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
  private final Integer maxFileNumber = 99;

  // コンストラクタ
  public Logger(String dir, long logSize)
  {
    this.Dir = dir;
    this.unNumberedLogFile = new File(dir + File.separator + unNumberedLogFileFormater.format(new Date()) + "_logfile");
    this.checkFile = new File(this.unNumberedLogFile + "_1.log");
    this.maxFileSize = logSize;
  }
  
  // ログ出力
  public void WriteLog(LogLevel level, String content)
  {
    // ログに書き込む内容を生成する
    String nowDateTimeStr = writeLogFormater.format(new Date());
    String trace = GetStackTrace();
    String errorLevelStr = level.getValue();
    String contents = nowDateTimeStr + " " + errorLevelStr + " " + trace + " " + content + "\n";
    System.out.println(contents);

    // ファイルに書き込む
    this.WriteLogLine(contents);
  }
  
  /** ログ出力
   * @param thread 例外発生スレッドオブジェクト
   * @param throwable 例外オブジェクト
   */
  public void WriteLog(Thread thread, Throwable throwable)
  {
        // スタックトレースを文字列として取得
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        String stackTrace = sw.toString();
        var contents = "未処理の例外が発生しました（スレッド: " + thread.getName() + "）\n" + stackTrace;
        System.out.println(contents);

        // ファイルに書き込む
        this.WriteLogLine(contents);
  }

  // 書き込み権限を確認してファイルに書き込む
  public void WriteLogLine(String contents)
  {
    try {
        // logディレクトリが存在しない場合は作成する
        if (Files.exists(Paths.get(Dir)) == false)
        {
          Files.createDirectory(Paths.get(Dir));
        }

        // 初期のログファイルが存在しない場合は作成する
        if (checkFile.exists() == false)
        {
          checkFile.createNewFile();
        }
        
        // ファイルサイズとログ文字列を合わせたバイト数が、ログサイズ上限より大きい場合はファイルを精査する
        if(this.logFile == null || Files.size(this.logFile.toPath()) + this.GetWideStringLength(contents) > this.maxFileSize)
        {
          this.SetLogFilePath(this.maxFileSize);
        }

        // 書き込み権限の確認
        if (!this.logFile.canWrite())
        {
            // 書き込み可能に変更
            this.logFile.setWritable(true);
        }
        
        // ファイルに書き込む
        FileWriter filewriter;
        filewriter = new FileWriter(this.logFile, true);
        filewriter.write(contents);
        filewriter.close();

    } catch (IOException e) {
        e.printStackTrace();
    }
  }

  // 出力ログファイル名取得
  private void SetLogFilePath(long fileSize) throws IOException
  {
      int cnt = 1;
      var currentpath = Paths.get(this.unNumberedLogFile.toString() + "_" + cnt + ".log");

      // ファイルが存在するまで繰り返す（上限は99とする）
      while (Files.exists(currentpath) == true && cnt <= this.maxFileNumber)
      {
        try(BufferedReader reader = Files.newBufferedReader(currentpath, StandardCharsets.UTF_8))
        {
          // ファイルサイズを参照し、指定バイト数以上の場合はさらにインクリメントする
          if (Files.size(currentpath) < fileSize)
          {
              break;
          }
          else
          {
            cnt++;
            currentpath = Paths.get(this.unNumberedLogFile.toString() + "_" + cnt + ".log");
          }
          
        } catch (IOException e) {
          // ファイル読み込みに失敗した
        }
      }

      this.logFile = new File(currentpath.toString());
  }

  // エラークラスのスタックトレースから関数呼出元を抽出
  private String GetStackTrace()
  {
    StackTraceElement[] message = new Throwable().getStackTrace();
    Path currentRelativePath = Paths.get("");
    String relativePath = currentRelativePath.toAbsolutePath().toString();

    return relativePath + File.separator + message[2].getFileName() + " (" + message[2].getLineNumber() + "行目)";
  }

  // 文字列バイト数取得
  private int GetWideStringLength(String text)
  {
    return text.getBytes(StandardCharsets.UTF_8).length;
  }
}