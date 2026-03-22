package View.Dialog;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.EventListenerList;

import Entity.Dialog.Constants;
import View.Dialog.Listener.EditProgressRateDialogViewListener;

public class EditProgressRateDialogView extends JDialog implements ActionListener {

    /** 指示文言 */
    private JLabel OperationLabel;
    /** 期日上限 */
    private final int MaxProgressRate = 100;
    /** 期日下限 */
    private final int MinProgressRate = 0;
    /** 期日ステップ */
    private final int StepProgressRate = 1;
    /** 基底進捗率 */
    private SpinnerNumberModel model;
    /** スピナー */
    private JSpinner Spineer;
    /** 編集ボタン(OK) */
    private JButton OkButton;
    /** キャンセルボタン */
    private JButton CancelButton;
    // イベントリスナインスタンス
    protected EventListenerList ListenerList;
    /** ダイアログ定数クラス */
    private Constants Constants;

    /**
     * コストラクタ
     */
    public EditProgressRateDialogView(Constants constants)
    {
        this.Constants = constants;
        this.setSize(400, 256);
        this.setLayout(null);
        this.setLocationRelativeTo(null);
        this.setModal(true);

        this.ListenerList = new EventListenerList();

        // ダイアログタイトル
        this.setTitle(this.Constants.PROGRESS_RATE_EDIT_DIALOG);

        // 指示文言
        this.OperationLabel = new JLabel(this.Constants.PROGRESS_RATE_FAILURE_OPERATION);
        OperationLabel.setBounds(76,10,350,35);
        this.add(this.OperationLabel);

        // スピナー
        this.model = new SpinnerNumberModel(0, this.MinProgressRate, this.MaxProgressRate, this.StepProgressRate);
        this.Spineer = new JSpinner(this.model);
        this.Spineer.setBounds(100,50,100,48);
        this.add(this.Spineer);

        // 編集ボタン
        this.OkButton = new JButton("OK");
        this.OkButton.setBounds(90,130,76,35);
        this.OkButton.setActionCommand("OK");
        this.OkButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(OkButton);

        // キャンセルボタン
        this.CancelButton = new JButton("キャンセル");
        this.CancelButton.setBounds(242,130,100,35);
        this.CancelButton.setActionCommand("Cancel");
        this.CancelButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(this.CancelButton);
    }

    /**
     * 表示メソッド
     * @param progressRate 進捗率
     * @param taskId タスクID
     * @param listId リストID
     */
    public void Show(int progressRate)
    {
        this.OkButton.addActionListener(this);
        this.CancelButton.addActionListener(this);
        this.model.setValue(progressRate);
        this.setVisible(true);
    }

    /**
     * 非表示メソッド
     */
    public void Hide()
    {
        this.OkButton.removeActionListener(this);
        this.CancelButton.removeActionListener(this);
        this.setVisible(false);
        this.dispose();
    }

    /**
     * リスナ対象追加
     * @param listener 追加対象リスナインスタンス
     */
    public void AddListener(EditProgressRateDialogViewListener listener)
    {
        this.ListenerList.add(EditProgressRateDialogViewListener.class, listener);
    }

    /**
     * リスナ対象削除
     * @param listener 削除対象リスナインスタンス
     */
    public void RemoveListener(EditProgressRateDialogViewListener listener)
    {
        this.ListenerList.remove(EditProgressRateDialogViewListener.class, listener);
    }

    /**
     * ボタンからのアクションリスナー
     */
    public void actionPerformed(ActionEvent e)
    {
        switch(e.getActionCommand())
        {
            case "OK":
                this.OkButtonClicked();
                this.Hide();
                break;
            case "Cancel":
                this.CancelButtonClicked();
                this.Hide();
                break;
            default:
                break;
        }
    }

    /**
     * OKボタンクリック時処理
     */
    private void OkButtonClicked()
    {
        if (this.model.getValue() instanceof Integer)
        {
            int newProgressRate = (Integer) this.model.getValue();
            for (EditProgressRateDialogViewListener listener : this.ListenerList.getListeners(EditProgressRateDialogViewListener.class))
            {
                // OKボタン押下を通知
                listener.OkButtonClicked(newProgressRate);
            }
        }
    }

    /**
     * キャンセルボタンクリック時処理
     */
    private void CancelButtonClicked()
    {
        for (EditProgressRateDialogViewListener listener : this.ListenerList.getListeners(EditProgressRateDialogViewListener.class))
        {
            // キャンセルボタン押下を通知
            listener.CancelButtonClicked();
        }
    }



}
