package Controller.Todo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Controller.ControllerBase;
import Entity.Pair;
import Entity.UserList;
import Entity.UserTask;
import Entity.Enum.LogLevel;
import Entity.Enum.ViewStateEnum;
import Interface.Controller.Todo.ITodoListController;
import Interface.Model.Process.Todo.ITodoProcess;
import Interface.Model.Process.Todo.ITodoProcess.ResultType;
import Interface.Model.Todo.ITodoListModel;
import Interface.View.IViewProxyUtil;
import Interface.View.Todo.ITodoListView;

/*
 * Todoリスト（リスト型表示）コントローラ
 */
public class TodoListController extends ControllerBase implements ITodoListController {

    /** 画面表示状態 */
    private ViewStateEnum ViewState;

    /** Viewインスタンス */
    private ITodoListView View;

    /** Modelインスタンス */
    private ITodoListModel Model;

    /** 画面ラップ処理インスタンス */
    private IViewProxyUtil ViewProxyUtil;

    /** 選択中リスト名 */
    private String ListName;
    /**
     * コンストラクタ
     * 依存性注入
     * @param view 画面Viewインスタンス
     * @param model 画面Modelインスタンス
     * @param viewProxyUtil 画面ラップ処理インスタンス
     */
    public TodoListController(ITodoListView view, ITodoListModel model, IViewProxyUtil viewProxyUtil)
    {
        this.ViewProxyUtil = viewProxyUtil;
        this.View = this.ViewProxyUtil.WrapView(ITodoListView.class, view);
        this.Model = model;

        // 画面状態を設定
        this.ViewState = ViewStateEnum.Close;
    }

    /**
     * 画面表示
     */
    public void Show()
    {
        if (this.ViewState != ViewStateEnum.Open)
        {
            this.ViewState = ViewStateEnum.Open;
            this.View.Show();
            this.View.SetUserName(this.Model.GetUserName());
        }
    }

    /**
     * 画面非表示
     */
    public void Hide()
    {
        if (this.ViewState != ViewStateEnum.Close)
        {
            this.ViewState = ViewStateEnum.Close;
            this.View.Hide();
        }
    }

    /**
     * 画面インスタンスを取得
     */
    public ITodoListView GetViewInstance()
    {
        return this.ViewProxyUtil.UnwrapView(this.View);
    }

    /**
     * ユーザリスト取得
     * @param isBusyChanged 処理中イベントコールバック
     * @param finished 処理完了コールバック
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
            if(result.Value1 == ResultType.Success)
            {
                list = result.Value2;
                this.View.SetList(list);
            }
            else
            {
                this.View.ShowGetUserListFailureDialog();
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
            this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "リスト登録の状況：" + isBusy); });
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
     * タスク登録処理
     * @param taskText
     * @param startDate
     * @param endDate
     */
    public void CreateUserTask(String taskText, Date startDate, Date endDate)
    {
        this.Model.CreateUserTask(taskText, startDate, endDate, (isBusy) -> {
            // TODO:isBusyの値によって、viewのボタン等の要素の押下可否を制御
            System.out.println(isBusy);
        }, (result) -> {
            if (result == ResultType.Success)
            {
                this.Model.GetUserTask((isBusy) -> {
                    System.out.println(isBusy);
                }, (resultType2) -> {
                    this.NotifyCommonUserTaskResultToView(resultType2);
                });
            }
            else
            {
                // TODO:タスク登録失敗とDB接続失敗を分けて処理を記述
                this.View.ListLengthFailureDialog();
            }
        });
    }

    /**
     * タスク取得処理（共通処理）
     */
    public void NotifyCommonUserTaskResultToView(Pair<ITodoProcess.ResultType, List<UserTask>> resultType2)
    {
        if (resultType2.Value1 == ResultType.Success)
        {
            List<UserTask> task = new ArrayList<UserTask>();
            resultType2.Value2.forEach(item -> {
                task.add(item.Clone());
            });
            this.View.SetTask(task, this.ListName);
        }
        else
        {
            // タスク取得失敗ダイアログ表示
            this.View.GetTaskFailureDialog();
        }
    }


