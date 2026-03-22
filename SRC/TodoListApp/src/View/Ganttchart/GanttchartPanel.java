package View.Ganttchart;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.event.EventListenerList;

import Entity.GanttchartTask;
import View.Ganttchart.Listener.GanttchartPanelListener;

/*
 * ガントチャートパネル
 */
public class GanttchartPanel extends JPanel implements ActionListener
{
  /*
   * ガントチャートパネルの横幅
   */
  private final int Width = 740;

  /*
   * Gridセル一行あたりの高さ
   */
  private final int GridRowHeight = 25;

  /*
   * 列数合計
   */
  private final int ColumnNum = 39;

  /*
   * 項目ラベル情報定義
   */
  private final Map<FieldColumnType, FieldLabelInfo> ColumnLabelInfoTables = new LinkedHashMap<FieldColumnType, FieldLabelInfo>()
  {
    {
      put(FieldColumnType.TaskName, new FieldLabelInfo("タスク", 10, 100));
      put(FieldColumnType.ProgressRate, new FieldLabelInfo("進捗状況", 8, 50));
      put(FieldColumnType.StartDate, new FieldLabelInfo("開始", 10, 70));
      put(FieldColumnType.EndDate, new FieldLabelInfo("終了", 10, 70));
      put(FieldColumnType.WeekFields, new FieldLabelInfo("yyyy/M/d", 10, 80));
    }
  };

  /*
   * 項目名箇所背景色
   */
  private final Color ColumnNameBackgroundColor = new Color(79, 79, 79);

  /*
   * 日付フォーマット定義
   */
  private final DateTimeFormatter Formatter = DateTimeFormatter.ofPattern("yyyy/M/d");

  /*
   * タスク進捗率ボタンアクションコマンド文字列
   */
  private final String ProgressRateButtonActionCommand = "ProgressRateButtonClicked";

  /*
   * タスク進捗率ボタンクライアントプロパティ文字列
   */
  private final String ProgressRateButtonPutClientPropertyName = "ProgressRate";

  /*
   * 表示対象の年月
   */
  private YearMonth ActiveYearMonth;

  /*
   * 本日の日付（本日とする日付）
   */
  private LocalDate Today;

  /*
   * 表示タスク一覧
   */
  private List<GanttchartTask> Tasks;

  /*
   * イベントリスナインスタンス
   */
  private EventListenerList ListenerList;

  // コンストラクタ
  public GanttchartPanel()
  {
    // 初期値は行頭だけ表示する前提の高さで初期化
    this.setSize(this.Width, this.GridRowHeight);

    this.setLayout(new GridBagLayout());
    this.setBackground(null);

    // システム日付の当月で初期化
    this.ActiveYearMonth = YearMonth.now();

    // イベントリスナインスタンスを初期化
    this.ListenerList = new EventListenerList();

    this.Tasks = new ArrayList<>();
  }

  /*
   * 項目カラム定義
   */
  public enum FieldColumnType
  {
    /*
     * 定義なし
     */
    None(-1),

    /*
     * タスク名
     */
    TaskName(0),

    /*
     * 進捗率
     */
    ProgressRate(1),

    /*
     * 開始日
     */
    StartDate(2),

    /*
     * 終了日
     */
    EndDate(3),

    /*
     * ウィークフィールド
     */
    WeekFields(4);

    /**
     * Enum値
     */
    private int ColumnIndex;

    /**
     * Enum定義を追加
     * @param index 設定するEnum値
     */
    FieldColumnType(int index)
    {
      this.ColumnIndex = index;
    }

    /**
     * 対応カラムインデックス値を取得
     * @param 変換対象の値
     * @return 対応カラムインデックス値
     */
    public static FieldColumnType GetType(int num)
    {
      for (FieldColumnType type : values())
      {
          if (type.GetValue() == num)
          {
              return type;
          }
      }

      return FieldColumnType.None;
    }

