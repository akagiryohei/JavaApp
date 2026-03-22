package Entity.Enum;

public enum PropertyKey
{
    // 列挙
    DBConnectionString("DBConnectionString"),
    DBDriverName("DBDriverName"),
    DBUserName("DBUserName"),
    DBPassword("DBPassword"),
    DBName("DBName"),
    DisplayUpdateInterval("DisplayUpdateInterval"),
    LogRotationSize("LogRotationSize"),
    // 接続先サーバー
    BaseUrl("BaseUrl"),
    // チャット機能のAPIパス
    ChatPath("ChatPath"),
    // LM Studioでロード中のモデル名
    ModelName("ModelName"),
    // 仮のAPIキー
    ApiKey("ApiKey"),
    // システムプロンプト
    SystemPrompt("SystemPrompt");

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