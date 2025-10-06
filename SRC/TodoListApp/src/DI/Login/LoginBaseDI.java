package DI.Login;

import java.util.concurrent.ExecutorService;

import javax.swing.JFrame;

import View.*;
import View.Dialog.CommonDialogView;
import View.Dialog.ReminderDialogView;
import Model.*;
import View.Login.*;
import Controller.Login.*;
import Interface.Controller.Login.ILoginController;
import Interface.Controller.Login.ILostPassUserController;
import Interface.Controller.Login.ISignupController;
import Interface.DI.Login.ILoginBaseDI;
import Interface.Model.IDBClient;
import Interface.Model.ILogger;
import Interface.Model.IValidationUtil;
import Interface.Model.Process.Login.ILoginProcess;
import Interface.Model.Login.ILoginModel;
import Interface.Model.Login.ILostPassUserModel;
import Interface.Model.Login.ISignupModel;
import Interface.View.IMainWindowView;
import Interface.View.Login.ILoginView;
import Interface.View.Login.ILostPassUserView;
import Interface.View.Login.ISignupView;
import Model.Login.*;
import Model.Process.Login.LoginProcess;

// MainWindow画面の依存性注入クラス
public class LoginBaseDI implements ILoginBaseDI
{
  // ロガーインスタンス
  private ILogger Logger;

  // DBクライアントインスタンス
  private IDBClient DBClient;

  // DB処理キューインスタンス
  private ExecutorService DBQueue;

  // 親要素のインスタンス
  private IMainWindowView MainWindowViewInstance;

  // 共通ダイアログインスタンス
  private CommonDialogView CommonDialogViewInstance;

  // コンストラクタ
  public LoginBaseDI(ILogger logger, IDBClient dbClient, ExecutorService dbQueue, IMainWindowView mainWindowView, CommonDialogView commonDialogView)
  {
    this.Logger = logger;
    this.DBClient = dbClient;
    this.DBQueue = dbQueue;
    this.MainWindowViewInstance = mainWindowView;
    this.CommonDialogViewInstance = commonDialogView;
  }

  // 依存性注入したLoginコントローラオブジェクトを生成する
  public ILoginController CreateLoginMVC()
  {
    IValidationUtil util = new ValidationUtil();
    ILoginProcess loginProcess = new LoginProcess(this.DBClient, this.DBQueue);
    ILoginModel loginModel = new LoginModel(this.Logger, loginProcess, util);
    ReminderDialogView reminderDialogView = new ReminderDialogView((JFrame)this.MainWindowViewInstance);
    ILoginView loginView = new LoginView(this.CommonDialogViewInstance, reminderDialogView);
    loginView.SetLogger(this.Logger);
    ILoginController loginController = new LoginController(loginView, loginModel);
    loginController.SetLogger(this.Logger);
    loginView.SetController(loginController);

    return loginController;
  }

  // 依存性注入したSignupコントローラオブジェクトを生成する
  public ISignupController CreateSignupMVC()
  {
    IValidationUtil util = new ValidationUtil();
    ILoginProcess loginProcess = new LoginProcess(this.DBClient, this.DBQueue);
    ISignupModel signupModel = new SignupModel(this.Logger, loginProcess, util);
    ISignupView signupView = new SignupView(this.MainWindowViewInstance, this.CommonDialogViewInstance);
    signupView.SetLogger(this.Logger);
    ISignupController signupController = new SignupController(signupView, signupModel);
    signupController.SetLogger(this.Logger);
    signupView.SetController(signupController);
    
    return signupController;
  }

  // 依存性注入したLostPassUserコントローラオブジェクトを生成する
  public ILostPassUserController CreateLostPassUserMVC()
  {
    IValidationUtil util = new ValidationUtil();
    ILoginProcess loginProcess = new LoginProcess(this.DBClient, this.DBQueue);
    ILostPassUserModel lostPassUserModel = new LostPassUserModel(this.Logger, loginProcess, util);
    ILostPassUserView lostPassUserView = new LostPassUserView(this.MainWindowViewInstance, this.CommonDialogViewInstance);
    lostPassUserView.SetLogger(this.Logger);
    ILostPassUserController lostPassUserController = new LostPassUserController(lostPassUserView, lostPassUserModel);
    lostPassUserController.SetLogger(this.Logger);
    lostPassUserView.SetController(lostPassUserController);

    return lostPassUserController;
  }
}
