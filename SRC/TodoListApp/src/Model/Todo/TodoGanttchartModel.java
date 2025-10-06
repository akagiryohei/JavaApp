package Model.Todo;

import Interface.Model.ILogger;
import Interface.Model.Process.Todo.ITodoProcess;
import Interface.Model.Process.Todo.ITodoProcess.ResultType;
import Interface.Model.Todo.ITodoGanttchartModel;

import java.text.DateFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import Entity.GanttchartTask;
import Entity.Pair;
import Entity.UserData;
import Entity.UserTask;
import Interface.Model.IValidationUtil;



public class TodoGanttchartModel extends TodoBaseModel implements ITodoGanttchartModel
{
    /** 選択中リストのタスク */
    private List<GanttchartTask> Task;

    /** 選択中リストID */
    private int ListId;

    /**
     * コンストラクタ
     * 依存性注入
     */
    public TodoGanttchartModel(ILogger logger, ITodoProcess todoProcess, UserData userData, IValidationUtil util)
    {
        this.Process = todoProcess;
        this.Logger = logger;
        this.UserData = userData;
        this.Util = util;
    }

    /**
     * タスク取得メソッド
     * @param listId 選択中ID
     * @param activeYearMonth 選択中年月
     */
    public void GetUserTask(int listId, YearMonth activeYearMonth, Consumer<Boolean> isBusyChanged, Consumer<Pair<ITodoProcess.ResultType, List<GanttchartTask>>> finished)
    {
        this.Process.GetUserTask(this.UserData.UserId, listId, (isBusy) ->
        {
            isBusyChanged.accept(isBusy);
        }, (result) -> {
            // タスク編集メソッド等に使いまわしするために保存
            this.ListId = listId;
            List<GanttchartTask> taskList = new ArrayList<GanttchartTask>();
            result.Value2.forEach(item -> {
                GanttchartTask task = new GanttchartTask();
                String startDate = item.startDate;
                LocalDate localDateStart = null;
                LocalDate localDateEnd = null;
                if (startDate != null && !startDate.isEmpty()){
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    try {
                        localDateStart = LocalDate.parse(startDate, formatter);
                    } catch (DateTimeException e) {
                        // 変換に失敗した場合の処理
                        e.printStackTrace();
                    }
                }
                String endDate = item.endDate;
                if (endDate != null && !endDate.isEmpty()){
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    try {
                        localDateEnd = LocalDate.parse(endDate, formatter);
                    } catch (DateTimeException e) {
                        // 変換に失敗した場合の処理
                        e.printStackTrace();
                    }
                }
                if (
                    (localDateStart == null || localDateEnd == null) || this.IsYearMonthWithinPeriod(activeYearMonth, localDateStart, localDateEnd))
                {
                    task.TaskID = item.id;
                    task.ListID = item.list_id;
                    task.TaskName = item.taskText;
                    task.StartDate = localDateStart;
                    task.EndDate = localDateEnd;
                    task.ProgressRate = item.progressRate;
                    taskList.add(task.Clone());
                }
            });
            Pair<ITodoProcess.ResultType, List<GanttchartTask>> resultPair = new Pair<>(result.Value1, taskList);

            this.Task = taskList;// これを直す予定todo
            finished.accept(resultPair);
        });
    }

    /**
     * タスク編集（タスク進捗度＋完了/未完了）
     * @param taskId 画面の選択中タスクID
     * @param progress 画面の選択中タスクの進捗率
     * @param isBusyChanged 処理中イベントコールバック
     * @param finished 処理完了コールバック
     */
    public void UpdateTask(int taskId, int progress, Consumer<Boolean> isBusyChanged, Consumer<ResultType> finished)
    {
        this.Process.UpdateTask(taskId, progress, this.UserData.UserId, (isBusy) -> {
            isBusyChanged.accept(isBusy);
        }, (result) -> {
            finished.accept(result);
        });
    }

    /**
     * 選択月とタスクの期間が重なってるか判定
     * @param activeYearMonth 選択月
     * @param startDate タスク開始日
     * @param endDate タスク終了日
     * @return true/false 重なってる場合true/重なってない場合false
     */
    private boolean IsYearMonthWithinPeriod(YearMonth activeYearMonth, LocalDate startDate, LocalDate endDate)
    {
        // 月初日をとってくる
        LocalDate firstDayOfMonth = activeYearMonth.atDay(1);
        // 月最終日取得
        LocalDate lastDayOfMonth = activeYearMonth.atEndOfMonth();

        // 対象月に期間が重なっていたら戻り値がTrue
        return !lastDayOfMonth.isBefore(startDate) && !firstDayOfMonth.isAfter(endDate);
    }
}
