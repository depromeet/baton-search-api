package depromeet.batonsearch.domain.fcm.controller;

import depromeet.batonsearch.domain.fcm.dto.FCMRequestDto;
import depromeet.batonsearch.domain.fcm.service.FCMService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class FCMController {

    private final FCMService fcmService;

    @PostMapping("/fcm")
    public ResponseEntity pushMessage(@RequestBody FCMRequestDto requestDTO) throws IOException {
        System.out.println(requestDTO.getTargetToken() + " "
                +requestDTO.getTitle() + " " + requestDTO.getBody());

        fcmService.sendMessageTo(
                requestDTO.getTargetToken(),
                requestDTO.getTitle(),
                requestDTO.getBody());
        return ResponseEntity.ok().build();
    }
}
