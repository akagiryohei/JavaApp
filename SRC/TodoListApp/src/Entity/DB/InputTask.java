package Entity.DB;

import java.sql.Date;

/**
 * タスク登録データ型クラス
 */
public class InputTask
{
    // タスク内容
    public String TaskText;

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
    public int TaskStatus;

    public InputTask()
    {
        this.TaskText = "";
        this.StartDate = new Date(0);
        this.EndDate = new Date(0);
        this.UserId = 0;
        this.ListId = 0;
        this.TaskId = 0;
        this.TaskStatus = 0;
    }
}
