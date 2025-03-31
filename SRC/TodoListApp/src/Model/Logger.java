package Model;
import java.lang.String;
import java.util.Date;
import Entity.Enum.LogLevel;
import java.text.SimpleDateFormat;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Logger 
{
  private Date date = new Date();
  private File logFile;
  private long fileSize;

  // コンストラクタ
  public Logger(String dir, long logSize)
  {
    SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");
    this.logFile = new File(dir + "\\" + formater.format(date) + "_logfile");
    File newFile = new File(this.logFile + "_1.log");
    this.fileSize = logSize;

    // logディレクトリが存在しない場合は生成する
    if (newFile.exists() == false)
    {
        try
        {
            // ファイル新規作成
            File fileDir = new File(dir);
            fileDir.mkdir();
            newFile.createNewFile();
        }
        catch (Exception e)
        {
            // ログ出力自体の例外なので何もしない。
        }
    }
  }

  // ログ出力
  public void WriteLog(LogLevel level, String content)
  {
    // ログに書き込む内容を生成する
    SimpleDateFormat formater = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    String nowDateTimeStr = formater.format(date);
    String trace = GetStackTrace();
    String errorLevelStr = level.getValue();
    String contents = nowDateTimeStr + " " + errorLevelStr + " " + trace + " " + content + "\n";
    System.out.println(contents);

    try {
        // 書き込み対象のログファイルを精査する
        logFile = GetLogFileName(logFile, fileSize);

        // 書き込み権限の確認
        if (logFile.exists() == false || !logFile.canWrite()) {
            // ファイルがない場合は新規作成
            logFile.createNewFile();
            // 書き込み可能に変更
            logFile.setWritable(true);
        }
        
        // ファイルに書き込む
        FileWriter filewriter;
        filewriter = new FileWriter(logFile, true);
        filewriter.write(contents);
        filewriter.close();

    } catch (IOException e) {
        e.printStackTrace();
    }
  }

  // 出力ログファイル名取得
  public File GetLogFileName(File file, long fileSize) throws IOException
  {
      int cnt = 1;
      File loopFile = new File(file.toString() + "_" + cnt + ".log");

      // ファイルが存在するまで繰り返す（上限は99とする）
      while (loopFile.exists() == true && cnt <= 99)
      {
          // ファイルサイズを参照し、指定バイト数(512MB)以上の場合はさらにインクリメントする
          if (Files.size(loopFile.toPath()) < fileSize)
          {
              break;
          }
          else
          {
              loopFile = new File(file.toString() + "_" + cnt + ".log");
          }
          cnt++;
      }

      file = new File(loopFile.toString());
      return file;
  }

  // エラークラスのスタックトレースから関数呼出元を抽出
  private String GetStackTrace()
  {
    StackTraceElement[] message = new Throwable().getStackTrace();
    Path currentRelativePath = Paths.get("");
    String relativePath = currentRelativePath.toAbsolutePath().toString();

    return relativePath + "\\" + message[2].getFileName() + " (" + message[2].getLineNumber() + "行目)";
  } 
}