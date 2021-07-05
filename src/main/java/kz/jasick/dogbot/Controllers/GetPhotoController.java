package kz.jasick.dogbot.Controllers;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
@Controller
public class GetPhotoController {
    public static SendMessage getPhoto(Update update,String messageIn) {
        SendMessage message=new SendMessage();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String breed = messageIn.contains(" ") ? messageIn.split(" ")[1] : messageIn;
        try {
            HttpGet request = new HttpGet("https://dog.ceo/api/breed/" + breed + "/images/random");
            CloseableHttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();

            String result = EntityUtils.toString(entity);
            JSONObject obj = new JSONObject(result);
            if (obj.getString("status").equals("success")) {
                String url = obj.getString("message");
                url = url.replace("\"", "");
                message = SendMessage.builder()
                        .chatId(update.getMessage().getChatId().toString())
                        .text(url)
                        .build();
            } else {
                message = SendMessage.builder()
                        .chatId(update.getMessage().getChatId().toString())
                        .text("Порода не найдена")
                        .build();

            }
            response.close();
            httpClient.close();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return message;
    }
}

