package View.Todo.List;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import Interface.View.Todo.ITodoGanttchartView;
import Interface.View.Todo.ITodoListView;
import Entity.UserTask;
import View.JPanelViewBase;

/*
 * リスト表示のメインパネル
 */
public class TodoGanttchartContentPanel extends JPanelViewBase implements ActionListener
{
    // スクロールリストパネル
    private JScrollPane ScrollListPane;

    // 親画面インスタンス
    private ITodoGanttchartView TodoGanttchartViewInstance;

    // ガントチャートテーブル
    private JTable GanttchartTable;

    // ガントチャート表の枠組み
    private JPanel GanttchartPanel;

    // ヘッダパネル
    private JPanel HeadertPanel;

    // ガントのタスク名部分パネル
    //private JPanel TaskTitlePanel;

    // ガントの日割り部分パネル
    //private JPanel GanttDaysPanel;

    // ダイアログスクロールビュー
    private JScrollPane GanttchartScrollPanel;

    // RightJPanelのインスタンス
    private JPanel TodoListContentPanel;

    // リスト名
    private JLabel ListNameLabel;

    // 表示年月プルダウン
    JComboBox SelectDateCombo;

    // 表示年月プルダウン内容
    ArrayList<String> Combodata;

    // 表示年月の初期選択
    String InitSelectDate;

    // テーブルのヘッダ
    private String[] ColumnNames = {"タスク", "進捗状況", "開始","終了","8888/88/88~","8888/88/88~","8888/88/88~","8888/88/88~","8888/88/88~"};

    // テーブルデータの定義
    private DefaultTableModel Tabledata;

