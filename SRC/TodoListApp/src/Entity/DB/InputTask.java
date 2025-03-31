package Entity.DB;

/**
 * タスク登録データ型クラス
 */
public class InputTask 
{
    // タスク内容
    public String TaskText;

    // 開始年月日
    public String  StartDate;

    // 終了年月日
    public String  EndDate;

    // ユーザーID
    public int UserId;

    // リストID
    public int ListId;

    // タスクID
    public int TaskId;

    public InputTask()
    {
        this.TaskText = "";
        this.StartDate = "";
        this.EndDate = "";
        this.UserId = 0;
        this.ListId = 0;
        this.TaskId = 0;
    }
}