    /**
     * カラム定義からEnum値を取得
     * @return Enum値を取得
     */
    private int GetValue()
    {
      return this.ColumnIndex;
    }
  }

  /**
   * 表示
   */
  public void Show()
  {
    // イベントリスナ監視開始
    this.StartEndProgressRateButton(true);
  }

  /**
   * 非表示
   */
  public void Hide()
  {
    // イベントリスナ監視終了
    this.StartEndProgressRateButton(false);
  }

  /**
   * リスナ対象追加
   * @param listener 追加対象リスナインスタンス
   */
  public void AddListener(GanttchartPanelListener listener)
  {
    this.ListenerList.add(GanttchartPanelListener.class, listener);
  }

  /**
   * リスナ対象削除
   * @param listener 削除対象リスナインスタンス
   */
  public void RemoveListener(GanttchartPanelListener listener)
  {
    this.ListenerList.remove(GanttchartPanelListener.class, listener);
  }

  /**
   * ボタンからのイベント
   * @param e イベントオブジェクト
   */
  public void actionPerformed(ActionEvent e)
  {
    if (e.getActionCommand().equals(this.ProgressRateButtonActionCommand))
    {
      // タスク進捗率ボタンが押下された
      this.ProgressRateButtonClicked(e);
    }
  }

  /**
   * 表示座標を設定
   * @param xpos 表示X座標
   * @param ypos 表示Y座標
   */
  public void SetLocation(int xpos, int ypos)
  {
    // 高さは行頭＋要素数で決定
    this.setBounds(xpos, ypos, this.Width, this.GridRowHeight * (this.Tasks.size() + 1));
  }

  /**
   * ガントチャート表描画
   * @param activYearMonth 表示対象の年月
   * @param today 本日の日付
   * @param tasks 表示対象のタスク（タスク期間が表示対象の年月に含まれるタスクを入力すること）
   */
  public void RenderGanttchartGrid(YearMonth activYearMonth, LocalDate today, List<GanttchartTask> tasks)
  {
    // 旧インスタンスを破棄してから代入
    this.Tasks.clear();
    this.StartEndProgressRateButton(false);
    Arrays.stream(this.getComponents())
          .filter(comp -> comp instanceof JButton)
          .map(comp -> (JButton) comp)
          .forEach(button ->
          {
            // 旧ボタンのイベントインスタンスを破棄
            button.putClientProperty(this.ProgressRateButtonPutClientPropertyName, null);
          });
    this.removeAll();

    this.ActiveYearMonth = activYearMonth;
    this.Today = today;
    this.Tasks = tasks;

    // 要素数から高さを再設定
    this.RefreshHeight();

    // 行頭（項目名＋ウィークフィールド）を設定
    this.AddRowHeader();

    // 行頭以降（タスク表示部分）を設定
    this.AddRowContent();

    // イベントリスナ監視開始（タスク進捗率ボタン用の処理）
    this.StartEndProgressRateButton(true);

    // GridBagLayoutは自動で再描画しないため明示的に実行
    this.repaint();
    this.revalidate();
  }

  /**
   * 本日の日付を設定・更新（タスク設定済みの場合はセル表示も更新）
   * @param today 本日の日付
   */
  public void RefreshTodayCell(LocalDate today)
  {
    // 今日の日付を更新
    this.Today = today;

    if (this.Tasks.size() == 0)
    {
      // 表示中タスクが0件の場合は何もせず以降を処理せず終了
      return;
    }

    GridBagLayout layout = (GridBagLayout) this.getLayout();
    var preWeekColumnCount = (int)this.ColumnLabelInfoTables.keySet().stream().filter(key -> key != FieldColumnType.WeekFields).count();
    var taskloopNum = 1;

    for (var task : this.Tasks)
    {
      for (var day = 1; day <= this.ActiveYearMonth.lengthOfMonth(); day++)
      {
        var currentDate = this.ActiveYearMonth.atDay(day);
        final int gridx = (preWeekColumnCount + day) - 1;  // ラムダ式に渡すために定数に変換する必要がある
        var gridy = taskloopNum;

        Arrays.stream(this.getComponents())
              .filter(comp -> comp instanceof JPanel)
              .map(comp -> (JPanel) comp)
              .filter(panel ->
              {
                GridBagConstraints gbc = layout.getConstraints(panel);
                return gbc.gridx == gridx && gbc.gridy == gridy;
              })
              .findFirst()
              .ifPresent(panel ->
              {
                // 背景色を更新
                this.SetBackgroundPanelColor(panel, currentDate, task.StartDate, task.EndDate);
              });
      }

      taskloopNum++;
    }
  }

