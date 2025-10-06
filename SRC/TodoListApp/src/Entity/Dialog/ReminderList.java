package Entity.Dialog;

import java.util.Date;

/**
 * ダイアログ定数クラス
 */
public class ReminderList {

    // リマインダー表示内容
    public String ListName;
    public String TaskContent;
    public Date EndDate;

    public ReminderList()
    {
        this.ListName = "";
        this.TaskContent = "";
        this.EndDate = new Date();
    }
}