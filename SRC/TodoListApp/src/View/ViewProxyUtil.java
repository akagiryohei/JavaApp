package View;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import Interface.View.IUIDispatcher;
import Interface.View.IViewProxyUtil;

/**
 * View インターフェースを UI スレッドで安全に操作するための動的プロキシを生成・管理するユーティリティクラス
 */
public class ViewProxyUtil implements IViewProxyUtil
{
    /**
     * 画面スレッド実行を制御するディスパッチャ
     */
    private IUIDispatcher Dispatcher;

    /**
     * コンストラクタ
     * @param dispatcher UI スレッド実行を制御するディスパッチャ
     */
    public ViewProxyUtil(IUIDispatcher dispatcher)
    {
        this.Dispatcher = dispatcher;
    }

    /**
     * 指定された View インターフェースを UI スレッドで安全に呼び出せるプロキシにラップする。
     * @param viewInterface ラップ対象のインターフェース型
     * @param realView 実体の View 実装
     * @param <T> インターフェースの型
     * @return UI スレッドラップされたプロキシ View
     */
    public <T> T WrapView(Class<T> viewInterface, T realView)
    {
        InvocationHandler handler = new ViewInvocationHandler<>(realView);
        return (T) Proxy.newProxyInstance(viewInterface.getClassLoader(), new Class<?>[]{viewInterface}, handler);
    }

    /**
     * プロキシ化された View インスタンスから、元の実体 View を取得する。
     * @param proxyView プロキシ化された View インスタンス
     * @param <T> View の型
     * @return 実体の View（JPanel を継承したクラス）、取得できない場合はnullを戻り値とする
     */
    public <T> T UnwrapView(T proxyView)
    {
        if (!Proxy.isProxyClass(proxyView.getClass()))
        {
            return null;
        }

        InvocationHandler handler = Proxy.getInvocationHandler(proxyView);
        
        if (handler instanceof ViewInvocationHandler<?>)
        {
            return ((ViewInvocationHandler<T>) handler).getRealView();
        }

        return null;
    }

    /**
     * View インターフェースのメソッド呼び出しをUIスレッドで実行するための実装
     * @param <T> ラップ対象の View インターフェース型
     */
    private class ViewInvocationHandler<T> implements InvocationHandler
    {
        private final T realView;

        /**
         * コンストラクタ。
         * @param realView 実体の View 実装
         */
        public ViewInvocationHandler(T realView)
        {
            this.realView = realView;
        }

        /**
         * ラップされた View の実体を取得
         * @return 実体の View インスタンス
         */
        public T getRealView()
        {
            return realView;
        }

        /**
         * メソッド呼び出しを UI スレッドにディスパッチして実行します。
         * @param proxy プロキシインスタンス
         * @param method 呼び出されたメソッド
         * @param args メソッド引数
         * @return 戻り値は使用しない
         * @throws Throwable 呼び出し中に発生した例外
         */
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
        {
            Dispatcher.dispatch(() ->
            {
                try
                {
                    method.invoke(realView, args);
                }
                catch (InvocationTargetException e)
                {
                    // 例外発生時はスローする（ログ出力はフック処理に委ねる）
                    throw new RuntimeException(e.getCause());
                }
                catch (Exception e)
                {
                    // 例外発生時はスローする（ログ出力はフック処理に委ねる）
                    throw new RuntimeException(e);
                }
            });

            return null;
        }
    }
}
