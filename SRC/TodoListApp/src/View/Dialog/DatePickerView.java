package View.Dialog;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.ItemSelectable;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.WeekFields;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.event.EventListenerList;

import View.Dialog.Listener.DatePickerListener;

/**
  * 日付選択ピッカーView
  */
public class DatePickerView extends JDialog implements ActionListener, ItemListener, WindowFocusListener
{
    /* カレンダー日付ボタンアクションコマンド文字列 */
    private final String DateButtonActionCommand = "DateButtonClicked";

    /* カレンダー日付ボタンクライアントプロパティ文字列 */
    private final String DateButtonPutClientPropertyName = "Date";

    /* 選択可能年 */
    private final Set<Integer> Years = IntStream.rangeClosed(1970, 2069).boxed().collect(Collectors.toCollection(LinkedHashSet::new));

    /* 選択可能月 */
    private final Set<Integer> Months = IntStream.rangeClosed(1, 12).boxed().collect(Collectors.toCollection(LinkedHashSet::new));

    /* 共通ダイアログの横幅 */
    private final int DialogWidth = 350;

    /* 共通ダイアログの高さ */
    private final int DialogHeight = 300;

    /* 祝日情報（祝日日、祝日名） */
    private LinkedHashMap<LocalDate, String> Holidays;

    /* 年月選択パネル */
    private JPanel YearMonthPanel;

    /* カレンダーパネル */
    private JPanel CalendarPanel;

    /* 前月ボタン */
    private JButton PrevMonthButton;

    /* 翌月ボタン */
    private JButton NextMonthButton;

    /* 年選択コンボボックス */
    private JComboBox<Integer> YearComboBox;

    /* 年選択コンボボックス */
    private JComboBox<Integer> MonthComboBox;

    /* イベントリスナインスタンス */
    protected EventListenerList ListenerList;

    /**
     * コンストラクタ
     */
    public DatePickerView()
    {
        // イベントリスナインスタンスを初期化
        this.ListenerList = new EventListenerList();

        this.Holidays = new LinkedHashMap<LocalDate, String>();

        this.setSize(this.DialogWidth, this.DialogHeight);
        this.setLocationRelativeTo(null);
        this.setLayout(null);
        this.setModal(false);
        this.setResizable(false);
        this.setUndecorated(true);
        this.getRootPane().setBorder(new LineBorder(Color.gray, 2));

        this.YearMonthPanel = new JPanel();
        this.YearMonthPanel.setBounds(0, 0, this.DialogWidth, 30);
        this.YearMonthPanel.setLayout(new BoxLayout(this.YearMonthPanel, BoxLayout.X_AXIS));

        this.CalendarPanel = new JPanel();
        this.CalendarPanel.setBounds(0, 30, this.DialogWidth, 270);
        this.CalendarPanel.setLayout(new GridBagLayout());

        this.getContentPane().add(this.YearMonthPanel);
        this.getContentPane().add(this.CalendarPanel);

        this.PrevMonthButton = new JButton("<<");
        this.PrevMonthButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.PrevMonthButton.setAlignmentY(Component.CENTER_ALIGNMENT);

        this.NextMonthButton = new JButton(">>");
        this.NextMonthButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.NextMonthButton.setAlignmentY(Component.CENTER_ALIGNMENT);

        this.YearComboBox = new JComboBox<>(this.Years.stream().toArray(Integer[]::new));
        this.YearComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.YearComboBox.setAlignmentY(Component.CENTER_ALIGNMENT);

        this.MonthComboBox = new JComboBox<>(this.Months.stream().toArray(Integer[]::new));
        this.MonthComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.MonthComboBox.setAlignmentY(Component.CENTER_ALIGNMENT);
        this.MonthComboBox.setMaximumRowCount(12);

        this.YearMonthPanel.add(Box.createHorizontalGlue());
        this.YearMonthPanel.add(this.PrevMonthButton);
        this.YearMonthPanel.add(this.YearComboBox);
        this.YearMonthPanel.add(this.MonthComboBox);
        this.YearMonthPanel.add(this.NextMonthButton);
        this.YearMonthPanel.add(Box.createHorizontalGlue());
    }

    /**
     * ダイアログ表示する
     * @param initialYearMonth カレンダーダイアログ表示時の初期選択年月
     * @param dispPosition カレンダーダイアログ表示座標
     */
    public void Show(YearMonth initialYearMonth, Point dispPosition)
    {
        this.setLocation(dispPosition);

        this.addWindowFocusListener(this);
        this.PrevMonthButton.addActionListener(this);
        this.NextMonthButton.addActionListener(this);
        this.YearComboBox.addItemListener(this);
        this.MonthComboBox.addItemListener(this);
        
        this.SetYearMonthComboBoxSelection(initialYearMonth);

        this.setVisible(true);
    }

