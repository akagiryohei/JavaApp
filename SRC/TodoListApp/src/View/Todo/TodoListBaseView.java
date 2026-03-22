package View.Todo;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import Controller.ControllerBase;
import Interface.Controller.Todo.ITodoAICreateListTaskController;
import Interface.Controller.Todo.ITodoBoardController;
import Interface.Controller.Todo.ITodoGanttchartController;
import Interface.Controller.Todo.ITodoListController;
import Interface.DI.Todo.ITodoListBaseDI;
import Interface.View.IMainWindowView;
import Interface.View.Todo.ITodoAICreateListTaskView;
import Interface.View.Todo.ITodoBoardView;
import Interface.View.Todo.ITodoGanttchartView;
import Interface.View.Todo.ITodoListBaseView;
import Interface.View.Todo.ITodoListView;
import View.JPanelViewBase;
import View.Todo.Listener.TodoListViewCommonListener;

/*
 * ログイン後画面基底viewクラス
 */
public class TodoListBaseView extends JPanelViewBase implements ITodoListBaseView, TodoListViewCommonListener
{
    // Todoリスト（リスト型表示）のインスタンス
    private ITodoListView TodoListView;

    // Todoリスト（Board型表示）のインスタンス
    private ITodoBoardView TodoBoradView;

    // Todoリスト（ガントチャート）のインスタンス
    private ITodoGanttchartView TodoGanttchartView;

    // Todoリスト（AI作成リスト・タスク案型表示）のインスタンス
    private ITodoAICreateListTaskView TodoAICreateListTaskView;

    // CardLayoutオブジェクト
    private CardLayout Layout;

    // 親要素のインスタンス
    private IMainWindowView MainWindowViewInstance;

    // TodoListBaseDIのインスタンス
    private ITodoListBaseDI TodoListBaseDIInstance;

    // Todoリスト（リスト型表示）画面のコントローラインスタンス
    private ITodoListController TodoListControllerInstance;

    // Todoリスト（Board型表示）画面のコントローラインスタンス
    private ITodoBoardController TodoBoardControllerInstance;

    // Todoリスト（ガントチャート）画面のコントローラインスタンス
    private ITodoGanttchartController TodoGanttchartControllerInstance;

    // Todoリスト（AI作成リスト・タスク案型表示）画面のコントローラインスタンス
    private ITodoAICreateListTaskController TodoAICreateListTaskControllerInstance;

    // 現在表示中のビュータイプ
    private ViewType CurrentViewType;

    // 画面別表示種別
    public enum ViewType
    {
        // Todoリスト（リスト型表示）
        TodoListView,

        // Todoリスト（ボード型表示）
        TodoBoardView,

        // Todoリスト（ガントチャート）
        TodoGanttchartView,

        // Todoリスト（AI作成リスト・タスク案型表示）
        TodoAICreateListTaskView,

        // なし
        None;
    };

    // コンストラクタ
    /**
     * @param todoListBaseDI 
     * @param mainWindowViewInstance
     */
    public TodoListBaseView(ITodoListBaseDI todoListBaseDI, IMainWindowView mainWindowViewInstance)
    {
        this.TodoListBaseDIInstance = todoListBaseDI;
        this.MainWindowViewInstance = mainWindowViewInstance;
        // ここのコンストラクタでボード表示用のinstanceも受け取る必要がある
        
        // 画面遷移にあたってCardLayoutを設定
        this.Layout = new CardLayout();
        this.setLayout(this.Layout);
        this.CurrentViewType = ViewType.None;
    }

    public void Show()
    {
        this.ChangeView(ViewType.TodoListView);
        this.TodoListControllerInstance.Show();
    }

    public void Hide()
    {
    // TODO : ログイン後画面実装時にログイン画面に対してクローズを指示する
    }

    /**
     * ボードボタンクリック時の処理
     */
    public void BoardButtonClicked()
    {
        if (this.CurrentViewType != ViewType.TodoBoardView)
        {
            this.ChangeView(ViewType.TodoBoardView);
        }
    }

    /**
     * リストボタンクリック時の処理　現在リスト画面であるかを確認する必要がある
     */
    public void ListButtonClicked()
    {
        if (this.CurrentViewType != ViewType.TodoListView)
        {
            this.ChangeView(ViewType.TodoListView);
        }
    }

    /**
     * ガントチャートボタンクリック時の処理
     */
    public void GanttchartButtonClicked()
    {
        if (this.CurrentViewType != ViewType.TodoGanttchartView)
        {
            this.ChangeView(ViewType.TodoGanttchartView);
        }
    }

    /**
     * AIリスト・タスク案作成ボタンクリック時の処理
     */
    public void AICreateListTaskButtonClicked()
    {
        if (this.CurrentViewType != ViewType.TodoAICreateListTaskView)
        {
            this.ChangeView(ViewType.TodoAICreateListTaskView);
        }
    }
    
