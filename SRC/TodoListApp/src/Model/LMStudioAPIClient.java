package Model;

import Interface.Model.ILMStudioAPIClient;
import Interface.Model.ILogger;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.MediaType;

import java.io.IOException;
import java.net.ConnectException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import Entity.LMStudio.LMStudioAPIClientDtoPair;
import Entity.LMStudio.LMStudioResultType;

/**
 * AI接続用クライアントクラス
 */
public class LMStudioAPIClient implements ILMStudioAPIClient{

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
     * LM Studioに問い合わせを行う(サンプル用、JsonObjectで返す)
     * @param userInput ユーザ入力
     * @return
     * @throws Exception
     */
    @Override
    public String askSample(String userInput) throws Exception {
        OkHttpClient client = new OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)  // 接続タイムアウト: 30秒
        .readTimeout(60, TimeUnit.SECONDS)     // 読み取りタイムアウト: 60秒
        .writeTimeout(30, TimeUnit.SECONDS)    // 書き込みタイムアウト: 30秒
        .callTimeout(120, TimeUnit.SECONDS)    // 全体タイムアウト: 120秒（オプション）
        .build();
        
        // 送信先URL作成
        // TODO: URLの作成処理などは外に出して部品化する必要がある
        String url = BaseUrl + ChatPath;

        // ルートオブジェクト作成
        // JSON作成
        // ルートオブジェクト作成
        JsonObject root = new JsonObject();
        root.addProperty("model", ModelName);

        // メッセージ配列作成
        JsonArray messages = new JsonArray();

        // システムメッセージ作成
        JsonObject systemMessage = new JsonObject();
        systemMessage.addProperty("role", "system");
        systemMessage.addProperty("content", SystemPrompt);

        // ユーザーメッセージ作成
        JsonObject userMessage = new JsonObject();
        userMessage.addProperty("role", "user");
        userMessage.addProperty("content", userInput);
        
        // メッセージ配列に追加
        messages.add(systemMessage);
        messages.add(userMessage);

