import Model.GetProperties;
import Model.LMStudioAPIClient;
import Interface.Model.ILMStudioAPIClient;
import Interface.Model.ILogger;

import com.google.gson.JsonObject;

import Entity.Enum.PropertyKey;
import Entity.LMStudio.LMStudioAPIClientDtoPair;
import Entity.LMStudio.LMStudioResultType;

public class TestLMStudio {
    public static void main(String[] args) {
            ILogger logger = null;
        try {
            GetProperties settings = new GetProperties("bin/Settings.properties");
            settings.Load();

            ILMStudioAPIClient client = new LMStudioAPIClient(
                settings.getProperty(PropertyKey.BaseUrl),
                settings.getProperty(PropertyKey.ChatPath),
                settings.getProperty(PropertyKey.ModelName),
                settings.getProperty(PropertyKey.ApiKey),
                settings.getProperty(PropertyKey.SystemPrompt),
                logger
            );

            String response = client.askSample("javaの学習のためのタスクを作ってほしい。");
            System.out.println("Response: " + response);
            // これは現在のClientの実装を反映したものである
            LMStudioAPIClientDtoPair<LMStudioResultType, JsonObject> response1 = client.Ask("javaの学習のためのタスクを作ってほしい。");
            System.out.println("Response: " + response1.Value2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}