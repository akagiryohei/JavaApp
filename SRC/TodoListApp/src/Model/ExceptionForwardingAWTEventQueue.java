package Model;

import java.awt.*;

/**
 * 例外スロー処理追加AWTイベントキュー
 */
public class ExceptionForwardingAWTEventQueue extends EventQueue
{
    /**
     * 例外スロー処理追加ディスパッチイベント
     * @param event AWTイベント
     */
    @Override
    protected void dispatchEvent(AWTEvent event)
    {
        try
        {
            // 例外が発生しなかった場合は処理を継続する
            super.dispatchEvent(event);
        }
        catch (Throwable t)
        {
            // 例外発生時はスローする（ログ出力はフック処理に委ねる）
            throw new RuntimeException(t);
        }
    }
}
