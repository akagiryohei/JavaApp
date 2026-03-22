package DI;

import View.*;

import java.util.concurrent.ExecutorService;

import javax.swing.JFrame;

import View.Dialog.CommonDialogView;
import View.Dialog.DialogView;
import View.Dialog.FatalErrorDialogView;
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
import Entity.UserData;
import Interface.Controller.IMainWindowController;
import Interface.Controller.Login.ILoginBaseController;
import Interface.Controller.Todo.ITodoListBaseController;
import Interface.DI.IMainWindowDI;
import Interface.Model.IDBClient;
import Interface.Model.IGetHolidayInfoService;
import Interface.Model.ILMStudioAPIClient;
import Interface.Model.ILogger;
import Interface.Model.IMainWindowModel;
import Interface.Model.Login.ILoginBaseModel;
import Interface.View.IMainWindowView;
import Interface.View.IViewProxyUtil;
import Interface.View.Login.ILoginBaseView;
import Interface.View.Todo.ITodoListBaseView;

// MainWindow画面の依存性注入クラス
public class MainWindowDI implements IMainWindowDI
{
  // ロガーインスタンス
  private ILogger Logger;

  // DBクライアントインスタンス
  private IDBClient DBClient;

  // AIクライアントインスタンス
  private ILMStudioAPIClient LMStudioAPIClient;

  // 祝日情報取得サービスインスタンス
  private IGetHolidayInfoService GetHolidayInfoService;

  // DB処理キューインスタンス
  private ExecutorService DBQueue;

  // 画面インタフェースプロキシインスタンス
  private IViewProxyUtil ViewProxyUtil;

  // MainWindowViewのインスタンス
  private IMainWindowView MainWindowViewInstance;

  // DialogViewのインスタンス
  private DialogView DialogViewInstance;

  // 共通ダイアログのインスタンス
  private CommonDialogView CommonDialogViewInstance;

  // コンストラクタ
  public MainWindowDI(ILogger logger, IDBClient dbClient, ILMStudioAPIClient lMStudioAPIClient, IGetHolidayInfoService getHolidayInfoService, ExecutorService dbQueue, IViewProxyUtil viewProxyUtil)
  {
    this.Logger = logger;
    this.DBClient = dbClient;
    this.LMStudioAPIClient = lMStudioAPIClient;
    this.DBQueue = dbQueue;
    this.GetHolidayInfoService = getHolidayInfoService;
    this.ViewProxyUtil = viewProxyUtil;
  }

  // 依存性注入したMainWindowコントローラオブジェクトを生成する
  public IMainWindowController CreateMainWindowMVC()
  {
    IMainWindowModel mainWindowModel = new MainWindowModel();
    FatalErrorDialogView fatalErrorDialogView = new FatalErrorDialogView();
    this.MainWindowViewInstance = new MainWindowView(this, fatalErrorDialogView);
    IMainWindowController mainWindowController = new MainWindowController(this.MainWindowViewInstance, mainWindowModel, this.ViewProxyUtil);
    this.MainWindowViewInstance.SetController(mainWindowController);
    
    // 共通ダイアログのインスタンスを生成
    this.CommonDialogViewInstance = new CommonDialogView((JFrame)this.MainWindowViewInstance);

    return mainWindowController;
  }

  /*
   * ログインベース作成
   */
  public ILoginBaseController CreateLoginBaseMVC(IMainWindowView mainWindowView)
  {

    // 子画面のDIを作成している
    var loginBaseDi = new LoginBaseDI(this.Logger, this.DBClient, this.DBQueue, mainWindowView, this.CommonDialogViewInstance, this.ViewProxyUtil);

    // LoginBase とはログイン前画面の操作用の物
    ILoginBaseModel loginBaseModel = new LoginBaseModel();
    ILoginBaseView loginBaseView = new LoginBaseView(loginBaseDi);
    ILoginBaseController loginBaseController = new LoginBaseController(loginBaseView, loginBaseModel, this.ViewProxyUtil);

    return loginBaseController;
  }

  /*
   * Todoリストベース作成
   */
  public ITodoListBaseController CreateTodoListBaseMVC(IMainWindowView mainWindowView, UserData userData)
  {
    // 子要素のDIを作成
    var todoListBaseDi = new TodoListBaseDI(this.Logger, this.DBClient, this.LMStudioAPIClient,this.GetHolidayInfoService, this.MainWindowViewInstance, this.DBQueue, userData, this.CommonDialogViewInstance, this.ViewProxyUtil);

    ITodoListBaseView todoListBaseView = new TodoListBaseView(todoListBaseDi, this.MainWindowViewInstance);
    ITodoListBaseController todoListBaseController = new TodoListBaseController(todoListBaseView, this.ViewProxyUtil);

    return todoListBaseController;
  }
}
