package Controller.Todo;

import java.util.ArrayList;

import javax.swing.SwingUtilities;

import Controller.ControllerBase;
import Entity.AIListTask;
import Entity.UserTask;
import Entity.Enum.LogLevel;
import Entity.Enum.ViewStateEnum;
import Interface.Controller.Todo.ITodoAICreateListTaskController;
import Interface.Model.Todo.ITodoAICreateListTaskModel;
import Interface.View.IViewProxyUtil;
import Interface.View.Todo.ITodoAICreateListTaskView;
import Interface.Model.Process.Todo.ITodoProcess.ResultType;


/**
 * Todoリスト（AI作成リスト・タスク案型表示）コントローラ
 */
public class TodoAICreateListTaskController extends ControllerBase implements ITodoAICreateListTaskController
{
    /** 画面状態表示 */
    private ViewStateEnum ViewState;

    /** Viewインスタンス */
    private ITodoAICreateListTaskView View;

    /** Modelインスタンス */
    private ITodoAICreateListTaskModel Model;

    // 画面ラップ処理インスタンス
    private IViewProxyUtil ViewProxyUtil;

    /**
     * コンストラクタ
     * 依存性注入
     * @param View 画面viewインスタンス
     * @param Model 画面Modelインスタンス
     * @param viewProxyUtil 画面ラップ処理インスタンス
     */
    public TodoAICreateListTaskController(ITodoAICreateListTaskView View, ITodoAICreateListTaskModel Model, IViewProxyUtil viewProxyUtil)
    {
        this.ViewProxyUtil = viewProxyUtil;
        this.View = this.ViewProxyUtil.WrapView(ITodoAICreateListTaskView.class, View);
        this.Model = Model;

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
     * 画面のインスタンスを取得
     * @return Todoリスト（AI作成リスト・タスク案型表示
     */
    public ITodoAICreateListTaskView GetViewInstance()
    {
        return this.ViewProxyUtil.UnwrapView(this.View);
    }

    /**
     * AIリスト・タスク案作成
     * @param userInput ユーザー入力
     */
    public void Ask(String userInput)
    {
        this.Model.Ask(userInput ,(isBusyChanged) -> {
            this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "処理中：" + isBusyChanged); });
            this.View.SideElementDisabled(isBusyChanged);
            this.View.LeftElementDisabled(isBusyChanged);
        }, (result) -> {
            // 処理成功時
            if (result.Value1 == ResultType.Success)
            {
                // 画面に取得内容を表示（リスト・タスク案）
                this.View.SetAIListTask(result.Value2);
            }
            // 処理失敗時
            else
            {
                // 画面に生成失敗を表示（ダイアログ等）
                this.View.AskFailDialog();
            }
        });
    }

    /**
     * AIリスト・タスク案再生成
     * @param userInput ユーザー入力
     * @param addUserInput 追加ユーザー入力
     */
    public void ReAsk(String userInput, String addUserInput)
    {
        this.Model.ReAsk(userInput, addUserInput, (isBusyChanged) -> {
            this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "処理中：" + isBusyChanged); });
            this.View.SideElementDisabled(isBusyChanged);
            this.View.LeftElementDisabled(isBusyChanged);
        }, (result) -> {
            // 処理成功時
            if (result.Value1 == ResultType.Success)
            {
                // 画面に取得内容を表示（リスト・タスク案再生成）
                this.View.SetAIListTask(result.Value2);
            }
            // 処理失敗時
            else
            {
                // 画面に再生成失敗を表示（ダイアログ等）
                this.View.AskFailDialog();
            }
        });
    }

    /**
     * 保持しているタスクリストの修正
     * @param userDeleteTaskId ユーザが削除したタスクID
     */
    public void EditAITaskList(int userDeleteTaskId)
    {
        // モデルにあるタスクリストの修正+情報の取得
        ResultType result = this.Model.EditAITaskList(userDeleteTaskId);
        if (result == ResultType.Success)
        {
            // Modelの保存データを取得し画面に表示する
            AIListTask aIListTask = this.Model.GetAIListTask();
            this.View.SetAIListTask(aIListTask);
        }
        else
        {
            // 失敗のダイアログを表示する
            this.View.ListTaskCreateFailDialog();
        }
    }

    /**
     * リスト名の編集
     * @param userEditListName ユーザが編集したリスト名
     */
    public void EditAIList(String userEditListName)
    {
        this.View.SideElementDisabled(true);
        this.View.LeftElementDisabled(true);
        ResultType result = this.Model.EditAIList(userEditListName);
        if (result == ResultType.Success)
        {
            // とりあえずリスト名を変えたけど画面自体を再度作り直す：不要なら消す
            AIListTask aIListTask = this.Model.GetAIListTask();
            this.View.SetAIListTask(aIListTask);
            this.View.SideElementDisabled(false);
            this.View.LeftElementDisabled(false);
        }
        else
        {
            this.View.ListTaskCreateFailDialog();
            this.View.SideElementDisabled(false);
            this.View.LeftElementDisabled(false);
        }
    }

    /**
     * リストとタスクの登録メソッド
     */
    public void AddListTask()
    {
        //リスト登録処理の呼び出し
        this.Model.CreateUserList((isBusy) ->
        {
            this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "リスト登録中：" + isBusy); });
            this.View.SideElementDisabled(isBusy);
            this.View.LeftElementDisabled(isBusy);
        }, (resultList) ->
        {
            if (resultList == ResultType.Success)
            {
                this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "リスト登録成功"); });
                //タスク登録処理の呼び出し
                this.Model.CreateUserTask((isBusy) ->
                {
                    this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "タスク登録中：" + isBusy); });
                    this.View.SideElementDisabled(isBusy);
                    this.View.LeftElementDisabled(isBusy);
                }, (resultTask) ->
                {
                    if (resultTask == ResultType.Success)
                    {
                        this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "タスク登録成功"); });
                        // 画面への成功の通知＋画面の切り替え
                        this.View.ListTaskCreateSuccessDialog();
                        this.View.Clear();
                    }
                    else
                    {
                        this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "タスク登録失敗"); });
                        // リストの削除
                        this.DeleteList();
                        this.View.ListTaskCreateFailDialog();
                    }
                });
            }
            else
            {
                this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "リスト登録失敗"); });
                this.View.ListTaskCreateFailDialog();
            }
        });
    }

    /**
     * リスト削除メソッド
     */
    public void DeleteList()
    {
        this.Model.DeleteList((isBusy) -> {
            this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "リスト削除の状況：" + isBusy); });
        }, (result) -> {
            if (result == ResultType.Success)
            {
                this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "リスト削除成功"); });
            }
            else
            {
                this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "リスト削除失敗"); });
            }
        });
    }
}
