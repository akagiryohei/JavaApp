package Controller.Todo;

import Controller.ControllerBase;
import Entity.Enum.ViewStateEnum;
import Model.Todo.TodoListBaseModel;
import View.Todo.TodoListBaseView;

/*
 * Todoリスト全般対応コントローラ
 */
public class TodoListBaseController extends ControllerBase
{
    // 画面表示状態
    private ViewStateEnum ViewState;

    // Viewインスタンス
    private TodoListBaseView View;

    // Modelインスタンス
    private TodoListBaseModel Model;

    // コンストラクタ
    public TodoListBaseController(TodoListBaseView view, TodoListBaseModel model)
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

    public TodoListBaseView GetViewInstance()
    {
        return this.View;
    }

}
