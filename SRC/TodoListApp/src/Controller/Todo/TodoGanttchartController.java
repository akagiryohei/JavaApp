package Controller.Todo;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import Controller.ControllerBase;
import Entity.GanttchartTask;
import Entity.UserList;
import Entity.UserTask;
import Entity.Enum.ViewStateEnum;
import Interface.Controller.Todo.ITodoGanttchartController;
import Interface.Model.Process.Todo.ITodoProcess.ResultType;
import Interface.Model.Todo.ITodoGanttchartModel;
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

    /** 選択中リスト名 */
    private String ListName;

    /**
     * コンストラクタ
     */
    public TodoGanttchartController(ITodoGanttchartView view, ITodoGanttchartModel model)
    {
        this.View = view;
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
        return this.View;
    }

    /**
     * ユーザリスト取得
     */
    public void GetUserList()
    {
        this.Model.GetUserList((isBusy) ->
        {
            // TODO:isBusyの値によって、viewのボタン等の要素の押下可否を制御
            System.out.println(isBusy);
        }, (result) -> {
            //View側で保持するメソッドを作る
            List<UserList> list = new ArrayList<UserList>();
            list = result.Value2;
            this.View.SetList(list);
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
            // TODO:isBusyの値によって、viewのボタン等の要素の押下可否を制御
            System.out.println(isBusy);
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
        this.Model.DeleteList(listId, (isBusy) -> {
            // TODO:isBusyの値によって、viewのボタン等の要素の押下可否を制御
            System.out.println(isBusy);
        }, (result) -> {
            if (result == ResultType.Success)
            {
                this.GetUserList();
                this.View.SetTask(new ArrayList<GanttchartTask>(), "", null);
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
            // TODO:isBusyの値によって、viewのボタン等の要素の押下可否を制御
            System.out.println(isBusy);
        }, (result) -> {
            if (result == ResultType.Success)
            {
                this.View.CloseListDialog();
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
        this.ListName = listName;
        this.Model.GetUserTask(listId, activeYearMonth, (isBusy) -> {
            // TODO:isBusyの値によって、viewのボタン等の要素の押下可否を制御
            System.out.println(isBusy);
        }, (result) -> {
            List<GanttchartTask> task = new ArrayList<GanttchartTask>();
            result.Value2.forEach(item -> {
                task.add(item.Clone());
            });
            this.View.SetTask(task, this.ListName, activeYearMonth);
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
            System.out.println(isBusy);
        }, (result) -> {
            this.GetUserTask(listId, listName, activeYearMonth);
        });
    }
}