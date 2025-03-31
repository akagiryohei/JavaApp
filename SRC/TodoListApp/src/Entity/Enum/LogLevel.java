package Entity.Enum;

public enum LogLevel
{
    // 列挙
    Info("情報"),
    Warning("警告"),
    Exception("例外");

    // コンストラクタ
    private String name;
    private LogLevel(String name)
    {
        this.name = name;
    }

    // name を返すメソッド
    public String getValue()
    {
        return this.name;
    }
}