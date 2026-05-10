package Interface.DI.Todo;

import Interface.Controller.Todo.ITodoAICreateListTaskController;
import Interface.Controller.Todo.ITodoBoardController;
import Interface.Controller.Todo.ITodoGanttchartController;
import Interface.Controller.Todo.ITodoListController;
import Interface.View.Todo.ITodoListBaseView;

/*
 * ログイン後画面の依存性注入クラス
 */
public interface ITodoListBaseDI
{
    /*
     * Todoリスト（リスト型表示）
     * @return 依存性注入したTodoListControllerオブジェクト
     */
    public ITodoListController CreateTodoListMVC(ITodoListBaseView todoListBaseView);

    /*
     * Todoリスト（ボード型表示）
     * @return 依存性注入したTodoBoardControllerオブジェクト
     */
    public ITodoBoardController CreateTodoBoardMVC(ITodoListBaseView todoListBaseView);

    /*
     * Todoリスト（ガントチャート型表示）
     * @return 依存性注入したTodoBoardControllerオブジェクト
     */
    public ITodoGanttchartController CreateGanttchartMVC(ITodoListBaseView todoListBaseView);

    /**
     * Todoリスト（AI作成リスト・タスク案型表示）
     * @return 依存性注入したTodoAICreateListTaskControllerオブジェクト
     */
    public ITodoAICreateListTaskController CreateAIListTaskMVC(ITodoListBaseView todoListBaseView);

}