    public TodoGanttchartContentPanel(ITodoGanttchartView todoGanttchartViewInstance)
    {
        // 親画面インスタンスの保持
        this.TodoGanttchartViewInstance = todoGanttchartViewInstance;

        // テーブルデータの初期化
        this.Tabledata = new DefaultTableModel(ColumnNames, 0);

        // レイアウトマネージャを無効化
        this.setLayout(null);

        // リスト名ラベル
        this.ListNameLabel = new JLabel("リスト名");
        ListNameLabel.setBounds(30,50,200,50);
        ListNameLabel.setVerticalAlignment(JLabel.CENTER);
        ListNameLabel.setHorizontalAlignment(JLabel.CENTER);
        ListNameLabel.setOpaque(true);
        ListNameLabel.setBackground(Color.CYAN);
        this.add(ListNameLabel);

        // 表示年月プルダウン
        Combodata = new ArrayList<>();
        Combodata = SetComboData();
        this.SelectDateCombo = new JComboBox(Combodata.toArray());
        SelectDateCombo.setBounds(30, 110, 150, 50);
        SelectDateCombo.setMaximumRowCount(12);
        SelectDateCombo.setSelectedItem(InitSelectDate);
        this.add(SelectDateCombo);

        // ガントチャート表
        /*this.GanttchartTable = new JTable(Tabledata);
        this.setBackground(Color.white);

        ArrayList<String> rowData = new ArrayList<>(); // あとで動的に変更
        rowData.add("テストタスク"); // あとで動的に変更
        rowData.add("100%"); // あとで動的に変更
        rowData.add("2025/12/31"); // あとで動的に変更
        rowData.add("2025/12/31"); // あとで動的に変更
        rowData.add("1"); // あとで動的に変更
        rowData.add("1"); // あとで動的に変更
        rowData.add("0"); // あとで動的に変更
        rowData.add("0"); // あとで動的に変更
        rowData.add("1"); // あとで動的に変更
        rowData.add("2"); // あとで動的に変更
        this.Tabledata.addRow(rowData.toArray()); // あとで動的に変更
        
        DefaultTableCellRenderer whiteCellRenderer = new DefaultTableCellRenderer();
        whiteCellRenderer.setBackground(Color.white);
        GanttchartTable.getColumnModel().getColumn(4).setCellRenderer(whiteCellRenderer);

        DefaultTableCellRenderer pinkCellRenderer = new DefaultTableCellRenderer();
        pinkCellRenderer.setBackground(Color.pink);
        GanttchartTable.getColumnModel().getColumn(5).setCellRenderer(pinkCellRenderer);

        DefaultTableCellRenderer yellowCellRenderer = new DefaultTableCellRenderer();
        yellowCellRenderer.setBackground(Color.yellow);
        GanttchartTable.getColumnModel().getColumn(6).setCellRenderer(yellowCellRenderer);

        this.GanttchartTable.setBounds(30,200,700,300);
        this.GanttchartTable.setRowHeight(57);
        this.GanttchartTable.setRowHeight(0,20);
        this.GanttchartTable.getColumnModel().getColumn(0).setPreferredWidth(700);
        this.GanttchartTable.getColumnModel().getColumn(1).setPreferredWidth(500);
        this.GanttchartTable.getColumnModel().getColumn(2).setPreferredWidth(500);
        this.GanttchartTable.getColumnModel().getColumn(3).setPreferredWidth(500);
        this.GanttchartTable.getColumnModel().getColumn(4).setPreferredWidth(700);
        this.GanttchartTable.getColumnModel().getColumn(5).setPreferredWidth(700);
        this.GanttchartTable.getColumnModel().getColumn(6).setPreferredWidth(700);
        this.GanttchartTable.getColumnModel().getColumn(7).setPreferredWidth(700);
        this.GanttchartTable.getColumnModel().getColumn(8).setPreferredWidth(700);
        this.GanttchartScrollPanel = new JScrollPane(this.GanttchartTable);
        this.GanttchartScrollPanel.setPreferredSize(new Dimension(560, 171));
        this.GanttchartScrollPanel.setBounds(30, 200, 700, 300);
        this.add(this.GanttchartScrollPanel);*/

        // ガントチャート表の枠組み
        this.GanttchartPanel = new JPanel();
        this.GanttchartPanel.setBounds(30,200,700,300);
        this.GanttchartPanel.setLayout(new GridBagLayout());
        this.setBackground(Color.white);

        // ヘッダパネル
        //this.TaskTitlePanel = new JPanel();
        //this.TaskTitlePanel.setBounds(30,200,200,250);
        GridBagConstraints gbc = new GridBagConstraints();
        JLabel headerColumn1 = new JLabel("タスク");
        headerColumn1.setBounds(0,0,10,0);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        GanttchartPanel.add(headerColumn1, gbc);

        gbc = new GridBagConstraints();
        JLabel headerColumn2 = new JLabel("進捗状況");
        headerColumn2.setBounds(0,0,10,0);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        GanttchartPanel.add(headerColumn2, gbc);

        gbc = new GridBagConstraints();
        JLabel headerColumn3 = new JLabel("開始");
        headerColumn3.setBounds(0,0,10,0);
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        GanttchartPanel.add(headerColumn3, gbc);

        gbc = new GridBagConstraints();
        JLabel headerColumn4 = new JLabel("終了");
        headerColumn4.setBounds(0,0,10,0);
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        GanttchartPanel.add(headerColumn4, gbc);

        // ガントの日割り部分パネル
        //this.GanttDaysPanel = new JPanel();
        //this.GanttDaysPanel.setBounds(230,200,500,250);
        gbc = new GridBagConstraints();
        JLabel headerColumn5 = new JLabel("8888/88/88~");
        headerColumn5.setBounds(0,0,10,0);
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.gridwidth = 7;
        gbc.fill = GridBagConstraints.BOTH;
        GanttchartPanel.add(headerColumn5, gbc);

        gbc = new GridBagConstraints();
        JLabel headerColumn6 = new JLabel("8888/88/88~");
        headerColumn6.setBounds(0,0,10,0);
        gbc.gridx = 11;
        gbc.gridy = 0;
        gbc.gridwidth = 7;
        gbc.fill = GridBagConstraints.BOTH;
        GanttchartPanel.add(headerColumn6, gbc);

        gbc = new GridBagConstraints();
        JLabel headerColumn7 = new JLabel("8888/88/88~");
        headerColumn7.setBounds(0,0,10,0);
        gbc.gridx = 18;
        gbc.gridy = 0;
        gbc.gridwidth = 7;
        gbc.fill = GridBagConstraints.BOTH;
        GanttchartPanel.add(headerColumn7, gbc);

        gbc = new GridBagConstraints();
        JLabel headerColumn8 = new JLabel("8888/88/88~");
        headerColumn8.setBounds(0,0,10,0);
        gbc.gridx = 25;
        gbc.gridy = 0;
        gbc.gridwidth = 7;
        gbc.fill = GridBagConstraints.BOTH;
        GanttchartPanel.add(headerColumn8, gbc);

        gbc = new GridBagConstraints();
        JLabel headerColumn9 = new JLabel("8888/88/88~");
        headerColumn9.setBounds(0,0,10,0);
        gbc.gridx = 32;
        gbc.gridy = 0;
        gbc.gridwidth = 7;
        gbc.fill = GridBagConstraints.BOTH;
        GanttchartPanel.add(headerColumn9, gbc);
        
        /*headerColumn1.setBorder(new LineBorder(Color.BLACK, 1));
        headerColumn2.setBorder(new LineBorder(Color.BLACK, 1));
        headerColumn3.setBorder(new LineBorder(Color.BLACK, 1));
        headerColumn4.setBorder(new LineBorder(Color.BLACK, 1));
        headerColumn5.setBorder(new LineBorder(Color.BLACK, 1));
        headerColumn6.setBorder(new LineBorder(Color.BLACK, 1));
        headerColumn7.setBorder(new LineBorder(Color.BLACK, 1));
        headerColumn8.setBorder(new LineBorder(Color.BLACK, 1));
        headerColumn9.setBorder(new LineBorder(Color.BLACK, 1));*/
        /*GanttchartPanel.add(headerColumn1, gbc);
        GanttchartPanel.add(headerColumn2, gbc);
        GanttchartPanel.add(headerColumn3, gbc);
        GanttchartPanel.add(headerColumn4, gbc);
        GanttchartPanel.add(headerColumn5, gbc);
        GanttchartPanel.add(headerColumn6, gbc);
        GanttchartPanel.add(headerColumn7, gbc);
        GanttchartPanel.add(headerColumn8, gbc);
        GanttchartPanel.add(headerColumn9, gbc);*/
        //GanttchartPanel.add(GanttDaysPanel);

        // ガントのタスク名部分パネル
        //this.TaskTitlePanel = new JPanel();
        //this.TaskTitlePanel.setBounds(30,250,200,250);
        
        // タスクの生成（後で動的に生成されるように変更する）
        gbc.gridwidth = 0;
        
        for(int i = 1; i <= 5; i++)
        {
            // ガントのタスク名部分パネル
            JLabel dataHead1 = new JLabel("テストタスク名");
            gbc.gridx = i;
            gbc.gridy = 0;
            GanttchartPanel.add(dataHead1, gbc);

            JButton dataHead2 = new JButton("100%");
            gbc.gridx = i;
            gbc.gridy = 1;
            GanttchartPanel.add(dataHead2, gbc);

            JLabel dataHead3 = new JLabel("2024/1/1");
            gbc.gridx = i;
            gbc.gridy = 2;
            GanttchartPanel.add(dataHead3, gbc);

            JLabel dataHead4 = new JLabel("2024/12/31");
            gbc.gridx = i;
            gbc.gridy = 3;
            GanttchartPanel.add(dataHead4, gbc);

            // ガントの日割り部分パネル
            int manthEnd = 31;
            int today = 2;
            for(int dayCount = 1; dayCount <= manthEnd; dayCount++)
            {
                JLabel chartDay = new JLabel("1");
                gbc.gridx = dayCount + 4;
                gbc.gridy = i;
                gbc.fill = GridBagConstraints.VERTICAL;
                System.out.println(gbc.gridx + ", " + gbc.gridy);
                System.out.println("gbc.gridwidth = " + gbc.gridwidth);
                System.out.println(i);
                System.out.print(dayCount);

                if(today == dayCount)
                {
                    chartDay.setBackground(Color.red);
                System.out.println("  red");
                }
                else
                {
                    chartDay.setBackground(Color.yellow);
                    System.out.println("  yellow");
                }

                GanttchartPanel.add(chartDay, gbc);
            }
        }

        //GanttchartPanel.add(TaskTitlePanel);
        //GanttchartPanel.add(GanttDaysPanel);
        this.add(GanttchartPanel);
        
        /*this.GanttchartScrollPanel = new JScrollPane(this.GanttchartTable);
        this.GanttchartScrollPanel.setPreferredSize(new Dimension(560, 171));
        this.GanttchartScrollPanel.setBounds(30, 200, 700, 300);
        this.add(this.GanttchartScrollPanel);*/
    }

