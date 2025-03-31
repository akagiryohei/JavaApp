package Model;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import Entity.Enum.PropertyKey;

public class GetProperties
{
  Properties properties;

  // コンストラクタ
  public GetProperties(String filePath)
  {
    this.properties = new Properties();

    try {
      this.properties.load(Files.newBufferedReader(Paths.get(filePath), StandardCharsets.UTF_8));

    } catch (IOException e) {
      // ファイル読み込みに失敗した
    }
  }

  // プロパティの値を取得する
  // TODO: 取得に失敗した場合にエラーが発生しないように要対策。defaultValueに何かしらをセットしておく？
  public String getProperty(PropertyKey key) {
    return this.properties.getProperty(key.getValue(), "");
  }
}
