
package View.Dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.EventListenerList;

import View.Dialog.Listener.FatalErrorDialogViewListener;

/**
  * 致命的エラーダイアログクラス
  */
public class FatalErrorDialogView extends JDialog implements ActionListener
{
    /* ダイアログ表示文言定義 */
    private final String OperationLabelText = "<HTML>処理中に甚大なエラーが発生しました。<BR>アプリを終了します。</HTML>";

    /* アプリ終了ボタン文言定義 */
    private final String TerminateApplicationButtonLabelText = "アプリ終了";
    
    /* 共通ダイアログの横幅 */
    private final int DialogWidth = 400;

    /* 共通ダイアログの高さ */
    private final int DialogHeight = 200;

    /* ダイアログ表示文言 */
    private JLabel OperationLabel;

    /* アプリ終了ボタン */
    private JButton TerminateApplicationButton;

    /* イベントリスナインスタンス */
    private EventListenerList ListenerList;

    /**
     * コンストラクタ
     * @param ownerView 親JFrameインスタンス
     */
    public FatalErrorDialogView()
    {
        // JDialogクラスを初期化
        super();
        this.setSize(this.DialogWidth, this.DialogHeight);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());

        this.setModal(true);
        this.setUndecorated(true);
        this.setAlwaysOnTop(true);
        this.setResizable(false);
        this.getRootPane().setBorder(new LineBorder(Color.gray, 2));

        this.OperationLabel = new JLabel();
        this.OperationLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.OperationLabel.setVerticalAlignment(SwingConstants.CENTER);
        this.OperationLabel.setText(this.OperationLabelText);
        this.add(this.OperationLabel, BorderLayout.CENTER);

        this.TerminateApplicationButton = new JButton(this.TerminateApplicationButtonLabelText);
        this.add(this.TerminateApplicationButton, BorderLayout.SOUTH);

        // イベントリスナインスタンスを初期化
        this.ListenerList = new EventListenerList();
    }

    /**
     * ダイアログ表示する
     * @param ownerComponent 親要素オブジェクト（表示位置決定に使用）
     */
    public void Show(Component ownerComponent)
    {
        // 親要素に対して中央に配置する
        this.setLocationRelativeTo(ownerComponent);

        this.setModal(true);
        this.TerminateApplicationButton.addActionListener(this);

        // ダイアログ表示指示
        this.setVisible(true);
    }

    /**
     * ダイアログ閉じる
     * 画面スレッドがロックするため外部からのクローズは不可
     */
    public void Hide()
    {
        this.setModal(false);
        this.TerminateApplicationButton.removeActionListener(this);

        // ダイアログ非表示指示
        this.setVisible(false);
    }

    /**
     * ボタンからのイベント
     * @param e イベントオブジェクト
     */
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == this.TerminateApplicationButton)
        {
            // アプリ終了ボタンが押下された
            this.TerminateApplicationButtonClicked();
        }
    }

    /**
     * リスナ対象追加
     * @param listener 追加対象リスナインスタンス
     */
    public void AddListener(FatalErrorDialogViewListener listener)
    {
        this.ListenerList.add(FatalErrorDialogViewListener.class, listener);
    }

    /**
     * リスナ対象削除
     * @param listener 削除対象リスナインスタンス
     */
    public void RemoveListener(FatalErrorDialogViewListener listener)
    {
        this.ListenerList.remove(FatalErrorDialogViewListener.class, listener);
    }

    /**
     * アプリ終了ボタン押下時の処理
     */
    private void TerminateApplicationButtonClicked()
    {
        for (FatalErrorDialogViewListener listener : this.ListenerList.getListeners(FatalErrorDialogViewListener.class))
        {
          // アプリ終了ボタン押下を通知
          listener.TerminateApplicationButtonClicked();
        }
    }
}
