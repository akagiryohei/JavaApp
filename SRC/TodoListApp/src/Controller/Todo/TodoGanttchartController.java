package Controller.Todo;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import Controller.ControllerBase;
import Entity.GanttchartTask;
import Entity.UserList;
import Entity.Enum.LogLevel;
import Entity.Enum.ViewStateEnum;
import Interface.Controller.Todo.ITodoGanttchartController;
import Interface.Model.Process.Todo.ITodoProcess.ResultType;
import Interface.Model.Todo.ITodoGanttchartModel;
import Interface.View.IViewProxyUtil;
import Interface.View.Todo.ITodoGanttchartView;

/*
 * Todoリスト（ガントチャート）コントローラ
 */
public class TodoGanttchartController extends ControllerBase implements ITodoGanttchartController {

    // 画面表示状態
    private ViewStateEnum ViewState;

    // Viewインスタンス
    private ITodoGanttchartView View;

    // Modelインスタンス
    private ITodoGanttchartModel Model;

    // 画面ラップ処理インスタンス
    private IViewProxyUtil ViewProxyUtil;

    /** 選択中リスト名 */
    private String ListName;

    /**
     * コンストラクタ
     */
    public TodoGanttchartController(ITodoGanttchartView view, ITodoGanttchartModel model, IViewProxyUtil viewProxyUtil)
    {
        this.ViewProxyUtil = viewProxyUtil;
        this.View = this.ViewProxyUtil.WrapView(ITodoGanttchartView.class, view);
        this.Model = model;

        // 画面状態を設定
        this.ViewState = ViewStateEnum.Close;
    }

    // 画面表示
    public void Show()
    {
        if (this.ViewState != ViewStateEnum.Open)
        {
            this.ViewState = ViewStateEnum.Open;
            this.View.Show();
            this.View.SetUserName(this.Model.GetUserName());
        }
    }
    // 画面非表示
    public void Hide()
    {
        if (this.ViewState != ViewStateEnum.Close)
        {
            this.ViewState = ViewStateEnum.Close;
            this.View.Hide();
        }
    }

    // 画面のインスタンスを取得
    public ITodoGanttchartView GetViewInstance()
    {
        return this.ViewProxyUtil.UnwrapView(this.View); 
    }

    /**
     * ユーザリスト取得
     */
    public void GetUserList()
    {
        this.Model.GetUserList((isBusy) ->
        {
            this.View.SideElementDisabled(isBusy);
            this.View.LeftElementDisabled(isBusy);
            this.WithLogger((logger) -> {
                logger.WriteLog(LogLevel.Info, "ユーザーリスト取得状況：" + isBusy);
            });
        }, (result) -> {
            if(result.Value1 == ResultType.Success)
            {
                //View側で保持するメソッドを作る
                List<UserList> list = new ArrayList<UserList>();
                list = result.Value2;
                this.View.SetList(list);
            }
            else
            {
                this.View.GetListFailureDialog();
            }
        });
    }

    /**
     * ユーザリスト登録
     * @param listText 画面入力リスト名
     * @param isBusyChanged 処理中イベントコールバック
     * @param finished 処理完了コールバック
     */
    public void CreateUserList(String listText)
    {
        this.Model.CreateUserList(listText, (isBusy) -> {
            this.View.SideElementDisabled(isBusy);
            this.View.LeftElementDisabled(isBusy);
            this.WithLogger((logger) -> {
                logger.WriteLog(LogLevel.Info, "ユーザーリスト登録状況：" + isBusy);
            });
        }, (result) -> {
            if (result == ResultType.Success)
            {
                this.GetUserList();
                this.ClearListNameInput();
            }
            else
            {
                // TODO:リスト登録失敗とDB接続失敗を分けて処理を記述
                this.View.ListLengthFailureDialog();
            }
        });
    }

    /**
     * リスト名入力欄クリア
     */
    public void ClearListNameInput()
    {
        this.View.ClearListNameInput();
    }

    /**
     * リスト削除メソッド
     * @param listId 画面選択リストID
     */
    public void DeleteList(int listId)
    {
        // DB問い合わせ後に本日の判定で使用する日時をこのタイミングで取得する
        LocalDate today = LocalDate.now();

        this.Model.DeleteList(listId, (isBusy) -> {
            this.View.SideElementDisabled(isBusy);
            this.View.LeftElementDisabled(isBusy);
            this.WithLogger((logger) -> {
                logger.WriteLog(LogLevel.Info, "リスト削除の状況：" + isBusy);
            });
        }, (result) -> {
            if (result == ResultType.Success)
            {
                this.GetUserList();
                this.View.SetTask(new ArrayList<GanttchartTask>(), "", null, today);
            }
            else
            {
                // TODO:リスト登録失敗とDB接続失敗を分けて処理を記述
                this.View.ListDeleteFailureDialog();
            }
        });
    }
    /**
     * リスト編集メソッド
     * @param listId 画面の選択中リストID
     * @param listName 編集予定のリスト名
     */
    public void UpdateList(int listId, String listName)
    {
        this.Model.UpdateList(listId, listName, (isBusy) -> {
            this.View.SideElementDisabled(isBusy);
            this.View.LeftElementDisabled(isBusy);
            this.WithLogger((logger) -> {
                logger.WriteLog(LogLevel.Info, "リスト編集状況：" + isBusy);
            });
        }, (result) -> {
            if (result == ResultType.Success)
            {
                this.GetUserList();
            }
            else
            {
                // TODO:リスト登録失敗とDB接続失敗を分けて処理を記述
                this.View.ListUpdateFailureDialog();
            }
        });
    }

    /**
     * タスク取得メソッド
     * @param listId 選択中リストID
     * @param listName 選択中リスト名
     * @param activeYearMonth 選択中年月
     */
    public void GetUserTask(int listId, String listName, YearMonth activeYearMonth)
    {
        // DB問い合わせ後に本日の判定で使用する日時をこのタイミングで取得する
        LocalDate today = LocalDate.now();

        this.ListName = listName;
        this.Model.GetUserTask(listId, activeYearMonth, (isBusy) -> {
            this.View.SideElementDisabled(isBusy);
            this.View.LeftElementDisabled(isBusy);
            this.WithLogger((logger) -> {
                logger.WriteLog(LogLevel.Info, "タスク取得状況：" + isBusy);
            });
        }, (result) -> {
            if(result.Value1 == ResultType.Success)
            {
                List<GanttchartTask> task = new ArrayList<GanttchartTask>();
                result.Value2.forEach(item -> {
                task.add(item.Clone());
                });
                this.View.SetTask(task, this.ListName, activeYearMonth, today);
            }
            else
            {
                this.View.SetTask(new ArrayList<GanttchartTask>(), this.ListName, activeYearMonth, today);
                this.View.GetTaskFailureDialog();
            }
        });
    }
    
    /**
     * タスク編集（タスク進捗度＋完了/未完了）
     * @param taskId 画面の選択中タスクID
     * @param progress 画面の選択中タスクの進捗率
     */
    public void UpdateTask(int taskId, int progress, int listId, String listName, YearMonth activeYearMonth)
    {
        this.Model.UpdateTask(taskId, progress, (isBusy) -> {
            this.View.SideElementDisabled(isBusy);
            this.View.LeftElementDisabled(isBusy);
            this.WithLogger((logger) -> {
                logger.WriteLog(LogLevel.Info, "タスク更新状況：" + isBusy);
            });
        }, (result) -> {
            if(result == ResultType.Success)
            {
                this.GetUserTask(listId, listName, activeYearMonth);
            }
            else
            {
                this.View.UpdateTaskFailureDialog();
            }
        });
    }
}