    // 表示年月の作成
    public ArrayList<String> SetComboData()
    {
        for(int subMonth = -24; subMonth <= 24; subMonth++)
        {
            Calendar todayCalendar = Calendar.getInstance();
            todayCalendar.setTime(new Date());
            todayCalendar.add(Calendar.MONTH, subMonth);
            SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月");
            String todayString = format.format(todayCalendar.getTime());
            Combodata.add(todayString);

            if(subMonth == 0)
            {
                InitSelectDate = todayString;
            }
        }
        return Combodata;
    }

    public void Show()
    {
        //this.PlusButton.addActionListener(this);
        //this.PeriodButton.addActionListener(this);
    }

    public void Hide()
    {
        //this.PlusButton.removeActionListener(this);
        //this.PeriodButton.removeActionListener(this);
    }

    public void actionPerformed(ActionEvent e)
    {
        /*if(e.getActionCommand().equals("PlusButton"))
        {
            // このボタンは画面に１つしか存在しない。
            this.PlusButtonClicked();
        }
        else if(e.getActionCommand().equals("PeriodButton")){
            this.PeriodButtonClicked();
        }
        else
        {
            // 以下のボタンは複数存在するためindex値によって参照
            var command = e.getActionCommand().split("_");
            var index = Integer.parseInt(command[1]);

            switch (command[0])
            {
                case "DeleteButton":
                    this.DeleteButtonClicked(index);
                    break;
                case "UpdateButton":
                    this.UpdateButtonClicked(index);
                    break;
                default:
                // ロジック上あり得ない
                break;
            }
        }*/
    }

