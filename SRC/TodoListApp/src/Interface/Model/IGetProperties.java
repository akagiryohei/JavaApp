package Interface.Model;

import Entity.Enum.PropertyKey;

/*
 * GetPropertiesのインタフェース
 */
public interface IGetProperties
{
  /**
   * プロパティの値を取得する
   * @param key 取得対象のキー
   * @return 取得対象の設定値
   */
  public String getProperty(PropertyKey key);

  /**
   * ファイルをロードする
   */
  public void Load();
}