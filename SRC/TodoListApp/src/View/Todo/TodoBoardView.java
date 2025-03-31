package View.Todo;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import Controller.Todo.TodoBoardController;
import View.JPanelViewBase;
import View.Todo.Board.TodoBoardContentPanel;
import View.Todo.Board.TodoSideBoardPanel;
import View.Todo.List.*;

/**
 * ボード型表示の画面（左と右の部品を配置する画面）
 */
public class TodoBoardView extends JPanelViewBase implements ActionListener
{
    // 親画面のインスタンス
    public TodoListBaseView BaseViewInstance;

    // TodoList(リスト型表示)コントローラ
    public TodoBoardController controller;

    // 左のPanelのインスタンス
    private TodoSideBoardPanel TodoSideBoardPanel;

    // 右のPanelのインスタンス
    private TodoBoardContentPanel TodoBoardContentPanel;

    public TodoBoardView(TodoListBaseView baseViewInstance)
    {
      // 親画面インスタンスを保持
      this.BaseViewInstance = baseViewInstance;

      // レイアウトマネージャー解除
      this.setLayout(null);
      this.setBackground(Color.RED);

      // 左要素作成（Panel)
      // ここでこの要素のインスタンスを渡してるから、下のTodoSideCommonPanel内でも操作が可能
      this.TodoSideBoardPanel = new TodoSideBoardPanel(this);
      this.TodoSideBoardPanel.setBounds(0,0,230,600);

      // 右要素作成（Panel）
      this.TodoBoardContentPanel = new TodoBoardContentPanel();
      this.TodoBoardContentPanel.setBounds(230,0,769,600);
      
      // 左要素追加
      this.add(this.TodoSideBoardPanel);
      // 右要素追加
      this.add(this.TodoBoardContentPanel);

    }

    /**
     * ボタンのアクション
     * ここでは役目無し
     */
    public void actionPerformed(ActionEvent e)
    {
    }

    public void Show()
    {
      this.TodoSideBoardPanel.Show();
    }

    public void Hide()
    {
        
    }
      //ここでやはりメソッドを作る必要がある？（リストやガントチャートに移動するときに必要）
    //baseViewInstanceを使用して、上のTodoListBaseViewのところで宣言してるchengeViewを呼んでくる必要がある。

}
