package Model.Login;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import Entity.Pair;
import Entity.UserData;
import Entity.Dialog.ReminderList;
import Entity.Enum.LogLevel;
import Interface.Model.ILogger;
import Interface.Model.IValidationUtil;
import Interface.Model.Process.Login.ILoginProcess;
import Interface.Model.Process.Login.ILoginProcess.ResultType;

public abstract class LoginModelBase {

    /** 日付型のフォーマット */
    protected final SimpleDateFormat afterDateFormat = new SimpleDateFormat("yyyy年MM月dd日");

    /** パスワードのバイト数の定数（下限）*/
    protected final Integer MIN_PASSWORD_LENGTH = 7;

    /** パスワードのバイト数の定数（上限）*/
    protected final Integer MAX_PASSWORD_LENGTH = 13;

    /** ログイン処理インスタンス */
    protected ILoginProcess Process;

    /** ロガーインスタンス */
    protected ILogger Logger;

    /** validationインスタンス */
    protected IValidationUtil Util;

    /**
    * ログイン認証実施
    * @param userId ログイン対象アカウントのID
    * @param clickedDate ログインボタンを押下した時刻
    * @param isBusyChanged 処理中イベントコールバック
    * @param resultValue 処理完了コールバック
   */
    public void GetReminderList(String userId, Date clickedDate, Consumer<Boolean> isBusyChanged, Consumer<Pair<ILoginProcess.ResultType,List<ArrayList<String>>>> resultValue)
    {
        this.Process.GetReminderList(userId, clickedDate, (isBusy) -> {
        }, (result) -> {

            isBusyChanged.accept(false);
            if(result.Value1 == ResultType.Success)
            {
                if(result.Value2.size() > 0)
                {
                List<ArrayList<String>> reminderArrayList = new ArrayList<>(this.ReminderListToArray(result.Value2));

                this.Logger.WriteLog(LogLevel.Info, "リマインダー情報取得に成功、リマインダーが存在した");
                resultValue.accept(new Pair<ILoginProcess.ResultType,List<ArrayList<String>>>(result.Value1, reminderArrayList));
                }
                else
                {
                List<ArrayList<String>> reminderArrayList = new ArrayList<>(this.ReminderListToArray(result.Value2));

                this.Logger.WriteLog(LogLevel.Info, "リマインダー情報取得に成功したが、リマインダーが０件だった。");
                resultValue.accept(new Pair<ILoginProcess.ResultType,List<ArrayList<String>>>(result.Value1, reminderArrayList));
                }
            }
            else
            {
                List<ArrayList<String>> reminderArrayList = new ArrayList<>();

                this.Logger.WriteLog(LogLevel.Info, "リマインダー情報取得で異常終了しました");
                resultValue.accept(new Pair<ILoginProcess.ResultType,List<ArrayList<String>>>(result.Value1, reminderArrayList));
            }
        });
    }

    private List<ArrayList<String>> ReminderListToArray(List<ReminderList> reminderList)
    {
        List<ArrayList<String>> stringList = new ArrayList<>();
        
        for(int num = 0; num < reminderList.size(); num++)
        {
        reminderList.get(num).ListName.replace("\n", "").replace("\r", "");
        reminderList.get(num).TaskContent.replace("\n", "").replace("\r", "");

        if(this.Util.GetWideStringLength(reminderList.get(num).ListName) > 20)
        {
            this.Logger.WriteLog(LogLevel.Info, "リマインダーリスト配列の " + (num + 1) + " 番目のリスト名が文字列超過により除外されました");
            continue;
        }
        if(this.Util.GetWideStringLength(reminderList.get(num).TaskContent) > 40)
        {
            this.Logger.WriteLog(LogLevel.Info, "リマインダーリスト配列の " + (num + 1) + " 番目のタスク名が文字列超過により除外されました");
            continue;
        }

        ArrayList<String> stringData = new ArrayList<>();
        stringData.add(reminderList.get(num).ListName);
        stringData.add(reminderList.get(num).TaskContent);
        if(reminderList.get(num).EndDate != null)
        {
            stringData.add(afterDateFormat.format(reminderList.get(num).EndDate));
        }
        else
        {
            stringData.add("");
        }
        stringList.add(stringData);
        }

        return stringList;
    }

}
