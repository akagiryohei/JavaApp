package Interface.Model;

import Entity.AIListTask;
import Entity.Pair;
import Entity.AI.AIResultType;

/**
 * AI接続用クライアントインターフェース
 */
public interface IAIAPIClient {

    /**
     * AIにタスク生成の問い合わせを行う
     * @param userInput ユーザ入力
     * @return 処理結果とAIListTask（プラットフォーム非依存）
     */
    public Pair<AIResultType, AIListTask> Ask(String userInput);

    /**
     * AIにタスク再生成の問い合わせを行う
     * @param userInput ユーザ入力
     * @param addUserInput 追加ユーザ入力
     * @return 処理結果とAIListTask（プラットフォーム非依存）
     */
    public Pair<AIResultType, AIListTask> ReAsk(String userInput, String addUserInput);

}


