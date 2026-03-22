package Interface.Model;

import java.util.List;

import Entity.DB.User;
import Entity.DB.DBClientDtoPair;
import Entity.DB.DBResultType;
import Entity.DB.InputTask;
import Entity.DB.ListColumn;
import Entity.DB.TaskColumn;

/**
  * DB接続クライアントインタフェース
  */
public interface IDBClient
{
  /**
   * ログイン認証できるか
   * 依存性注入
   * @param email ログイン対象アカウントのメールアドレス
   * @param password ログイン対象アカウントのパスワード（ハッシュ後文字列）
   * @return 処理結果、ログイン対象ユーザ情報
  */
  public DBClientDtoPair<DBResultType, User> CanLogin(String email, String password);

  /**
   * リスト取得メソッド
   * @param userID ログイン中ユーザーID
   * @return ログイン中ユーザー作成のListを取得、リスト取得成否（成功：リスト配列、失敗（存在しない） :-1）
   */
  public DBClientDtoPair<DBResultType, List<ListColumn>> GetList(int userID);
  
  /**
   * リスト登録メソッド
   */
  public DBClientDtoPair<DBResultType, Integer> InsertList(String listText, int userID);

  /**
  * リスト編集メソッド
  */
  public DBResultType UpdateList(String listName, int userID);

  /**
   * リスト削除メソッド
   */
  public DBResultType DeleteList(int listID, int userID);

  /**
   * タスク取得メソッド
   */
  public DBClientDtoPair<DBResultType, List<TaskColumn>> GetTask(int userID, int listID);

  /**
   * 未完了タスク取得メソッド
   */
  public DBClientDtoPair<DBResultType, List<TaskColumn>> GetUnFinishedTask(int userID);

  /**
   * タスク登録メソッド
   */
  public DBResultType CreateTask(InputTask inputTask);

  /**
   * 複数タスク登録メソッド
   * @param inputTasks 登録タスクリスト
   * @return 処理結果
   */
  public DBResultType CreateTasks(List<InputTask> inputTasks);

  /**
   * タスク編集メソッド（タスク名）
   * @param taskID
   * @param userID
   * @return
   */
  public DBResultType UpdateTask(int taskID, String taskText, int userID);


  /**
   * タスク削除メソッド
   */
  public DBResultType DeleteTask(int taskID, int userID);

  /**
   * ユーザー作成メソッド
   */
  public DBResultType InsertUser(User user);

  /**
   * Supportボタン押下時のヒント文取得
   * GetUserIdメソッドとセットで実装
   */
  public DBClientDtoPair<DBResultType, String> GetSecret(int secretID);

  /**
   * Supportボタン押下時：ユーザー認証
   */
  public DBClientDtoPair<DBResultType, Integer> GetSecretId(String email);

  /**
   * ヒントテキスト一覧取得
   */
  public DBClientDtoPair<DBResultType, List<String>> getSecretTipsList();

  /**
   * 忘却者ログイン認証
   * 依存性注入
   * @param email ログイン対象アカウントのメールアドレス
   * @param secretPassword ログイン対象アカウントのパスワード（ハッシュ後文字列）
   * @return 処理結果、ログイン対象ユーザ情報
  */
  public DBClientDtoPair<DBResultType, User> CanLostPassUserLogin(String email, String secretPassword);

  /**
   * タスク編集メソッド（タスク進捗度＋完了/未完了）
   * @param taskID 選択中タスクID
   * @param isChecked 選択中タスクの状態
   * @param userID ログインユーザーID
   * @return
   */
  public DBResultType UpdateTask(int taskID, int userID, int taskPercent);

  /**
   * 期日編集メソッド
   */
  public DBResultType EditPeriodDate(InputTask inputTask);

  /**
   * リスト＋タスクの一括取得
   * @param userID ログイン中ユーザーID
   * @return
   */
  public DBClientDtoPair<DBResultType, List<ListColumn>> GetListTask(int userID);
}

