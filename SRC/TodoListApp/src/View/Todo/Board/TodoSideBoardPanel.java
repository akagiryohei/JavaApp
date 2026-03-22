package View.Todo.Board;

import java.awt.*;
import java.awt.event.*;

import javax.swing.event.EventListenerList;

import Entity.Enum.LogLevel;
import Interface.View.Todo.ITodoBoardView;
import View.Todo.TodoSideBasePanel;
import View.Todo.Listener.TodoSideBoardPanelListener;

/*
 * ボード表示画面　左サイドパネル
 */
public class TodoSideBoardPanel extends TodoSideBasePanel implements ActionListener
{
    /** イベントリスナインスタンス */
    protected EventListenerList ListenerList;
    
    /**
     * コンストラクタ
     * @param todoBoardView ボード型表示画面のインスタンス
     */
    public TodoSideBoardPanel()
    {
        // イベントリスナインスタンスを初期化
        this.ListenerList = new EventListenerList();
        // レイアウトマネージャー無効化
        this.setLayout(null);
        this.setBackground(Color.PINK);
    }

    public void Show()
    {
        this.BoardButton.addActionListener(this);
        this.ListButton.addActionListener(this);
        this.GanttchartButton.addActionListener(this);
        this.AICreateListTaskButton.addActionListener(this);
    }
    public void Hide()
    {
        this.BoardButton.removeActionListener(this);
        this.ListButton.removeActionListener(this);
        this.GanttchartButton.removeActionListener(this);
        this.AICreateListTaskButton.removeActionListener(this);
    }
    
    /**
     * ボタンからのアクションリスナー
     */
    public void actionPerformed(ActionEvent e)
    {
        switch(e.getActionCommand())
        {
            case "BoardButton":
                // ボードボタン押下イベントを受信
                this.BoardButtonClicked();
                break;
            case "ListButton":
                // リストボタン押下イベントを受信
                this.ListButtonClicked();
                break;
            case "GanttchartButton":
                // ガントチャートボタン押下イベントを受信
                this.GanttchartButtonClicked();
                break;
            case "AICreateListTaskButton":
                // AIリスト・タスク案作成ボタン押下イベントを受信
                this.AICreateListTaskButtonClicked();
                break;
            default:
                // ロジック上あり得ない
                break;
        }
    }
    /**
     * ボードボタンクリック時の処理
     */
    private void BoardButtonClicked()
    {
        System.out.println("ボードボタンが押下された");
        for (TodoSideBoardPanelListener listener : this.ListenerList.getListeners(TodoSideBoardPanelListener.class))
        {
            listener.BoardButtonClicked();
        }
    }

    /**
     * リストボタンクリック時の処理　現在リスト画面であるかを確認する必要がある
     */
    private void ListButtonClicked()
    {
        System.out.println("リスト作成ボタンが押下された");
        for (TodoSideBoardPanelListener listener : this.ListenerList.getListeners(TodoSideBoardPanelListener.class))
        {
            listener.ListButtonClicked();
        }
    }

    /**
     * ガントチャートボタンクリック時の処理
     */
    private void GanttchartButtonClicked()
    {
        System.out.println("ガントチャートボタンが押下された");
        for (TodoSideBoardPanelListener listener : this.ListenerList.getListeners(TodoSideBoardPanelListener.class))
        {
            listener.GanttchartButtonClicked();
        }
    }

    /**
     * AIリスト・タスク案作成ボタンクリック時の処理
     */
    private void AICreateListTaskButtonClicked()
    {
        System.out.println("AIリスト・タスク案作成ボタンが押下された");
        for (TodoSideBoardPanelListener listener : this.ListenerList.getListeners(TodoSideBoardPanelListener.class))
        {
            listener.AICreateListTaskButtonClicked();
        }
    }

    /**
     * Controllerで渡す：isBusyで使う
     * @param isBusy 処理中かどうか
     */
    public void ElementDisabled(boolean isDisabled)
    {
        this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "画面要素押下可否設定" + "(" + String.valueOf(isDisabled) + ")"); });
        this.BoardButton.setEnabled(!isDisabled);
        this.ListButton.setEnabled(!isDisabled);
        this.GanttchartButton.setEnabled(!isDisabled);
        this.AICreateListTaskButton.setEnabled(!isDisabled);
    }

    /**
     * リスナ対象追加
     * @param listener 追加対象リスナインスタンス
     */
    public void AddListener(TodoSideBoardPanelListener listener)
    {
        this.ListenerList.add(TodoSideBoardPanelListener.class, listener);
    }

    /**
     * リスナ対象削除
     * @param listener 削除対象リスナインスタンス
     */
    public void RemoveListener(TodoSideBoardPanelListener listener)
    {
        this.ListenerList.remove(TodoSideBoardPanelListener.class, listener);
    }


}
