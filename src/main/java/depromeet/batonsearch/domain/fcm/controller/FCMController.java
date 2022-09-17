package depromeet.batonsearch.domain.fcm.controller;

import depromeet.batonsearch.domain.fcm.dto.FCMRequestDto;
import depromeet.batonsearch.domain.fcm.dto.FCMSetDto;
import depromeet.batonsearch.domain.fcm.service.FCMService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/fcm")
public class FCMController {

    private final FCMService fcmService;

    @PostMapping("")
    public ResponseEntity pushMessage(@RequestBody FCMRequestDto requestDTO) throws IOException {
        fcmService.sendMessageTo(
                requestDTO.getUserId(),
                requestDTO.getTitle(),
                requestDTO.getBody());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/user")
    public ResponseEntity setFcmToken(@RequestBody FCMSetDto fcmSetDto) {
        fcmService.setFcmMessage(fcmSetDto.getToken());
        return ResponseEntity.accepted().build();
    }
}
