package View.Todo;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.EventListenerList;
import java.util.ArrayList;
import java.util.List;

import Entity.UserList;
import Entity.Enum.LogLevel;
import View.Common.JPlaceholderTextField;
import View.Todo.Listener.TodoSideBoardPanelListener;
import View.Todo.Listener.TodoSideCommonPanelListener;


/*
 * リスト・ガントチャート共通設定
 */
public class TodoSideCommonPanel extends TodoSideBasePanel implements ActionListener, MouseListener, DocumentListener
{
    // イベントリスナインスタンス
    protected EventListenerList ListenerList;

    // +ボタン
    private JButton PlusButton;

    // リスト名入力欄
    private JPlaceholderTextField ListNameInputField;

    // リスト一覧
    private JList<String> ScrollList;

    // スクロールリストパネル
    private JScrollPane ScrollListPane;

    // リストリスト
    private List<UserList> UserLists;

    // リストモデル
    private DefaultListModel ListModel;

    // ポップアップメニュー
    private JPopupMenu Popup;

    // 選択中リストのID
    private int SelectedListId;

    // 選択中リストのリスト名
    private String ListSelectListName;

    // 削除メニュー
    private JMenuItem DeleteMenuItem;

    // 編集メニュー
    private JMenuItem UpdateMenuItem;

    /**
     * コンストラクタ
     */
    public TodoSideCommonPanel()
    {
        // イベントリスナインスタンスを初期化
        this.ListenerList = new EventListenerList();

        this.setLayout(null);
        this.setBackground(Color.PINK);

        this.UserLists = new ArrayList<UserList>();
        this.ListModel = new DefaultListModel();

        this.ScrollList = new JList();
        this.ScrollList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.ScrollList.setModel(this.ListModel);
        this.ScrollListPane = new JScrollPane(this.ScrollList);
        this.ScrollListPane.setBounds(0,192,230,336);
        this.add(ScrollListPane);

        // リスト名入力欄設定ListNameInputField
        this.ListNameInputField = new JPlaceholderTextField();
        this.ListNameInputField.SetPlaceholderText("新規リスト");
        this.ListNameInputField.setBounds(76,528,152,35);
        this.ListNameInputField.setColumns(1);
        this.add(this.ListNameInputField);

        // +ボタン設定
        this.PlusButton = new JButton("＋");
        this.PlusButton.setActionCommand("PlusButton");
        this.PlusButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.PlusButton.setBounds(0,528,76,35);
        this.add(this.PlusButton);

        // ポップアップメニュー設定
        this.Popup = new JPopupMenu();
        this.DeleteMenuItem = new JMenuItem();
        this.UpdateMenuItem = new JMenuItem();
        this.DeleteMenuItem.setActionCommand("DeleteListButton");
        this.UpdateMenuItem.setActionCommand("UpdateListButton");
        this.Popup.add(this.DeleteMenuItem);
        this.Popup.add(this.UpdateMenuItem);
    }
    public void mouseReleased(MouseEvent e){
        showPopup(e);
    }