        // ルートオブジェクトにメッセージ配列を追加
        root.add("messages", messages);

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
                // Gsonで文字列をパース、JSONオブジェクトとして取得
                JsonObject jsonResponse = JsonParser.parseString(bodyStr).getAsJsonObject();
                // TODO: 返す時にはjsonResponseをそのままでOK
                return jsonResponse.getAsJsonArray("choices")
                    .get(0).getAsJsonObject()
                    .getAsJsonObject("message")
                    .get("content").getAsString();
            } else {
                String bodyStr = response.body().string();
                throw new RuntimeException("API request failed with status: " + response.code() + ", body: " + response.body().string());
            }
        }catch (IOException e) {
            System.out.println("★★★ IOException CATCH に入りました ★★★");
            System.out.println(e);
            throw new ConnectException("Failed to connect to LM Studio server at " + url + ". Please ensure the server is running and accessible.");
        }
    }

    /**
     * LM Studioに問い合わせを行う
     * @param userInput ユーザ入力
     * @return
     * @throws Exception
     */
    @Override
    public LMStudioAPIClientDtoPair<LMStudioResultType, JsonObject> Ask(String userInput) throws Exception {
        // 初期値は失敗を持っておく
        LMStudioResultType result = LMStudioResultType.Failure;

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

        // ルートオブジェクト作成
        // JSON作成
        // ルートオブジェクト作成
        JsonObject root = new JsonObject();
        root.addProperty("model", ModelName);

        // メッセージ配列作成
        JsonArray messages = new JsonArray();

        // システムメッセージ作成
        JsonObject systemMessage = new JsonObject();
        systemMessage.addProperty("role", "system");
        systemMessage.addProperty("content", SystemPrompt);

        // ユーザーメッセージ作成
        JsonObject userMessage = new JsonObject();
        userMessage.addProperty("role", "user");
        userMessage.addProperty("content", userInput);
        
        // メッセージ配列に追加
        messages.add(systemMessage);
        messages.add(userMessage);

        // ルートオブジェクトにメッセージ配列を追加
        root.add("messages", messages);

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
                // Gsonで文字列をパース、JSONオブジェクトとして取得
                JsonObject jsonResponse = JsonParser.parseString(bodyStr).getAsJsonObject();
                result = LMStudioResultType.Success;
                // TODO: 返す時にはjsonResponseをそのままでOK
                return new LMStudioAPIClientDtoPair<LMStudioResultType, JsonObject>(result, jsonResponse);
            } else {
                // 失敗時もステータスや内容を取得できるようにする
                // body部分を文字列として取得
                String bodyStr = response.body().string();
                // Gsonで文字列をパース、JSONオブジェクトとして取得
                JsonObject jsonResponse = JsonParser.parseString(bodyStr).getAsJsonObject();
                // ステータスコードも追加しておく
                jsonResponse.addProperty("httpStatusCode", response.code());
                // 失敗時処理
                return new LMStudioAPIClientDtoPair<LMStudioResultType,JsonObject>(result, jsonResponse);
            }
        } catch (IOException e) {
            // TODO:ロガーに置き換える
            System.out.println("★★★ IOException CATCH に入りました ★★★");
            System.out.println(e);
            // 失敗時処理
            // 空インスタンスを返す
            JsonObject jsonResponse = new JsonObject();
            return new LMStudioAPIClientDtoPair<LMStudioResultType,JsonObject>(result, jsonResponse);
        }
    }

    /**
     * LM Studioにタスク再生成の問い合わせを行う
     * @param userInput ユーザ入力
     * @param addUserInput 追加ユーザ入力
     * @return
     * @throws Exception
     */
    @Override
    public LMStudioAPIClientDtoPair<LMStudioResultType, JsonObject> ReAsk(String userInput, String addUserInput) throws Exception {

        // 初期値は失敗を持っておく
        LMStudioResultType result = LMStudioResultType.Failure;

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
        JsonObject root = new JsonObject();
        root.addProperty("model", ModelName);

        // メッセージ配列作成
        JsonArray messages = new JsonArray();

        // システムメッセージ作成
        JsonObject systemMessage = new JsonObject();
        systemMessage.addProperty("role", "system");
        systemMessage.addProperty("content", SystemPrompt);

        // 追加指示付きユーザーメッセージ作成
        String combinedUserInput = userInput + "\n追加指示: " + addUserInput;
        JsonObject userMessage = new JsonObject();
        userMessage.addProperty("role", "user");
        userMessage.addProperty("content", combinedUserInput);
        
        // メッセージ配列に追加
        messages.add(systemMessage);
        messages.add(userMessage);

        // ルートオブジェクトにメッセージ配列を追加
        root.add("messages", messages);

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
                // Gsonで文字列をパース、JSONオブジェクトとして取得
                JsonObject jsonResponse = JsonParser.parseString(bodyStr).getAsJsonObject();
                result = LMStudioResultType.Success;
                // TODO: 返す時にはjsonResponseをそのままでOK
                return new LMStudioAPIClientDtoPair<LMStudioResultType, JsonObject>(result, jsonResponse);
            } else {
                // 失敗時もステータスや内容を取得できるようにする
                // body部分を文字列として取得
                String bodyStr = response.body().string();
                // Gsonで文字列をパース、JSONオブジェクトとして取得
                JsonObject jsonResponse = JsonParser.parseString(bodyStr).getAsJsonObject();
                // ステータスコードも追加しておく
                jsonResponse.addProperty("httpStatusCode", response.code());
                // 失敗時処理
                return new LMStudioAPIClientDtoPair<LMStudioResultType,JsonObject>(result, jsonResponse);
            }
        } catch (IOException e) {
            // TODO:ロガーに置き換える
            System.out.println("★★★ IOException CATCH に入りました ★★★");
            System.out.println(e);
            // 失敗時処理
            // 空インスタンスを返す
            JsonObject jsonResponse = new JsonObject();
            return new LMStudioAPIClientDtoPair<LMStudioResultType,JsonObject>(result, jsonResponse);
        }
    }
}
