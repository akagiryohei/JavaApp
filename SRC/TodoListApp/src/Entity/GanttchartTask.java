package Entity;

import java.time.LocalDate;

/**
 * ガントチャートタスククラス
 */
public class GanttchartTask
{
    /*
     * リストID
     */
    public int ListID;
    
    /*
     * タスクID
     */
    public int TaskID;

    /*
     * タスク名
     */
    public String TaskName;

    /*
     * 開始日
     */
    public LocalDate StartDate;

    /*
     * 終了日
     */
    public LocalDate EndDate;

    /*
     * 進捗率
     */
    public int ProgressRate;

    /**
     * インスタンス複製
     * @returns 複製されたインスタンス
     */
    public GanttchartTask Clone()
    {
        var cloneInstance = new GanttchartTask();
        cloneInstance.ListID = this.ListID;
        cloneInstance.TaskID = this.TaskID;
        cloneInstance.TaskName = this.TaskName;
        cloneInstance.ProgressRate = this.ProgressRate;

        if (this.StartDate != null)
        {
            cloneInstance.StartDate = LocalDate.of(this.StartDate.getYear(), this.StartDate.getMonth(), this.StartDate.getDayOfMonth());
        }

        if (this.EndDate != null)
        {
            cloneInstance.EndDate = LocalDate.of(this.EndDate.getYear(), this.EndDate.getMonth(), this.EndDate.getDayOfMonth());
        }

        return cloneInstance;
    }
}
