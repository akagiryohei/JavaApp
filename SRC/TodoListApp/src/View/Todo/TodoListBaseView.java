package View.Todo;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import Controller.Todo.*;
import DI.Todo.*;
import View.JPanelViewBase;
import View.MainWindowView;

/*
 * ログイン後画面基底viewクラス
 */
public class TodoListBaseView extends JPanelViewBase
{
    // Todoリスト（リスト型表示）のインスタンス
    private JPanel TodoListView;

    // Todoリスト（Board型表示）のインスタンス
    private JPanel TodoBoradView;

    // Todoリスト（ガントチャート）のインスタンス
    private JPanel GanttchartView;

    // CardLayoutオブジェクト
    private CardLayout layout;

    // 親要素のインスタンス
    private MainWindowView MainWindowViewInstance;

    // TodoListBaseDIのインスタンス
    private TodoListBaseDI TodoListBaseDIInstance;
    
    // TodoListBaseDIのインスタンス
    private TodoListDI TodoListDIInstance;
    
    // TodoListBaseDIのインスタンス
    private TodoGanttchartDI TodoGanttchartDIInstance;

    // TodoListBaseDIのインスタンス
    private TodoBoardDI TodoBoardDIInstance;


    // Todoリスト（リスト型表示）画面のコントローラインスタンス
    private TodoListController TodoListControllerInstance;

    // Todoリスト（Board型表示）画面のコントローラインスタンス
    private TodoBoardController TodoBoardControllerInstance;

    // Todoリスト（ガントチャート）画面のコントローラインスタンス
    private TodoGanttchartController TodoGanttchartControllerInstance;

    // 画面別表示種別
    public enum ViewType
    {
        // Todoリスト（リスト型表示）
        TodoListView,

        // Todoリスト（ボード型表示）
        TodoBoardView,

        // Todoリスト（ガントチャート）
        TodoGanttchartView,
        
    };

    // コンストラクタ
    /**
     * 
     * @param todoListBaseDI 
     * @param mainWindowViewInstance
     */
    public TodoListBaseView(TodoListBaseDI todoListBaseDI, MainWindowView mainWindowViewInstance)
    {
        this.TodoListBaseDIInstance = todoListBaseDI;
        this.MainWindowViewInstance = mainWindowViewInstance;
        // ここのコンストラクタでボード表示用のinstanceも受け取る必要がある
        
        // 画面遷移にあたってCardLayoutを設定
        this.layout = new CardLayout();
        this.setLayout(this.layout);
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

    // CardLayoutで表示するViewを変更する
    public void ChangeView(ViewType type)
    {
        switch (type)
        {
            case ViewType.TodoListView:
                if (this.TodoListView == null)
                {
                    this.TodoListControllerInstance = this.TodoListBaseDIInstance.CreateTodoListMVC(this);
                    this.TodoListView = this.TodoListControllerInstance.GetViewInstance();
                    this.add(this.TodoListView, ViewType.TodoListView.name());
                }
                // 一番前に対象画面を出す（カードだから）
                this.layout.show(this, ViewType.TodoListView.name());
                this.TodoListControllerInstance.Show();
                // 後ほど実装
                // this.TodoBoardControllerInstance.Hide();
                // this.TodoGanttchartControllerInstance.Hide();
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
                    this.add(this.TodoBoradView, ViewType.TodoBoardView.name());
                }
                // 一番前に対象画面を出す（カードだから）
                this.layout.show(this, ViewType.TodoBoardView.name());
                this.TodoBoardControllerInstance.Show();
                // 後ほど実装
                // this.TodoListControllerInstance.Hide();
                // this.TodoGanttchartControllerInstance.Hide();
                break;
            // case ViewType.TodoGanttchartView:
            //     // 時間があれば実装
            //     break;
        }
    }
}
