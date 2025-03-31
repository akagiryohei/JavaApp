package DI.Login;

import View.*;

import java.util.concurrent.ExecutorService;

import Controller.*;
import Model.*;

import View.Login.*;
import Controller.Login.*;
import Model.Login.*;
import Model.Process.Login.LoginProcess;

// MainWindow画面の依存性注入クラス
public class LoginBaseDI
{
  // ロガーインスタンス
  private Logger Logger;

  // DBクライアントインスタンス
  private DBClient DBClient;

  // DB処理キューインスタンス
  private ExecutorService DBQueue;

  // 親要素のインスタンス
  private MainWindowView MainWindowViewInstance;

  // コンストラクタ
  public LoginBaseDI(Logger logger, DBClient dbClient, ExecutorService dbQueue, MainWindowView mainWindowView)
  {
    this.Logger = logger;
    this.DBClient = dbClient;
    this.DBQueue = dbQueue;
    this.MainWindowViewInstance = mainWindowView;

  }

  // 依存性注入したLoginコントローラオブジェクトを生成する
  public LoginController CreateLoginMVC(LoginBaseView loginBaseView)
  {
    ValidationUtil util = new ValidationUtil();
    LoginProcess loginProcess = new LoginProcess(this.DBClient, this.DBQueue);
    LoginModel loginModel = new LoginModel(this.Logger, loginProcess, util);
    LoginView loginView = new LoginView(loginBaseView, this.MainWindowViewInstance);
    LoginController loginController = new LoginController(loginView, loginModel);
    loginView.controller = loginController;

    return loginController;
  }

  // 依存性注入したSignupコントローラオブジェクトを生成する
  public SignupController CreateSignupMVC(LoginBaseView loginBaseView)
  {
    SignupModel signupModel = new SignupModel();
    SignupView signupView = new SignupView(loginBaseView);
    SignupController signupController = new SignupController(signupView, signupModel);

    return signupController;
  }

  // 依存性注入したLostPassUserコントローラオブジェクトを生成する
  public LostPassUserController CreateLostPassUserMVC(LoginBaseView loginBaseView)
  {
    LostPassUserModel lostPassUserModel = new LostPassUserModel();
    LostPassUserView lostPassUserView = new LostPassUserView(loginBaseView, this.MainWindowViewInstance);
    LostPassUserController lostPassUserController = new LostPassUserController(lostPassUserView, lostPassUserModel);

    return lostPassUserController;
  }
}
