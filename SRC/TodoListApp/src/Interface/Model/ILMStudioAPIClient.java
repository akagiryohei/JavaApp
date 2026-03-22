package Interface.Model;

import com.google.gson.JsonObject;

import Entity.LMStudio.LMStudioAPIClientDtoPair;
import Entity.LMStudio.LMStudioResultType;

/**
 * AI接続用クライアントインターフェース
 */
public interface ILMStudioAPIClient {

    /**
     * LM Studioに問い合わせを行う
     * @param userInput ユーザ入力
     * @return
     * @throws Exception
     */
    public String askSample(String userInput) throws Exception;

    /**
     * LM Studioに問い合わせを行う
     * @param userInput ユーザ入力
     * @return
     * @throws Exception
     */
    public LMStudioAPIClientDtoPair<LMStudioResultType, JsonObject> Ask(String userInput) throws Exception;

    /**
     * LM Studioにタスク再生成の問い合わせを行う
     * @param userInput ユーザ入力
     * @param addUserInput 追加ユーザ入力
     * @return
     * @throws Exception
     */
    public LMStudioAPIClientDtoPair<LMStudioResultType, JsonObject> ReAsk(String userInput, String addUserInput) throws Exception;

}


