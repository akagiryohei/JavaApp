package Controller.Todo;

import java.util.ArrayList;
import java.util.List;

import Controller.ControllerBase;
import Entity.UserList;
import Entity.Enum.ViewStateEnum;
import Model.Todo.TodoListModel;
import View.Todo.TodoListView;

/*
 * Todoリスト（リスト型表示）コントローラ
 */
public class TodoListController extends ControllerBase{

    /** 画面表示状態 */
    private ViewStateEnum ViewState;

    /** Viewインスタンス */
    private TodoListView View;

    /** Modelインスタンス */
    private TodoListModel Model;

    /**
     * コンストラクタ
     * 依存性注入
     * @param view 画面Viewインスタンス
     * @param model 画面Modelインスタンス
     */
    public TodoListController(TodoListView view, TodoListModel model)
    {
        this.View = view;
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
    public TodoListView GetViewInstance()
    {
        return this.View;
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
            System.out.println(isBusy);
        }, (result) -> {
            //View側で保持するメソッドを作る
            List<UserList> list = new ArrayList<UserList>();
            list = result.Value2;
            this.View.SetList(list);
            System.out.println(result.Value2);
        });
    }

    /**
     * ユーザリスト登録
     * @param isBusyChanged 処理中イベントコールバック
     * @param finished 処理完了コールバック
     */
    public void CreateUserList(String listText)
    {
        this.Model.CreateUserList((isBusy) -> {
            
        }, (result) -> {
        }, 
        listText);
    }
}
