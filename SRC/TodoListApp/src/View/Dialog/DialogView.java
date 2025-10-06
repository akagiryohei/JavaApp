package View.Dialog;

import javax.swing.*;

import DI.MainWindowDI;
import View.JPanelViewBase;
import View.MainWindowView;

import java.awt.event.*;
import java.awt.*;

public class DialogView extends JPanelViewBase implements ActionListener
{
    // 親要素のインスタンス
    private JFrame Owner;
    // ダイアログのタイトル
    private String DialogTitle;

    // ダイアログの操作指示文言
    private String DialogOperation;

    // 画面別表示種別
    public enum ViewType{
        // Todoリスト（リスト型表示）
        TodoListView,

        // Todoリスト（ボード型表示）
        TodoBoardView,

        // Todoリスト（ガントチャート）
        TodoGanttchartView,
    }

    /**
     * コストラクタ
     * @param dialogTitle ダイアログタイトル
     * @param dialogOperation ダイアログオプション
     * @param owner 親要素インスタンス
     */
    public DialogView(String dialogTitle, String dialogOperation, JFrame owner)
    {
        this.Owner = owner;
        this.DialogTitle= dialogTitle;
        JDialog dialog = new JDialog(this.Owner, DialogTitle, true);
        dialog.setSize(256,380);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        Owner.add(dialog);
    }
    public void Show()
    {
    }
    public void Hide()
    {
    }
    /**
     * ボタンからのアクションリスナー
     */
    public void actionPerformed(ActionEvent e)
    {
    }

}
