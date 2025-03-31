package View.Todo.List;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import View.JPanelViewBase;
import View.Todo.TodoListView;
/*
 * リスト表示のメインパネル
 */
public class TodoListContentPanel extends JPanelViewBase implements ActionListener
{
    // スクロールリストパネル
    private JScrollPane scrollListPane;

    // 親画面インスタンス
    private TodoListView TodoListViewInstance;

    // +ボタン
    private JButton PlusButton;

    public TodoListContentPanel(TodoListView todoListViewInstance)
    {
        // 親画面インスタンスの保持
        this.TodoListViewInstance = todoListViewInstance;

        this.setLayout(null);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // 上のPanel作成:サイズ指定在り
        JPanel upPanel = new JPanel();
        upPanel.setLayout(null);
        upPanel.setPreferredSize(new Dimension(760,112));
        upPanel.setBackground(Color.LIGHT_GRAY);

        // リスト名ラベル
        JLabel listNameLabel = new JLabel("リスト名取得予定");
        listNameLabel.setVerticalAlignment(JLabel.TOP); //垂直位置
        listNameLabel.setHorizontalAlignment(JLabel.LEFT); //水平位置
        listNameLabel.setBounds(76,32,228,32);
        listNameLabel.setOpaque(true);
        listNameLabel.setBackground(Color.CYAN);

        upPanel.add(listNameLabel);

        // 下のPanel作成:サイズ指定なし
        JPanel downPanel = new JPanel();
        downPanel.setLayout(new BoxLayout(downPanel, BoxLayout.Y_AXIS));
        downPanel.setBackground(Color.RED);

        // タスク配置用パネル
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setPreferredSize(new Dimension(760, 272));

        for(int i = 0; i < 10; i++)
        {
            // タスク名、チェックボックス、期日配置パネル
            JPanel taskPanel = new JPanel();
            taskPanel.setLayout(new BoxLayout(taskPanel, BoxLayout.X_AXIS));
            taskPanel.setPreferredSize(new Dimension(760,32));
            // チェックボックス作成
            JCheckBox checkBox = new JCheckBox();
            checkBox.setPreferredSize(new Dimension(76,32));
            // タスク名表示欄
            JLabel taskNameLabel = new JLabel("タスク" + i);
            taskNameLabel.setPreferredSize(new Dimension(608,32));
            // 期日ボタン
            JButton PlusButton = new JButton("期日");
            PlusButton.setPreferredSize(new Dimension(76,32));
            taskPanel.add(checkBox);
            taskPanel.add(taskNameLabel);
            taskPanel.add(PlusButton);
            listPanel.add(taskPanel);
        }
        downPanel.add(listPanel);

        // 完了済みラベル配置用パネル
        JPanel compPanel = new JPanel();
        compPanel.setLayout(null);
        compPanel.setPreferredSize(new Dimension(760, 64));

        // 完了済みラベル
        JLabel completeLabel = new JLabel("完了済み");
        completeLabel.setVerticalAlignment(JLabel.TOP); //垂直位置
        completeLabel.setHorizontalAlignment(JLabel.LEFT); //水平位置
        completeLabel.setBounds(76,16,228,32);
        completeLabel.setOpaque(true);
        completeLabel.setBackground(Color.CYAN);
        compPanel.add(completeLabel);

        downPanel.add(compPanel);

        // 完了済みタスク配置用パネル
        JPanel completeTaskPanel = new JPanel();
        completeTaskPanel.setLayout(new BoxLayout(completeTaskPanel, BoxLayout.Y_AXIS));
        completeTaskPanel.setPreferredSize(new Dimension(760, 272));
        for(int i = 0; i < 10; i++)
        {
            // タスク名、チェックボックス配置パネル
            JPanel taskPanel = new JPanel();
            taskPanel.setLayout(new BoxLayout(taskPanel, BoxLayout.X_AXIS));
            taskPanel.setPreferredSize(new Dimension(760,32));
            // チェックボックス作成
            JCheckBox checkBox = new JCheckBox();
            checkBox.setPreferredSize(new Dimension(76,32));
            // タスク名表示欄
            JLabel taskNameLabel = new JLabel("タスク" + i);
            taskNameLabel.setPreferredSize(new Dimension(684,32));
            taskPanel.add(checkBox);
            taskPanel.add(taskNameLabel);
            completeTaskPanel.add(taskPanel);
        }
        downPanel.add(completeTaskPanel);

        // 完了済みラベル配置用パネル
        JPanel newTaskPanel = new JPanel();
        newTaskPanel.setLayout(null);
        newTaskPanel.setPreferredSize(new Dimension(760, 64));

        // タスク名、チェックボックス、期日配置パネル
        JPanel addPanel = new JPanel();
        addPanel.setLayout(new BoxLayout(addPanel, BoxLayout.X_AXIS));
        addPanel.setPreferredSize(new Dimension(760,32));
        addPanel.setBounds(0,0,760,32);

        // +ボタン設定 aakgi:ここに画面
        this.PlusButton = new JButton("＋");
        this.PlusButton.setActionCommand("PlusButton");
        this.PlusButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.PlusButton.setPreferredSize(new Dimension(76,35));
        this.add(this.PlusButton);
        // タスク入力欄
        JTextField newTaskNameLabel = new JTextField(10);
        newTaskNameLabel.setPreferredSize(new Dimension(608, 32));
        // 期日ボタン
        JButton PlusButton = new JButton("期日");
        PlusButton.setPreferredSize(new Dimension(76,32));
        addPanel.add(this.PlusButton);
        addPanel.add(newTaskNameLabel);
        addPanel.add(PlusButton);
        newTaskPanel.add(addPanel);
        downPanel.add(newTaskPanel);

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
        this.PlusButton.addActionListener(this);
    }
    public void Hide()
    {
        this.PlusButton.removeActionListener(this);
    }
    
    public void actionPerformed(ActionEvent e)
    {
        switch (e.getActionCommand())
        {
            case "PlusButton":
            this.PlusButtonClicked();
            break;
        }
    }
    public void PlusButtonClicked()
    {
        this.TodoListViewInstance.dialog();
    }
}
