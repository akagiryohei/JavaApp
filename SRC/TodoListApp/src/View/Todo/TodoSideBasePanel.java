package View.Todo;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import View.JPanelViewBase;

/*
 * Sideパネル基底クラス
 */
public abstract class TodoSideBasePanel extends JPanelViewBase implements ActionListener
{
    // ログインユーザー名ラベル
    private JLabel LoginUserNameLabel;

    // ボードボタン
    public JButton BoardButton;

    // リストボタン
    public JButton ListButton;

    // ガントチャートボタン
    public JButton GanttchartButton;

    public TodoSideBasePanel()
    {
        this.setLayout(null);
        this.setBackground(Color.PINK);

        // ログインユーザー名ラベルの設定
        // 何文字上限かはまた設定
        this.LoginUserNameLabel = new JLabel("user@gmail.com");
        this.LoginUserNameLabel.setBounds(0,0,230,64);
        this.LoginUserNameLabel.setVerticalAlignment(JLabel.CENTER); //垂直位置
        this.LoginUserNameLabel.setHorizontalAlignment(JLabel.CENTER); //水平位置
        this.LoginUserNameLabel.setOpaque(true);
        this.LoginUserNameLabel.setBackground(Color.CYAN);
        
        this.add(LoginUserNameLabel);

        // ボードボタンの設定
        this.BoardButton = new JButton("ボード");
        this.BoardButton.setActionCommand("BoardButton");
        this.BoardButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.BoardButton.setBounds(0,64,230,32);
        this.add(this.BoardButton);

        // リストボタンの設定
        this.ListButton = new JButton("リスト");
        this.ListButton.setActionCommand("ListButton");
        this.ListButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.ListButton.setBounds(0,96,230,32);
        this.add(this.ListButton);

        // ガントチャートボタンの設定
        this.GanttchartButton = new JButton("ガントチャート");
        this.GanttchartButton.setActionCommand("GanttchartButton");
        this.GanttchartButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.GanttchartButton.setBounds(0,128,230,32);
        this.add(this.GanttchartButton);

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
