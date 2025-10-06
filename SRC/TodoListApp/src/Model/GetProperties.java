package Model;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import Entity.Pair;
import Entity.Enum.PropertyKey;
import Interface.Model.IGetProperties;
import View.Dialog.CommonDialogView.CommonDialogType;

public class GetProperties implements IGetProperties
{
  Properties properties;

  private final HashMap<PropertyKey, String> DefaultValueTable = new HashMap<PropertyKey, String>()
  {
      {
          put(PropertyKey.DBConnectionString, "");
          put(PropertyKey.DBDriverName, "");
          put(PropertyKey.DBUserName, "");
          put(PropertyKey.DBPassword, "");
          put(PropertyKey.DBName, "");
          put(PropertyKey.DisplayUpdateInterval, "");
          put(PropertyKey.LogRotationSize, "536870912");
      }
  };

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
  public String getProperty(PropertyKey key) {
    return this.properties.getProperty(key.getValue(), this.DefaultValueTable.get(key));
  }
}