    /**
     * リスト削除メソッド
     * @param listId 画面選択リストID
     * @param isBusyChanged 処理中イベントコールバック
     * @param finished 処理完了コールバック
     */
    public void DeleteList(int listId)
    {
        this.Model.DeleteList(listId, (isBusy) -> {
            this.View.SideElementDisabled(isBusy);
            this.View.LeftElementDisabled(isBusy);
            this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "リスト削除の状況：" + isBusy); });
        }, (result) -> {
            if (result == ResultType.Success)
            {
                this.GetUserList();
                this.View.SetTask(new ArrayList<UserTask>(), "");
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
            this.View.SideElementDisabled(isBusy);
            this.View.LeftElementDisabled(isBusy);
            this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "リスト名更新の状況：" + isBusy); });
        }, (result) -> {
            if (result == ResultType.Success)
            {
                this.ListName = listName;
                // リスト編集
                this.GetUserList();
                // タスク再取得
                this.Model.GetUserTask((isBusy) -> {
                    this.View.SideElementDisabled(isBusy);
                    this.View.LeftElementDisabled(isBusy);
                    this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "タスク再取得中か否か：" + isBusy); });
                }, (resultType2) -> {
                    if (resultType2.Value1 == ResultType.Success)
                    {
                        List<UserTask> task = new ArrayList<UserTask>();
                        resultType2.Value2.forEach(item -> {
                            task.add(item.Clone());
                        });
                        this.View.SetTask(task, this.ListName);
                        this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "リスト名更新完了"); });
                    }
                    else
                    {
                        // タスク取得失敗ダイアログ表示
                        this.View.GetTaskFailureDialog();
                    }
                });
            }
            else
            {
                // TODO:リスト登録失敗とDB接続失敗を分けて処理を記述
                this.View.ListUpdateFailureDialog();
            }
        });
    }

    /**
     * タスク編集メソッド
     * @param taskId 画面の選択中タスクID
     * @param taskText 編集予定のタスク名
     */
    public void UpdateTask(int taskId, String taskText)
    {
        this.Model.UpdateTask(taskId, taskText, (isBusy) -> {
            this.View.SideElementDisabled(isBusy);
            this.View.LeftElementDisabled(isBusy);
            this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "タスク名更新の状況：" + isBusy); });
        }, (result) -> {
            if (result == ResultType.Success)
            {
                this.Model.GetUserTask((isBusy) -> {
                    this.View.SideElementDisabled(isBusy);
                    this.View.LeftElementDisabled(isBusy);
                    this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "タスク再取得中か否か：" + isBusy); });
                }, (resultType2) -> {
                    this.NotifyCommonUserTaskResultToView(resultType2);
                });
            }
            else{
                this.View.TaskUpdateFailureDialog();
            }
        });
    }

    /**
     * タスク編集メソッド（タスク進捗度＋完了/未完了）
     */
    public void UpdateTask(int taskid, boolean isChecked)
    {
        this.Model.UpdateTask(taskid, isChecked, (isBusy) -> {
            // TODO:isBusyの値によって、viewのボタン等の要素の押下可否を制御
            System.out.println(isBusy);
        }, (result) -> {
            if (result == ResultType.Success)
            {
                this.Model.GetUserTask((isBusy) -> {
                    System.out.println(isBusy);
                }, (resultType2) -> {
                    if (resultType2.Value1 == ResultType.Success)
                    {
                        List<UserTask> task = new ArrayList<UserTask>();
                        resultType2.Value2.forEach(item -> {
                            task.add(item.Clone());
                        });
                        this.View.SetTask(task, this.ListName);
                    }
                    else
                    {
                        // タスク取得失敗ダイアログ表示
                        this.View.GetTaskFailureDialog();
                    }
                });
            }
            else{
                this.View.TaskUpdateFailureDialog();
            }
        });
    }

    /**
     * 期日編集メソッド
     */
    public void EditPeriodDate(int taskId, Date startDate, Date endDate)
    {
        this.Model.EditPeriodDate(taskId, startDate, endDate, (isBusy) -> {
            this.View.SideElementDisabled(isBusy);
            this.View.LeftElementDisabled(isBusy);
            this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "期日編集の状況：" + isBusy); });
        }, (result) -> {
            if (result == ResultType.Success)
            {
                this.Model.GetUserTask((isBusy) -> {
                    this.View.SideElementDisabled(isBusy);
                    this.View.LeftElementDisabled(isBusy);
                    this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "タスク再取得中か否か：" + isBusy); });
                }, (resultType2) -> {
                    this.NotifyCommonUserTaskResultToView(resultType2);
                });
            }
            else {
                // TODO:リスト登録失敗とDB接続失敗を分けて処理を記述
                // TODO:共通ダイアログで〇〇ダイアログを表示する処理を実装
                this.View.TaskDeleteFailureDialog();
            }
        });
    }

    /**
     * タスク取得メソッド
     * @param listId 選択中リストID
     */
    public void GetUserTask(int listId, String listName)
    {
        this.ListName = listName;
        this.Model.GetUserTask(listId, (isBusy) -> {
            // TODO:isBusyの値によって、viewのボタン等の要素の押下可否を制御
            System.out.println(isBusy);
        }, (result) -> {
            this.NotifyCommonUserTaskResultToView(result);
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
     * タスク削除メソッド
     */
    public void DeleteTask(int taskId)
    {
        this.Model.DeleteTask(taskId, (isBusy) -> {
            // TODO:isBusyの値によって、viewのボタン等の要素の押下可否を制御
            System.out.println(isBusy);
        }, (result) -> {
            if (result == ResultType.Success)
            {
                this.Model.GetUserTask((isBusy) -> {
                    System.out.println(isBusy);
                }, (resultType2) -> {
                    if (resultType2.Value1 == ResultType.Success)
                    {
                        List<UserTask> task = new ArrayList<UserTask>();
                        resultType2.Value2.forEach(item -> {
                            task.add(item.Clone());
                        });
                        this.View.SetTask(task, this.ListName);
                    }
                    else
                    {
                        // タスク取得失敗ダイアログ表示
                        this.View.GetTaskFailureDialog();
                    }
                });
            }
            else
            {
                // TODO:リスト登録失敗とDB接続失敗を分けて処理を記述
                // TODO:共通ダイアログで〇〇ダイアログを表示する処理を実装
                this.View.TaskDeleteFailureDialog();
            }
        });
    }

    /**
     * 入力欄のイベント情報の変化が発生した
     * @param taskText タスク入力欄の文字列
     */
    public void ChangeTextField(String taskText)
    {
        this.View.ChangeTextField(this.Model.GetPlusButtonPossibility(taskText));
    }

}
