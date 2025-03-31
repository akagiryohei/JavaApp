package Model;

import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.cj.exceptions.CJCommunicationsException;

import Entity.DB.User;
import Entity.DB.InputTask;
import Entity.DB.ListColumn;
import Entity.DB.TaskColumn;

/**
  * DB接続クライアントクラス
  */
public class DBClient
{
  /** DB接続文字列 */
  private String ConnectionString;

  /** DBドライバ文字列 */
  private String DBDriverString;

  /** DB名 */
  private String DBName;

  /** DBユーザ名 */
  private String DBUserName;

  /** DBパスワード */
  private String DBPassword;

  /** 処理結果 */
  public enum DBResultType
  {
    /** 正常 */
    Success,

    /** 失敗 */
    Failure,

    /** タイムアウト */
    Timeout,
  }

  /**
   * コンストラクタ
   * 依存性注入
   * @param connectionString DB接続文字列
   * @param dbDriver DBドライバ文字列
   * @param dbName DB名
   * @param dbUserName DBユーザ名
   * @param dbPassword DBパスワード
  */
  public DBClient(String connectionString, String dbDriver, String dbName, String dbUserName, String dbPassword)
  {
    this.ConnectionString = connectionString;
    this.DBDriverString = dbDriver;
    this.DBName = dbName;
    this.DBUserName = dbUserName;
    this.DBPassword = dbPassword;
  }

  /**
   * ログイン認証できるか
   * 依存性注入
   * @param email ログイン対象アカウントのメールアドレス
   * @param password ログイン対象アカウントのパスワード（ハッシュ後文字列）
   * @return 処理結果、ログイン成否（成功：ユーザID、失敗（存在しない）：-1）
  */
  public DBClientDtoPair<DBResultType, String> CanLogin(String email, String password)
  {
    DBResultType result = DBResultType.Failure;
    String userId = "";

    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    try
    {
      Class.forName(this.DBDriverString);
      con = this.GetConnection();

      StringBuilder builder = new StringBuilder();
      builder.append("SELECT ");
      builder.append("* ");
      builder.append("FROM ");
      builder.append(this.DBName);
      builder.append(".users ");
      builder.append("WHERE ");
      builder.append("email=");
      builder.append("? ");
      builder.append("AND ");
      builder.append("password=");
      builder.append("?;");
  
      // SQL文の実行
      pstmt = con.prepareStatement(builder.toString());
      pstmt.setString(1, email);//akagiプレースホルダーのパラメータのキーを選択してる(1とかで)
      pstmt.setString(2, password);// akagi これも同じ
      rs = pstmt.executeQuery();//akagi 実行結果をここに入れてる
  
      // 検索結果を表示　//akagi ログインに成功したらuserIDを返す　userIDを使いながら次の処理を走らせる必要がある
      while (rs.next())//akagi行がある場合はtrue trueの場合は中に入って対応する（findとかでもよさそう？）最初の１行を取得するはず
      {
        result = DBResultType.Success;//akagi enumの成功を返す
        userId = rs.getString("Id");//akagi IDに対応する値を取得してStringで返す
        break;
      }
  
      // 後処理（リソースのクローズ）
      rs.close();//akagi リソースの解放を意味する：DB接続等を解除するという意味でもありそう。
      pstmt.close();// 
      con.close();
    }
    catch (ClassNotFoundException | SQLException e)// akagi エラーがあるときはキャッチする
    {
      result = this.GetErrorResult(e.getCause());
    }

    return new DBClientDtoPair<DBResultType,String>(result, userId);
  }

