package DI.Todo;

import java.util.concurrent.ExecutorService;

import Controller.Todo.TodoAICreateListTaskController;
import Controller.Todo.TodoBoardController;
import Controller.Todo.TodoGanttchartController;
import Controller.Todo.TodoListController;
import Entity.UserData;
import Entity.Dialog.Constants;
import Interface.Controller.Todo.ITodoAICreateListTaskController;
import Interface.Controller.Todo.ITodoBoardController;
import Interface.Controller.Todo.ITodoGanttchartController;
import Interface.Controller.Todo.ITodoListController;
import Interface.DI.Todo.ITodoListBaseDI;
import Interface.Model.IAIAPIClient;
import Interface.Model.IDBClient;
import Interface.Model.IGetHolidayInfoService;
import Interface.Model.ILogger;
import Interface.Model.IValidationUtil;
import Interface.Model.Process.Todo.ITodoProcess;
import Interface.Model.Todo.ITodoAICreateListTaskModel;
import Interface.Model.Todo.ITodoBoardModel;
import Interface.Model.Todo.ITodoGanttchartModel;
import Interface.Model.Todo.ITodoListModel;
import Interface.View.IMainWindowView;
import Interface.View.IViewProxyUtil;
import Interface.View.Todo.ITodoAICreateListTaskView;
import Interface.View.Todo.ITodoBoardView;
import Interface.View.Todo.ITodoGanttchartView;
import Interface.View.Todo.ITodoListBaseView;
import Interface.View.Todo.ITodoListView;
import Model.ValidationUtil;
import Model.Process.Todo.TodoProcess;
import Model.Todo.TodoAICreateListTaskModel;
import Model.Todo.TodoBoardModel;
import Model.Todo.TodoGanttchartModel;
import Model.Todo.TodoListModel;
import View.Dialog.CommonDialogView;
import View.Dialog.DatePickerView;
import View.Dialog.EditPeriodDialogView;
import View.Dialog.EditProgressRateDialogView;
import View.Dialog.UpdateListDialogView;
import View.Dialog.UpdateTaskDialogView;
import View.Todo.TodoAICreateListTaskView;
import View.Todo.TodoBoardView;
import View.Todo.TodoGanttchartView;
import View.Todo.TodoListView;

public class TodoListBaseDI implements ITodoListBaseDI
{
    // ロガーインスタンス
    private ILogger Logger;

    // DBクライアントインスタンス
    private IDBClient DBClient;

    // AIクライアントインスタンス
    private IAIAPIClient AIAPIClient;

    // 祝日情報取得サービスインスタンス
    private IGetHolidayInfoService GetHolidayInfoService;

    // MainWindowViewのインスタンス
    private IMainWindowView MainWindowViewInstance;

    // DB処理キューインスタンス
    private ExecutorService DBQueue;

    // 画面ラップ処理インスタンス
    private IViewProxyUtil ViewProxyUtil;

    // ログインユーザーID
    private UserData UserData;

    // 共通ダイアログインスタンス
    private CommonDialogView CommonDialogViewInstance;

    // コンストラクタ
    public TodoListBaseDI(ILogger logger, IDBClient dbClient, IAIAPIClient aiApiClient, IGetHolidayInfoService getHolidayInfoService, IMainWindowView mainWindowViewInstance, ExecutorService dbQueue, UserData userData, CommonDialogView commonDialogView, IViewProxyUtil viewProxyUtil)
    {
        this.Logger = logger;
        this.DBClient = dbClient;
        this.AIAPIClient = aiApiClient;
        this.GetHolidayInfoService = getHolidayInfoService;
        this.MainWindowViewInstance = mainWindowViewInstance;
        this.DBQueue = dbQueue;
        this.ViewProxyUtil = viewProxyUtil;
        this.UserData = userData;
        this.CommonDialogViewInstance = commonDialogView;
    }

    // Todoリスト（リスト型表示）
    public ITodoListController CreateTodoListMVC(ITodoListBaseView todoListBaseView)
    {
        IValidationUtil util = new ValidationUtil();
        ITodoProcess todoProcess = new TodoProcess(this.DBClient, this.DBQueue, this.AIAPIClient);
        ITodoListModel todoListModel = new TodoListModel(this.Logger, todoProcess, this.UserData, util);
        DatePickerView datePickerView = new DatePickerView();
        this.GetHolidayInfoService.RequestHolidayInfoAsync((holidays) -> {
            datePickerView.SetHolidays(holidays);
        });
        Constants constants = new Constants();
        EditPeriodDialogView editPeriodDialogView = new EditPeriodDialogView(datePickerView, constants);
        UpdateListDialogView updateListDialogView = new UpdateListDialogView(constants);
        UpdateTaskDialogView updateTaskDialogView = new UpdateTaskDialogView(constants);
        ITodoListView todoListView = new TodoListView(this.CommonDialogViewInstance, editPeriodDialogView, updateListDialogView, updateTaskDialogView);
        todoListView.SetLogger(this.Logger);
        // TODO: todoListViewの中でリスト・ボード・ガントチャートを中に入れてカードレイアウトで入れ替えるため。
        ITodoListController todoListController = new TodoListController(todoListView, todoListModel, this.ViewProxyUtil);
        todoListController.SetLogger(this.Logger);
        todoListView.SetController(todoListController);

        return todoListController;
    }

