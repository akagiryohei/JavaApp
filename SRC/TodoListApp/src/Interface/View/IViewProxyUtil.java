package Interface.View;

/**
 * ViewProxyUtilのインタフェース
 */
public interface IViewProxyUtil
{
    /**
     * 指定された View インターフェースを UI スレッドで安全に呼び出せるプロキシにラップする。
     * @param viewInterface ラップ対象のインターフェース型
     * @param realView 実体の View 実装
     * @param <T> インターフェースの型
     * @return UI スレッドラップされたプロキシ View
     */
    public <T> T WrapView(Class<T> viewInterface, T realView);
    
    /**
     * プロキシ化された View インスタンスから、元の実体 View を取得する。
     * @param proxyView プロキシ化された View インスタンス
     * @param <T> View の型
     * @return 実体の View（JPanel を継承したクラス）、取得できない場合はnullを戻り値とする
     */
    public <T> T UnwrapView(T proxyView);
}