  /**
   * リスト取得メソッド
   * @param userID ログイン中ユーザーID
   * @return ログイン中ユーザー作成のListを取得、リスト取得成否（成功：リスト配列、失敗（存在しない） :-1）
   */
  public DBClientDtoPair<DBResultType, List<ListColumn>> GetList(int userID)
  {
    DBResultType result = DBResultType.Failure;

    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    // java.sql.Array list = null;
    List<ListColumn> list = new ArrayList<ListColumn>();

    try
    {
      Class.forName(this.DBDriverString);
      con = this.GetConnection();

      StringBuilder builder = new StringBuilder();
      builder.append("SELECT ");
      builder.append("* ");
      builder.append("FROM ");
      builder.append(this.DBName);
      builder.append(".list ");
      builder.append("WHERE ");
      builder.append("user_id=");
      builder.append("?;");

      // SQL文の実行
      pstmt = con.prepareStatement(builder.toString());
      pstmt.setInt(1, userID);//akagiプレースホルダーのパラメータのキーを選択してる(1とかで)
      rs = pstmt.executeQuery();//akagi 実行結果をここに入れてる

      while(rs.next())
      {
        result = DBResultType.Success;
        var listColumn = new  ListColumn();
        listColumn.id = rs.getInt("id");
        listColumn.list_name = rs.getString("list_name");
        list.add(listColumn);
      }

      // 後処理（リソースのクローズ）
      rs.close();//akagi リソースの解放を意味する：DB接続等を解除するという意味でもありそう。
      pstmt.close();// 
      con.close();
    }
    catch (ClassNotFoundException | SQLException e)
    {
      result = this.GetErrorResult(e.getCause());
    }

    return new DBClientDtoPair<DBClient.DBResultType, List<ListColumn>>(result, list);
  }

  /**
   * リスト登録メソッド
   */
  public DBResultType InsertList(String listText, int userID)
  {
    DBResultType result = DBResultType.Failure;

    Connection con = null;
    PreparedStatement pstmt = null;
    int rs = 0;
    //INSERT INTO todojava_db.list(`list_name`, `user_id`) VALUES ('涙の訳','1');
    try
    {
      Class.forName(this.DBDriverString);

      con = this.GetConnection();

      StringBuilder builder = new StringBuilder();
      builder.append("INSERT ");
      builder.append("INTO ");
      builder.append(this.DBName);
      builder.append(".list ");
      builder.append("( ");
      builder.append("list_name, ");
      builder.append("user_id) ");
      builder.append("VALUES ");
      builder.append("(");
      builder.append("?, ");
      builder.append("?");
      builder.append(");");

      // SQL文の実行
      pstmt = con.prepareStatement(builder.toString());
      pstmt.setString(1, listText);//akagiプレースホルダーのパラメータのキーを選択してる(1とかで)
      pstmt.setInt(2, userID);//akagiプレースホルダーのパラメータのキーを選択してる(1とかで)
      rs = pstmt.executeUpdate();//akagi 実行結果をここに入れてる TODO:挙動要確認

      result = DBResultType.Success;

      // 後処理（リソースのクローズ）
      pstmt.close();// 
      con.close();
    }
    catch (ClassNotFoundException | SQLException e)
    {
      result = this.GetErrorResult(e.getCause());
    }

    return result;
  }

  /**
  * リスト編集メソッド
  */
  public DBClientDtoPair<DBResultType, Integer> UpdateList(String listName, int userID)
  {
    //UPDATE `list` SET `list_name`='転職', WHERE id = 1;
    DBResultType result = DBResultType.Failure;

    Connection con = null;
    PreparedStatement pstmt = null;
    int rs = 0;
    int updateFlg = 0;

    try
    {
      Class.forName(this.DBDriverString);

      con = this.GetConnection();

      StringBuilder builder = new StringBuilder();
      builder.append("UPDATE ");
      builder.append(this.DBName);
      builder.append(".list ");
      builder.append("SET ");
      builder.append("list_name= ");
      builder.append("? ");
      builder.append("WHERE ");
      builder.append("id= ");
      builder.append("?;");

      // SQL文の実行
      pstmt = con.prepareStatement(builder.toString());
      pstmt.setString(1, listName);//akagiプレースホルダーのパラメータのキーを選択してる(1とかで)
      pstmt.setInt(2, userID);//akagiプレースホルダーのパラメータのキーを選択してる(1とかで)
      rs = pstmt.executeUpdate();//akagi 実行結果をここに入れてる

      result = DBResultType.Success;
      updateFlg = rs;

      // 後処理（リソースのクローズ）
      pstmt.close();// 
      con.close();
    }
    catch (ClassNotFoundException | SQLException e)
    {
      result = this.GetErrorResult(e.getCause());
    }

    return new DBClientDtoPair<DBClient.DBResultType, Integer>(result, updateFlg);
  }
  
  /**
   * リスト削除メソッド
   */
  
