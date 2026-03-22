package View.Todo;

import Interface.Controller.Todo.ITodoAICreateListTaskController;
import Interface.View.Todo.ITodoAICreateListTaskView;
import View.JPanelViewBase;
import View.Dialog.CommonDialogView;
import View.Dialog.CommonDialogView.CommonDialogType;

import java.awt.event.ActionEvent;

import javax.swing.event.EventListenerList;

import Entity.AIListTask;
import Entity.Enum.LogLevel;
import View.Todo.AICreateLIstTask.TodoAICreateListTaskContentPanel;
import View.Todo.Board.TodoSideBoardPanel;
import View.Todo.Listener.TodoAICreateListTaskContentPanelListener;
import View.Todo.Listener.TodoListViewCommonListener;
import View.Todo.Listener.TodoSideBoardPanelListener;



public class TodoAICreateListTaskView extends JPanelViewBase implements ITodoAICreateListTaskView, TodoSideBoardPanelListener, TodoAICreateListTaskContentPanelListener
{
    // 共通ダイアログのインスタンス
    private CommonDialogView CommonDialogView;

    // LeftJPanel のインスタンス
    private TodoSideBoardPanel TodoSideBoardPanel;

    // RightJPanel のインスタンス
    private TodoAICreateListTaskContentPanel TodoAICreateListTaskContentPanel;

    // コントローラインスタンス
    private ITodoAICreateListTaskController Controller;

    // イベントリスナインスタンス
    protected EventListenerList ListenerList;

    /**
     * コンストラクタ
     */
    public TodoAICreateListTaskView(CommonDialogView commonDialogView)
    {
        this.setLayout(null);
        // ダイアログ
        this.CommonDialogView = commonDialogView;
        // 左画面作成
        this.TodoSideBoardPanel = new TodoSideBoardPanel();
        this.TodoSideBoardPanel.setBounds(0, 0, 230, 600);
        // 右画面作成
        this.TodoAICreateListTaskContentPanel = new TodoAICreateListTaskContentPanel();
        this.TodoAICreateListTaskContentPanel.setBounds(230,0,769,600);

        this.ListenerList = new EventListenerList();

        // 左要素追加
        this.add(this.TodoSideBoardPanel);
        // 右要素追加
        this.add(this.TodoAICreateListTaskContentPanel);
    }

    /**
     * 表示メソッド
     */
    public void Show()
    {
        this.TodoSideBoardPanel.Show();
        this.TodoAICreateListTaskContentPanel.Show();
        this.TodoSideBoardPanel.AddListener(this);
        this.TodoAICreateListTaskContentPanel.AddListener(this);
        this.TodoAICreateListTaskContentPanel.MainAddListener(this);

    }

    /**
     * 非表示メソッド
     */
    public void Hide()
    {
        // イベントの停止処理
        this.TodoSideBoardPanel.Hide();
        this.TodoAICreateListTaskContentPanel.Hide();
        this.TodoSideBoardPanel.RemoveListener(this);
        this.TodoAICreateListTaskContentPanel.RemoveListener(this);
        this.TodoAICreateListTaskContentPanel.MainRemoveListener(this);
    }

    /**
     * ボタンからのアクションリスナー
     */
    public void actionPerformed(ActionEvent e)
    {
        switch (e.getActionCommand())
        {
            default:
                break;
        }
    }

    /**
     * ユーザ名を設定する
     * @param userName ユーザー名
     */
    public void SetUserName(String userName)
    {
        //TODO:サイドパネルを用意した上で実装予定
        this.TodoSideBoardPanel.SetUserName(userName);
    }

    /**
     * コントローラインスタンスを設定
     * @param controller コントローラインスタンス
     */
    public void SetController(ITodoAICreateListTaskController controller)
    {
        this.Controller = controller;
    }

    /**
     * AIリスト・タスク案作成
     * @param userInput ユーザー入力
     */
    public void Ask(String userInput)
    {
        this.Controller.Ask(userInput);
    }

    /**
     * AIリスト・タスク案再生成
     * @param userInput ユーザー入力
     * @param addUserInput 追加ユーザー入力
     */
    public void ReAsk(String userInput, String addUserInput)
    {
        this.Controller.ReAsk(userInput, addUserInput);
    }

