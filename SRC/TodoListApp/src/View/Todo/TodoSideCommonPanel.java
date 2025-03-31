package View.Todo;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.List;

import Entity.UserList;


/*
 * リスト・ガントチャート共通設定
 */
public class TodoSideCommonPanel extends TodoSideBasePanel implements ActionListener
{
    // 親画面インスタンス
    private TodoListView TodoListViewInstance;
    // +ボタン
    private JButton PlusButton;

    // リスト名入力欄
    private JTextField ListNameInputField;

    // リスト一覧
    private JList<String> ScrollList;

    // スクロールリストパネル
    private JScrollPane scrollListPane;

    private List<UserList> List;

    private String[] ListNames;

    /**
     * 
     * @param todoListViewInstance 
     */
    public TodoSideCommonPanel(TodoListView todoListViewInstance)
    {
        // 親インスタンスの保持
        // TODO:親画面側の切り替え処理呼び出し関数を呼び出す
        this.TodoListViewInstance = todoListViewInstance;

        this.setLayout(null);
        this.setBackground(Color.PINK);

        // List<UserList> List = TodoListViewInstance.List;
        // String[] ListNames = new String[list.size()];
        // for (int i = 0; i < list.size(); i++)
        // {
        //     ListNames[i] = List.get(i).list_name;
        // }
        // this.ScrollList = new JList<>(ListNames);

        // リスト一覧の設定
        // this.ScrollList = new JList<>();
        // this.scrollListPane = new JScrollPane(this.ScrollList);
        // this.scrollListPane.setBounds(0,160,230,368);
        // this.add(scrollListPane);

        
        // リスト名入力欄設定ListNameInputField
        this.ListNameInputField = new JTextField();
        this.ListNameInputField.setBounds(76,528,152,35);
        this.ListNameInputField.setColumns(1);
        this.add(this.ListNameInputField);
        
        // +ボタン設定
        this.PlusButton = new JButton("＋");
        this.PlusButton.setActionCommand("PlusButton");
        this.PlusButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.PlusButton.setBounds(0,528,76,35);
        this.add(this.PlusButton);
    }

    public void Show()
    {
        this.BoardButton.addActionListener(this);
        this.ListButton.addActionListener(this);
        this.GanttchartButton.addActionListener(this);
        this.PlusButton.addActionListener(this);
    }
    public void Hide()
    {
        this.BoardButton.removeActionListener(this);
        this.ListButton.removeActionListener(this);
        this.GanttchartButton.removeActionListener(this);
        this.PlusButton.removeActionListener(this);
    }
    
    public void actionPerformed(ActionEvent e)
    {
        switch (e.getActionCommand())
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
            case "PlusButton":
                this.PlusButton();
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
        this.TodoListViewInstance.BaseViewInstance.ChangeView(TodoListBaseView.ViewType.TodoBoardView);

    }

    // リストボタンクリック時の処理　現在リスト画面であるかを確認する必要がある
    private void ListButtonClicked()
    {
        System.out.println("リスト作成ボタンが押下された");

        // TODO: 親画面に対して子画面から表示状態を指示するのは違和感を感じる（コールバックやイベントハンドラに修正を検討）
        this.TodoListViewInstance.BaseViewInstance.ChangeView(TodoListBaseView.ViewType.TodoListView);
    }

    // ガントチャートボタンクリック時の処理
    private void GanttchartButtonClicked()
    {
        System.out.println("ガントチャートボタンが押下された");

        // TODO: 親画面に対して子画面から表示状態を指示するのは違和感を感じる（コールバックやイベントハンドラに修正を検討）
        this.TodoListViewInstance.BaseViewInstance.ChangeView(TodoListBaseView.ViewType.TodoGanttchartView);
    }

    // ＋ボタン押下時処理
    private void PlusButton()
    {
        String listText = this.ListNameInputField.getText();
        this.TodoListViewInstance.CreateUserList(listText);

    }

    public void SetList(List<UserList> list)
    {
        List<UserList> List = list;
        this.ListNames = new String[list.size()];
        for (int i = 0; i < list.size(); i++)
        {
            ListNames[i] = List.get(i).list_name;
        }
        this.ScrollList = new JList<>(ListNames);
        this.scrollListPane = new JScrollPane(this.ScrollList);
        this.scrollListPane.setBounds(0,160,230,368);
        this.add(scrollListPane);
    }
}
