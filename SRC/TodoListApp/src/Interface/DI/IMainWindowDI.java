package Interface.DI;

import Entity.UserData;
import Interface.Controller.IMainWindowController;
import Interface.Controller.Login.ILoginBaseController;
import Interface.Controller.Todo.ITodoListBaseController;
import Interface.View.IMainWindowView;

// MainWindow画面の依存性注入クラス
public interface IMainWindowDI
{
  /*
   * 依存性注入したMainWindowコントローラオブジェクトを生成する
   * @return 依存性注入したMainWindowコントローラオブジェクト
   */
  public IMainWindowController CreateMainWindowMVC();

  /*
   * ログインベース作成
   * @return 依存性注入したLoginBaseコントローラオブジェクト
   */
  public ILoginBaseController CreateLoginBaseMVC(IMainWindowView mainWindowView);

  /*
   * Todoリストベース作成
   * @return 依存性注入したTodoListBaseコントローラオブジェクト
   */
  public ITodoListBaseController CreateTodoListBaseMVC(IMainWindowView mainWindowView, UserData userData);
}
