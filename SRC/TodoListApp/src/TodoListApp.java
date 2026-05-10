import java.awt.Toolkit;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;

import DI.MainWindowDI;
import Entity.Enum.LogLevel;
import Entity.Enum.PropertyKey;
import Interface.Controller.IMainWindowController;
import Interface.DI.IMainWindowDI;
import Interface.Model.IAIAPIClient;
import Interface.Model.IDBClient;
import Interface.Model.IDuplicateLaunchChecker;
import Interface.Model.IGetHolidayInfoService;
import Interface.Model.IGetProperties;
import Interface.Model.ILogger;
import Model.DBClient;
import Model.DuplicateLaunchChecker;
import Model.ExceptionForwardingAWTEventQueue;
import Model.GetHolidayInfoService;
import Model.GetProperties;
import Model.LMStudioAPIClient;
import Model.Logger;
import Model.LoggingRepaintManager;
import View.SwingUIDispatcher;
import View.ViewProxyUtil;

public class TodoListApp
{
  public static void main(String[] args)
  {
    // Settings.propertiesはbin配下に置くとリビルドにて消えるためsrc直下に置く
    final String PROPERTIES_FILE_PATH = "src/Settings.properties";
    final String LOG_FOLDER_PATH = "bin/log/";
    final String SYUKUJITSU_CSV_FILE_PATH = "bin/syukujitsu.csv";
    final String LOCK_FILE_PATH = "bin/app.lock";

    IDuplicateLaunchChecker checker = new DuplicateLaunchChecker(LOCK_FILE_PATH);

    // 多重起動チェック
    if (!checker.TryLock())
    {
      // ロックファイルを解放して何もせずに終了する
      checker.Release();
      System.exit(1);
    }

    Runtime.getRuntime().addShutdownHook(new Thread(() ->
    {
      // アプリ終了時に多重起動チェックのロックファイルを解放する
      checker.Release();
    }));
    
    // 環境変数が書かれた設定ファイルを読み取る
    IGetProperties settings = new GetProperties(PROPERTIES_FILE_PATH);
    settings.Load();
    
    // ロガーのインスタンスを生成する
    ILogger logger = new Logger(LOG_FOLDER_PATH, Long.parseLong(settings.getProperty(PropertyKey.LogRotationSize)));
    logger.WriteLog(LogLevel.Info, "アプリ起動中");

    // グローバル例外ハンドラ参照用のメインウィンドウコントローラインスタンス
    AtomicReference<IMainWindowController> mainWindowControllerRef = new AtomicReference<>(); 

    // グローバル例外ハンドラの設定（
    Thread.setDefaultUncaughtExceptionHandler((thread, throwable) ->
    {
      // 取りこぼしたCatchはここでログ出力（ここに来るのは想定外）
      logger.WriteLog(thread, throwable);

      // 致命的なエラーが発生した旨をダイアログ表示する
      mainWindowControllerRef.get().NotifyFatalErrorOccurred();
    });

    // 祝日CSVを読み取る（非同期で読取して結果は使用画面で取得する）
    IGetHolidayInfoService getHolidayInfoService = new GetHolidayInfoService(SYUKUJITSU_CSV_FILE_PATH);
    getHolidayInfoService.FetchAsync();

    // ログ出力対応EventQueueをインストール
    Toolkit.getDefaultToolkit().getSystemEventQueue().push(new ExceptionForwardingAWTEventQueue());
    
    // RepaintManager を差し替え
    RepaintManager.setCurrentManager(new LoggingRepaintManager(logger));

    // DBクライアントのインスタンスを生成する
    IDBClient dbClient = new DBClient(settings.getProperty(PropertyKey.DBConnectionString),
                                      settings.getProperty(PropertyKey.DBDriverName),
                                      settings.getProperty(PropertyKey.DBName),
                                      settings.getProperty(PropertyKey.DBUserName),
                                      settings.getProperty(PropertyKey.DBPassword),
                                      logger);
    
    // AIクライアントのインスタンスを生成する
    IAIAPIClient LMStudioAPIClient = new LMStudioAPIClient(settings.getProperty(PropertyKey.BaseUrl),
                                                           settings.getProperty(PropertyKey.ChatPath),
                                                           settings.getProperty(PropertyKey.ModelName),
                                                           settings.getProperty(PropertyKey.ApiKey),
                                                           settings.getProperty(PropertyKey.SystemPrompt),
                                                           logger);

    // DB処理キューの生成（シングルスレッド逐次実行）
    ExecutorService dbQueue = Executors.newSingleThreadExecutor();

    SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        // MainWindow依存性注入
        IMainWindowDI di = new MainWindowDI(logger, dbClient, LMStudioAPIClient, getHolidayInfoService, dbQueue, new ViewProxyUtil(new SwingUIDispatcher()));

        // アプリのメインウィンドウを表示
        var mainWindowController = di.CreateMainWindowMVC(); // MainWindowControllerが返ってきてる
        mainWindowControllerRef.set(mainWindowController);

        // アプリのメインウィンドウを表示
        mainWindowController.Show();
      }
    }); 
  }
}
