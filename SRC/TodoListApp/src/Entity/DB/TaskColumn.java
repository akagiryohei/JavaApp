package Entity.DB;
import java.sql.Date;

public class TaskColumn {
    // タスクID
    public int id;
    // タスク内容
    public String task_text;
    // 開始年月日
    public Date start_date;
    // 終了年月日
    public Date end_date;
    // タスクの状態
    public int task_status;
    // リストID
    public int list_id;
    // リスト名（副問い合わせにてリストテーブルから取得する）
    public String list_name;
    // 進捗率
    public int progress_rate;

    public TaskColumn()
    {
        this.id = 0;
        this.task_text = "";
        this.start_date = new Date(0);
        this.end_date = new Date(0);
        this.task_status = 0;
        this.list_id = 0;
    }
}
