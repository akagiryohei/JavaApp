package Entity;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class UserList {
    // リストID
    public int id;
    // リスト名
    public String listName;
    
    // タスクID
    public int taskId;

    // タスク名
    public List<UserTask> tasks;

    /**
     * コンストラクタ
     */
    public UserList()
    {
        this.id = 0;
        this.listName = "";
        this.taskId = 0;
        this.tasks = new ArrayList<UserTask>();
    }

    /**
     * インスタンス複製メソッド
     * @return 複製したインスタンス
     */
    public UserList Clone()
    {
        var ret = new UserList();
        ret.id = this.id;
        ret.listName = this.listName;
        ret.taskId = this.taskId;
        for (UserTask task : this.tasks) {
            ret.tasks.add(task.Clone());
        }
        return ret;
    }
}
