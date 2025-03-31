import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.SwingUtilities;
import DI.MainWindowDI;
import Entity.Enum.LogLevel;
import Entity.Enum.PropertyKey;
import Model.DBClient;
import Model.GetProperties;
import Model.Logger;

public class TodoListApp
{
  public static void main(String[] args)
  {
    SwingUtilities.invokeLater(new Runnable() {
      public void run()
      {
        final String PROPERTIES_FILE_PATH = "bin/Settings.properties";
        final String LOG_FOLDER_PATH = "bin/log/";

        // 環境変数が書かれた設定ファイルを読み取る
        GetProperties logRotationSize = new GetProperties(PROPERTIES_FILE_PATH);
        
        // ロガーのインスタンスを生成する
        Logger logger = new Logger(LOG_FOLDER_PATH, Long.parseLong(logRotationSize.getProperty(PropertyKey.LogRotationSize)));
        logger.WriteLog(LogLevel.Info, "メイン画面が起動しました");

        // DBクライアントのインスタンスを生成する TODO: 引数は設定ファイルから取得する
        DBClient dbClient = new DBClient("jdbc:mysql://localhost:3306/todojava_db?connectTimeout=10000&socketTimeout=10000", "com.mysql.cj.jdbc.Driver", "todojava_db", "root", "root");
        
        // DB処理キューの生成（シングルスレッド逐次実行）
        ExecutorService dbQueue = Executors.newSingleThreadExecutor();

        // MainWindow依存性注入
        MainWindowDI di = new MainWindowDI(logger, dbClient, dbQueue);

        // アプリのメインウィンドウを表示
        var mainWindowController = di.CreateMainWindowMVC(); // MainWindowControllerが返ってきてる

        // アプリのメインウィンドウを表示
        mainWindowController.Show();
      }
    });
  }
}
