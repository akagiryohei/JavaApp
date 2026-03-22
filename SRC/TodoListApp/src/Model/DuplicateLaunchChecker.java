package Model;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

import Interface.Model.IDuplicateLaunchChecker;

/**
 * 多重起動チェッカー
 */
public class DuplicateLaunchChecker implements IDuplicateLaunchChecker
{
  /**
   * ロックファイルのパス
   */
  private String LockFilePath;

  /**
   * ロックファイルへのアクセス用オブジェクト
   */
  private RandomAccessFile LockFile;

  /**
   * ロックファイルのチャンネル（ロック取得に使用）
   */
  private FileChannel LockFileChannel;

  /**
   * ファイルロックオブジェクト（排他制御）
   */
  private FileLock Lock;

  /**
   * コンストラクタ
   * @param lockFilePath ロックファイルのパス
   */
  public DuplicateLaunchChecker(String lockFilePath)
  {
    this.LockFilePath = lockFilePath;
  }

  /**
   * ロック取得して、多重起動ではないかを確認する
   * @return true: ロック取得成功（多重起動ではない）、false: ロック取得失敗（多重起動に該当）
   */
  public boolean TryLock()
  {
    try
    {
      this.LockFile = new RandomAccessFile(this.LockFilePath, "rw");
      this.LockFileChannel = this.LockFile.getChannel();
      this.Lock = this.LockFileChannel.tryLock();
      return this.Lock != null;
    }
    catch (IOException e)
    {
      // アプリ起動前（ロガー生成前）にあたるためログ出力できない
      return false;
    }
  }

  /**
   * ロックを解放し、関連するリソースをクローズする
   */
  public void Release()
  {
    try
    {
      if (this.Lock != null && this.Lock.isValid())
      {
        this.Lock.release();
      }

      if (this.LockFileChannel != null && this.LockFileChannel.isOpen())
      {
        this.LockFileChannel.close();
      }

      if (this.LockFile != null)
      {
        this.LockFile.close();
      }
    }
    catch (IOException e)
    {
      // タイミングによってはアプリ起動前（ロガー生成前）にあたるためログ出力できない
    }
  }
}
