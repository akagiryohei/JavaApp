package View.Dialog;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.EventListenerList;
import java.awt.event.*;
import Entity.Dialog.Constants;
import View.Dialog.Listener.UpdateListDialogViewListener;

public class UpdateListDialogView extends JDialog implements ActionListener
{
    /** 指示文言 */
    private JLabel OperationLabel;
    /** 修正用テキストフィールド */
    private JTextField ListNameInputField;
    /** 編集ボタン */
    private JButton EditButton;
    /** イベントリスナインスタンス */
    protected EventListenerList ListenerList;
    /** ダイアログ定数クラス */
    private Constants Constants;

    /**
     * コンストラクタ
     */
    public UpdateListDialogView(Constants constants)
    {
        this.Constants = constants;
        this.setSize(400, 256);
        this.setLayout(null);
        this.setTitle(this.Constants.EDIT_DIALOG);
        this.ListenerList = new EventListenerList();

        // 指示文言
        this.OperationLabel = new JLabel(this.Constants.LIST_EDIT_OPERATION);
        this.OperationLabel.setBounds(76, 10, 350, 35);
        this.add(this.OperationLabel);

        // Text入力
        this.ListNameInputField = new JTextField();
        this.ListNameInputField.setText("");
        this.ListNameInputField.setBounds(76, 50, 228, 35);
        this.ListNameInputField.setColumns(1);
        this.add(this.ListNameInputField);

        // 編集ボタン
        this.EditButton = new JButton("編集");
        this.EditButton.setBounds(90, 130, 76, 35);
        this.EditButton.setActionCommand("EditListName");
        this.add(EditButton);
        this.setLocationRelativeTo(null);
    }

    /**
     * show
     */
    public void Show(String selectedListName)
    {
        this.ListNameInputField.setText(selectedListName);
        this.EditButton.addActionListener(this);
        this.setModal(true);
        this.setVisible(true);
    }

    /**
     * hide
     */
    public void Hide()
    {
        this.ListNameInputField.setText("");
        this.EditButton.removeActionListener(this);
        this.setVisible(false);
        this.setModal(false);
    }

    /**
     * アクションリスナ
     */
    public void actionPerformed(ActionEvent e)
    {
        switch (e.getActionCommand())
        {
            case "EditListName":
                System.out.println("EditListNameボタンが押下された");
                this.UpdateListClicked();
        }
    }

    /**
     * 編集ボタン押下時処理
     */
    public void UpdateListClicked()
    {
        for (UpdateListDialogViewListener listener : this.ListenerList.getListeners(UpdateListDialogViewListener.class))
        {
            listener.UpdateListClicked(this.ListNameInputField.getText());
        }
    }

    /**
     * リスナ対象追加
     * @param listener 追加対象リスナインスタンス
     */
    public void AddListener(UpdateListDialogViewListener listener)
    {
        this.ListenerList.add(UpdateListDialogViewListener.class, listener);
    }

    /**
     * リスナ対象削除
     * @param listener 削除対象リスナインスタンス
     */
    public void RemoveListener(UpdateListDialogViewListener listener)
    {
        this.ListenerList.remove(UpdateListDialogViewListener.class, listener);
    }
}