  /**
   * タスク進捗率ボタンイベントリスナ監視開始終了処理
   * @param isStart イベントリスナ監視開始:true, イベントリスナ監視終了:false
   */
  private void StartEndProgressRateButton(boolean isStart)
  {
    Arrays.stream(this.getComponents())
          .filter(comp -> comp instanceof JButton)
          .map(comp -> (JButton) comp)
          .forEach(button ->
          {
            if (isStart)
            {
              button.addActionListener(this);
            }
            else
            {
              button.removeActionListener(this);
            }
          });
  }

  /**
   * タスク要素数に応じてGridの高さのみ再設定
   */
  private void RefreshHeight()
  {
    // Calculate new height based on number of tasks (rows)
    int height = this.GridRowHeight * (this.Tasks.size() + 1);

    // Update preferred/minimum/actual size so JScrollPane can detect scrolling requirements
  this.setPreferredSize(new Dimension(this.Width, height));
  this.setMinimumSize(new Dimension(this.Width, height));
  // Prevent BoxLayout parent from stretching this panel vertically when there are few rows
  this.setMaximumSize(new Dimension(this.Width, height));
    // Also keep the component's size in sync for cases where absolute positioning is used
    this.setSize(this.Width, height);
    // Trigger layout updates
    this.revalidate();
  }

  /**
   * 行頭を挿入
   */
  private void AddRowHeader()
  {
    var columnIndex = 0;

    // 行頭の項目名部分を設定
    for (var columnType : this.ColumnLabelInfoTables.keySet())
    {
      if (columnType == FieldColumnType.WeekFields)
      {
        // ウィークフィールドは次のループで処理する
        continue;
      }

      var type = this.ColumnLabelInfoTables.get(FieldColumnType.GetType(columnIndex));

      GridBagConstraints gbc = new GridBagConstraints();
      gbc.gridx = columnIndex;
      gbc.gridy = 0; // 先頭行
      gbc.fill = GridBagConstraints.BOTH;  // 代入要素のサイズをセルいっぱいに広げる
      gbc.weightx = 1.0;
      gbc.weighty = 1.0;
      gbc.anchor = GridBagConstraints.CENTER;  // 代入要素をセンタリングする

      JPanel backgroundPanel = new JPanel();
      backgroundPanel.setBackground(this.ColumnNameBackgroundColor);
      backgroundPanel.setPreferredSize(new Dimension(type.Width, 1));
      backgroundPanel.setMinimumSize(new Dimension(type.Width, 1));

      JLabel label = new JLabel(this.ColumnLabelInfoTables.get(columnType).Text);
      label.setForeground(Color.white);
      label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
      label.setHorizontalAlignment(SwingConstants.CENTER);

      // フォントサイズを設定
      Font currentFont = label.getFont();
      label.setFont(new Font(currentFont.getName(), currentFont.getStyle(), this.ColumnLabelInfoTables.get(columnType).FontSize));

      // Gridに設定
      this.add(label, gbc);
      this.add(backgroundPanel, gbc);
      columnIndex++;
    }

    var weekNum = 1;

    // 行頭のウィークフィールドを設定
    while (columnIndex < this.ColumnNum)
    {
      var date = this.ActiveYearMonth.atDay(1).plusDays((weekNum - 1) * 7);
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.gridx = columnIndex;
      gbc.gridy = 0;
      gbc.fill = GridBagConstraints.BOTH;
      gbc.weightx = 0;
      gbc.weighty = 1.0;
      gbc.gridwidth = 7;
      gbc.anchor = GridBagConstraints.CENTER;

      JPanel backgroundPanel = new JPanel();

      var columnTypeInfo = this.ColumnLabelInfoTables.get(FieldColumnType.WeekFields);

      if (YearMonth.from(date).equals(this.ActiveYearMonth))
      {
        // 閏年など第4週までしか存在しない月は第5週目のラベルを設定しない（この条件分岐に入らない）
        JLabel label = new JLabel(date.format(this.Formatter) + "~");
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        label.setHorizontalAlignment(SwingConstants.CENTER);

        // フォントサイズを設定
        Font currentFont = label.getFont();
        label.setFont(new Font(currentFont.getName(), currentFont.getStyle(), columnTypeInfo.FontSize));

        // Gridに設定
        this.add(label, gbc);

        backgroundPanel.setBackground(Color.white);
      }
      else
      {
        backgroundPanel.setBackground(null);
      }

      backgroundPanel.setPreferredSize(new Dimension(columnTypeInfo.Width, 1));
      backgroundPanel.setMinimumSize(new Dimension(columnTypeInfo.Width, 1));

      // Gridに設定
      this.add(backgroundPanel, gbc);

      weekNum++;
      columnIndex += 7;
    }
  }

