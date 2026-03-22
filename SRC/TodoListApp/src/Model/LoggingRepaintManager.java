package Model;

import java.awt.*;
import javax.swing.*;
import Entity.Enum.LogLevel;
import Interface.Model.ILogger;

/**
 * ログ出力用再描画マネージャ
 */
public class LoggingRepaintManager extends RepaintManager
{
    /**
     * ロガーインスタンス
     */
    private ILogger Logger;

    /**
     * コンストラクタ
     * @param logger ロガーインスタンス
     */
    public LoggingRepaintManager(ILogger logger)
    {
        this.Logger = logger;
    }
    
    /**
     * 描画要求を追加（併せてログ出力）
     * @param 描画要求対象コンポーネント
     * @param c Component to repaint, null results in nothing happening.
     * @param x X coordinate of the region to repaint
     * @param y Y coordinate of the region to repaint
     * @param w Width of the region to repaint
     * @param h Height of the region to repaint
     */
    @Override
    public void addDirtyRegion(JComponent c, int x, int y, int w, int h)
    {
        String name = c.getName();

        // 名前定義が存在しないコンポーネントはログ出力の対象外とする。
        if (name != null && !name.isEmpty())
        {
            // コンポーネントのクラス名（フルパス）
            String type = c.getClass().getName();

            // Swing関連のコンポーネントのみ対象とする
            if (type.startsWith("javax.swing."))
            {
                String target = "";
                var owner = this.GetNearestPanelOrFrameInstance(c);
                var instanceId = System.identityHashCode(owner);

                if (owner instanceof JPanel)
                {
                    target = owner.getClass().getName();
                }
                else if (owner instanceof JFrame)
                {
                    target = ((JFrame) owner).getTitle();
                }
                else
                {
                    target = "Unknown";
                }

                // コンポーネントの描画領域
                Rectangle bounds = c.getBounds();

                // ログ出力
                this.Logger.WriteLog(LogLevel.Info, "【描画要求】" + " type=" + type + " target=" + target + (owner != null ? "." + name + " (instanceId=" + instanceId + ")" : "") + " bounds=" + bounds);
            }
        }

        // 元の処理を呼出
        super.addDirtyRegion(c, x, y, w, h);
    }

    /**
     * 入力コンポーネント直上のJPanelまたはJFrameのインスタンスを取得する
     * @param c 対象コンポーネント
     * @return 直上のJPanelまたはJFrameのインスタンス、検索できなかった場合はnull
     */
    private Component GetNearestPanelOrFrameInstance(Component c)
    {
        Container parent = c.getParent();
        while (parent != null)
        {
            if (parent instanceof JPanel)
            {
                // フルパスのクラス名を返す
                return parent;
            }

            parent = parent.getParent();
        }

        // JPanelが見つからなかった場合はJFrameのタイトルを返す
        Window window = SwingUtilities.getWindowAncestor(c);
        if (window instanceof JFrame)
        {
            return window;
        }

        return null;
    }
}
