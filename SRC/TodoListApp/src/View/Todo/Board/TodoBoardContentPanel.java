package View.Todo.Board;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import View.JPanelViewBase;
import View.Todo.TodoListView;

/**
 * ボード型表示の右コンテンツ部分
 */
public class TodoBoardContentPanel extends JPanelViewBase implements ActionListener
{
    // スクロールリストパネル
    private JScrollPane scrollListPane;

    // 親画面インスタンス
    // private TodoListView TodoListViewInstance;
    /**
     * コンストラクタ
     */
    public TodoBoardContentPanel()
    {
        // 親画面インスタンスの保持
        // this.TodoListViewInstance = todoListViewInstance;
        this.setLayout(null);
        // 全てを包括していれるPanel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // 上のPanel作成:サイズ指定在り
        JPanel upPanel = new JPanel();
        upPanel.setLayout(null);
        // 縦横だけを指定する方法
        upPanel.setPreferredSize(new Dimension(760,160));
        upPanel.setBackground(Color.LIGHT_GRAY);

        // リスト名ラベル
        JLabel listNameLabel = new JLabel("リスト名取得予定");
        listNameLabel.setVerticalAlignment(JLabel.TOP); //垂直位置
        listNameLabel.setHorizontalAlignment(JLabel.LEFT); //水平位置
        listNameLabel.setBounds(76,48,228,32);
        listNameLabel.setOpaque(true);
        listNameLabel.setBackground(Color.CYAN);
        
        upPanel.add(listNameLabel);
        

        // 下のPanel作成:サイズ指定なし
        JPanel downPanel = new JPanel();
        downPanel.setLayout(new BoxLayout(downPanel, BoxLayout.X_AXIS));
        // downPanel.setPreferredSize(new Dimension(760,416));
        downPanel.setBackground(Color.RED);

        // addPanelとtaskPanelを入れるPanel(listPanel)
        for(int i = 0; i < 10; i++)
        {
            JPanel listPanel = new JPanel();
            listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
            listPanel.setPreferredSize(new Dimension(228, 416));

            JPanel addPanel = new JPanel();
            addPanel.setLayout(new BoxLayout(addPanel, BoxLayout.X_AXIS));
            addPanel.setPreferredSize(new Dimension(228,32));
            // リスト名入力欄
            JTextField ListNameInputField = new JTextField(10);
            ListNameInputField.setPreferredSize(new Dimension(152, 32));
            // +ボタン
            JButton PlusButton = new JButton("＋");
            PlusButton.setPreferredSize(new Dimension(76,32));
            addPanel.add(ListNameInputField);
            addPanel.add(PlusButton);
            listPanel.add(addPanel);

            for(int x = 0; x < 10; x++)
            {
                // タスク名、チェックボックス配置パネル
                JPanel taskPanel = new JPanel();
                taskPanel.setPreferredSize(new Dimension(228,32));
                taskPanel.setLayout(new BoxLayout(taskPanel, BoxLayout.X_AXIS));
                // タスク名表示欄
                JLabel taskNameLabel = new JLabel("タスク" + x);
                taskNameLabel.setPreferredSize(new Dimension(152,32));
                // チェックボックス作成
                JCheckBox checkBox = new JCheckBox();
                checkBox.setPreferredSize(new Dimension(76,32));
                taskPanel.add(taskNameLabel);
                taskPanel.add(checkBox);
                listPanel.add(taskPanel);
            }
            downPanel.add(listPanel);
        }
        // リスト登録用部品入れPanel(for文の手前に入れる）(addPanel)
        // タスク名とチェックボックス入れPanel(for文の中に入れる)(taskPanel)
        
        
        mainPanel.add(upPanel);
        mainPanel.add(downPanel);

        // scrollパネルの用意
        this.scrollListPane = new JScrollPane(mainPanel);
        this.scrollListPane.setBackground(Color.CYAN);
        this.scrollListPane.setBounds(0,0,757,565);
        this.add(scrollListPane);
    }
    public void Show()
    {

    }
    public void Hide()
    {

    }
    
    public void actionPerformed(ActionEvent e)
    {
        
    }
}