  /**
   * 行頭以降を挿入
   */
  private void AddRowContent()
  {
    var rowIndex = 1;
    var preWeekColumnCount = (int)this.ColumnLabelInfoTables.keySet().stream().filter(key -> key != FieldColumnType.WeekFields).count();

    for (var task : this.Tasks)
    {
      for (int columnIndex = 0; columnIndex < this.ColumnNum; columnIndex++)
      {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = columnIndex; // 列番号
        gbc.gridy = rowIndex; // 行番号
        gbc.fill = GridBagConstraints.BOTH; // ボタンのサイズをセルいっぱいに広げる
        gbc.weightx = 1.0; // 各列の幅を均等にする
        gbc.weighty = 1.0; // 各行の高さを均等にする
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel backgroundPanel = new JPanel();

        if (columnIndex < preWeekColumnCount)
        {
          // 項目表示部分
          var type = this.ColumnLabelInfoTables.get(FieldColumnType.GetType(columnIndex));
          Font currentFont = null;

          switch (FieldColumnType.GetType(columnIndex))
          {
            // 進捗率以外はラベル
            case FieldColumnType.TaskName:
              gbc.weightx = 0;

            case FieldColumnType.StartDate:
            case FieldColumnType.EndDate:
              JLabel label = new JLabel();

              if (FieldColumnType.GetType(columnIndex) == FieldColumnType.TaskName)
              {
                label.setText(task.TaskName);
                label.setToolTipText(task.TaskName);
              }
              else if (FieldColumnType.GetType(columnIndex) == FieldColumnType.StartDate)
              {
                label.setText(task.StartDate != null ? task.StartDate.format(this.Formatter) : "");
              }
              else if (FieldColumnType.GetType(columnIndex) == FieldColumnType.EndDate)
              {
                label.setText(task.EndDate != null ? task.EndDate.format(this.Formatter) : "");
              }
              
              label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
              label.setHorizontalAlignment(SwingConstants.CENTER);
              label.setPreferredSize(new Dimension(type.Width, 1));
              label.setMinimumSize(new Dimension(type.Width, 1));

              currentFont = label.getFont();
              label.setFont(new Font(currentFont.getName(), currentFont.getStyle(), type.FontSize));
              
              // Gridに設定
              this.add(label, gbc);
              break;
            case FieldColumnType.ProgressRate:
              // 進捗率はボタン
              JButton button = new JButton();
              button.setText(String.valueOf(task.ProgressRate) + "%");
              button.setBorder(BorderFactory.createLineBorder(Color.BLACK));
              button.setHorizontalAlignment(SwingConstants.CENTER);
              button.setContentAreaFilled(false);
              button.setFocusPainted(false);

              currentFont = button.getFont();
              button.setFont(new Font(currentFont.getName(), currentFont.getStyle(), type.FontSize));

              // 押下時の処理を設定（該当タスクのインスタンスを設定）
              button.setActionCommand(this.ProgressRateButtonActionCommand);
              button.putClientProperty(this.ProgressRateButtonPutClientPropertyName, task.Clone());

              // Gridに設定
              this.add(button, gbc);
              break;
            default:
              // Enum定義のためあり得ない。
              break;
          }

          backgroundPanel.setBackground(Color.white);
        }
        else
        {
          // ウィーク表示部分
          var offset = columnIndex - preWeekColumnCount;

          // 同じ月かどうかを判断
          if (offset + 1 <= this.ActiveYearMonth.lengthOfMonth())
          {
            this.SetBackgroundPanelColor(backgroundPanel, this.ActiveYearMonth.atDay(offset + 1), task.StartDate, task.EndDate);
            backgroundPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
          }
          else
          {
            // 翌月のため余りセルを透明で設定
            backgroundPanel.setBackground(null);
          }
        }

        this.add(backgroundPanel, gbc);
      }

      rowIndex++;
    }
  }