    public void mousePressed(MouseEvent e){
        showPopup(e);
    }
    /**
     * リストクリック時イベント
     */
    public void mouseClicked(MouseEvent e){
        var index = this.ScrollList.getMinSelectionIndex();
        if (index < 0) {
            return;
        }
        this.SelectedListId = this.UserLists.get(index).id;
        this.ListSelectListName = this.UserLists.get(index).listName;
        for (TodoSideCommonPanelListener listener : this.ListenerList.getListeners(TodoSideCommonPanelListener.class))
        {
            listener.UserListButtonClicked(this.SelectedListId, this.ListSelectListName);
        }
    }
    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}
    private void showPopup(MouseEvent e){
        if (e.isPopupTrigger()) {
            var index = this.ScrollList.getMinSelectionIndex();
            if(index < 0)
            {
                return;
            }
            var text = this.ListModel.getElementAt(index);
            this.SelectedListId = this.UserLists.get(index).id;
            this.ListSelectListName = this.UserLists.get(index).listName;
            // ボタン押下時のイベントを取得できるように設定
            this.DeleteMenuItem.setText(text + "を削除");
            this.UpdateMenuItem.setText(text + "を更新");
            this.Popup.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    public void Show()
    {
        this.BoardButton.addActionListener(this);
        this.ListButton.addActionListener(this);
        this.GanttchartButton.addActionListener(this);
        this.AICreateListTaskButton.addActionListener(this);
        this.PlusButton.addActionListener(this);
        this.ScrollList.addMouseListener(this);
        this.DeleteMenuItem.addActionListener(this);
        this.UpdateMenuItem.addActionListener(this);
        this.ListNameInputField.getDocument().addDocumentListener(this);
        this.PlusButton.setEnabled(false);
    }

    public void Hide()
    {
        this.BoardButton.removeActionListener(this);
        this.ListButton.removeActionListener(this);
        this.GanttchartButton.removeActionListener(this);
        this.AICreateListTaskButton.removeActionListener(this);
        this.PlusButton.removeActionListener(this);
        this.ScrollList.removeMouseListener(this);
        this.DeleteMenuItem.removeActionListener(this);
        this.UpdateMenuItem.removeActionListener(this);
        this.ListNameInputField.setText("");
        this.ListNameInputField.getDocument().removeDocumentListener(this);
    }

    /**
     * リスナ対象追加
     * @param listener 追加対象リスナインスタンス
     */
    public void AddListener(TodoSideCommonPanelListener listener)
    {
        this.ListenerList.add(TodoSideCommonPanelListener.class, listener);
    }

    /**
     * リスナ対象削除
     * @param listener 削除対象リスナインスタンス
     */
    public void RemoveListener(TodoSideCommonPanelListener listener)
    {
        this.ListenerList.remove(TodoSideCommonPanelListener.class, listener);
    }

    /**
     * ボタンからのアクションリスナー
     */
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
            case "AICreateListTaskButton":
                // AIリスト・タスク案作成ボタン押下イベントを受信
                this.AICreateListTaskButtonClicked();
                break;
            case "PlusButton":
                this.PlusButton();
                break;
            case "DeleteListButton":
                // リスト削除ボタン押下イベントを受診
                this.DeleteListButtonClicked();
                break;
            case "UpdateListButton":
                // リスト更新ボタン押下イベントを受信
                this.UpdateListButtonClicked();
                break;
            default:
                // ロジック上あり得ない
                break;
        }
    }
    /**
     * ボードボタンクリック時の処理
     */
    private void BoardButtonClicked()
    {
        System.out.println("ボードボタンが押下された");

        for (TodoSideCommonPanelListener listener : this.ListenerList.getListeners(TodoSideCommonPanelListener.class))
        {
            listener.BoardButtonClicked();
        }
    }

    /**
     * リストボタンクリック時の処理　現在リスト画面であるかを確認する必要がある
     */
    private void ListButtonClicked()
    {
        System.out.println("リスト作成ボタンが押下された");

        for (TodoSideCommonPanelListener listener : this.ListenerList.getListeners(TodoSideCommonPanelListener.class))
        {
            listener.ListButtonClicked();
        }
    }

    /**
     * ガントチャートボタンクリック時の処理
     */
    private void GanttchartButtonClicked()
    {
        System.out.println("ガントチャートボタンが押下された");

        for (TodoSideCommonPanelListener listener : this.ListenerList.getListeners(TodoSideCommonPanelListener.class))
        {
            listener.GanttchartButtonClicked();
        }
    }

    /**
     * AIリスト・タスク案作成ボタンクリック時の処理
     */
    private void AICreateListTaskButtonClicked()
    {
        System.out.println("AIリスト・タスク案作成ボタンが押下された");
        for (TodoSideCommonPanelListener listener : this.ListenerList.getListeners(TodoSideCommonPanelListener.class))
        {
            listener.AICreateListTaskButtonClicked();
        }
    }


    /**
     * ＋ボタン押下時処理
     */
    private void PlusButton()
    {
        String listText = this.ListNameInputField.getText();
        System.out.println(listText);
        for (TodoSideCommonPanelListener listener : this.ListenerList.getListeners(TodoSideCommonPanelListener.class))
        {
            listener.CreateUserList(listText);
        }
    }

    /**
     * デリートボタン押下時処理
     */
    private void DeleteListButtonClicked()
    {
        for (TodoSideCommonPanelListener listener : this.ListenerList.getListeners(TodoSideCommonPanelListener.class))
        {
            listener.DeleteListButtonClicked(this.SelectedListId);
        }
    }

    /**
     * アップデートボタン押下時処理
     */
    private void UpdateListButtonClicked()
    {
        // ダイアログを呼び出す処理を書くべき
        for (TodoSideCommonPanelListener listener : this.ListenerList.getListeners(TodoSideCommonPanelListener.class))
        {
            listener.UpdateListDialog(this.SelectedListId, this.ListSelectListName);
        }
    }

    /**
     * リストセットメソッド
     * @param list
     */
    public void SetList(List<UserList> list)
    {
        this.ListModel.clear();
        list.forEach(listItem -> {
            this.UserLists.add(listItem.Clone());
            this.ListModel.addElement(listItem.listName);
        });
    }

    /**
     * リスト名入力欄クリア
     */
    public void ClearListNameInputField()
    {
        this.ListNameInputField.setText("");
    }

    /**
     * 選択中のリストID取得メソッド
     * @return 選択中リストのID
     */
    public int GetSelectedListId()
    {
        return this.SelectedListId;
    }

    /**
     * リスト名が入力されたとき
     */
    @Override
    public void insertUpdate(DocumentEvent e)
    {
        this.ChangedTextField(e);
    }

    /**
     * リスト名が消されたとき
     */
    @Override
    public void removeUpdate(DocumentEvent e)
    {
        this.ChangedTextField(e);
    }

    /**
     * Controllerで渡す：isBusyで使う
     * @param isBusy 処理中かどうか
     */
    public void ElementDisabled(boolean isDisabled)
    {
        this.WithLogger((logger) -> { logger.WriteLog(LogLevel.Info, "画面要素押下可否設定" + "(" + String.valueOf(isDisabled) + ")"); });
        this.PlusButton.setEnabled(!isDisabled);
        this.ListNameInputField.setEnabled(!isDisabled);
        this.ScrollList.setEnabled(!isDisabled);
        this.BoardButton.setEnabled(!isDisabled);
        this.ListButton.setEnabled(!isDisabled);
        this.GanttchartButton.setEnabled(!isDisabled);
    }

    /**
     * リスト名が変更されたときの入力監視
     * @param e 入力欄のインスタンス情報
     */
    private void ChangedTextField(DocumentEvent e)
    {
        if (e.getDocument() == this.ListNameInputField.getDocument())
        {
            String listName = this.ListNameInputField.getText();
            this.PlusButton.setEnabled(!listName.isEmpty());
        }
    }
    @Override
    public void changedUpdate(DocumentEvent e) {
        // ここは何もしない
    }
}
