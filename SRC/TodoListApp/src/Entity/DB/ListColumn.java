package Entity.DB;

import java.util.ArrayList;
import java.util.List;

public class ListColumn {
    // リストID
    public int id;
    // リスト名
    public String list_name;

    public List<TaskColumn> taskList;

    public ListColumn()
    {
        this.id = 0;
        this.list_name = "";
        this.taskList = new ArrayList<TaskColumn>();
    }
}