  /**
   * 引数で入力したパネルに対応する日付とタスクの開始日終了日を元に背景色を設定
   * @param panel 対象のJPanelインスタンス（ここで入力されたインスタンスに直接設定）
   * @param panelDate 対象のJPanelと対応する日付
   * @param taskStartDate タスクの開始日
   * @param taskEndDate タスクの終了日
   */
  private void SetBackgroundPanelColor(JPanel panel, LocalDate panelDate, LocalDate taskStartDate, LocalDate taskEndDate)
  {
    if (panelDate.isEqual(this.Today))
    {
      // 本日の場合
      panel.setBackground(Color.red);
    }
    else if (taskStartDate != null && taskEndDate != null)
    {
      if ((!panelDate.isBefore(taskStartDate)) && (!panelDate.isAfter(taskEndDate)))
      {
        // タスク期間内の場合（開始日と終了日の両方が設定されている）
        panel.setBackground(Color.yellow);
      }
      else
      {
        // タスク期間外の場合
        panel.setBackground(Color.white);
      }
    }
    else
    {
      // 上記以外の場合
      panel.setBackground(Color.white);
    }
  }

  /**
   * タスク進捗率ボタン押下時の処理
   * @param e アクションイベント
   */
  private void ProgressRateButtonClicked(ActionEvent e)
  {
    if (e.getSource() instanceof JButton)
    {
      var button = (JButton) e.getSource();

      if (button.getClientProperty(this.ProgressRateButtonPutClientPropertyName) instanceof GanttchartTask)
      {
        var selectedTask = (GanttchartTask) button.getClientProperty(this.ProgressRateButtonPutClientPropertyName);

        for (GanttchartPanelListener listener : this.ListenerList.getListeners(GanttchartPanelListener.class))
        {
          // 選択されたガントチャートタスクのインスタンスを通知
          listener.OnTaskProgressRateClicked(selectedTask);
        }
      }
    }
  }

  /**
   * 項目ラベルクラス
   */
  private class FieldLabelInfo
  {
    /*
     * 表示文言
     */
    private String Text;

    /*
     * 表示フォントサイズ
     */
    private int FontSize;

    /*
     * ラベルパーツの横幅
     */
    private int Width;

    /**
     * コンストラクタ
     * @param text 表示文言
     * @param size 表示フォントサイズ
     * @param width ラベルパーツの横幅
     */
    public FieldLabelInfo(String text, int size, int width)
    {
      this.Text = text;
      this.FontSize = size;
      this.Width = width;
    }
  }
}
