package View.Dialog.Listener;

import java.util.EventListener;

public interface EditProgressRateDialogViewListener extends EventListener
{
    /**
     * OKボタン押下時イベント
     * @param progressRate 進捗率
     */
    public void OkButtonClicked(int progressRate);

    /**
     * キャンセルボタン押下時イベント
     */
    public void CancelButtonClicked();
}
