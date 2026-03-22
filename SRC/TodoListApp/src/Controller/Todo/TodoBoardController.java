package Controller.Todo;

import java.util.ArrayList;
import java.util.List;

import Controller.ControllerBase;
import Entity.UserList;
import Entity.UserTask;
import Entity.Enum.LogLevel;
import Entity.Enum.ViewStateEnum;
import Interface.Controller.Todo.ITodoBoardController;
import Interface.Model.Process.Todo.ITodoProcess.ResultType;
import Interface.Model.Todo.ITodoBoardModel;
import Interface.View.IViewProxyUtil;
import Interface.View.Todo.ITodoBoardView;

/*
 * Todoリスト（ボード型表示）コントローラ
 */
public class TodoBoardController extends ControllerBase implements ITodoBoardController {
    // 画面表示状態
    private ViewStateEnum ViewState;

    // Viewインスタンス
    private ITodoBoardView View;

    // Modelインスタンス
    private ITodoBoardModel Model;

    // 画面ラップ処理インスタンス
    private IViewProxyUtil ViewProxyUtil;

    /**
     * コンストラクタ
     */
    public TodoBoardController(ITodoBoardView view, ITodoBoardModel model, IViewProxyUtil viewProxyUtil) {
        this.ViewProxyUtil = viewProxyUtil;
        this.View = this.ViewProxyUtil.WrapView(ITodoBoardView.class, view);
        this.Model = model;

        // 画面状態を設定
        this.ViewState = ViewStateEnum.Close;
    }

    // 画面表示
    public void Show() {
        if (this.ViewState != ViewStateEnum.Open) {
            this.ViewState = ViewStateEnum.Open;
            this.View.Show();
        }
    }

    // 画面非表示
    public void Hide() {
        if (this.ViewState != ViewStateEnum.Close) {
            this.ViewState = ViewStateEnum.Close;
            this.View.Hide();
        }
    }

    // 画面のインスタンスを取得
    public ITodoBoardView GetViewInstance() {
        return this.ViewProxyUtil.UnwrapView(this.View);
    }

    /**
     * ユーザリスト＋タスク取得（ボード画面用）
     * 
     * @param isBusyChanged 処理中イベントコールバック
     * @param finished      処理完了コールバック
     */
    public void GetUserListAndTask() {
        this.Model.GetUserListAndTask((isBusy) -> {
            this.View.SideElementDisabled(isBusy);
            this.View.LeftElementDisabled(isBusy);
            this.WithLogger((logger) -> {
                logger.WriteLog(LogLevel.Info, "ユーザーリスト＋タスク取得状況：" + isBusy);
            });
        }, (result) -> {
            if(result.Value1 == ResultType.Success)
            {
                // Listの保持
                List<UserList> list = new ArrayList<UserList>();
                list = result.Value2;
                this.View.SetListTask(list);
            }
            else
            {
                this.View.GetListAndTaskFailureDialog();
            }
        });
    }

    /**
     * リスト削除メソッド
     * 
     * @param listId        画面選択リストID
     * @param isBusyChanged 処理中イベントコールバック
     * @param finished      処理完了コールバック
     */
    public void DeleteList(int listId) {
        this.Model.DeleteList(listId, (isBusy) -> {
            this.View.SideElementDisabled(isBusy);
            this.View.LeftElementDisabled(isBusy);
            this.WithLogger((logger) -> {
                logger.WriteLog(LogLevel.Info, "リスト削除の状況：" + isBusy);
            });
        }, (result) -> {
            if (result == ResultType.Success) {
                this.GetUserListAndTask();
            } else {
                this.View.ListDeleteFailureDialog();
            }
        });
    }

    /**
     * タスク削除メソッド
     */
    public void DeleteTask(int taskId) {
        this.Model.DeleteTask(taskId, (isBusy) -> {
            this.View.SideElementDisabled(isBusy);
            this.View.LeftElementDisabled(isBusy);
            this.WithLogger((logger) -> {
                logger.WriteLog(LogLevel.Info, "タスク削除の状況：" + isBusy);
            });
        }, (result) -> {
            if (result == ResultType.Success) {
                this.GetUserListAndTask();
            } else {
                this.View.TaskDeleteFailureDialog();
            }
        });
    }

    /**
     * リスト編集メソッド
     * 
     * @param listId   画面の選択中リストID
     * @param listName 編集予定のリスト名
     */
    public void UpdateList(int listId, String listName) {
        this.Model.UpdateList(listId, listName, (isBusy) -> {
            this.View.SideElementDisabled(isBusy);
            this.View.LeftElementDisabled(isBusy);
            this.WithLogger((logger) -> {
                logger.WriteLog(LogLevel.Info, "リスト編集状況：" + isBusy);
            });
        }, (result) -> {
            if (result == ResultType.Success) {
                this.GetUserListAndTask();
            } else {
                this.View.ListUpdateFailureDialog();
            }
        });
    }

    /**
     * タスク編集メソッド
     * 
     * @param taskId   画面の選択中タスクID
     * @param taskText 編集予定のタスク名
     */
    public void UpdateTask(int taskId, String taskText) {
        this.Model.UpdateTask(taskId, taskText, (isBusy) -> {
            this.View.SideElementDisabled(isBusy);
            this.View.LeftElementDisabled(isBusy);
            this.WithLogger((logger) -> {
                logger.WriteLog(LogLevel.Info, "タスク編集状況：" + isBusy);
            });
        }, (result) -> {
            if (result == ResultType.Success) {
                this.GetUserListAndTask();
            } else {
                this.View.TaskUpdateFailureDialog();
            }
        });
    }

    /**
     * タスク編集メソッド（タスク進捗度＋完了/未完了）
     */
    public void UpdateTask(int taskid, boolean isChecked) {
        this.Model.UpdateTask(taskid, isChecked, (isBusy) -> {
            this.View.SideElementDisabled(isBusy);
            this.View.LeftElementDisabled(isBusy);
            this.WithLogger((logger) -> {
                logger.WriteLog(LogLevel.Info, "タスク編集状況：" + isBusy);
            });
        }, (result) -> {
            if (result == ResultType.Success) {
                this.GetUserListAndTask();
            } else {
                this.View.TaskUpdateFailureDialog();
            }
        });
    }

    /**
     * タスク登録処理
     * 
     * @param listId   リストID
     * @param taskText タスクテキスト
     */
    public void CreateUserTask(int listId, String taskText) {
        this.Model.CreateUserTask(listId, taskText, null, null, (isBusy) -> {
            this.View.SideElementDisabled(isBusy);
            this.View.LeftElementDisabled(isBusy);
            this.WithLogger((logger) -> {
                logger.WriteLog(LogLevel.Info, "タスク登録の状況：" + isBusy);
            });
        }, (result) -> {
            if (result == ResultType.Success) {
                this.GetUserListAndTask();
            } else {
                this.View.TaskCreateFailureDialog();
            }
        });
    }

    /**
     * ユーザリスト登録
     * 
     * @param listText 画面入力リスト名
     */
    public void CreateUserList(String listText) {
        this.Model.CreateUserList(listText, (isBusy) -> {
            this.View.SideElementDisabled(isBusy);
            this.View.LeftElementDisabled(isBusy);
            this.WithLogger((logger) -> {
                logger.WriteLog(LogLevel.Info, "リスト登録の状況：" + isBusy);
            });
        }, (result) -> {
            if (result == ResultType.Success) {
                this.GetUserListAndTask();
            } else {
                this.View.ListLengthFailureDialog();
            }
        });
    }
}
