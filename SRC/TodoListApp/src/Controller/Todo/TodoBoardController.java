package Controller.Todo;

import Controller.ControllerBase;
import Entity.Enum.ViewStateEnum;
import Model.Todo.TodoBoardModel;
import View.Todo.TodoBoardView;

/*
 * Todoリスト（ボード型表示）コントローラ
 */
public class TodoBoardController extends ControllerBase
{
    // 画面表示状態
    private ViewStateEnum ViewState;

    // Viewインスタンス
    private TodoBoardView View;

    // Modelインスタンス
    private TodoBoardModel Model;

    /**
     * コンストラクタ
     */
    public TodoBoardController(TodoBoardView view, TodoBoardModel model)
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
    public TodoBoardView GetViewInstance()
    {
        return this.View;
    }
}
