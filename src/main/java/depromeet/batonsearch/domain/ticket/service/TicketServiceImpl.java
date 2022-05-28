package depromeet.batonsearch.domain.ticket.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import depromeet.batonsearch.domain.tag.StaticTag;
import depromeet.batonsearch.domain.tag.repository.TagRepository;
import depromeet.batonsearch.domain.ticket.Ticket;
import depromeet.batonsearch.domain.ticket.dto.TicketRequestDto;
import depromeet.batonsearch.domain.ticket.dto.TicketResponseDto;
import depromeet.batonsearch.domain.ticket.repository.TicketQueryRepository;
import depromeet.batonsearch.domain.ticket.repository.TicketRepository;
import depromeet.batonsearch.domain.ticketimage.TicketImage;
import depromeet.batonsearch.domain.ticketimage.repository.TicketImageRepository;
import depromeet.batonsearch.domain.tickettag.TicketTag;
import depromeet.batonsearch.domain.tickettag.repository.TicketTagRepository;
import depromeet.batonsearch.domain.user.User;
import depromeet.batonsearch.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.prefix}")
    private String prefix;

    final private TicketRepository ticketRepository;
    final private TicketQueryRepository ticketQueryRepository;
    final private UserRepository userRepository;
    final private TagRepository tagRepository;
    final private TicketTagRepository ticketTagRepository;
    final private HttpServletRequest request;
    final private TicketImageRepository ticketImageRepository;
    final private AmazonS3 amazonS3;

    @Override
    public Page<TicketResponseDto.Simple> findAll(TicketRequestDto.Search search) {
        return ticketQueryRepository.searchAll(search, PageRequest.of(search.getPage(), search.getSize()));
    }

    @Override
    public TicketResponseDto.Simple save(TicketRequestDto.Info info, Set<String> tags, Set<MultipartFile> images) {
        // User Check
        User user = getUserByUserIdInHeader();
        info.setSeller(user);

        Ticket ticket = ticketRepository.save(info.toEntity());

        // Tag add
        if (tags != null) {
            ticketTagRepository.saveAll(
                tags.stream()
                    .map(x -> StaticTag.tagToKey.getOrDefault(x, -1))
                    .filter(x -> x != -1)
                    .map(tagRepository::getById)
                    .map(tag ->
                            TicketTag.builder().ticket(ticket).tag(tag).build()
                    ).collect(Collectors.toSet())
            );
        }

        // Image add
        if (images != null) {
            List<TicketImage> ticketImages = images.stream()
                    .filter(image -> image.getContentType() != null && image.getContentType().startsWith("image/"))
                    .map(image -> {
                        String originFileName = createFileName(image.getOriginalFilename());
                        String fileName = (prefix != null ? prefix : "") + originFileName;
                        String thumbnailFileName = (prefix != null ? prefix : "") + "s_" + originFileName;

                        // 원본 저장
                        ObjectMetadata objectMetadata = new ObjectMetadata();
                        objectMetadata.setContentLength(image.getSize());
                        objectMetadata.setContentType(image.getContentType());

                        try (InputStream inputStream = image.getInputStream()) {
                            amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                                    .withCannedAcl(CannedAccessControlList.PublicRead));
                        } catch (IOException e) {
                            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");
                        }

                        // 썸네일 저장
                        try (InputStream inputStream = image.getInputStream()) {
                            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                            Thumbnails.of(inputStream)
                                    .crop(Positions.CENTER)
                                    .size(512, 512)
                                    .toOutputStream(outputStream);

                            objectMetadata.setContentLength(outputStream.size());

                            amazonS3.putObject(new PutObjectRequest(bucket, thumbnailFileName, new ByteArrayInputStream(outputStream.toByteArray()), objectMetadata)
                                    .withCannedAcl(CannedAccessControlList.PublicRead));
                        } catch (IOException e) {
                            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");
                        }

                        return TicketImage.builder()
                                .url(String.format("https://%s.s3.ap-northeast-2.amazonaws.com/%s", bucket, fileName))
                                .thumbnailUrl(String.format("https://%s.s3.ap-northeast-2.amazonaws.com/%s", bucket, thumbnailFileName))
                                .isMain(false)
                                .ticket(ticket)
                                .build();
                    })
                    .collect(Collectors.toList());

            if (ticketImages.size() == 0) {
                return TicketResponseDto.Simple.of(ticket);
            }
            ticket.setMainImage(ticketImages.get(0).getThumbnailUrl());
            ticketImageRepository.saveAll(ticketImages);
        }

        return TicketResponseDto.Simple.of(ticketRepository.save(ticket));
    }

    @Override
    public String deleteById(Integer id) {
        User user = getUserByUserIdInHeader();
        Ticket ticket = ticketRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket Not Found")
        );

        if (ticket.getSeller().getId().equals(user.getId())) {
            ticketRepository.delete(ticket);
            return "delete success";
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "user doesn't match");
        }
    }

    @Override
    public TicketResponseDto.Info findById(Integer id, Double latitude, Double longitude) {
        TicketResponseDto.Info info = TicketResponseDto.Info.of(
                ticketRepository.findById(id).orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found")
                )
        );
        info.setDistance(TicketResponseDto.Info.distance(info.getLatitude(), info.getLongitude(), latitude, longitude));
        return info;
    }

    private User getUserByUserIdInHeader() {
        String userIdString = request.getHeader("userId");
        Integer userId;

        if (userIdString == null) {
            userId = 1;
            // throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        } else {
            try {
                userId = Integer.parseInt(userIdString);
            } catch (NumberFormatException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "userID를 파싱할 수 없습니다.");
            }
        }
        return userRepository.getById(userId);
    }

    private String createFileName(String fileName) { // 먼저 파일 업로드 시, 파일명을 난수화하기 위해 random으로 돌립니다.
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    private String getFileExtension(String fileName) { // file 형식이 잘못된 경우를 확인하기 위해 만들어진 로직이며, 파일 타입과 상관없이 업로드할 수 있게 하기 위해 .의 존재 유무만 판단하였습니다.
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 형식의 파일(" + fileName + ") 입니다.");
        }
    }
}
