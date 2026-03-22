package Model.Todo;

import Interface.Model.ILogger;
import Interface.Model.Process.Todo.ITodoProcess;
import Interface.Model.Process.Todo.ITodoProcess.ResultType;
import Interface.Model.Todo.ITodoGanttchartModel;

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
import Entity.Enum.LogLevel;
import Interface.Model.IValidationUtil;



public class TodoGanttchartModel extends TodoBaseModel implements ITodoGanttchartModel
{
    /** 日付フォーマット */
    private final DateTimeFormatter DateFormat = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
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
            Pair<ITodoProcess.ResultType, List<GanttchartTask>> resultPair = new Pair<>(result.Value1, this.PrepareGanttTasks(result.Value2, activeYearMonth));

            finished.accept(resultPair);
        });
    }

    /**
     * タスク加工用メソッド（共通処理）
     */
    public List<GanttchartTask> PrepareGanttTasks(List<UserTask> userTasks, YearMonth activeYearMonth)
    {
        // タスク編集メソッド等に使いまわしするために保存
        List<GanttchartTask> taskList = new ArrayList<GanttchartTask>();
        userTasks.forEach(item -> {
            GanttchartTask task = new GanttchartTask();
            String startDate = item.startDate;
            LocalDate localDateStart = null;
            LocalDate localDateEnd = null;
            if (startDate != null && !startDate.isEmpty()){
                try {
                    localDateStart = LocalDate.parse(startDate, this.DateFormat);
                } catch (DateTimeException e) {
                    // 変換に失敗した場合の処理
                    e.printStackTrace();
                    this.Logger.WriteLog(LogLevel.Exception, "開始日の変換に失敗した：" + startDate);
                }
            }
            String endDate = item.endDate;
            if (endDate != null && !endDate.isEmpty()){
                try {
                    localDateEnd = LocalDate.parse(endDate, this.DateFormat);
                } catch (DateTimeException e) {
                    // 変換に失敗した場合の処理
                    e.printStackTrace();
                    this.Logger.WriteLog(LogLevel.Exception, "終了日の変換に失敗した：" + endDate);
                }
            }
            // 選択月とタスクの期間が重なってるか or （タスクの開始日or終了日）がnullの場合は表示対象とする
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
        return taskList;
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