  public DBResultType DeleteList(int userID, int listID)
  {
    DBResultType result = DBResultType.Failure;
    //DELETE FROM todojava_db.list WHERE user_id = 1 AND id = 1;
    Connection con = null;
    PreparedStatement pstmt = null;
    int rs = 0;

    try
    {
      Class.forName(this.DBDriverString);
      con = this.GetConnection();

      StringBuilder builder = new StringBuilder();
      builder.append("DELETE ");
      builder.append("FROM ");
      builder.append(this.DBName);
      builder.append(".list ");
      builder.append("WHERE ");
      builder.append("user_id=");
      builder.append("? ");
      builder.append("AND ");
      builder.append("id= ");
      builder.append("?;");

      // SQL文の実行
      pstmt = con.prepareStatement(builder.toString());
      pstmt.setInt(1, userID);//akagiプレースホルダーのパラメータのキーを選択してる(1とかで)
      pstmt.setInt(2, listID);//akagiプレースホルダーのパラメータのキーを選択してる(1とかで)
      rs = pstmt.executeUpdate();//akagi 実行結果をここに入れてる
      
      result = DBResultType.Success;

      // 後処理（リソースのクローズ）
      pstmt.close();// 
      con.close();
    }
    catch (ClassNotFoundException | SQLException e)
    {
      result = this.GetErrorResult(e.getCause());
    }

    return result;
  } 

  /**
   * タスク取得メソッド
   */
  public DBClientDtoPair<DBResultType, List<TaskColumn>> GetTask(int userID, int listID)
  {
    // SELECT * FROM todojava_db.task WHERE user_id = 1 AND list_id = 1;
    DBResultType result = DBResultType.Failure;

    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    List<TaskColumn> taskList = new ArrayList<TaskColumn>();

    try
    {
      Class.forName(this.DBDriverString);

      con = this.GetConnection();

      StringBuilder builder = new StringBuilder();
      builder.append("SELECT ");
      builder.append("* ");
      builder.append("FROM ");
      builder.append(this.DBName);
      builder.append(".task ");
      builder.append("WHERE ");
      builder.append("user_id=");
      builder.append("? ");
      builder.append("AND ");
      builder.append("list_id ");
      builder.append("?;");
      
      // SQL文の実行
      pstmt = con.prepareStatement(builder.toString());
      pstmt.setInt(1, userID);//akagiプレースホルダーのパラメータのキーを選択してる(1とかで)
      pstmt.setInt(2, listID);//akagiプレースホルダーのパラメータのキーを選択してる(1とかで)
      rs = pstmt.executeQuery();//akagi 実行結果をここに入れてる

      while(rs.next())
      {
        result = DBResultType.Success;
        var taskColumn = new TaskColumn();
        taskColumn.id = rs.getInt("id");
        taskColumn.task_text = rs.getString("task_text");
        taskColumn.start_date = rs.getString("start_date");
        taskColumn.end_date = rs.getString("end_date");
        taskColumn.task_status = rs.getInt("task_status");
        taskList.add(taskColumn);
      }

      // 後処理（リソースのクローズ）
      rs.close();//akagi リソースの解放を意味する：DB接続等を解除するという意味でもありそう。
      pstmt.close();// 
      con.close();
    }
    catch (ClassNotFoundException | SQLException e)
    {
      result = this.GetErrorResult(e.getCause());
    }
    return new DBClientDtoPair<DBClient.DBResultType, List<TaskColumn>>(result, taskList);
  }

  /**
   * タスク登録メソッド
   */
  public DBResultType CreateTask(InputTask inputTask)
  {
    // INSERT INTO todojava_db.task(`task_text`, `start_date`, `end_date`, `user_id`, `list_id`) VALUES ('新宮に行く','2025-03-24 12:12:12','2025-03-30 00:00:00','1','1');
    DBResultType result = DBResultType.Failure;

    Connection con = null;
    PreparedStatement pstmt = null;
    int rs = 0;

    try
    {
      Class.forName(this.DBDriverString);

      con = this.GetConnection();

      StringBuilder builder = new StringBuilder();
      builder.append("INSERT ");
      builder.append("INTO ");
      builder.append(this.DBName);
      builder.append(".task ");
      builder.append("( ");
      builder.append("task_text, ");
      builder.append("start_date, ");
      builder.append("end_date, ");
      builder.append("user_id, ");
      builder.append("list_id) ");
      builder.append("VALUES ");
      builder.append("(");
      builder.append("?, ");
      builder.append("?, ");
      builder.append("?, ");
      builder.append("?, ");
      builder.append("?");
      builder.append(");");
      // SQL文の実行
      pstmt = con.prepareStatement(builder.toString());
      pstmt.setString(1, inputTask.TaskText);//akagiプレースホルダーのパラメータのキーを選択してる(1とかで)
      pstmt.setString(2, inputTask.StartDate);//akagiプレースホルダーのパラメータのキーを選択してる(1とかで)
      pstmt.setString(3, inputTask.EndDate);//akagiプレースホルダーのパラメータのキーを選択してる(1とかで)
      pstmt.setInt(4, inputTask.UserId);//akagiプレースホルダーのパラメータのキーを選択してる(1とかで)
      pstmt.setInt(5, inputTask.ListId);//akagiプレースホルダーのパラメータのキーを選択してる(1とかで)
      rs = pstmt.executeUpdate();//akagi 実行結果をここに入れてる

      result = DBResultType.Success;

      // 後処理（リソースのクローズ）
      pstmt.close();// 
      con.close();
    }
    catch (ClassNotFoundException | SQLException e)
    {
      result = this.GetErrorResult(e.getCause());
    }
    return result;
  }

