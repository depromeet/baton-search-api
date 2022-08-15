package depromeet.batonsearch.domain.inquiry.controller;

import depromeet.batonsearch.domain.inquiry.dto.InquiryRequestDto;
import depromeet.batonsearch.domain.inquiry.dto.InquiryResponseDto;
import depromeet.batonsearch.domain.inquiry.service.InquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/inquiries")
public class InquiryController {
    private final InquiryService inquiryService;

    @PostMapping(value = "")
    public ResponseEntity<InquiryResponseDto.Simple> postInquiry(@RequestBody InquiryRequestDto.Post data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inquiryService.postInquiry(data));
    }

    @GetMapping(value = "/send")
    public ResponseEntity<List<InquiryResponseDto.Simple>> getSendInquiries() {
        return ResponseEntity.ok(inquiryService.getSendInquiries());
    }

    @GetMapping(value = "/receive")
    public ResponseEntity<List<InquiryResponseDto.Simple>> getReceivedInquiries() {
        return ResponseEntity.ok(inquiryService.getReceivedInquiries());
    }

    @GetMapping(value = "/{inquiryId}")
    public ResponseEntity<InquiryResponseDto.Info> findInquiryById(@PathVariable Integer inquiryId) {
        return ResponseEntity.ok(inquiryService.findInquiryById(inquiryId));
    }
}