    /**
     * タスク登録処理
     */
    public void PlusButtonClicked()
    {
        /*String taskText = this.NewTaskNameLabel.getText();
        this.TodoGanttchartViewInstance.CreateUserTask(taskText, this.InputStartPeriod, this.InputEndPeriod);*/
    }

    /**
     * ガントチャート内容の表示作成
     */
    public void CreateGanttchartList()
    {
        /*this.DeleteButtons = new JButton[this.IncTask.size()];
        this.UpdateButtons = new JButton[this.IncTask.size()];
        for(int i = 0; i < this.IncTask.size(); i++)
        {
            // タスク名、チェックボックス、期日配置パネル
            JPanel taskPanel = new JPanel();
            taskPanel.setLayout(new BoxLayout(taskPanel, BoxLayout.X_AXIS));
            taskPanel.setPreferredSize(new Dimension(760,32));
            // チェックボックス作成
            JCheckBox checkBox = new JCheckBox();
            checkBox.setPreferredSize(new Dimension(76,32));
            checkBox.setSelected(false);
            // タスク名表示欄
            JLabel taskNameLabel = new JLabel(this.IncTask.get(i).taskText);
            taskNameLabel.setPreferredSize(new Dimension(608,32));
            // 期日ボタン
            this.TaskPeriodButton = new JButton("開始" + this.IncTask.get(i).startDate +  " ： 終了" +this.IncTask.get(i).endDate);
            this.TaskPeriodButton.setPreferredSize(new Dimension(170,32));
            //削除ボタン
            this.DeleteButtons[i] = new JButton("削除");
            this.DeleteButtons[i].setPreferredSize(new Dimension(76,32));
            this.DeleteButtons[i].setActionCommand("DeleteButton_"+i);
            this.DeleteButtons[i].addActionListener(this);
            //編集ボタン
            this.UpdateButtons[i] = new JButton("更新");
            this.UpdateButtons[i].setPreferredSize(new Dimension(76,32));
            this.UpdateButtons[i].setActionCommand("UpdateButton_"+i);
            this.UpdateButtons[i].addActionListener(this);
            

            taskPanel.add(checkBox);
            taskPanel.add(taskNameLabel);
            taskPanel.add(this.TaskPeriodButton);
            // taskPanel.add(this.DeleteButton);
            taskPanel.add(this.DeleteButtons[i]);
            taskPanel.add(this.UpdateButtons[i]);
            this.listPanel.add(taskPanel);
        }*/
    }
}