  /**
  * タスク編集メソッド
  */
  public DBResultType UpdateTask(InputTask inputTask)
  {
    //UPDATE todojava_db.task SET `task_text`='長髪',`start_date`=null,`end_date`=null,`list_id`='4' WHERE id = 3 AND user_id = 1;
    DBResultType result = DBResultType.Failure;

    Connection con = null;
    PreparedStatement pstmt = null;
    int rs = 0;

    try
    {
      Class.forName(this.DBDriverString);
      con = this.GetConnection();

      StringBuilder builder = new StringBuilder();
      builder.append("UPDATE ");
      builder.append(this.DBName);
      builder.append(".task ");
      builder.append("SET ");
      builder.append("task_text= ");
      builder.append("?, ");
      builder.append("start_date= ");
      builder.append("?, ");
      builder.append("end_date= ");
      builder.append("?, ");
      builder.append("list_id= ");
      builder.append("? ");
      builder.append("WHERE ");
      builder.append("id= ");
      builder.append("? ");
      builder.append("AND ");
      builder.append("user_id= ");
      builder.append("?;");
      // SQL文の実行
      pstmt = con.prepareStatement(builder.toString());
      pstmt.setString(1, inputTask.TaskText);//akagiプレースホルダーのパラメータのキーを選択してる(1とかで)
      pstmt.setString(2, inputTask.StartDate);//akagiプレースホルダーのパラメータのキーを選択してる(1とかで)
      pstmt.setString(3, inputTask.EndDate);//akagiプレースホルダーのパラメータのキーを選択してる(1とかで)
      pstmt.setInt(4, inputTask.ListId);//akagiプレースホルダーのパラメータのキーを選択してる(1とかで)
      pstmt.setInt(5, inputTask.TaskId);//akagiプレースホルダーのパラメータのキーを選択してる(1とかで)
      pstmt.setInt(6, inputTask.UserId);//akagiプレースホルダーのパラメータのキーを選択してる(1とかで)
      rs = pstmt.executeUpdate();//akagi 実行結果をここに入れてる
      
      result = DBResultType.Success;

      // 後処理（リソースのクローズ）
      pstmt.close();// 
      con.close();
    }
    catch (ClassNotFoundException | SQLException e)
    {
      result = this.GetErrorResult(e.getCause());
    }

    return result;
  }

