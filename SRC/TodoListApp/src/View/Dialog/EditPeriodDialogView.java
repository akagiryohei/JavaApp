package View.Dialog;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.event.EventListenerList;

import Entity.Dialog.Constants;
import View.Dialog.Listener.DatePickerListener;
import View.Dialog.Listener.EditPeriodDialogViewListener;
import View.Dialog.Listener.ReminderDialogViewListener;


public class EditPeriodDialogView extends JDialog implements ActionListener, MouseListener, DatePickerListener {
    /** 指示文言 */
    private JLabel OperationLabel;
    /** 開始日 */
    private JLabel StartLabel;
    /** 終了日 */
    private JLabel EndLabel;
    /** 編集ボタン */
    private JButton EditButton;
    /** 開始日入力欄 */
    private JFormattedTextField StartDateInputField;
    /** 終了日入力欄 */
    private JFormattedTextField EndDateInputField;
    // 期日のフォーマット
    private SimpleDateFormat DateFormat;
    // 開始日
    private String StartDate;
    // 終了日
    private String EndDate;
    // イベントリスナインスタンス
    protected EventListenerList ListenerList;
    // カレンダーダイアログ
    private DatePickerView DatePickerView;
    // 選択フラグ
    private String SelectFlg;
    // 編集モード：true 登録モード:false
    private boolean IsEditMode;

    public EditPeriodDialogView(DatePickerView datePickerView) {
        this.setSize(400, 256);
        this.setLayout(null);
        this.DatePickerView = datePickerView;
        this.IsEditMode = false;

        // 期日のフォーマットを宣言しておく
        this.DateFormat = new SimpleDateFormat("yyyy-MM-dd");
        // 指示文言
        this.OperationLabel = new JLabel(Constants.DATE_EDIT_OPERATION);
        OperationLabel.setBounds(76,10,350,35);
        this.add(OperationLabel);

        JLabel startLabel = new JLabel("開始日");
        startLabel.setBounds(76,48,100,32);
        this.add(startLabel);
        JLabel endLabel = new JLabel("終了日");
        endLabel.setBounds(76,80,100,32);
        this.add(endLabel);
        // 開始日入力フィールド
        this.StartDateInputField = new JFormattedTextField(this.DateFormat);
        this.StartDateInputField.setBounds(176,48,100,32);
        this.StartDateInputField.setColumns(1);
        this.add(this.StartDateInputField);
        // 終了日入力フィールド
        this.EndDateInputField = new JFormattedTextField(this.DateFormat);
        this.EndDateInputField.setBounds(176,80,100,32);
        this.EndDateInputField.setColumns(1);
        this.add(this.EndDateInputField);

        // 編集ボタン
        this.EditButton = new JButton();
        this.EditButton.setBounds(162,130,76,35);
        this.EditButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(this.EditButton);
        // イベントリスナインスタンスを初期化
        this.ListenerList = new EventListenerList();

    }

    /**
     * 表示メソッド
     */
    public void Show(String startDate, String endDate, boolean isEditMode) {
        this.StartDate = startDate;
        this.EndDate = endDate;
        this.IsEditMode = isEditMode;
        // 開始日入力フィールド
        this.StartDateInputField.setText(this.StartDate);

        // 終了日入力フィールド
        this.EndDateInputField.setText(this.EndDate);

        if (this.IsEditMode)
        {
            this.EditButton.setText("編集");
            this.EditButton.setActionCommand("EditPeriodDate");
        }
        else
        {
            this.EditButton.setText("セット");
            this.EditButton.setActionCommand("SetPeriodDate");
        }
        this.EditButton.addActionListener(this);
        this.StartDateInputField.addMouseListener(this);
        this.EndDateInputField.addMouseListener(this);
        this.DatePickerView.AddListener(this);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    /**
     * 非表示メソッド
     */
    public void Hide() {
        this.EditButton.removeActionListener(this);
        this.EditButton.setText("");
        this.EditButton.setActionCommand("");
        this.setVisible(false);
        this.dispose();
        this.DatePickerView.RemoveListener(this);
        this.DatePickerView.Hide();
    }

    /**
     * リスナ対象追加
     * @param listener 追加対象リスナインスタンス
     */
    public void AddListener(EditPeriodDialogViewListener listener)
    {
        this.ListenerList.add(EditPeriodDialogViewListener.class, listener);
    }

    /**
     * リスナ対象削除
     * @param listener 削除対象リスナインスタンス
     */
    public void RemoveListener(EditPeriodDialogViewListener listener)
    {
        this.ListenerList.remove(EditPeriodDialogViewListener.class, listener);
    }

    /**
     * ボタンのアクションリスナー
     */
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "EditPeriodDate":
                this.EditPeriodDateClicked();
                break;
            case "SetPeriodDate":
                this.SetPeriodDateClicked();
                break;
            default:
                break;
        }
    }

    /**
     * 編集ボタン押下時処理
     */
    public void EditPeriodDateClicked() {
        for (EditPeriodDialogViewListener listener : this.ListenerList.getListeners(EditPeriodDialogViewListener.class))
        {
            // 編集ボタン押下を通知
            listener.EditPeriodButtonClicked((Date)this.StartDateInputField.getValue(), (Date)this.EndDateInputField.getValue());
        }
        this.Hide();
    }

    /**
     * 期日セットボタン押下時処理
     */
    private void SetPeriodDateClicked()
    {
        for (EditPeriodDialogViewListener listener : this.ListenerList.getListeners(EditPeriodDialogViewListener.class))
        {
            // 期日セットボタン押下を通知
            listener.SetPeriodButtonClicked((Date)this.StartDateInputField.getValue(), (Date)this.EndDateInputField.getValue());
        }
        this.Hide();
    }

    /**
    * コンポーネント座標を取得する
    * @param 取得対象コンポーネントオブジェクト
    * @return 取得対象コンポーネント表示座標
    */
    public Point GetComponentPostion(Component component)
    {
        int titleBarHeight = 0;
        Insets insets = this.getInsets();
        titleBarHeight = insets.top;

        int x = this.getLocation().x + component.getX();
        int y = (this.getLocation().y + titleBarHeight) + component.getY() + component.getHeight();
        Point ret = new Point(x, y);

        return ret;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Object source = e.getSource();
        if (source == this.StartDateInputField) {
            this.SelectFlg = "Start";
        } else if (source == this.EndDateInputField) {
            this.SelectFlg = "End";
        } else {
            return;
        }
        this.DatePickerView.Show(YearMonth.of(2025, 8), this.GetComponentPostion((Component)this.StartDateInputField));
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void SelectedDate(LocalDate selectedDate) {
        Date date = java.sql.Date.valueOf(selectedDate);
        if (this.SelectFlg == "Start") {
            // 開始日入力フィールドに選択日を設定
            this.StartDateInputField.setValue(date);
        } else if (this.SelectFlg == "End") {
            // 終了日入力フィールドに選択日を設定
            this.EndDateInputField.setValue(date);
        }
        this.DatePickerView.Hide();
    }
}
