package Model;

import java.io.IOException;
import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import Entity.AIListTask;
import Entity.Pair;
import Entity.AI.AIResultType;
import Interface.Model.IAIAPIClient;
import Interface.Model.ILogger;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * AI接続用クライアントクラス
 */
public class LMStudioAPIClient implements IAIAPIClient{

/** 接続先サーバー */
    private String BaseUrl;

  /** チャット機能のAPIパス */
    private String ChatPath;

  /** LM Studioでロード中のモデル名 */
    private String ModelName;

  /** 仮のAPIキー */
    private String ApiKey;

  /** システムプロンプト */
    private String SystemPrompt;

  /** ロガーインスタンス */
    private ILogger Logger;


    /**
     * コンストラクタ
     * @param baseUrl 接続先サーバー
     * @param chatPath チャット機能のAPIパス
     * @param modelName LM Studioでロード中のモデル名
     * @param apiKey 仮のAPIキー
     * @param systemPrompt システムプロンプト
     */
    public LMStudioAPIClient(String baseUrl, String chatPath, String modelName, String apiKey, String systemPrompt, ILogger logger)
    {
        this.BaseUrl = baseUrl;
        this.ChatPath = chatPath;
        this.ModelName = modelName;
        this.ApiKey = apiKey;
        this.SystemPrompt = systemPrompt;
        this.Logger = logger;
    }

    /**
    * ロガーインスタンスが設定されていたらロギングする
    * @param action ロガーインスタンスが設定されていたらインスタンスをコールバック
    */
    protected void WithLogger(Consumer<ILogger> action)
    {
        Optional.ofNullable(this.Logger).ifPresent(action);
    }

    /**
        * ロガーインスタンスを依存性注入する
        * @param ロガーインスタンス
        */
    public void SetLogger(ILogger logger)
    {
        this.Logger = logger;
    }

    /**
     * AIにタスク生成の問い合わせを行う
     * @param userInput ユーザ入力
     * @return 処理結果とAIListTask（プラットフォーム非依存）
     */
    @Override
    public Pair<AIResultType, AIListTask> Ask(String userInput) {
        // 初期値は失敗を持っておく
        AIResultType result = AIResultType.Failure;

        // クライアント作成(タイムアウト設定付き)
        OkHttpClient client = new OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)  // 接続タイムアウト: 30秒
        .readTimeout(60, TimeUnit.SECONDS)     // 読み取りタイムアウト: 60秒
        .writeTimeout(30, TimeUnit.SECONDS)    // 書き込みタイムアウト: 30秒
        .callTimeout(120, TimeUnit.SECONDS)    // 全体タイムアウト: 120秒（オプション）
        .build();

        // 送信先URL作成
        // TODO: URLの作成処理などは外に出して部品化する必要がある
        String url = BaseUrl + ChatPath;

        // JSON作成
        // ルートオブジェクト作成
        JsonObject root = this.CreateRequestJson(ModelName, SystemPrompt, userInput, Optional.empty());