    /**
     * ダイアログ閉じる
     */
    public void Hide()
    {
        this.removeWindowFocusListener(this);
        this.PrevMonthButton.removeActionListener(this);
        this.NextMonthButton.removeActionListener(this);
        this.YearComboBox.removeItemListener(this);
        this.MonthComboBox.removeItemListener(this);

        for (Component component : this.CalendarPanel.getComponents())
        {
            if (component instanceof JButton)
            {
                JButton button = (JButton) component;
                button.removeActionListener(this);
                button.putClientProperty(this.DateButtonPutClientPropertyName, null);
                this.CalendarPanel.remove(button);
            }
        }

        // ダイアログ非表示指示
        this.dispose();
        this.setVisible(false);
    }

    /**
     * リスナ対象追加
     * @param listener 追加対象リスナインスタンス
     */
    public void AddListener(DatePickerListener listener)
    {
        this.ListenerList.add(DatePickerListener.class, listener);
    }

    /**
     * リスナ対象削除
     * @param listener 削除対象リスナインスタンス
     */
    public void RemoveListener(DatePickerListener listener)
    {
        this.ListenerList.remove(DatePickerListener.class, listener);
    }

    /**
     * ボタンからのイベント
     * @param e イベントオブジェクト
     */
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == this.PrevMonthButton)
        {
            this.PrevMonthButtonClicked();
        }
        else if (e.getSource() == this.NextMonthButton)
        {
            this.NextMonthButtonClicked();
        }
        else if (e.getActionCommand().equals(this.DateButtonActionCommand))
        {
            this.CalendarButtonClicked(e);
        }
    }

    /**
     * コンボボックスからのイベント
     * @param e イベントオブジェクト
     */
    public void itemStateChanged(ItemEvent e)
    {
        // ロジックによるコンボボックス操作でもこのイベントは発生する
        if (e.getStateChange() == ItemEvent.SELECTED)
        {
            ItemSelectable source = e.getItemSelectable();

            if (source == this.YearComboBox)
            {
                this.YearComboBoxSelected();
            }
            else if (source == this.MonthComboBox)
            {
                this.MonthComboBoxSelected();
            }
            else
            {
                // 定義外のためあり得ない。
            }
        }
    }

    /**
     * カレンダーダイアログがアクティブウィンドウになった
     * @param e イベントオブジェクト
     */
    public void windowGainedFocus(WindowEvent e)
    {
        // 何もしない
    }

    /**
     * カレンダーダイアログが非アクティブウィンドウになった
     * @param e イベントオブジェクト
     */
    public void windowLostFocus(WindowEvent e)
    {
        // フォーカスが外れたらクローズする
        this.Hide();
    }

    /**
     * 祝日情報を設定
     * @param e イベントオブジェクト
     */
    public void SetHolidays(LinkedHashMap<LocalDate, String> holidays)
    {
        this.Holidays = holidays;
    }

    /**
     * 前月ボタン押下時の処理
     */
    private void PrevMonthButtonClicked()
    {
        this.SetYearMonthComboBoxSelection(this.GetYearMonthComboBoxSelection().minusMonths(1));
    }

    /**
     * 翌月ボタン押下時の処理
     */
    private void NextMonthButtonClicked()
    {
        this.SetYearMonthComboBoxSelection(this.GetYearMonthComboBoxSelection().plusMonths(1));
    }

    /**
     * 年コンボボックス選択時の処理
     */
    private void YearComboBoxSelected()
    {
        this.SetYearMonthComboBoxSelection(this.GetYearMonthComboBoxSelection());
    }

    /**
     * 月コンボボックス選択時の処理
     */
    private void MonthComboBoxSelected()
    {
        this.SetYearMonthComboBoxSelection(this.GetYearMonthComboBoxSelection());
    }

    /**
     * カレンダ日付ボタン押下時の処理
     * @param e アクションイベント
     */
    private void CalendarButtonClicked(ActionEvent e)
    {   
        if (e.getSource() instanceof JButton)
        {
            var button = (JButton) e.getSource();

            if (button.getClientProperty(this.DateButtonPutClientPropertyName) instanceof LocalDate)
            {
                var selectedDate = (LocalDate) button.getClientProperty(this.DateButtonPutClientPropertyName);

                for (DatePickerListener listener : this.ListenerList.getListeners(DatePickerListener.class))
                {
                    // 選択された日付を通知
                    listener.SelectedDate(selectedDate);
                }
            }
        }
    }

    /**
     * 年月コンボボックス選択状態取得
     * @return 年月コンボボックス選択状態
     */
    private YearMonth GetYearMonthComboBoxSelection()
    {
        var selectedYear = (int)this.YearComboBox.getSelectedItem();
        var selectedMonth = (int)this.MonthComboBox.getSelectedItem();
        return YearMonth.of(selectedYear, selectedMonth);
    }

    /**
     * 年月コンボボックス選択処理
     * @param yearMonth 更新後の年月
     */
    private void SetYearMonthComboBoxSelection(YearMonth yearMonth)
    {
        this.YearComboBox.setSelectedItem(yearMonth.getYear());
        this.MonthComboBox.setSelectedItem(yearMonth.getMonthValue());

        this.SetMonthSelectButtonEnabled();
        this.RenderCalendarButton();

        this.YearMonthPanel.repaint();
        this.YearMonthPanel.revalidate();
        this.CalendarPanel.repaint();
        this.CalendarPanel.revalidate();
    }

    /**
     * 前月、翌月ボタンの押下可否を設定
     */
    private void SetMonthSelectButtonEnabled()
    {
        // 前月が1970年1月以後の場合は許容する
        this.PrevMonthButton.setEnabled(this.Years.contains(this.GetYearMonthComboBoxSelection().minusMonths(1).getYear()) ? true : false);

        // 翌月が2069年12月以前の場合は許容する
        this.NextMonthButton.setEnabled(this.Years.contains(this.GetYearMonthComboBoxSelection().plusMonths(1).getYear()) ? true : false);
    }

    /**
     * カレンダー日付ボタンを設定
     * @param 描画年を指定
     * @param 描画月を指定
     */
    private void RenderCalendarButton()
    {
        // 更新前ボタンのリスナーとインスタンスを破棄する
        for (Component component : this.CalendarPanel.getComponents())
        {
            if (component instanceof JButton)
            {
                JButton button = (JButton) component;
                button.removeActionListener(this);
                button.putClientProperty(this.DateButtonPutClientPropertyName, null);
                this.CalendarPanel.remove(button);
            }
        }
        
        this.CalendarPanel.removeAll();

        var currentYearMonth = this.GetYearMonthComboBoxSelection();

        // 選択月のカレンダーを描画する
        for (int index = 0; index < currentYearMonth.lengthOfMonth(); index++)
        {
            LocalDate localDate = LocalDate.of(currentYearMonth.getYear(), currentYearMonth.getMonthValue(), index + 1);
            
            // 日曜日を週の開始日に設定
            WeekFields weekFields = WeekFields.of(DayOfWeek.SUNDAY, 1);

            // 第何週目かを判定
            int weekOfMonthNum = localDate.get(weekFields.weekOfMonth());

            // 一週間のうち何日目かを判定
            int dayOfWeekNum = localDate.get(weekFields.dayOfWeek());

            // 曜日を判定
            DayOfWeek dayOfWeek = localDate.getDayOfWeek();
            
            JButton button = new JButton(Integer.toString(index + 1));

            if (this.Holidays.containsKey(localDate))
            {
                // 祝日のため曜日に関係なく赤文字で表示する
                button.setForeground(Color.RED);
            }
            else
            {
                // 曜日ごとの色を設定
                switch (localDate.getDayOfWeek())
                {
                    case DayOfWeek.SUNDAY:
                        // 日曜日
                        button.setForeground(Color.RED);
                        break;
                    case DayOfWeek.SATURDAY:
                        // 土曜日
                        button.setForeground(Color.BLUE);
                        break;
                    default:
                        // 平日
                        button.setForeground(Color.BLACK);
                        break;
                }
            }

            // 押下イベントを設定
            button.setActionCommand(this.DateButtonActionCommand);
            button.putClientProperty(this.DateButtonPutClientPropertyName, localDate);
            button.addActionListener(this);

            // 表示座標を決定
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = dayOfWeekNum - 1; // 列番号
            gbc.gridy = weekOfMonthNum - 1; // 行番号
            gbc.fill = GridBagConstraints.BOTH; // ボタンのサイズをセルいっぱいに広げる
            gbc.weightx = 1.0; // 各列の幅を均等にする
            gbc.weighty = 1.0; // 各行の高さを均等にする

            // Gridに設定
            this.CalendarPanel.add(button, gbc);
        }
    }
}
