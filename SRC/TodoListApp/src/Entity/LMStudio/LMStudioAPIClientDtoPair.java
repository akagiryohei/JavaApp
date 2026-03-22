package Entity.LMStudio;

/**
 * LM Studio接続クライアントタプル定義
 */
public class LMStudioAPIClientDtoPair<V1, V2> 
{
    /** 変数1 */
    public final V1 Value1;

    /** 変数2 */
    public final V2 Value2;

    /**
     * コンストラクタ
     * 依存性注入
     * @param value1 変数1（任意型）
     * @param value2 変数2（任意型）
     */
    public LMStudioAPIClientDtoPair(V1 value1, V2 value2)
    {
        this.Value1 = value1;
        this.Value2 = value2;
    }
}
