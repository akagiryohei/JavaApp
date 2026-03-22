package Interface.View.Todo;

import Interface.Controller.Todo.ITodoAICreateListTaskController;
import Interface.Model.ILogger;
import View.Todo.Listener.TodoListViewCommonListener;
import Entity.AIListTask;

/**
 * Todoリスト（AI作成リスト・タスク案型表示）画面Viewインターフェース
 */
public interface ITodoAICreateListTaskView
{
    /**
     * ロガーインスタンスを依存性注入する
     * @param ロガーインスタンス
     */
    public void SetLogger(ILogger logger);

    /**
     * 表示メソッド
     */
    public void Show();

    /**
     * 非表示メソッド
     */
    public void Hide();

    /**
     * ユーザー名を設定する
     * @param userName ユーザー名
     */
    public void SetUserName(String userName);

    /**
     * コントローラインスタンスを設定
     * @param controller コントローラインスタンス
     */
    public void SetController(ITodoAICreateListTaskController controller);

    /**
     * リスト・タスクの生成案を表示する
     * @param aIListTask リスト・タスク案
     */
    public void SetAIListTask(AIListTask aIListTask);

    /**
     * AIリスト・タスク案作成
     * @param userInput ユーザー入力
     */
    public void Ask(String userInput);

    /**
     * AIリスト・タスク案再生成
     * @param userInput ユーザー入力
     * @param addUserInput 追加ユーザー入力
     */
    public void ReAsk(String userInput, String addUserInput);

    /**
     * リスナ対象追加
     * @param listener 追加対象リスナインスタンス
     */
    public void AddListener(TodoListViewCommonListener listener);


    /**
     * リスナ対象削除
     * @param listener 削除対象リスナインスタンス
     */
    public void RemoveListener(TodoListViewCommonListener listener);

    /**
     * 保持しているタスクリストの修正
     * @param userDeleteTaskId ユーザが削除したタスクID
     */
    public void EditAITaskList(int userDeleteTaskId);

    /**
     * リスト名編集
     * @param userEditListName ユーザが編集したリスト名
     */
    public void EditAIList(String userEditListName);

    /**
     * リストとタスクの登録メソッド
     */
    public void AddListTask();

    /**
     * 登録成功ダイアログ表示
     */
    public void ListTaskCreateSuccessDialog();

    /**
     * 登録失敗ダイアログ表示
     */
    public void ListTaskCreateFailDialog();

    /**
     * 生成失敗ダイアログ表示
     */
    public void AskFailDialog();

    /**
     * 画面の初期化（登録完了後）
     */
    public void Clear();

    /**
     * 左側パネルの要素の活性・非活性処理(ControllerのisBusyを使う)
     * @param isBusy
     */
    public void SideElementDisabled(boolean isBusy);

    /**
     * 右側パネルの要素の活性・非活性処理(ControllerのisBusyを使う)
     * @param isBusy 処理中フラグ
     */
    public void LeftElementDisabled(boolean isBusy);



}
