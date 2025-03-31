package Entity.Enum;

public enum PropertyKey
{
    // 列挙
    DBHostIPAddress("DBHostIPAddress"),
    DBHostPortNum("DBHostPortNum"),
    DBUserName("DBUserName"),
    DBPassword("DBPassword"),
    DBName("DBName"),
    DBAccessTimeout("DBAccessTimeout"),
    DisplayUpdateInterval("DisplayUpdateInterval"),
    LogRotationSize("LogRotationSize");

    // コンストラクタ
    private String name;
    private PropertyKey(String name)
    {
        this.name = name;
    }

    // 文字列を返すメソッド
    public String getValue()
    {
        return this.name;
    }
}