package View.Common;

import javax.swing.*;
import java.awt.*;

/**
  * プレースホルダ対応テキストフィールドView
  */
public class JPlaceholderTextField extends JTextField
{
    /**
     * プレースホルダ文字デフォルト色
     */
    private final Color DefaultPlaceholderTextColor = Color.LIGHT_GRAY;

    /**
     * プレースホルダ文字
     */
    private String PlaceholderText;

    /**
     * プレースホルダ文字色
     */
    private Color PlaceholderTextColor;

    /**
     * コンストラクタ
     */
    public JPlaceholderTextField()
    {
        this.PlaceholderText = "";
        this.PlaceholderTextColor = null;
    }

    /**
     * コンストラクタ
     * @param columns JTextFieldに設定
     */
    public JPlaceholderTextField(int columns)
    {
        super(columns);
        this.PlaceholderText = "";
        this.PlaceholderTextColor = null;
    }

    /**
     * プレースホルダ文字を設定
     * @param columns プレースホルダ文字
     */
    public void SetPlaceholderText(String placeholderText)
    {
        this.PlaceholderText = placeholderText;
    }

    /**
     * プレースホルダ文字色を設定
     * @param c プレースホルダ文字色
     */
    public void SetPlaceholderColor(Color c)
    {
        this.PlaceholderTextColor = c;
    }

    /**
     * 描画関数
     * @param g グラフィックスオブジェクト
     */
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        if (getText().isEmpty() && !isFocusOwner())
        {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(this.PlaceholderTextColor != null ? this.PlaceholderTextColor : this.DefaultPlaceholderTextColor);
            g2.setFont(getFont());
            Insets insets = getInsets();
            FontMetrics fm = g2.getFontMetrics();
            int x = insets.left;
            int y = getHeight() / 2 + fm.getAscent() / 2 - 2;
            g2.drawString(this.PlaceholderText, x, y);
            g2.dispose();
        }
    }
}
