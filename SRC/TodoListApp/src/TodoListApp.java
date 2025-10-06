import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.SwingUtilities;
import DI.MainWindowDI;
import Entity.Enum.LogLevel;
import Entity.Enum.PropertyKey;
import Interface.DI.IMainWindowDI;
import Interface.Model.IDBClient;
import Interface.Model.IGetHolidayInfoService;
import Interface.Model.IGetProperties;
import Interface.Model.ILogger;
import Model.DBClient;
import Model.GetHolidayInfoService;
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
        final String SYUKUJITSU_CSV_FILE_PATH = "bin/syukujitsu.csv";

        // 環境変数が書かれた設定ファイルを読み取る
        IGetProperties settings = new GetProperties(PROPERTIES_FILE_PATH);

        // 祝日CSVを読み取る（非同期で読取して結果は使用画面で取得する）
        IGetHolidayInfoService getHolidayInfoService = new GetHolidayInfoService(SYUKUJITSU_CSV_FILE_PATH);
        getHolidayInfoService.FetchAsync();
        
        // ロガーのインスタンスを生成する
        ILogger logger = new Logger(LOG_FOLDER_PATH, Long.parseLong(settings.getProperty(PropertyKey.LogRotationSize)));
        logger.WriteLog(LogLevel.Info, "メイン画面が起動しました");

        // DBクライアントのインスタンスを生成する
        IDBClient dbClient = new DBClient(settings.getProperty(PropertyKey.DBConnectionString),
                                          settings.getProperty(PropertyKey.DBDriverName),
                                          settings.getProperty(PropertyKey.DBName),
                                          settings.getProperty(PropertyKey.DBUserName),
                                          settings.getProperty(PropertyKey.DBPassword));
        
        // DB処理キューの生成（シングルスレッド逐次実行）
        ExecutorService dbQueue = Executors.newSingleThreadExecutor();

        // MainWindow依存性注入
        IMainWindowDI di = new MainWindowDI(logger, dbClient, getHolidayInfoService, dbQueue);

        // アプリのメインウィンドウを表示
        var mainWindowController = di.CreateMainWindowMVC(); // MainWindowControllerが返ってきてる

        // アプリのメインウィンドウを表示
        mainWindowController.Show();
      }
    });
  }
}
