package View.Dialog;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.ModuleLayer.Controller;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import javax.swing.table.DefaultTableModel;

import Entity.Pair;
import Entity.Dialog.ReminderList;
import Entity.Enum.LogLevel;
import View.Dialog.Listener.ReminderDialogViewListener;
import View.Login.Listener.LoginViewListener;

/**
  * 共通ダイアログクラス
  */
public class ReminderDialogView extends JDialog implements ActionListener
{
    // イベントリスナインスタンス
    protected EventListenerList ListenerList;

    /* 共通ダイアログの横幅 */
    private final int DialogWidth = 800;

    /* 共通ダイアログの高さ */
    private final int DialogHeight = 400;

    /* 共通ダイアログ文言の横幅 */
    private final int OperationWidth = 350;

    /* 共通ダイアログ文言の高さ */
    private final int OperationHeight = 100;

    /* 共通ダイアログ文言の表示X座標 */
    private final int OperationPositionX = 100;

    /* 共通ダイアログ文言の表示Y座標 */
    private final int OperationPositionY = 10;

    /* ダイアログ表示文言 */
    private JLabel OperationLabel;

    /* ダイアログスクロールビュー */
    private JScrollPane TaskScrollPanel;

    /* ダイアログテーブル */
    private JTable TaskTable;

    /* OKボタン */
    private JButton OKButton;

    /* テーブルのヘッダ */
    private String[] ColumnNames = {"COUNTRY", "WIN", "LOST"};

    /* テーブルデータの初期化 */
    private DefaultTableModel Tabledata = new DefaultTableModel(ColumnNames, 0);

    /**
     * コンストラクタ
     * @param ownerView 親JFrameインスタンス
     */
    public ReminderDialogView(JFrame ownerView)
    {
        // JDialogクラスを初期化
        super(ownerView);
        this.setSize(this.DialogWidth, this.DialogHeight);
        this.setLocationRelativeTo(null);
        this.setLayout(null);
        this.setTitle("通知ダイアログ");

        // TODO: 表示位置も可変にしたいなら、文言と同様にテーブル化する。
        this.OperationLabel = new JLabel();
        this.OperationLabel.setBounds(this.OperationPositionX, this.OperationPositionY, this.OperationWidth, this.OperationHeight);
        this.add(this.OperationLabel);

        this.TaskTable = new JTable(Tabledata);
        this.TaskTable.setTableHeader(null);
        this.TaskTable.setRowHeight(57);
        this.TaskTable.getColumnModel().getColumn(0).setPreferredWidth(140);
        this.TaskTable.getColumnModel().getColumn(1).setPreferredWidth(250);
        this.TaskTable.getColumnModel().getColumn(2).setPreferredWidth(110);
        this.TaskScrollPanel = new JScrollPane(this.TaskTable);
        this.TaskScrollPanel.setPreferredSize(new Dimension(560, 171));
        this.TaskScrollPanel.setBounds(100, 80, 560, 171);
        this.add(this.TaskScrollPanel);

        this.OKButton = new JButton("OK");
        this.OKButton.setActionCommand("OKButton");
        this.OKButton.setBounds(262,300,255,44);
        this.add(this.OKButton);
        
        // TODO: ボタンも表示するなら追加する。YesとNo（閉じると共用、文言切替で対応）で2^2=4パターンの列挙体を定義し表示切替できるように対応する

        // イベントリスナインスタンスを初期化
        this.ListenerList = new EventListenerList();
    }

    /**
     * ボタンからのアクションリスナー
     */
    public void actionPerformed(ActionEvent e)
    {
        switch (e.getActionCommand())
        {
            case "OKButton":
                // OKボタン押下イベントを受信
                this.ReminderOKButtonClicked();
                break;
        }
    }

    /**
     * 新規登録ボタンクリック時の処理
     */
    private void ReminderOKButtonClicked()
    {
        // 画面を非表示にする
        this.Hide();

        for (ReminderDialogViewListener listener : this.ListenerList.getListeners(ReminderDialogViewListener.class))
        {
            // 新規登録ボタン押下を通知
            listener.ReminderOKButtonClicked();
        }
    }

    /**
     * ダイアログ表示する
     * @param dialogType 表示するダイアログ種別
     * @param isModal モーダル表示とするか（ダイアログ表示中は画面スレッドがロックするため注意）
     */
    public void Show(List<ArrayList<String>> list, boolean isModal)
    {
        this.OKButton.addActionListener(this);
        this.OperationLabel.setText(list.size() > 0 ? "未完了タスク一覧です" : "タスクなし");

        for(int rowNum = 0; rowNum < list.size(); rowNum++)
        {
            ArrayList<String> rowData = new ArrayList<>(list.get(rowNum));
            Tabledata.addRow(rowData.toArray());
        }

        // ダイアログ表示指示
        this.setModal(isModal);
        this.setVisible(true);
    }

    /**
     * ダイアログ閉じる
     * モーダルの場合は画面スレッドがロックするため外部からのクローズは不可
     */
    public void Hide()
    {
        // ダイアログ非表示指示
        this.setVisible(false);
        this.OKButton.removeActionListener(this);

        // 表示文言をクリアする
        this.setModal(false);
        this.setTitle("");
        this.OperationLabel.setText("");
    }

    /**
     * リスナ対象追加
     * @param listener 追加対象リスナインスタンス
     */
    public void AddListener(ReminderDialogViewListener listener)
    {
        this.ListenerList.add(ReminderDialogViewListener.class, listener);
    }
    
    /**
     * リスナ対象削除
     * @param listener 削除対象リスナインスタンス
     */
    public void RemoveListener(ReminderDialogViewListener listener)
    {
        this.ListenerList.remove(ReminderDialogViewListener.class, listener);
    }
}