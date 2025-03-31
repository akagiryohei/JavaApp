package View.Todo;

import java.awt.*;
import java.awt.event.*;
import java.util.List;

import javax.security.auth.login.LoginContext;
import javax.swing.*;

import Controller.Todo.TodoListController;
import Entity.UserList;
import Entity.Dialog.Constants;
// コントローラーimport必要
import View.JPanelViewBase;
import View.MainWindowView;
import View.Todo.Board.TodoSideBoardPanel;
import View.Todo.List.*;
/**
 * リスト型表示の画面（左と右の部品を配置する画面）
 */
public class TodoListView extends JPanelViewBase implements ActionListener
{
  // 親画面のインスタンス
  public TodoListBaseView BaseViewInstance;

  // TodoList(リスト型表示)コントローラ
  public TodoListController controller;

  // LeftJPanelのインスタンス
  private TodoSideCommonPanel TodoSideCommonPanel;

  // LeftJPanel（ボード表示時）のインスタンス
  private TodoSideBoardPanel TodoSideBoardPanel;

  // RightJPanelのインスタンス
  private TodoListContentPanel TodoListContentPanel;    

  // MainWindowViewのインスタンス
  private MainWindowView MainWindowViewInstance;

  // ログイン中ユーザーのリスト
  public List<UserList> List;

  int titleBarHeight = 0;
  // コンストラクタ
  // baseViewInstance : 親画面のインスタンス
  public TodoListView(TodoListBaseView baseViewInstance, MainWindowView mainWindowViewInstance)
  {
    // TODO : 親画面のインスタンスは型依存ではなくinterface等に修正し依存しないようにする
    // TODO : 親画面のインスタンスをView層で直接持つのは検討の余地あり
    this.BaseViewInstance = baseViewInstance;
    this.MainWindowViewInstance = mainWindowViewInstance;
    // this.controller.GetUserList();

    

    // 一番親のLoginViewにかかってるLayoutマネージャーの向こうかが可能になる
    this.setLayout(null);
    this.setBackground(Color.BLACK);
    
    // 左要素作成（Panel)
    // ここでこの要素のインスタンスを渡してるから、下のTodoSideCommonPanel内でも操作が可能
    this.TodoSideCommonPanel = new TodoSideCommonPanel(this);
    this.TodoSideCommonPanel.setBounds(0,0,230,600);

    // 右要素作成（Panel）
    this.TodoListContentPanel = new TodoListContentPanel(this);
    this.TodoListContentPanel.setBounds(230,0,769,600);
    
    // 左要素追加
    this.add(this.TodoSideCommonPanel);
    // 右要素追加
    this.add(this.TodoListContentPanel);



  }
  public void dialog()
  {
    // JButton openDialogButton = new JButton("ダイアログを開く");
    // openDialogButton.addActionListener(e -> {
        // JDialogの作成(ownerとしてfreameを指定)
        JDialog dialog = new JDialog(this.MainWindowViewInstance, "ダイアログのタイトル", true);
        dialog.setSize(250,150);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

    // });
    // this.add(openDialogButton);
}

  /**
   * ボタンからのアクションリスナー
   * ここは特に役目無し
   * ボタンの処理はそれぞれが右、左の配置してるPanelの中でやってる
   */
  public void actionPerformed(ActionEvent e)
  {
  }

  public void Show()
  {
    // ここでリスト取得をしてくるべき
    // ここまでuserIDを渡してくるべきかDBUserIdで渡してきた

    // さらに中のコントローラーを呼ぶべきか
    // ここで条件分岐を作ってやる必要がある？
    this.controller.GetUserList();
    this.TodoSideCommonPanel.Show();
    this.TodoListContentPanel.Show();
    // JDialog dialog = new JDialog(this.MainWindowViewInstance, Constants.EDIT_DELETE_DIALOG, true);
    // dialog.setSize(380,256);
    // JPanel DialogPanel = new JPanel();
    // DialogPanel.setLayout(new BoxLayout(DialogPanel, BoxLayout.Y_AXIS));
    // JLabel operation = new JLabel(Constants.SELECTION_OPERATION);
    // DialogPanel.add(operation);

    // Object[][] data = 
    // {
    //   {"リスト名", "2025/03/15"},
    //   {"リスト名", "2025/04/25"}
    // };
    // String[] columnNames = {"リスト名", "期日"};
    // JTable table = new JTable(data, columnNames);
    // DialogPanel.add(table);
    // dialog.add(DialogPanel);
    // dialog.setLocationRelativeTo(null);
    // dialog.setVisible(true);
  }

  public void Hide()
  {
    this.TodoSideCommonPanel.Hide();
    this.TodoListContentPanel.Hide();
  }

  public void SetList(List<UserList> list)
  {
    this.List = list;
    this.TodoSideCommonPanel.SetList(list);
  }

  public void CreateUserList(String listText)
  {
    this.controller.CreateUserList(listText);
  }

  //ここでやはりメソッドを作る必要がある？
  //baseViewInstanceを使用して、上のTodoListBaseViewのところで宣言してるchengeViewを呼んでくる必要がある。

}
