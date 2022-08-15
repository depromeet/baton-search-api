package depromeet.batonsearch.domain.fcm.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.net.HttpHeaders;
import depromeet.batonsearch.domain.fcm.FCMMessage;
import depromeet.batonsearch.domain.user.User;
import depromeet.batonsearch.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class FCMService {

    @Value("${firebase.url}")
    private String API_URL;

    @Value("${firebase.key}")
    private String API_KEY;

    private final UserRepository userRepository;
    final private HttpServletRequest request;
    private final ObjectMapper objectMapper;

    public void setFcmMessage(String string) {
        User user = userRepository.findById(getUserIdInHeader()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유저를 찾을 수 없습니다.")
        );
        user.setFcmToken(string);
        userRepository.save(user);
    }

    private Integer getUserIdInHeader() {
        String userIdString = request.getHeader("Remote-User");

        if (userIdString == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        try {
            return Integer.parseInt(userIdString);
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "userID를 파싱할 수 없습니다.");
        }
    }

    public void sendMessageTo(String targetToken, String title, String body) throws IOException {
        String message = makeMessage(targetToken, title, body);

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(message,
                MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();

        Response response = client.newCall(request).execute();
        switch (response.code()) {
            case 200:
                return;
            case 400:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 요청 매개변수입니다.");
            case 404:
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 토큰을 가진 유저를 찾을 수 없습니다.");
            default:
                log.error(response.message());
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 에러입니다.");
        }
    }

    private String makeMessage(String targetToken, String title, String body) throws JsonProcessingException {
        FCMMessage fcmMessage = FCMMessage.builder()
                .message(FCMMessage.Message.builder()
                        .token(targetToken)
                        .notification(FCMMessage.Notification.builder()
                                .title(title)
                                .body(body)
                                .image(null)
                                .build()
                        ).build()).validateOnly(false).build();

        return objectMapper.writeValueAsString(fcmMessage);
    }

    private String getAccessToken() throws IOException {

        String firebaseConfigPath = "firebaseKey.json";

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        return "Bearer " + googleCredentials.getAccessToken().getTokenValue();
    }
}