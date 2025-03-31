package View.Todo.Board;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import View.Todo.TodoBoardView;
import View.Todo.TodoListBaseView;
import View.Todo.TodoSideBasePanel;

/*
 * ボード表示画面　左サイドパネル
 */
public class TodoSideBoardPanel extends TodoSideBasePanel implements ActionListener
{
    // 親画面インスタンス
    private TodoBoardView TodoBoardViewInstance;
    

    public TodoSideBoardPanel(TodoBoardView todoBoardView)
    {
        // 親インスタンス保持
        this.TodoBoardViewInstance = todoBoardView;

        // レイアウトマネージャー無効化
        this.setLayout(null);
        this.setBackground(Color.PINK);
    }

    public void Show()
    {
        this.BoardButton.addActionListener(this);
        this.ListButton.addActionListener(this);
        this.GanttchartButton.addActionListener(this);
    }
    public void Hide()
    {
        this.BoardButton.removeActionListener(this);
        this.ListButton.removeActionListener(this);
        this.GanttchartButton.removeActionListener(this);
    }
    
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
            default:
                // ロジック上あり得ない
                break;
        }
    }
      // ボードボタンクリック時の処理
    private void BoardButtonClicked()
    {
        System.out.println("ボードボタンが押下された");
        // TODO : ログインボタン押下時の処理を実装
        // これに変わる処理を作る必要がある
        // ボード画面に遷移する
        // this.TodoListViewInstance.TarnationAfterLoginView();
        this.TodoBoardViewInstance.BaseViewInstance.ChangeView(TodoListBaseView.ViewType.TodoBoardView);

    }

    // リストボタンクリック時の処理　現在リスト画面であるかを確認する必要がある
    private void ListButtonClicked()
    {
        System.out.println("リスト作成ボタンが押下された");

        // TODO: 親画面に対して子画面から表示状態を指示するのは違和感を感じる（コールバックやイベントハンドラに修正を検討）
        this.TodoBoardViewInstance.BaseViewInstance.ChangeView(TodoListBaseView.ViewType.TodoListView);
    }

    // ガントチャートボタンクリック時の処理
    private void GanttchartButtonClicked()
    {
        System.out.println("ガントチャートボタンが押下された");

        // TODO: 親画面に対して子画面から表示状態を指示するのは違和感を感じる（コールバックやイベントハンドラに修正を検討）
        this.TodoBoardViewInstance.BaseViewInstance.ChangeView(TodoListBaseView.ViewType.TodoGanttchartView);
    }
}
