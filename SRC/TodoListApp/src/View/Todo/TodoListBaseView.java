package View.Todo;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import Controller.ControllerBase;
import Interface.Controller.Todo.ITodoBoardController;
import Interface.Controller.Todo.ITodoGanttchartController;
import Interface.Controller.Todo.ITodoListController;
import Interface.DI.Todo.ITodoBoardDI;
import Interface.DI.Todo.ITodoGanttchartDI;
import Interface.DI.Todo.ITodoListBaseDI;
import Interface.DI.Todo.ITodoListDI;
import Interface.View.IMainWindowView;
import Interface.View.Todo.ITodoBoardView;
import Interface.View.Todo.ITodoGanttchartView;
import Interface.View.Todo.ITodoListBaseView;
import Interface.View.Todo.ITodoListView;
import View.JPanelViewBase;

/*
 * ログイン後画面基底viewクラス
 */
public class TodoListBaseView extends JPanelViewBase implements ITodoListBaseView
{
    // Todoリスト（リスト型表示）のインスタンス
    private ITodoListView TodoListView;

    // Todoリスト（Board型表示）のインスタンス
    private ITodoBoardView TodoBoradView;

    // Todoリスト（ガントチャート）のインスタンス
    private ITodoGanttchartView TodoGanttchartView;

    // CardLayoutオブジェクト
    private CardLayout Layout;

    // 親要素のインスタンス
    private IMainWindowView MainWindowViewInstance;

    // TodoListBaseDIのインスタンス
    private ITodoListBaseDI TodoListBaseDIInstance;
    
    // TodoListBaseDIのインスタンス
    private ITodoListDI TodoListDIInstance;
    
    // TodoListBaseDIのインスタンス
    private ITodoGanttchartDI TodoGanttchartDIInstance;

    // TodoListBaseDIのインスタンス
    private ITodoBoardDI TodoBoardDIInstance;

    // Todoリスト（リスト型表示）画面のコントローラインスタンス
    private ITodoListController TodoListControllerInstance;

    // Todoリスト（Board型表示）画面のコントローラインスタンス
    private ITodoBoardController TodoBoardControllerInstance;

    // Todoリスト（ガントチャート）画面のコントローラインスタンス
    private ITodoGanttchartController TodoGanttchartControllerInstance;

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
     * View切り替えメソッド
     * @param type ViewType
     */
    public void ChangeView(ViewType type)
    {
        ControllerBase closeViewController = null;

        switch (this.CurrentViewType) {
            case ViewType.TodoListView:
                closeViewController = TodoListControllerInstance != null ? (ControllerBase)TodoListControllerInstance : null;
                break;
            case ViewType.TodoBoardView:
                closeViewController = TodoBoardControllerInstance != null ? (ControllerBase)TodoBoardControllerInstance : null;
                break;
            case ViewType.TodoGanttchartView:
                closeViewController = TodoGanttchartControllerInstance != null ? (ControllerBase)TodoGanttchartControllerInstance : null;
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
                this.TodoGanttchartControllerInstance.Show();
                break;
        }
    }
}