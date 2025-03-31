package DI;

import View.*;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

import View.Dialog.DialogView;
import Controller.*;
import Model.*;

import View.Login.*;
import View.Todo.*;
import Controller.Login.*;
import Controller.Todo.*;
import Model.Login.*;
import Model.Todo.*;
import DI.Login.LoginBaseDI;
import DI.Todo.TodoListBaseDI;

// MainWindow画面の依存性注入クラス
public class MainWindowDI
{
  // ロガーインスタンス
  private Logger Logger;

  // DBクライアントインスタンス
  private DBClient DBClient;

  // DB処理キューインスタンス
  private ExecutorService DBQueue;

  // MainWindowViewのインスタンス
  private MainWindowView MainWindowViewInstance;

  // DialogViewのインスタンス
  private DialogView DialogViewInstance;

  // コンストラクタ
  public MainWindowDI(Logger logger, DBClient dbClient, ExecutorService dbQueue)
  {
    this.Logger = logger;
    this.DBClient = dbClient;
    this.DBQueue = dbQueue;
  }

  // 依存性注入したMainWindowコントローラオブジェクトを生成する
  public MainWindowController CreateMainWindowMVC()
  {
    MainWindowModel mainWindowModel = new MainWindowModel();
    this.MainWindowViewInstance = new MainWindowView(this);
    MainWindowController mainWindowController = new MainWindowController(this.MainWindowViewInstance, mainWindowModel);

    return mainWindowController;
  }

  /*
   * ログインベース作成
   */
  public LoginBaseController CreateLoginBaseMVC(MainWindowView mainWindowView)
  {

    // 子画面のDIを作成している
    var loginBaseDi = new LoginBaseDI(this.Logger, this.DBClient, this.DBQueue, mainWindowView);

    // LoginBase とはログイン前画面の操作用の物
    LoginBaseModel loginBaseModel = new LoginBaseModel();
    LoginBaseView loginBaseView = new LoginBaseView(loginBaseDi, this.MainWindowViewInstance);
    LoginBaseController loginBaseController = new LoginBaseController(loginBaseView, loginBaseModel);

    return loginBaseController;
  }

  /*
   * Todoリストベース作成
   */
  public TodoListBaseController CreateTodoListBaseMVC(MainWindowView mainWindowView, String userID)
  {
    // 子要素のDIを作成
    var todoListBaseDi = new TodoListBaseDI(this.Logger, this.DBClient, this.MainWindowViewInstance, this.DBQueue, userID);

    
    TodoListBaseModel todoListBaseModel = new TodoListBaseModel();
    TodoListBaseView todoListBaseView = new TodoListBaseView(todoListBaseDi, this.MainWindowViewInstance);
    TodoListBaseController todoListBaseController = new TodoListBaseController(todoListBaseView, todoListBaseModel);

    return todoListBaseController;
  }
}
