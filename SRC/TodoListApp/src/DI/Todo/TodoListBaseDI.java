package DI.Todo;

import View.*;

import java.util.concurrent.ExecutorService;

import Controller.*;
import Model.*;
import Model.Process.Todo.TodoProcess;
import View.Todo.*;
import Controller.Todo.*;
import Model.Todo.*;

public class TodoListBaseDI
{
    // ロガーインスタンス
    private Logger Logger;

    // DBクライアントインスタンス
    private DBClient DBClient;

    // MainWindowViewのインスタンス
    private MainWindowView MainWindowViewInstance;

    // DB処理キューインスタンス
    private ExecutorService DBQueue;

    // ログインユーザーID
    private String UserID;

    // コンストラクタ
    public TodoListBaseDI(Logger logger, DBClient dbClient, MainWindowView mainWindowViewInstance, ExecutorService dbQueue, String userID)
    {
        this.Logger = logger;
        this.DBClient = dbClient;
        this.MainWindowViewInstance = mainWindowViewInstance;
        this.DBQueue = dbQueue;
        this.UserID = userID;
    }

    // Todoリスト（リスト型表示）
    public TodoListController CreateTodoListMVC(TodoListBaseView todoListBaseView)
    {
        TodoProcess todoProcess = new TodoProcess(this.DBClient, this.DBQueue);
        TodoListModel todoListModel = new TodoListModel(this.Logger, todoProcess, this.UserID);
        TodoListView todoListView = new TodoListView(todoListBaseView, this.MainWindowViewInstance);
        // TODO: todoListViewの中でリスト・ボード・ガントチャートを中に入れてカードレイアウトで入れ替えるため。
        TodoListController todoListController = new TodoListController(todoListView, todoListModel);
        todoListView.controller = todoListController;

        return todoListController;
    }

    // Todoリスト（ボード型表示）
    public TodoBoardController CreateTodoBoardMVC(TodoListBaseView todoListBaseView)
    {
        // モデルインスタンス作成
        TodoBoardModel todoBoardModel = new TodoBoardModel();
        // Viewインスタンス作成
        TodoBoardView todoBoardView = new TodoBoardView(todoListBaseView);
        // controllerインスタンス作成
        TodoBoardController todoBoardController = new TodoBoardController(todoBoardView, todoBoardModel);
        todoBoardView.controller = todoBoardController;

        return todoBoardController;
    }
    
    
}
