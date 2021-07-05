package kz.jasick.dogbot.Controllers;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class GetBreedsListController {
    public static SendMessage getBreedsList(Update update) {
        SendMessage message=new SendMessage();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpGet request = new HttpGet("https://dog.ceo/api/breeds/list/all");
            CloseableHttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity);
            response.close();
            httpClient.close();
            result = result.replaceAll("[{\":}]", "");
            result = result.replaceAll("message", "");
            result = result.replace(",", ", ");
            result = result.replaceAll("[\\[^)\\]+]", "");
            message = SendMessage.builder()
                    .chatId(update.getMessage().getChatId().toString())
                    .text("Список доступных пород: " + result)
                    .build();


        } catch (Throwable e) {
            e.printStackTrace();
        }
        return message;
    }
}
