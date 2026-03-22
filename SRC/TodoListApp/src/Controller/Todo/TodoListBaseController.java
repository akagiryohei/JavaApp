package Controller.Todo;

import Controller.ControllerBase;
import Entity.Enum.ViewStateEnum;
import Interface.Controller.Todo.ITodoListBaseController;
import Interface.View.IViewProxyUtil;
import Interface.View.Todo.ITodoListBaseView;
import View.ViewProxyUtil;

/*
 * Todoリスト全般対応コントローラ
 */
public class TodoListBaseController extends ControllerBase implements ITodoListBaseController
{
    // 画面表示状態
    private ViewStateEnum ViewState;

    // Viewインスタンス
    private ITodoListBaseView View;

    // 画面ラップ処理インスタンス
    private IViewProxyUtil ViewProxyUtil;

    // コンストラクタ
    public TodoListBaseController(ITodoListBaseView view, IViewProxyUtil viewProxyUtil)
    {
        this.ViewProxyUtil = viewProxyUtil;
        this.View = this.ViewProxyUtil.WrapView(ITodoListBaseView.class, view);

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

    public ITodoListBaseView GetViewInstance()
    {
        return this.ViewProxyUtil.UnwrapView(this.View);
    }

}
