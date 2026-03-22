package View.Dialog;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.EventListenerList;

import Entity.Dialog.Constants;
import View.Dialog.Listener.UpdateTaskDialogViewListener;

import java.awt.event.*;

public class UpdateTaskDialogView extends JDialog implements ActionListener
{
    /** 指示文言 */
    private JLabel OperationLabel;
    /** イベントリスナインスタンス */
    protected EventListenerList ListenerList;
    /** タスク名入力欄 */
    private JTextField TaskNameInputField;
    /** 編集ボタン */
    private JButton EditButton;
    /** ダイアログ定数クラス */
    private Constants Constants;
    /**
     * コンストラクタ
     */
    public UpdateTaskDialogView(Constants constants)
    {
        this.Constants = constants;
        this.setSize(400, 256);
        this.setLayout(null);
        this.setTitle(this.Constants.EDIT_DIALOG);
        this.ListenerList = new EventListenerList();

        // 指示文言
        this.OperationLabel = new JLabel(this.Constants.TASK_EDIT_OPERATION);
        this.OperationLabel.setBounds(76, 10, 350, 35);
        this.add(this.OperationLabel);

        // Text入力
        this.TaskNameInputField = new JTextField();
        this.TaskNameInputField.setText("");
        this.TaskNameInputField.setBounds(76, 50, 228, 35);
        this.TaskNameInputField.setColumns(1);
        this.add(this.TaskNameInputField);

        /** 編集ボタン */
        this.EditButton = new JButton("編集");
        this.EditButton.setBounds(90, 130, 76, 35);
        this.EditButton.setActionCommand("EditTaskName");
        this.add(EditButton);
        this.setLocationRelativeTo(null);
    }

    /**
     * show
     */
    public void Show(String selectedTaskName)
    {
        this.TaskNameInputField.setText(selectedTaskName);
        this.EditButton.addActionListener(this);
        this.setModal(true);
        this.setVisible(true);
    }

    /**
     * Hide
     */
    public void Hide()
    {
        this.TaskNameInputField.setText("");
        this.EditButton.removeActionListener(this);
        this.setVisible(false);
        this.setModal(false);
    }

    /**
     * アクションリスナー
     */
    public void actionPerformed(ActionEvent e)
    {
        switch (e.getActionCommand())
        {
            case "EditTaskName":
                System.out.println("EditTaskNamebボタンが押下された");
                this.UpdateTaskClicked();
        }
    }

    /**
     * 編集ボタン押下時処理
     */
    public void UpdateTaskClicked()
    {
        for (UpdateTaskDialogViewListener listener : this.ListenerList.getListeners(UpdateTaskDialogViewListener.class))
        {
            listener.UpdateTaskClicked(this.TaskNameInputField.getText());
        }
    }

    /**
     * リスナ対象追加
     * @param listener 追加対象リスナインスタンス
     */
    public void AddListener(UpdateTaskDialogViewListener listener)
    {
        this.ListenerList.add(UpdateTaskDialogViewListener.class, listener);
    }

    /**
     * リスナ対象削除
     * @param listener 削除対象リスナインスタンス
     */
    public void RemoveListener(UpdateTaskDialogViewListener listener)
    {
        this.ListenerList.remove(UpdateTaskDialogViewListener.class, listener);
    }

}