  /**
   * タスク削除メソッド
   */
  public DBResultType DeleteTask(InputTask inputTask)
  {
    //DELETE FROM todojava_db.task WHERE id = 8 AND user_id = 1;
    DBResultType result = DBResultType.Failure;
    Connection con = null;
    PreparedStatement pstmt = null;
    int rs = 0;
    
    try
    {
      Class.forName(this.DBDriverString);
      con = this.GetConnection();

      StringBuilder builder = new StringBuilder();
      builder.append("DELETE ");
      builder.append("FROM ");
      builder.append(this.DBName);
      builder.append(".task ");
      builder.append("WHERE ");
      builder.append("user_id=");
      builder.append("? ");
      builder.append("AND ");
      builder.append("id= ");
      builder.append("?;");

      // SQL文の実行
      pstmt = con.prepareStatement(builder.toString());
      pstmt.setInt(1, inputTask.UserId);//akagiプレースホルダーのパラメータのキーを選択してる(1とかで)
      pstmt.setInt(2, inputTask.ListId);//akagiプレースホルダーのパラメータのキーを選択してる(1とかで)
      rs = pstmt.executeUpdate();//akagi 実行結果をここに入れてる

      result = DBResultType.Success;

      // 後処理（リソースのクローズ）
      pstmt.close();// 
      con.close();
    }
    catch (ClassNotFoundException | SQLException e)
    {
      result = this.GetErrorResult(e.getCause());
    }

    return result;
  }
  /**
   * ユーザー作成メソッド
   */
  public DBResultType InsertUser(User user)
  {
    //INSERT INTO todojava_db.users(`email`, `password`, `secret_tips_id`, `secret_password`) VALUES ('ryohei@gmail.com','ryohei',1,'高積');
    DBResultType result = DBResultType.Failure;
    Connection con = null;
    PreparedStatement pstmt = null;
    int rs = 0;

    try
    {
      Class.forName(this.DBDriverString);
      con = this.GetConnection();

      StringBuilder builder = new StringBuilder();
      builder.append("INSERT ");
      builder.append("INTO ");
      builder.append(this.DBName);
      builder.append(".users ");
      builder.append("( ");
      builder.append("email, ");
      builder.append("password, ");
      builder.append("secret_tips_id, ");
      builder.append("secret_password) ");
      builder.append("VALUES ");
      builder.append("(");
      builder.append("?, ");
      builder.append("?, ");
      builder.append("?, ");
      builder.append("?");
      builder.append(");");

      // SQL文の実行
      pstmt = con.prepareStatement(builder.toString());
      pstmt.setString(1, user.Email);//akagiプレースホルダーのパラメータのキーを選択してる(1とかで)
      pstmt.setString(2, user.Password);//akagiプレースホルダーのパラメータのキーを選択してる(1とかで)
      pstmt.setString(3, user.SecretTipsId);//akagiプレースホルダーのパラメータのキーを選択してる(1とかで)
      pstmt.setString(4, user.SecretPassword);//akagiプレースホルダーのパラメータのキーを選択してる(1とかで)
      rs = pstmt.executeUpdate();//akagi 実行結果をここに入れてる

      result = DBResultType.Success;
      // 後処理（リソースのクローズ）
      pstmt.close();// 
      con.close();
    }
    catch (ClassNotFoundException | SQLException e)
    {
      result = this.GetErrorResult(e.getCause());
    }

    return result;
  }

  /**
   * Supportボタン押下時のヒント文取得
   * GetUserIdメソッドとセットで実装
   */
  public DBClientDtoPair<DBResultType, Array> GetSecret(int secretID)
  {
    //SELECT * FROM todojava_db.secret WHERE id = 1;
    DBResultType result = DBResultType.Failure;

    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    java.sql.Array getSecretTipsText = null;

    try
    {
      Class.forName(this.DBDriverString);

      con = this.GetConnection();

      StringBuilder builder = new StringBuilder();
      builder.append("SELECT ");
      builder.append("* ");
      builder.append("FROM ");
      builder.append(this.DBName);
      builder.append(".secret ");
      builder.append("WHERE ");
      builder.append("id=");
      builder.append("?;");

      // SQL文の実行
      pstmt = con.prepareStatement(builder.toString());
      pstmt.setInt(1, secretID);//akagiプレースホルダーのパラメータのキーを選択してる(1とかで)
      rs = pstmt.executeQuery();//akagi 実行結果をここに入れてる

      while(rs.next())
      {
        result = DBResultType.Success;
        getSecretTipsText = rs.getArray("secret_tips_text");
        break;
      }
      // 後処理（リソースのクローズ）
      rs.close();//akagi リソースの解放を意味する：DB接続等を解除するという意味でもありそう。
      pstmt.close();// 
      con.close();
    }
    catch (ClassNotFoundException | SQLException e)
    {
      result = this.GetErrorResult(e.getCause());
    }
    return new DBClientDtoPair<DBClient.DBResultType,Array>(result, getSecretTipsText);
  }