    /**
     * ボードボタンクリック時の処理
     */
    public void BoardButtonClicked() {
        for (TodoListViewCommonListener listener : this.ListenerList.getListeners(TodoListViewCommonListener.class)) {
        // ボードボタン押下を通知
        listener.BoardButtonClicked();
        }
    }

    /**
     * リストボタンクリック時の処理
     */
    public void ListButtonClicked() {
        for (TodoListViewCommonListener listener : this.ListenerList.getListeners(TodoListViewCommonListener.class)) {
        // リストボタン押下を通知
        listener.ListButtonClicked();
        }
    }

    /**
     * ガントチャートボタンクリック時の処理
     */
    public void GanttchartButtonClicked() {
        for (TodoListViewCommonListener listener : this.ListenerList.getListeners(TodoListViewCommonListener.class)) {
        // ガントチャートボタン押下を通知
        listener.GanttchartButtonClicked();
        }
    }

    /**
     * AIリスト・タスク案作成画面ボタンクリック時の処理
     */
    public void AICreateListTaskButtonClicked() {
        for (TodoListViewCommonListener listener : this.ListenerList.getListeners(TodoListViewCommonListener.class)) {
        // AIリスト・タスク案作成ボタン押下を通知
        listener.AICreateListTaskButtonClicked();
        }
    }

    /**
     * リスナ対象追加
     * @param listener 追加対象リスナインスタンス
     */
    public void AddListener(TodoListViewCommonListener listener)
    {
        this.ListenerList.add(TodoListViewCommonListener.class, listener);
    }

    /**
     * リスト対象削除
     * @param listener 削除対象リスナインスタンス
     */
    public void RemoveListener(TodoListViewCommonListener listener)
    {
        this.ListenerList.remove(TodoListViewCommonListener.class, listener);
    }

    /**
     * AI作成のリスト・タスク案を画面に反映
     * @param aIListTask AIが作成したリスト・タスク案
     */
    public void SetAIListTask(AIListTask aIListTask)
    {
        this.TodoAICreateListTaskContentPanel.SetAIListTask(aIListTask);
    }

    /**
     * 保持しているタスクリストの修正
     * @param userDeleteTaskId ユーザが削除したタスクID
     */
    public void EditAITaskList(int userDeleteTaskId)
    {
        this.Controller.EditAITaskList(userDeleteTaskId);
    }

    /**
     * リスト名編集
     * @param userEditListName ユーザが編集したリスト名
     */
    public void EditAIList(String userEditListName)
    {
        this.Controller.EditAIList(userEditListName);
    }

    /**
     * リストとタスクの登録メソッド
     */
    public void AddListTask()
    {
        this.Controller.AddListTask();
    }

    /**
     * 登録成功ダイアログ表示
     */
    public void ListTaskCreateSuccessDialog()
    {
        this.CommonDialogView.Show(CommonDialogType.ListTaskCreateSuccessDialog, true);
    }

    /**
     * 登録失敗ダイアログ表示
     */
    public void ListTaskCreateFailDialog()
    {
        this.CommonDialogView.Show(CommonDialogType.ListTaskCreateFailDialog, true);
    }

    /**
     * 生成失敗ダイアログ表示
     */
    public void AskFailDialog()
    {
        this.CommonDialogView.Show(CommonDialogType.AskFailDialog, true);
    }

    /**
     * 画面の初期化（登録完了後）
     */
    public void Clear()
    {
        this.TodoAICreateListTaskContentPanel.Clear();
    }

    /**
     * 左側パネルの要素の活性・非活性処理(ControllerのisBusyを使う)
     * @param isBusy
     */
    public void SideElementDisabled(boolean isBusy)
    {
        this.TodoSideBoardPanel.ElementDisabled(isBusy);
        this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "画面要素押下可否設定" + "(" + String.valueOf(isBusy) + ")");});
    }


    /**
     * 右側パネルの要素の活性・非活性処理(ControllerのisBusyを使う)
     * @param isBusy 処理中フラグ
     */
    public void LeftElementDisabled(boolean isBusy)
    {
        this.TodoAICreateListTaskContentPanel.ElementDisabled(isBusy);
        this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "画面要素押下可否設定" + "(" + String.valueOf(isBusy) + ")");});
    }
}
