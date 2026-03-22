package Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * LM studioからの回答代入用クラス
 */
public class AIListTask {
    // リスト名
    public String listName;
    // タスク名
    public List<String> tasks;
    
    /**
     * コンストラクタ
     */
    public AIListTask()
    {
        this.listName = "";
        this.tasks = new ArrayList<String>();
    }

    /**
     * インスタンス複製メソッド
     * @return 複製したインスタンス
     */
    public AIListTask Clone()
    {
        var ret = new AIListTask();
        ret.listName = this.listName;
        for (String task : this.tasks) {
            ret.tasks.add(task);
        }
        return ret;
    }
}