  /**
   * Supportボタン押下時：ユーザー認証
   */
  public DBClientDtoPair<DBResultType, String> GetSecretId(String email)
  {
    //SELECT * FROM todojava_db.users WHERE email = "akagi@gmail.com";
    DBResultType result = DBResultType.Failure;
    String secretId = "";

    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    try 
    {
      Class.forName(this.DBDriverString);
      con = this.GetConnection();

      StringBuilder builder = new StringBuilder();
      builder.append("SELECT ");
      builder.append("* ");
      builder.append("FROM ");
      builder.append(this.DBName);
      builder.append(".users ");
      builder.append("WHERE ");
      builder.append("email=");
      builder.append("?;");

      // SQL文の実行
      pstmt = con.prepareStatement(builder.toString());
      pstmt.setString(1, email);//akagiプレースホルダーのパラメータのキーを選択してる(1とかで)
      rs = pstmt.executeQuery();//akagi 実行結果をここに入れてる
      
      // 検索結果を表示　//akagi ログインに成功したらuserIDを返す　userIDを使いながら次の処理を走らせる必要がある
      while (rs.next())//akagi行がある場合はtrue trueの場合は中に入って対応する（findとかでもよさそう？）最初の１行を取得するはず
      {
        result = DBResultType.Success;//akagi enumの成功を返す
        secretId = rs.getString("secret_tip_id");//akagi IDに対応する値を取得してStringで返す
        break;
      }
      // 後処理（リソースのクローズ）
      rs.close();//akagi リソースの解放を意味する：DB接続等を解除するという意味でもありそう。
      pstmt.close();// 
      con.close();
    } catch (ClassNotFoundException | SQLException e) {
      result = this.GetErrorResult(e.getCause());
    }

    return new DBClientDtoPair<DBClient.DBResultType, String>(result, secretId);
  }

  /**
   * ヒントテキスト一覧取得
   */
  public DBClientDtoPair<DBResultType, Array> getSecretTipsList()
  {
    //SELECT * FROM todojava_db.secret;
    DBResultType result = DBResultType.Failure;

    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    java.sql.Array secretList = null;
    
    try
    {
      Class.forName(this.DBDriverString);
      con = this.GetConnection();

      StringBuilder builder = new StringBuilder();
      builder.append("SELECT ");
      builder.append("* ");
      builder.append("FROM ");
      builder.append(this.DBName);
      builder.append(".secret;");
      pstmt = con.prepareStatement(builder.toString());
      rs = pstmt.executeQuery();//akagi 実行結果をここに入れてる

      while(rs.next())
      {
        result = DBResultType.Success;
        secretList = rs.getArray("secret_tips_text");
        break;
      }

      // 後処理（リソースのクローズ）
      rs.close();//akagi リソースの解放を意味する：DB接続等を解除するという意味でもありそう。
      pstmt.close();// 
      con.close();
    }
    catch (ClassNotFoundException | SQLException e)
    {
      result = this.GetErrorResult(e.getCause());
    }
    return new DBClientDtoPair<DBClient.DBResultType,Array>(result, secretList);
  }

    /**
   * DB接続オブジェクトを取得
   * @return Connection DB接続オブジェクト
   * @throws SQLException DB接続オブジェクトの取得に失敗
   */
  private Connection GetConnection() throws SQLException
  {
    return DriverManager.getConnection(this.ConnectionString, this.DBUserName, this.DBPassword);
  }

  /**
   * 例外オブジェクトから異常理由を判定
   * @param cause DB接続処理に発生した例外オブジェクト
   * @return DBResultType 処理結果
   */
  private DBResultType GetErrorResult(Throwable cause)
  {
    var result = DBResultType.Failure;//akagi エラーのときは失敗を入れる

    if (cause != null && cause instanceof CJCommunicationsException) //akagi:instanceof　オブジェクトが特定クラスのインスタンスか確認/boolean CJCommunicationsException:通信エラー等
    {
      // タイムアウトと判断
      result = DBResultType.Timeout;// akagi:タイムアウトを入れる
    }
    else 
    {
      // クエリ実行失敗やライブラリロード異常と判断
      result = DBResultType.Failure;//akagi:原因不明のため失敗を入れる
    }

    return result;
  }
  /**
   * DB接続クライアントタプル定義
   */
  public class DBClientDtoPair<V1, V2>
  {
      /** 変数1 */
      public final V1 Value1;

      /** 変数2 */
      public final V2 Value2;

      /**
       * コンストラクタ
       * 依存性注入
       * @param value1 変数1（任意型）
       * @param value2 変数2（任意型）
      */
      public DBClientDtoPair(V1 value1, V2 value2)
      {
          this.Value1 = value1;
          this.Value2 = value2;
      }
  }

}
