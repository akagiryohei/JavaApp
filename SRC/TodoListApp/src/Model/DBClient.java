package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.cj.exceptions.CJCommunicationsException;

import Entity.DB.User;
import Interface.Model.IDBClient;
import Entity.DB.DBClientDtoPair;
import Entity.DB.DBResultType;
import Entity.DB.InputTask;
import Entity.DB.ListColumn;
import Entity.DB.TaskColumn;

/**
  * DB接続クライアントクラス
  */
public class DBClient implements IDBClient
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
   * @return 処理結果、ログイン対象ユーザ情報
  */
  public DBClientDtoPair<DBResultType, User> CanLogin(String email, String password)
  {
    DBResultType result = DBResultType.Failure;
    User user = new User();
    
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

    try (
      Connection con = this.GetConnection();
      PreparedStatement pstmt = con.prepareStatement(builder.toString());
    )
    {
      pstmt.setString(1, email);
      pstmt.setString(2, password);

      try (ResultSet rs = pstmt.executeQuery())
      {
        while (rs.next())
        {
          User data = new User();
          data.Id = rs.getString("Id");
          data.Email = rs.getString("email");
          data.Password = rs.getString("password");
          data.SecretTipsId = rs.getString("secret_tips_id");
          data.SecretPassword = rs.getString("secret_password");
          user = data;
          break;
        }
      }
      result = DBResultType.Success;
    }
    catch (ClassNotFoundException | SQLException e)// akagi エラーがあるときはキャッチする
    {
      result = this.GetErrorResult(e.getCause());
    }

    return new DBClientDtoPair<DBResultType, User>(result, user);
  }

  /**
   * リスト取得メソッド
   * @param userID ログイン中ユーザーID
   * @return ログイン中ユーザー作成のListを取得、リスト取得成否（成功：リスト配列、失敗（存在しない） :-1）
   */
  public DBClientDtoPair<DBResultType, List<ListColumn>> GetList(int userID)
  {
    DBResultType result = DBResultType.Failure;
    List<ListColumn> list = new ArrayList<ListColumn>();

    StringBuilder builder = new StringBuilder();
    builder.append("SELECT ");
    builder.append("* ");
    builder.append("FROM ");
    builder.append(this.DBName);
    builder.append(".list ");
    builder.append("WHERE ");
    builder.append("user_id=");
    builder.append("?;");

    try (
      Connection con = this.GetConnection();
      PreparedStatement pstmt = con.prepareStatement(builder.toString());
    )
    {
      pstmt.setInt(1, userID);

      try (ResultSet rs = pstmt.executeQuery())
      {
        while(rs.next())
        {
          var listColumn = new  ListColumn();
          listColumn.id = rs.getInt("id");
          listColumn.list_name = rs.getString("list_name");
          list.add(listColumn);
        }
      }
      result = DBResultType.Success;
    }
    catch (ClassNotFoundException | SQLException e)
    {
      result = this.GetErrorResult(e.getCause());
    }

    return new DBClientDtoPair<DBResultType, List<ListColumn>>(result, list);
  }

  /**
   * リスト登録メソッド
   */
  public DBResultType InsertList(String listText, int userID)
  {
    DBResultType result = DBResultType.Failure;
    int rs = 0;

    //INSERT INTO todojava_db.list(`list_name`, `user_id`) VALUES ('涙の訳','1');
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

    try (
      Connection con = this.GetConnection();
      PreparedStatement pstmt = con.prepareStatement(builder.toString());
    )
    {
      pstmt.setString(1, listText);
      pstmt.setInt(2, userID);

      rs = pstmt.executeUpdate();//akagi 実行結果をここに入れてる

      result = DBResultType.Success;
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
  public DBResultType UpdateList(String listName, int userID)
  {
    //UPDATE `list` SET `list_name`='転職', WHERE id = 1;
    DBResultType result = DBResultType.Failure;
    int rs = 0;

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

    try (
      Connection con = this.GetConnection();
      PreparedStatement pstmt = con.prepareStatement(builder.toString());
    )
    {
      pstmt.setString(1, listName);
      pstmt.setInt(2, userID);
      
      rs = pstmt.executeUpdate();

      result = DBResultType.Success;
    }
    catch (ClassNotFoundException | SQLException e)
    {
      result = this.GetErrorResult(e.getCause());
    }

    return result;
  }
  
  /**
   * リスト削除メソッド(リストに紐づくタスクも削除)
   */
  
  public DBResultType DeleteList(int listID, int userID)
  {
    DBResultType result = DBResultType.Failure;
    //DELETE FROM todojava_db.list WHERE user_id = 1 AND id = 1;
    int rs = 0;

    StringBuilder listBuilder = new StringBuilder();
    listBuilder.append("DELETE ");
    listBuilder.append("FROM ");
    listBuilder.append(this.DBName);
    listBuilder.append(".list ");
    listBuilder.append("WHERE ");
    listBuilder.append("user_id=");
    listBuilder.append("? ");
    listBuilder.append("AND ");
    listBuilder.append("id= ");
    listBuilder.append("?;");

    StringBuilder taskBuilder = new StringBuilder();
    taskBuilder.append("DELETE ");
    taskBuilder.append("FROM ");
    taskBuilder.append(this.DBName);
    taskBuilder.append(".task ");
    taskBuilder.append("WHERE ");
    taskBuilder.append("user_id=");
    taskBuilder.append("? ");
    taskBuilder.append("AND ");
    taskBuilder.append("list_id=");
    taskBuilder.append("?;");

    try (
      Connection con = this.GetConnection();
    )
    {

      try (
        
        PreparedStatement listPstmt = con.prepareStatement(listBuilder.toString());
        PreparedStatement taskPstmt = con.prepareStatement(taskBuilder.toString());
      )
      {
        con.setAutoCommit(false);
        listPstmt.setInt(1, userID);
        listPstmt.setInt(2, listID);
        taskPstmt.setInt(1, userID);
        taskPstmt.setInt(2, listID);
        rs = listPstmt.executeUpdate();
        if (rs != 0)
        {
          rs = taskPstmt.executeUpdate();
        }
        con.commit();
        con.setAutoCommit(true);
        result = DBResultType.Success;
      }
      catch (SQLException e)
      {
        if (e instanceof SQLException)
        {
          try
          {
            con.rollback();
          }
          catch (SQLException rollbackEx)
          {
            // ロールバック失敗時の処理（ログ出力など）
            rollbackEx.printStackTrace();
          }
        }
        result = this.GetErrorResult(e.getCause());
      }
    }
    catch(ClassNotFoundException | SQLException e)
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
    List<TaskColumn> taskList = new ArrayList<TaskColumn>();

    StringBuilder builder = new StringBuilder();
    builder.append("SELECT ");
    builder.append("* ");
    builder.append("FROM ");
    builder.append(this.DBName);
    builder.append(".task ");
    builder.append("WHERE ");
    builder.append("user_id =");
    builder.append("? ");
    builder.append(" AND ");
    builder.append("list_id= ");
    builder.append("?;");

    try (
      Connection con = this.GetConnection();
      PreparedStatement pstmt = con.prepareStatement(builder.toString());
    )
    {
      pstmt.setInt(1, userID);
      pstmt.setInt(2, listID);

      try (ResultSet rs = pstmt.executeQuery())
      {
        while(rs.next())
        {
          var taskColumn = new TaskColumn();
          taskColumn.id = rs.getInt("id");
          taskColumn.task_text = rs.getString("task_text");
          taskColumn.start_date = rs.getDate("start_date");
          taskColumn.end_date = rs.getDate("end_date");
          taskColumn.task_status = rs.getInt("task_status");
          taskColumn.list_id = rs.getInt("list_id");
          taskColumn.progress_rate = rs.getInt("progress");
          taskList.add(taskColumn);
        }
      }
      result = DBResultType.Success;
    }
    catch (ClassNotFoundException | SQLException e)
    {
      result = this.GetErrorResult(e.getCause());
    }

    return new DBClientDtoPair<DBResultType, List<TaskColumn>>(result, taskList);
  }

  /**
   * タスク取得メソッド
   */
  public DBClientDtoPair<DBResultType, List<TaskColumn>> GetUnFinishedTask(int userID)
  {
    // SELECT * FROM todojava_db.task WHERE user_id = 1 AND list_id = 1;
    DBResultType result = DBResultType.Failure;
    List<TaskColumn> taskList = new ArrayList<TaskColumn>();

    StringBuilder builder = new StringBuilder();
    builder.append("SELECT ");
    builder.append("task.task_text, ");
    builder.append("task.end_date, ");
    builder.append("task_status, ");
    builder.append("list_name ");
    builder.append("FROM ");
    builder.append(this.DBName);
    builder.append(".task ");
    builder.append("JOIN ");
    builder.append("list ");
    builder.append("ON ");
    builder.append("list.id=");
    builder.append("task.list_id ");
    builder.append("WHERE ");
    builder.append("list.user_id=");
    builder.append("? ");
    builder.append("AND ");
    builder.append("task.task_status=");
    builder.append("FALSE");

    try (
      Connection con = this.GetConnection();
      PreparedStatement pstmt = con.prepareStatement(builder.toString());
    )
    {
      pstmt.setInt(1, userID);

      try (ResultSet rs = pstmt.executeQuery())
      {
        while(rs.next())
        {
          var taskColumn = new TaskColumn();
          taskColumn.task_text = rs.getString("task_text");
          taskColumn.end_date = rs.getDate("end_date");
          taskColumn.list_name = rs.getString("list_name");
          taskList.add(taskColumn);
        }
      }
      result = DBResultType.Success;
    }
    catch (ClassNotFoundException | SQLException e)
    {
      result = this.GetErrorResult(e.getCause());
    }

    return new DBClientDtoPair<DBResultType, List<TaskColumn>>(result, taskList);
  }

  /**
   * タスク登録メソッド
   */
  public DBResultType CreateTask(InputTask inputTask)
  {
    // INSERT INTO todojava_db.task(`task_text`, `start_date`, `end_date`, `user_id`, `list_id`) VALUES ('新宮に行く','2025-03-24 12:12:12','2025-03-30 00:00:00','1','1');
    DBResultType result = DBResultType.Failure;
    int rs = 0;

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

    try (
      Connection con = this.GetConnection();
      PreparedStatement pstmt = con.prepareStatement(builder.toString());
    )
    {
      pstmt.setString(1, inputTask.TaskText);

      if (inputTask.StartDate != null)
      {
        pstmt.setDate(2, inputTask.StartDate);
      }
      else
      {
        pstmt.setNull(2,java.sql.Types.DATE);
      }

      if (inputTask.EndDate != null)
      {
        pstmt.setDate(3, inputTask.EndDate);
      }
      else
      {
        pstmt.setNull(3,java.sql.Types.DATE);
      }

      pstmt.setInt(4, inputTask.UserId);//akagiプレースホルダーのパラメータのキーを選択してる(1とかで)
      pstmt.setInt(5, inputTask.ListId);//akagiプレースホルダーのパラメータのキーを選択してる(1とかで)

      rs = pstmt.executeUpdate();
      result = DBResultType.Success;
    }
    catch (ClassNotFoundException | SQLException e)
    {
      result = this.GetErrorResult(e.getCause());
    }

    return result;
  }

  /**
   * タスク編集メソッド（タスク名）
   */
  public DBResultType UpdateTask(int taskID, String taskText,int userID)
  {
    //UPDATE todojava_db.task SET `task_text`='テキスト45'WHERE id = 11 AND user_id = 1;
    DBResultType result = DBResultType.Failure;
    int rs = 0;

    StringBuilder builder = new StringBuilder();
    builder.append("UPDATE ");
    builder.append(this.DBName);
    builder.append(".task ");
    builder.append("SET ");
    builder.append("task_text= ");
    builder.append("? ");
    builder.append("WHERE ");
    builder.append("id= ");
    builder.append("? ");
    builder.append("AND ");
    builder.append("user_id= ");
    builder.append("?;");

    try (
      Connection con = this.GetConnection();
      PreparedStatement pstmt = con.prepareStatement(builder.toString());
    )
    {
      pstmt.setString(1, taskText);
      pstmt.setInt(2, taskID);
      pstmt.setInt(3, userID);

      rs = pstmt.executeUpdate();
      result = DBResultType.Success;
    }
    catch (ClassNotFoundException | SQLException e)
    {
      result = this.GetErrorResult(e.getCause());
    }

    return result;
  }

  /**
   * タスク編集メソッド（タスク進捗度＋完了/未完了）
   * @param taskID 選択中タスクID
   * @param isChecked 選択中タスクの状態
   * @param userID ログインユーザーID
   * @return
   */
  public DBResultType UpdateTask(int taskID, int userID, int taskPercent)
  {
    DBResultType result = DBResultType.Failure;
    int rs = 0;

    StringBuilder builder = new StringBuilder();
    builder.append("UPDATE ");
    builder.append(this.DBName);
    builder.append(".task ");
    builder.append("SET ");
    builder.append("task_status= ");
    builder.append("? ,");
    builder.append("progress=");
    builder.append("? ");
    builder.append("WHERE ");
    builder.append("id= ");
    builder.append("? ");
    builder.append("AND ");
    builder.append("user_id= ");
    builder.append("?;");

    try (
      Connection con = this.GetConnection();
      PreparedStatement pstmt = con.prepareStatement(builder.toString());
    )
    {
      pstmt.setInt(1, taskPercent == 100 ? 1 : 0);
      pstmt.setInt(2, taskPercent);
      pstmt.setInt(3, taskID);
      pstmt.setInt(4, userID);

      rs = pstmt.executeUpdate();
      result = DBResultType.Success;
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
  public DBResultType DeleteTask(int taskID, int userID)
  {
    //DELETE FROM todojava_db.task WHERE id = 8 AND user_id = 1;
    DBResultType result = DBResultType.Failure;
    int rs = 0;

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

    try (
      Connection con = this.GetConnection();
      PreparedStatement pstmt = con.prepareStatement(builder.toString());
    )
    {
      pstmt.setInt(1, userID);
      pstmt.setInt(2, taskID);

      rs = pstmt.executeUpdate();
      result = DBResultType.Success;
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
    int rs = 0;

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

    try (
      Connection con = this.GetConnection();
      PreparedStatement pstmt = con.prepareStatement(builder.toString());
    )
    {
      pstmt.setString(1, user.Email);
      pstmt.setString(2, user.Password);
      pstmt.setString(3, user.SecretTipsId);
      pstmt.setString(4, user.SecretPassword);

      rs = pstmt.executeUpdate();
      result = DBResultType.Success;
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
  public DBClientDtoPair<DBResultType, String> GetSecret(int secretID)
  {
    //SELECT * FROM todojava_db.secret WHERE id = 1;
    DBResultType result = DBResultType.Failure;
    String getSecretTipsText = null;

    StringBuilder builder = new StringBuilder();
    builder.append("SELECT ");
    builder.append("* ");
    builder.append("FROM ");
    builder.append(this.DBName);
    builder.append(".secret ");
    builder.append("WHERE ");
    builder.append("id=");
    builder.append("?;");

    try (
      Connection con = this.GetConnection();
      PreparedStatement pstmt = con.prepareStatement(builder.toString());
    )
    {
      pstmt.setInt(1, secretID);//akagiプレースホルダーのパラメータのキーを選択してる(1とかで)

      try (ResultSet rs = pstmt.executeQuery())
      {
        while(rs.next())
        {
          getSecretTipsText = rs.getString("secret_tips_text");
          break;
        }
      }
      result = DBResultType.Success;
    }
    catch (ClassNotFoundException | SQLException e)
    {
      result = this.GetErrorResult(e.getCause());
    }

    return new DBClientDtoPair<DBResultType,String>(result, getSecretTipsText);
  }

  /**
   * Supportボタン押下時：ユーザー認証
   */
  public DBClientDtoPair<DBResultType, Integer> GetSecretId(String email)
  {
    //SELECT * FROM todojava_db.users WHERE email = "akagi@gmail.com";
    DBResultType result = DBResultType.Failure;
    int secretId = 0;

    StringBuilder builder = new StringBuilder();
    builder.append("SELECT ");
    builder.append("* ");
    builder.append("FROM ");
    builder.append(this.DBName);
    builder.append(".users ");
    builder.append("WHERE ");
    builder.append("email=");
    builder.append("?;");

    try (
      Connection con = this.GetConnection();
      PreparedStatement pstmt = con.prepareStatement(builder.toString());
    )
    {
      pstmt.setString(1, email);

      try (ResultSet rs = pstmt.executeQuery())
      {
        while (rs.next())//akagi行がある場合はtrue trueの場合は中に入って対応する（findとかでもよさそう？）最初の１行を取得するはず
        {
          secretId = rs.getInt("secret_tips_id");//akagi IDに対応する値を取得してIntegerで返す
          break;
        }
      }
      result = DBResultType.Success;
    }
    catch (ClassNotFoundException | SQLException e)
    {
      result = this.GetErrorResult(e.getCause());
    }

    return new DBClientDtoPair<DBResultType, Integer>(result, secretId);
  }

  /**
   * ヒントテキスト一覧取得
   */
  public DBClientDtoPair<DBResultType, List<String>> getSecretTipsList()
  {
    //SELECT * FROM todojava_db.secret;
    DBResultType result = DBResultType.Failure;
    List<String> secretList = new ArrayList<>();

    StringBuilder builder = new StringBuilder();
    builder.append("SELECT ");
    builder.append("* ");
    builder.append("FROM ");
    builder.append(this.DBName);
    builder.append(".secret;");

    try (
      Connection con = this.GetConnection();
      PreparedStatement pstmt = con.prepareStatement(builder.toString());
    )
    {
      try (ResultSet rs = pstmt.executeQuery())
      {
        while(rs.next())
        {
          secretList.add(rs.getString("secret_tips_text"));
        }
      }
      result = DBResultType.Success;
    }
    catch (ClassNotFoundException | SQLException e)
    {
      result = this.GetErrorResult(e.getCause());
    }
    
    return new DBClientDtoPair<DBResultType,List<String>>(result, secretList);
  }

    /**
   * DB接続オブジェクトを取得
   * @return Connection DB接続オブジェクト
   * @throws SQLException DB接続オブジェクトの取得に失敗
   * @throws ClassNotFoundException JDBC接続ドライバロード失敗
   */
  private Connection GetConnection() throws SQLException, ClassNotFoundException
  {
    Class.forName(this.DBDriverString);
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
   * 忘却者ログイン認証
   * 依存性注入
   * @param email ログイン対象アカウントのメールアドレス
   * @param secretPassword ログイン対象アカウントのパスワード（ハッシュ後文字列）
   * @return 処理結果、ログイン対象ユーザ情報
  */
  public DBClientDtoPair<DBResultType, User> CanLostPassUserLogin(String email, String secretPassword)
  {
    DBResultType result = DBResultType.Failure;
    User user = new User();

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
    builder.append("secret_password=");
    builder.append("?;");

    try (
      Connection con = this.GetConnection();
      PreparedStatement pstmt = con.prepareStatement(builder.toString());
    )
    {
      pstmt.setString(1, email);
      pstmt.setString(2, secretPassword);

      try (ResultSet rs = pstmt.executeQuery())
      {
        while (rs.next())
        {
          User data = new User();
          data.Id = rs.getString("Id");
          data.Email = rs.getString("email");
          data.Password = rs.getString("password");
          data.SecretTipsId = rs.getString("secret_tips_id");
          data.SecretPassword = rs.getString("secret_password");
          user = data;
          break;
        }
      }
      result = DBResultType.Success;
    }
    catch (ClassNotFoundException | SQLException e)
    {
      result = this.GetErrorResult(e.getCause());
    }

    return new DBClientDtoPair<DBResultType, User>(result, user);
  }

  /**
   * 期日編集メソッド
   */
  public DBResultType EditPeriodDate(InputTask inputTask)
  {
    DBResultType result = DBResultType.Failure;
    int rs = 0;

    StringBuilder builder = new StringBuilder();
    builder.append("UPDATE ");
    builder.append(this.DBName);
    builder.append(".task ");
    builder.append("SET ");
    builder.append("start_date= ");
    builder.append("? ,");
    builder.append("end_date= ");
    builder.append("? ");
    builder.append("WHERE ");
    builder.append("id= ");
    builder.append("? ");
    builder.append("AND ");
    builder.append("user_id= ");
    builder.append("?;");

    try (
      Connection con = this.GetConnection();
      PreparedStatement pstmt = con.prepareStatement(builder.toString());
    )
    {
      // DBClientは画面を考えない
      if (inputTask.StartDate != null)
      {
        pstmt.setDate(1, inputTask.StartDate);
      }
      else
      {
        pstmt.setNull(1,java.sql.Types.DATE);
      }

      if (inputTask.EndDate != null)
      {
        pstmt.setDate(2, inputTask.EndDate);
      }
      else
      {
        pstmt.setNull(2,java.sql.Types.DATE);
      }

      pstmt.setInt(3, inputTask.TaskId);
      pstmt.setInt(4, inputTask.UserId);

      rs = pstmt.executeUpdate();
      result = DBResultType.Success;
    }
    catch (ClassNotFoundException | SQLException e)
    {
      result = this.GetErrorResult(e.getCause());
    }

    return result;
  }

  /**
   * リスト＋タスクの一括取得
   * @param userID ログイン中ユーザーID
   * @return
   */
  public DBClientDtoPair<DBResultType, List<ListColumn>> GetListTask(int userID)
  {
    DBResultType result = DBResultType.Failure;
    List<ListColumn> list = new ArrayList<ListColumn>();

    StringBuilder builder = new StringBuilder();
    builder.append("SELECT ");
    builder.append("list.id AS listId, ");
    builder.append("task.id AS taskId, ");
    builder.append("list.list_name, ");
    builder.append("task.task_text, ");
    builder.append("task.start_date, ");
    builder.append("task.end_date, ");
    builder.append("task.task_status, ");
    builder.append("task.list_id ");
    builder.append("FROM ");
    builder.append(this.DBName);
    builder.append(".list AS list ");
    builder.append("LEFT JOIN ");
    builder.append(this.DBName);
    builder.append(".task AS task ");
    builder.append("ON ");
    builder.append("list.id=task.list_id ");
    builder.append("WHERE ");
    builder.append("list.user_id=");
    builder.append("?; ");

    try (
      Connection con = this.GetConnection();
      PreparedStatement pstmt = con.prepareStatement(builder.toString());
    )
    {
      pstmt.setInt(1, userID);

      try (ResultSet rs = pstmt.executeQuery())
      {
        while(rs.next())
        {
          int listId = rs.getInt("listId");
          if (!list.stream().anyMatch(x -> x.id == listId))
          {
            // リスト情報がまだ追加されていない場合のみ追加
            var listColumn = new ListColumn();
            listColumn.id = listId;
            listColumn.list_name = rs.getString("list_name");
            list.add(listColumn);
          }
          
          var target = list.stream().filter(x -> x.id == listId).findFirst();
          
          // タスク情報の取得
          if (rs.getInt("taskId") != 0) // タスクが存在する場合のみ追加
          {
            var taskColumn = new TaskColumn();
            taskColumn.id = rs.getInt("taskId");
            taskColumn.task_text = rs.getString("task_text");
            taskColumn.start_date = rs.getDate("start_date");
            taskColumn.end_date = rs.getDate("end_date");
            taskColumn.task_status = rs.getInt("task_status");
            taskColumn.list_id = rs.getInt("list_id");
            target.ifPresent(listColumn -> {
              // リストに紐づくタスク情報を追加
              listColumn.taskList.add(taskColumn);
            });
          }
        }
        result = DBResultType.Success;
      } catch (SQLException e) {
        result = this.GetErrorResult(e.getCause());
      }
    }
    catch (ClassNotFoundException | SQLException e)
    {
      result = this.GetErrorResult(e.getCause());
    }
    return new DBClientDtoPair<DBResultType,List<ListColumn>>(result, list);
  }
}
