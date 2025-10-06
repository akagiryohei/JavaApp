package Interface.DI.Login;

import Interface.Controller.Login.ILoginController;
import Interface.Controller.Login.ILostPassUserController;
import Interface.Controller.Login.ISignupController;

/* 
 * MainWindow画面の依存性注入クラス
 */
public interface ILoginBaseDI
{
  /* 
   * 依存性注入したLoginコントローラオブジェクトを生成する
   * @return 依存性注入したLoginコントローラオブジェクト
   */
  public ILoginController CreateLoginMVC();

  /* 
   * 依存性注入したSignupコントローラオブジェクトを生成する
   * @return 依存性注入したSignupコントローラオブジェクト
   */
  public ISignupController CreateSignupMVC();
  
  /* 
   * 依存性注入したLostPassUserコントローラオブジェクトを生成する
   * @return 依存性注入したLostPassUserコントローラオブジェクト
   */
  public ILostPassUserController CreateLostPassUserMVC();
}
