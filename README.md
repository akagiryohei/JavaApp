# TodoListApp

Java Swing 製のデスクトップ型 Todo 管理アプリです。
MySQL によるデータ永続化と、LM Studio（ローカル LLM）による AI タスク生成機能を持ちます。

---

## 機能

- ユーザー登録・ログイン・パスワードリセット
- Todo リスト／タスクの作成・編集・削除
- タスクへの開始日・終了日・進捗率の設定
- ガントチャート表示
- リマインダー（未完了タスクの一覧表示）
- AI によるリスト名・タスクの自動生成（LM Studio 経由、任意）

---

## 必要な環境

| ソフトウェア | バージョン |
|-------------|-----------|
| JDK | 21 以上 |
| VS Code | 最新版 |
| VS Code 拡張機能: Extension Pack for Java | 最新版 |
| MySQL | 8.x |
| LM Studio | 任意（AI 機能を使う場合のみ） |

---

## セットアップ手順

### 1. リポジトリのクローン

```bash
git clone <リポジトリURL>
cd TodoListApp
```

### 2. MySQL のセットアップ

MySQL に接続して、以下の SQL を実行してデータベースとテーブルを作成します。

```sql
CREATE DATABASE todojava_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE todojava_db;

CREATE TABLE users (
    Id          VARCHAR(255) PRIMARY KEY,
    email       VARCHAR(255) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    secret_tips_id   VARCHAR(255),
    secret_password  VARCHAR(255)
);

CREATE TABLE secret (
    id               INT AUTO_INCREMENT PRIMARY KEY,
    secret_tips_text VARCHAR(255) NOT NULL
);

CREATE TABLE list (
    id        INT AUTO_INCREMENT PRIMARY KEY,
    list_name VARCHAR(255) NOT NULL,
    user_id   VARCHAR(255) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(Id)
);

CREATE TABLE task (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    task_text   TEXT NOT NULL,
    start_date  DATE,
    end_date    DATE,
    task_status INT  DEFAULT 0,
    user_id     VARCHAR(255) NOT NULL,
    list_id     INT  NOT NULL,
    progress    INT  DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES users(Id),
    FOREIGN KEY (list_id) REFERENCES list(id)
);
```

### 3. 設定ファイルの準備

`src/Settings.properties` を編集して、お使いの環境に合わせます（詳細は後述の「設定ファイルの説明」を参照）。

```
DBConnectionString=jdbc:mysql://localhost:3306/todojava_db?connectTimeout=10000&socketTimeout=10000&useSSL=false
DBUserName=<MySQLユーザー名>
DBPassword=<MySQLパスワード>
```

### 4. 祝日データの配置

内閣府が公開している祝日データ CSV（`syukujitsu.csv`）を `bin/` ディレクトリに配置します。

- 公開元: https://www8.cao.go.jp/chosei/shukujitsu/gaiyou.html

### 5. `bin/` への設定ファイルのコピー

ビルド後に `bin/` が作成されるため、以下のファイルを `bin/` 直下にコピーします。

```
bin/
├── Settings.properties   ← src/Settings.properties をコピー
├── syukujitsu.csv        ← 祝日 CSV を配置
└── log/                  ← 空ディレクトリを作成
```

### 6. ビルドと実行

VS Code で本プロジェクトを開き、`src/TodoListApp.java` の `main` メソッドを実行します。

- **実行**: `Run > Run Without Debugging` または `F5`

---

## 設定ファイルの説明（Settings.properties）

| キー | 説明 | 例 |
|-----|------|----|
| `DBConnectionString` | MySQL 接続文字列 | `jdbc:mysql://localhost:3306/todojava_db?...` |
| `DBDriverName` | JDBC ドライバクラス名 | `com.mysql.cj.jdbc.Driver` |
| `DBUserName` | MySQL ユーザー名 | `root` |
| `DBPassword` | MySQL パスワード | `root` |
| `DBName` | データベース名 | `todojava_db` |
| `LogRotationSize` | ログローテーションサイズ（バイト） | `536870912`（512MB） |
| `BaseUrl` | LM Studio サーバーの URL | `http://192.168.0.10:1234` |
| `ChatPath` | LM Studio のチャット API パス | `/v1/chat/completions` |
| `ModelName` | LM Studio でロード中のモデル名 | `llama-3.2-3b-instruct` |
| `ApiKey` | API キー（LM Studio は任意の文字列） | `lm-studio` |
| `SystemPrompt` | AI へのシステムプロンプト | （デフォルト値をそのまま使用可） |

### AI 機能を使わない場合

`BaseUrl` / `ChatPath` / `ModelName` / `ApiKey` / `SystemPrompt` はダミー値のままで起動できます（AI タスク生成ボタンを押さない限り影響なし）。

### LM Studio の設定（AI 機能を使う場合）

1. [LM Studio](https://lmstudio.ai/) をインストールしてモデルをロードする
2. LM Studio の「Local Server」を起動する
3. `BaseUrl` に LM Studio サーバーの URL を設定する（デフォルト: `http://localhost:1234`）
4. `ModelName` にロード中のモデル名を設定する

---

## ディレクトリ構成

```
TodoListApp/
├── src/                  # ソースコード
│   ├── TodoListApp.java  # エントリーポイント
│   ├── Settings.properties
│   ├── Controller/       # UI 操作の調整（View ↔ Model）
│   ├── Model/            # ビジネスロジック・DB/API アクセス
│   ├── View/             # Swing UI コンポーネント
│   ├── Interface/        # 全レイヤーのインターフェース定義
│   ├── Entity/           # DTO・Enum 定義
│   └── DI/               # 依存性注入ファクトリ
├── lib/                  # 依存 JAR（手動管理）
├── bin/                  # コンパイル出力（リビルドで再生成）
└── .vscode/
    └── settings.json     # VS Code Java プロジェクト設定
```

---

## 外部ライブラリ（`lib/`）

| ファイル | 用途 |
|---------|------|
| `mysql-connector-j-9.2.0.jar` | MySQL JDBC ドライバ |
| `okhttp-4.12.0.jar` | LM Studio API への HTTP 通信 |
| `gson-2.10.1.jar` | JSON のシリアライズ・デシリアライズ |
| `okio-3.6.0.jar` | OkHttp の I/O 依存ライブラリ |

---

## 注意事項

- `bin/` はリビルドで消えるため、`Settings.properties` の編集は必ず `src/Settings.properties` で行い、`bin/` へコピーしてください
- アプリ起動中に `bin/app.lock` が生成されます。異常終了後などにロックファイルが残ると次回起動時にエラーになる場合があります。その場合は `bin/app.lock` を手動で削除してください
- `bin/log/` ディレクトリが存在しないとログ出力でエラーになる場合があります。初回は手動で作成してください
