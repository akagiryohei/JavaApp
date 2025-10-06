package Controller.Todo;

import java.util.ArrayList;
import java.util.List;

import Controller.ControllerBase;
import Entity.UserList;
import Entity.UserTask;
import Entity.Enum.ViewStateEnum;
import Interface.Controller.Todo.ITodoBoardController;
import Interface.Model.Process.Todo.ITodoProcess.ResultType;
import Interface.Model.Todo.ITodoBoardModel;
import Interface.View.Todo.ITodoBoardView;

/*
 * Todoリスト（ボード型表示）コントローラ
 */
public class TodoBoardController extends ControllerBase implements ITodoBoardController
{
    // 画面表示状態
    private ViewStateEnum ViewState;

    // Viewインスタンス
    private ITodoBoardView View;

    // Modelインスタンス
    private ITodoBoardModel Model;

    /**
     * コンストラクタ
     */
    public TodoBoardController(ITodoBoardView view, ITodoBoardModel model)
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
    public ITodoBoardView GetViewInstance()
    {
        return this.View;
    }

    /**
     * ユーザリスト＋タスク取得（ボード画面用）
     * @param isBusyChanged 処理中イベントコールバック
     * @param finished 処理完了コールバック
     */
    public void GetUserListAndTask()
    {
        this.Model.GetUserListAndTask((isBusy) ->
        {
            System.out.println(isBusy);
        }, (result) -> {
            // Listの保持
            List<UserList> list = new ArrayList<UserList>();
            list = result.Value2;

            this.View.SetListTask(list);
        });
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
            // TODO:isBusyの値によって、viewのボタン等の要素の押下可否を制御
            System.out.println(isBusy);
        }, (result) -> {
            if (result == ResultType.Success)
            {
                this.GetUserListAndTask();
            }
            else
            {
                // TODO:リスト登録失敗とDB接続失敗を分けて処理を記述
                this.View.ListDeleteFailureDialog();
            }
        });
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
                // ここはリストとタスクを取得する作りにする必要がある
                this.GetUserListAndTask();
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
                this.GetUserListAndTask();
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
            // TODO:isBusyの値によって、viewのボタン等の要素の押下可否を制御
            System.out.println(isBusy);
        }, (result) -> {
            if (result == ResultType.Success)
            {
                this.View.CloseTaskDialog();
                this.GetUserListAndTask();
            }
            else{
                // TODO:リスト登録失敗とDB接続失敗を分けて処理を記述
                // TODO:共通ダイアログで〇〇ダイアログを表示する処理を実装
                this.View.TaskDeleteFailureDialog();
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
                this.GetUserListAndTask();
            }
            else{
                // TODO:リスト登録失敗とDB接続失敗を分けて処理を記述
                this.View.TaskDeleteFailureDialog();
            }
        });
    }

    /**
     * タスク登録処理
     * @param listId リストID
     * @param taskText タスクテキスト
     */
    public void CreateUserTask(int listId, String taskText)
    {
        this.Model.CreateUserTask(listId, taskText, null, null, (isBusy) -> {
            // TODO:isBusyの値によって、viewのボタン等の要素の押下可否を制御
            System.out.println(isBusy);
        }, (result) -> {
            if (result == ResultType.Success)
            {
                this.GetUserListAndTask();
            }
            else
            {
                // TODO:タスク登録失敗とDB接続失敗を分けて処理を記述
                this.View.ListLengthFailureDialog();
            }
        });
    }


    /**
     * ユーザリスト登録
     * @param listText 画面入力リスト名
     */
    public void CreateUserList(String listText)
    {
        this.Model.CreateUserList(listText, (isBusy) -> {
            // TODO:isBusyの値によって、viewのボタン等の要素の押下可否を制御
            System.out.println(isBusy);
        }, (result) -> {
            if (result == ResultType.Success)
            {
                this.GetUserListAndTask();
            }
            else
            {
                // TODO:リスト登録失敗とDB接続失敗を分けて処理を記述
                this.View.ListLengthFailureDialog();
            }
        });
    }
}