    // Todoリスト（ボード型表示）
    public ITodoBoardController CreateTodoBoardMVC(ITodoListBaseView todoListBaseView)
    {
        IValidationUtil util = new ValidationUtil();
        ITodoProcess todoProcess = new TodoProcess(this.DBClient, this.DBQueue, this.AIAPIClient);
        // モデルインスタンス作成
        ITodoBoardModel todoBoardModel = new TodoBoardModel(this.Logger, todoProcess, this.UserData, util);
        DatePickerView datePickerView = new DatePickerView();
        this.GetHolidayInfoService.RequestHolidayInfoAsync((holidays) -> {
            datePickerView.SetHolidays(holidays);
        });
        Constants constants = new Constants();
        UpdateListDialogView updateListDialogView = new UpdateListDialogView(constants);
        UpdateTaskDialogView updateTaskDialogView = new UpdateTaskDialogView(constants);
        // Viewインスタンス作成
        // TODO: 現行の実装は期日選択ダイアログが共通化されていないが、ここでインスタンス生成する。インスタンス生成時に祝日情報取得サービスクラスから取得した祝日情報をセットする
        ITodoBoardView todoBoardView = new TodoBoardView(this.CommonDialogViewInstance, updateListDialogView, updateTaskDialogView);
        todoBoardView.SetLogger(this.Logger);
        // controllerインスタンス作成
        ITodoBoardController todoBoardController = new TodoBoardController(todoBoardView, todoBoardModel, this.ViewProxyUtil);
        todoBoardController.SetLogger(this.Logger);
        todoBoardView.SetController(todoBoardController);

        return todoBoardController;
    }

    // Todoリスト（ガントチャート型表示）
    public ITodoGanttchartController CreateGanttchartMVC(ITodoListBaseView todoListBaseView)
    {
        IValidationUtil util = new ValidationUtil();
        ITodoProcess todoProcess = new TodoProcess(this.DBClient, this.DBQueue, this.AIAPIClient);
        // モデルインスタンス作成
        ITodoGanttchartModel todoGanttchartModel = new TodoGanttchartModel(this.Logger, todoProcess, this.UserData, util);
        Constants constants = new Constants();
        EditProgressRateDialogView editProgressRateDialogView = new EditProgressRateDialogView(constants);
        UpdateListDialogView updateListDialogView = new UpdateListDialogView(constants);
        // Viewインスタンス作成
        ITodoGanttchartView todoGanttchartView = new TodoGanttchartView(this.CommonDialogViewInstance, editProgressRateDialogView, updateListDialogView);
        todoGanttchartView.SetLogger(this.Logger);
        // controllerインスタンス作成
        ITodoGanttchartController todoGanttchartController = new TodoGanttchartController(todoGanttchartView, todoGanttchartModel, this.ViewProxyUtil);
        todoGanttchartController.SetLogger(this.Logger);
        todoGanttchartView.SetController(todoGanttchartController);

        return todoGanttchartController;
    }

    // Todoリスト（AI作成リスト・タスク案型表示）
    public ITodoAICreateListTaskController CreateAIListTaskMVC(ITodoListBaseView todoListBaseView)
    {
        ITodoProcess todoProcess = new TodoProcess(this.DBClient, this.DBQueue, this.AIAPIClient);
        // モデルインスタンス作成
        ITodoAICreateListTaskModel todoAICreateListTaskModel = new TodoAICreateListTaskModel(this.Logger, todoProcess, this.UserData);
        Constants constants = new Constants();
        // TODO: ダイアログが必要ならここで作成（その時にはconstantsを渡す）
        // Viewインスタンス作成
        ITodoAICreateListTaskView todoAICreateListTaskView = new TodoAICreateListTaskView(this.CommonDialogViewInstance);
        todoAICreateListTaskView.SetLogger(this.Logger);
        // controllerインスタンス作成
        ITodoAICreateListTaskController todoAICreateListTaskController = new TodoAICreateListTaskController(todoAICreateListTaskView, todoAICreateListTaskModel, this.ViewProxyUtil);
        todoAICreateListTaskController.SetLogger(this.Logger);
        todoAICreateListTaskView.SetController(todoAICreateListTaskController);

        return todoAICreateListTaskController;
    }
    

}