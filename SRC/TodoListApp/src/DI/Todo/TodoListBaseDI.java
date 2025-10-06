package DI.Todo;

import View.*;
import View.Dialog.CommonDialogView;
import View.Dialog.DatePickerView;
import View.Dialog.EditPeriodDialogView;
import View.Dialog.EditProgressRateDialogView;

import java.util.concurrent.ExecutorService;

import Controller.*;
import Model.*;
import Model.Process.Todo.TodoProcess;
import View.Todo.*;
import Controller.Todo.*;
import Entity.UserData;
import Interface.Controller.Todo.ITodoBoardController;
import Interface.Controller.Todo.ITodoGanttchartController;
import Interface.Controller.Todo.ITodoListController;
import Interface.DI.Todo.ITodoListBaseDI;
import Interface.Model.IDBClient;
import Interface.Model.IGetHolidayInfoService;
import Interface.Model.ILogger;
import Interface.Model.IValidationUtil;
import Interface.Model.Process.Todo.ITodoProcess;
import Interface.Model.Todo.ITodoBoardModel;
import Interface.Model.Todo.ITodoGanttchartModel;
import Interface.Model.Todo.ITodoListModel;
import Interface.View.IMainWindowView;
import Interface.View.Todo.ITodoBoardView;
import Interface.View.Todo.ITodoGanttchartView;
import Interface.View.Todo.ITodoListBaseView;
import Interface.View.Todo.ITodoListView;
import Model.Todo.*;

public class TodoListBaseDI implements ITodoListBaseDI
{
    // ロガーインスタンス
    private ILogger Logger;

    // DBクライアントインスタンス
    private IDBClient DBClient;

    // 祝日情報取得サービスインスタンス
    private IGetHolidayInfoService GetHolidayInfoService;

    // MainWindowViewのインスタンス
    private IMainWindowView MainWindowViewInstance;

    // DB処理キューインスタンス
    private ExecutorService DBQueue;

    // ログインユーザーID
    private UserData UserData;

    // 共通ダイアログインスタンス
    private CommonDialogView CommonDialogViewInstance;

    // コンストラクタ
    public TodoListBaseDI(ILogger logger, IDBClient dbClient, IGetHolidayInfoService getHolidayInfoService, IMainWindowView mainWindowViewInstance, ExecutorService dbQueue, UserData userData, CommonDialogView commonDialogView)
    {
        this.Logger = logger;
        this.DBClient = dbClient;
        this.GetHolidayInfoService = getHolidayInfoService;
        this.MainWindowViewInstance = mainWindowViewInstance;
        this.DBQueue = dbQueue;
        this.UserData = userData;
        this.CommonDialogViewInstance = commonDialogView;
    }

    // Todoリスト（リスト型表示）
    public ITodoListController CreateTodoListMVC(ITodoListBaseView todoListBaseView)
    {
        IValidationUtil util = new ValidationUtil();
        ITodoProcess todoProcess = new TodoProcess(this.DBClient, this.DBQueue);
        ITodoListModel todoListModel = new TodoListModel(this.Logger, todoProcess, this.UserData, util);
        DatePickerView datePickerView = new DatePickerView();
        this.GetHolidayInfoService.RequestHolidayInfoAsync((holidays) -> {
            datePickerView.SetHolidays(holidays);
        });
        EditPeriodDialogView editPeriodDialogView = new EditPeriodDialogView(datePickerView);
        ITodoListView todoListView = new TodoListView(todoListBaseView, this.MainWindowViewInstance, this.CommonDialogViewInstance, editPeriodDialogView);
        // TODO: todoListViewの中でリスト・ボード・ガントチャートを中に入れてカードレイアウトで入れ替えるため。
        ITodoListController todoListController = new TodoListController(todoListView, todoListModel);
        todoListView.SetController(todoListController);
        
        return todoListController;
    }

    // Todoリスト（ボード型表示）
    public ITodoBoardController CreateTodoBoardMVC(ITodoListBaseView todoListBaseView)
    {
        IValidationUtil util = new ValidationUtil();
        ITodoProcess todoProcess = new TodoProcess(this.DBClient, this.DBQueue);
        // モデルインスタンス作成
        ITodoBoardModel todoBoardModel = new TodoBoardModel(this.Logger, todoProcess, this.UserData, util);
        DatePickerView datePickerView = new DatePickerView();
        this.GetHolidayInfoService.RequestHolidayInfoAsync((holidays) -> {
            datePickerView.SetHolidays(holidays);
        });
        EditPeriodDialogView editPeriodDialogView = new EditPeriodDialogView(datePickerView);
        // Viewインスタンス作成
        // TODO: 現行の実装は期日選択ダイアログが共通化されていないが、ここでインスタンス生成する。インスタンス生成時に祝日情報取得サービスクラスから取得した祝日情報をセットする
        ITodoBoardView todoBoardView = new TodoBoardView(todoListBaseView, this.MainWindowViewInstance, this.CommonDialogViewInstance, editPeriodDialogView);
        // controllerインスタンス作成
        ITodoBoardController todoBoardController = new TodoBoardController(todoBoardView, todoBoardModel);
        todoBoardView.SetController(todoBoardController);
        
        return todoBoardController;
    }

    // Todoリスト（ガントチャート型表示）
    public ITodoGanttchartController CreateGanttchartMVC(ITodoListBaseView todoListBaseView)
    {
        IValidationUtil util = new ValidationUtil();
        ITodoProcess todoProcess = new TodoProcess(this.DBClient, this.DBQueue);
        // モデルインスタンス作成
        ITodoGanttchartModel todoGanttchartModel = new TodoGanttchartModel(this.Logger, todoProcess, this.UserData, util);
        EditProgressRateDialogView editProgressRateDialogView = new EditProgressRateDialogView();
        // Viewインスタンス作成
        ITodoGanttchartView todoGanttchartView = new TodoGanttchartView(todoListBaseView, this.MainWindowViewInstance, this.CommonDialogViewInstance, editProgressRateDialogView);
        // controllerインスタンス作成
        ITodoGanttchartController todoGanttchartController = new TodoGanttchartController(todoGanttchartView, todoGanttchartModel);
        todoGanttchartView.SetController(todoGanttchartController);
        
        return todoGanttchartController;
    }
}