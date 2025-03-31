package Entity.DB;

public class TaskColumn {
    // タスクID
    public int id;
    // タスク内容
    public String task_text;
    // 開始年月日
    public String start_date;
    // 終了年月日
    public String end_date;
    // タスクの状態
    public int task_status;

    public TaskColumn()
    {
        this.id = 0;
        this.task_text = "";
        this.start_date = "";
        this.end_date = "";
        this.task_status = 0;
    }
}
