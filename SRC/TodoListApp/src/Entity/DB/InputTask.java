package Entity.DB;

import java.sql.Date;
import java.util.List;

/**
 * タスク登録データ型クラス
 */
public class InputTask
{
    // タスク内容(単体用)
    public String TaskText;

    // タスク内容(複数用)
    public List<String> TaskTexts;

    // 開始年月日
    public Date  StartDate;

    // 終了年月日
    public Date  EndDate;

    // ユーザーID
    public int UserId;

    // リストID
    public int ListId;

    // タスクID
    public int TaskId;

    // タスクの状態
    public Boolean TaskStatus;

    public InputTask()
    {
        this.TaskText = "";
        this.TaskTexts = null;
        this.StartDate = new Date(0);
        this.EndDate = new Date(0);
        this.UserId = 0;
        this.ListId = 0;
        this.TaskId = 0;
        this.TaskStatus = false;
    }
}