    /**
     * View切り替えメソッド
     * @param type ViewType
     */
    private void ChangeView(ViewType type)
    {
        ControllerBase closeViewController = null;

        switch (this.CurrentViewType) {
            case ViewType.TodoListView:
                this.TodoListView.RemoveListener(this);
                closeViewController = TodoListControllerInstance != null ? (ControllerBase)TodoListControllerInstance : null;
                break;
            case ViewType.TodoBoardView:
                this.TodoBoradView.RemoveListener(this);
                closeViewController = TodoBoardControllerInstance != null ? (ControllerBase)TodoBoardControllerInstance : null;
                break;
            case ViewType.TodoGanttchartView:
                this.TodoGanttchartView.RemoveListener(this);
                closeViewController = TodoGanttchartControllerInstance != null ? (ControllerBase)TodoGanttchartControllerInstance : null;
                break;
            case ViewType.TodoAICreateListTaskView:
                this.TodoAICreateListTaskView.RemoveListener(this);
                closeViewController = TodoAICreateListTaskControllerInstance != null ? (ControllerBase)TodoAICreateListTaskControllerInstance : null;
                break;
        }
        if(closeViewController != null)
        {
            closeViewController.Hide();
        }

        this.CurrentViewType = type;

        switch (type)
        {
            case ViewType.TodoListView:
                if (this.TodoListView == null)
                {
                    this.TodoListControllerInstance = this.TodoListBaseDIInstance.CreateTodoListMVC(this);
                    this.TodoListView = this.TodoListControllerInstance.GetViewInstance();
                    this.add((JPanel)this.TodoListView, ViewType.TodoListView.name());
                }
                // 一番前に対象画面を出す（カードだから）
                this.Layout.show(this, ViewType.TodoListView.name());
                this.TodoListView.AddListener(this);
                this.TodoListControllerInstance.Show();
                break;

            // ボード型表示への遷移をするための物
            // Hideの処理をリスト型表示の方にも作る必要がある
            case ViewType.TodoBoardView:
                if (this.TodoBoradView == null) // 初回であるかを確認してる。
                {
                    // cotrollerの取得
                    this.TodoBoardControllerInstance = this.TodoListBaseDIInstance.CreateTodoBoardMVC(this);
                    // viewの取得
                    this.TodoBoradView = this.TodoBoardControllerInstance.GetViewInstance();
                    // ViewとviewTypeを送る
                    this.add((JPanel)this.TodoBoradView, ViewType.TodoBoardView.name());
                }
                // 一番前に対象画面を出す（カードだから）
                this.Layout.show(this, ViewType.TodoBoardView.name());
                this.TodoBoradView.AddListener(this);
                this.TodoBoardControllerInstance.Show();
                break;

            // ボード型表示への遷移をするための物
            // Hideの処理をリスト型表示の方にも作る必要がある
            case ViewType.TodoGanttchartView:
                if (this.TodoGanttchartView == null) // 初回であるかを確認してる。
                {
                    // cotrollerの取得
                    this.TodoGanttchartControllerInstance = this.TodoListBaseDIInstance.CreateGanttchartMVC(this);
                    // viewの取得
                    this.TodoGanttchartView = this.TodoGanttchartControllerInstance.GetViewInstance();
                    // ViewとviewTypeを送る
                    this.add((JPanel)this.TodoGanttchartView, ViewType.TodoGanttchartView.name());
                }
                // 一番前に対象画面を出す（カードだから）
                this.Layout.show(this, ViewType.TodoGanttchartView.name());
                this.TodoGanttchartView.AddListener(this);
                this.TodoGanttchartControllerInstance.Show();
                break;

            // AI作成リスト・タスク案型表示への遷移をするための物
            // Hideの処理をリスト型表示の方にも作る必要がある
            case ViewType.TodoAICreateListTaskView:
                if (this.TodoAICreateListTaskView == null) // 初回であるかを確認している。
                {
                    // controllerの取得
                    this.TodoAICreateListTaskControllerInstance = this.TodoListBaseDIInstance.CreateAIListTaskMVC(this);
                    // viewの取得
                    this.TodoAICreateListTaskView = this.TodoAICreateListTaskControllerInstance.GetViewInstance();
                    // ViewとviewTypeを送る
                    this.add((JPanel)this.TodoAICreateListTaskView, ViewType.TodoAICreateListTaskView.name());
                }
                // 一番前に対象画面を出す（カードだから）
                this.Layout.show(this, ViewType.TodoAICreateListTaskView.name());
                this.TodoAICreateListTaskView.AddListener(this);
                this.TodoAICreateListTaskControllerInstance.Show();
                break;
        }
    }
}