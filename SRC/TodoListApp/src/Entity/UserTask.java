package Entity;

public class UserTask {
    // タスクID
    public int id;
    // タスク内容
    public String taskText;
    // 開始年月日
    public String startDate;
    // 終了年月日
    public String endDate;
    // タスクの状態
    public int taskStatus;
    // リストID
    public int list_id;
    // 進捗率
    public int progressRate;

    /**
     * コンストラクタ
     */
    public UserTask()
    {
        this.id = 0;
        this.taskText = "";
        this.startDate = "";
        this.endDate = "";
        this.taskStatus = 0;
        this.list_id = 0;
    }

    /**
     * インスタンス複製メソッド
     * @return 複製したインスタンス
     */
    public UserTask Clone()
    {
        var ret = new UserTask();
        ret.id = this.id;
        ret.taskText = this.taskText;
        ret.startDate = this.startDate;
        ret.endDate = this.endDate;
        ret.taskStatus = this.taskStatus;
        return ret;
    }
}