        // JSON文字列に変換
        String requestBody = root.toString();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, requestBody);

        // リクエスト作成
        Request request = new Request.Builder()
            .url(url)
            .post(body)
            .build();

        // リクエスト送信とレスポンス取得
        try (Response response = client.newCall(request).execute()) {
            // レスポンス状態の判定
            if (response.isSuccessful()) {
                // body部分を文字列として取得
                String bodyStr = response.body().string();
                return new Pair<>(AIResultType.Success, ParseAIListTask(bodyStr));
            } else {
                // 失敗時処理
                return new Pair<>(AIResultType.Failure, new AIListTask());
            }
        } catch (Exception e) {
            // TODO:ロガーに置き換える
            System.out.println("★★★ IOException CATCH に入りました ★★★");
            System.out.println(e);
            // 失敗時処理
            // 空インスタンスを返す
            return new Pair<>(AIResultType.Failure, new AIListTask());
        }
    }

    /**
     * AIにタスク再生成の問い合わせを行う
     * @param userInput ユーザ入力
     * @param addUserInput 追加ユーザ入力
     * @return 処理結果とAIListTask（プラットフォーム非依存）
     */
    @Override
    public Pair<AIResultType, AIListTask> ReAsk(String userInput, String addUserInput) {
        // 初期値は失敗を持っておく
        AIResultType result = AIResultType.Failure;

        // クライアント作成(タイムアウト設定付き)
        OkHttpClient client = new OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)  // 接続タイムアウト: 30秒
        .readTimeout(60, TimeUnit.SECONDS)     // 読み取りタイムアウト: 60秒
        .writeTimeout(30, TimeUnit.SECONDS)    // 書き込みタイムアウト: 30秒
        .callTimeout(120, TimeUnit.SECONDS)    // 全体タイムアウト: 120秒（オプション）
        .build();

        // 送信先URL作成
        // TODO: URLの作成処理などは外に出して部品化する必要がある
        String url = BaseUrl + ChatPath;

        // JSON作成
        // ルートオブジェクト作成
        JsonObject root = this.CreateRequestJson(ModelName, SystemPrompt, userInput, Optional.of(addUserInput));

        // JSON文字列に変換
        String requestBody = root.toString();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, requestBody);

        // リクエスト作成
        Request request = new Request.Builder()
            .url(url)
            .post(body)
            .build();

        // リクエスト送信とレスポンス取得
        try (Response response = client.newCall(request).execute()) {
            // レスポンス状態の判定
            if (response.isSuccessful()) {
                // body部分を文字列として取得
                String bodyStr = response.body().string();
                return new Pair<>(AIResultType.Success, ParseAIListTask(bodyStr));
            } else {
                // 失敗時処理
                return new Pair<>(AIResultType.Failure, new AIListTask());
            }
        } catch (Exception e) {
            // TODO:ロガーに置き換える
            System.out.println("★★★ IOException CATCH に入りました ★★★");
            System.out.println(e);
            // 失敗時処理
            // 空インスタンスを返す
            return new Pair<>(AIResultType.Failure, new AIListTask());
        }
    }

    private JsonObject CreateRequestJson(String modelName, String systemPrompt, String userInput, Optional<String> addUserInput)
    {
        // JSON作成
        // ルートオブジェクト作成
        JsonObject root = new JsonObject();
        root.addProperty("model", modelName);

        // メッセージ配列作成
        JsonArray messages = new JsonArray();

        // システムメッセージ作成
        JsonObject systemMessage = new JsonObject();
        systemMessage.addProperty("role", "system");
        systemMessage.addProperty("content", systemPrompt);

        messages.add(systemMessage);

        if (addUserInput.isPresent()) {
            // 追加指示付きユーザーメッセージ作成
            String combinedUserInput = userInput + "\n追加指示: " + addUserInput.get();
            JsonObject userMessage = new JsonObject();
            userMessage.addProperty("role", "user");
            userMessage.addProperty("content", combinedUserInput);
            messages.add(userMessage);
        } else {
            // 通常のユーザーメッセージ作成
            JsonObject userMessage = new JsonObject();
            userMessage.addProperty("role", "user");
            userMessage.addProperty("content", userInput);
            messages.add(userMessage);
        }
        root.add("messages", messages);
        return root;
    }

    /**
     * LMStudio固有のレスポンスJSON文字列をAIListTaskに変換する
     * @param responseBody HTTPレスポンスのbody文字列
     * @return AIListTask
     */
    private AIListTask ParseAIListTask(String responseBody) {
        JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
        String jsonContent = jsonResponse.getAsJsonArray("choices")
            .get(0).getAsJsonObject()
            .getAsJsonObject("message")
            .get("content").getAsString();
        jsonContent = jsonContent
            .replaceAll("^```json", "")
            .replaceAll("^```", "")
            .replaceAll("```$", "")
            .trim();

        System.out.println(jsonContent);

        JsonObject contentJson = JsonParser.parseString(jsonContent).getAsJsonObject();
        AIListTask aiListTask = new AIListTask();
        aiListTask.listName = contentJson.get("リスト名").getAsString();

        JsonArray tasksArray = contentJson.getAsJsonArray("タスク");
        for (JsonElement taskElement : tasksArray) {
            aiListTask.tasks.add(taskElement.getAsString());
        }
        return aiListTask;
    }
}
