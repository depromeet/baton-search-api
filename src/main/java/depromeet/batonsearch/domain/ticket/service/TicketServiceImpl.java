package depromeet.batonsearch.domain.ticket.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import depromeet.batonsearch.domain.bookmark.repository.BookmarkRepository;
import depromeet.batonsearch.domain.inquiry.dto.InquiryResponseDto;
import depromeet.batonsearch.domain.inquiry.repository.InquiryRepository;
import depromeet.batonsearch.domain.tag.TagEnum;
import depromeet.batonsearch.domain.tag.repository.TagRepository;
import depromeet.batonsearch.domain.ticket.Ticket;
import depromeet.batonsearch.domain.ticket.dto.TicketRequestDto;
import depromeet.batonsearch.domain.ticket.dto.TicketResponseDto;
import depromeet.batonsearch.domain.ticket.repository.TicketQueryRepository;
import depromeet.batonsearch.domain.ticket.repository.TicketRepository;
import depromeet.batonsearch.domain.ticketimage.TicketImage;
import depromeet.batonsearch.domain.ticketimage.dto.TicketImageResponseDto;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;
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
    final private BookmarkRepository bookmarkRepository;
    final private TicketTagRepository ticketTagRepository;
    final private HttpServletRequest request;
    final private TicketImageRepository ticketImageRepository;
    final private InquiryRepository inquiryRepository;
    final private AmazonS3 amazonS3;

    @Override
    public List<InquiryResponseDto.Simple> ticketInquiries(Integer ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "티켓을 찾을 수 없습니다.")
        );
        return new ArrayList<>(InquiryResponseDto.Simple.of(inquiryRepository.findByTicket(ticket)));
    }

    @Override
    public Integer countInquiries(Integer ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "티켓을 찾을 수 없습니다.")
        );
        return inquiryRepository.countByTicketEquals(ticket);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TicketResponseDto.Simple> findAll(TicketRequestDto.Search search) {
        return ticketQueryRepository.searchAll(search, PageRequest.of(search.getPage(), search.getSize()));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TicketResponseDto.Simple> stringSearch(TicketRequestDto.StringSearch search) {
        return ticketQueryRepository.stringSearch(search, PageRequest.of(search.getPage(), search.getSize()));
    }

    @Override
    @Transactional(readOnly = true)
    public Long countSearch(TicketRequestDto.Search search) {
        return ticketQueryRepository.countSearch(search);
    }

    @Override
    @Transactional
    public TicketResponseDto.Simple save(TicketRequestDto.Info info, Set<String> tags, Set<MultipartFile> images) {
        // User Check
        User user = getUserByUserIdInHeader();
        info.setSeller(user);

        Ticket ticket = ticketRepository.save(info.toEntity());

        // Tag add
        if (tags != null) {
            ticketTagRepository.saveAll(
                tags.stream()
                    .map(x -> {
                        try {
                            return TagEnum.valueOf(x).getHash();
                        } catch (IllegalAccessError e) {
                            return -1;
                        }
                    })
                    .filter(x -> x != -1)
                    .map(tagRepository::getById)
                    .map(tag -> TicketTag.builder().ticket(ticket).tag(tag).build())
                    .collect(Collectors.toSet())
            );
        }

        // Image add
        if (images != null) {
            List<TicketImage> ticketImages = fileToTicketImages(ticket, images);

            if (ticketImages.size() == 0) {
                return TicketResponseDto.Simple.of(ticket);
            }
            ticket.setMainImage(ticketImages.get(0).getThumbnailUrl());
            ticketImageRepository.saveAll(ticketImages);
        }

        return TicketResponseDto.Simple.of(ticketRepository.save(ticket));
    }

    @Override
    @Transactional
    public TicketResponseDto.Info modify(Integer id, TicketRequestDto.Put data) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket Not Found")
        );

        if (ticket.getSeller().getId() != getUserIdInHeader()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인의 글만 수정 할 수 있습니다.");
        }

        ticket.putData(data);
        TicketResponseDto.Info info = TicketResponseDto.Info.of(ticketRepository.save(ticket));
        info.setIsInquired(false);
        return info;
    }

    @Override
    @Transactional
    public String deleteById(Integer id) {
        Integer userId = getUserIdInHeader();
        Ticket ticket = ticketRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket Not Found")
        );

        if (ticket.getSeller().getId().equals(userId)) {
            ticketRepository.delete(ticket);
            return "delete success";
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "user doesn't match");
        }
    }

    @Override
    @Transactional
    public String deleteImageById(Integer id) {
        Integer userId = getUserIdInHeader();
        TicketImage ticketImage = ticketImageRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "TicketImage Not Found")
        );

        Ticket ticket = ticketImage.getTicket();

        if (ticket.getSeller().getId().equals(userId)) {
            ticket.getImages().remove(ticketImage);

            if (ticket.getImages().size() == 0)
                ticket.setMainImage(null);

            return "delete success";
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "user doesn't match");
        }
    }

    @Override
    @Transactional
    public TicketResponseDto.Info findById(Integer id, Double latitude, Double longitude) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket Not Found")
        );

        ticket.addViewCount();

        TicketResponseDto.Info info = TicketResponseDto.Info.of(ticket);
        info.setDistance(TicketResponseDto.Info.distance(info.getLatitude(), info.getLongitude(), latitude, longitude));

        try {
            User user = getUserByUserIdInHeader();
            Optional<Integer> bookmarkId = bookmarkRepository.findBookmarkIdByTicketAndUser(ticket, user);
            info.setBookmarkId((bookmarkId.isEmpty()) ? null : bookmarkId.get());
            ticketRepository.save(ticket);

            info.setIsInquired(inquiryRepository.existsByUserAndTicket(user, ticket));
        } catch (ResponseStatusException e) {
            log.debug("No User");
        }

        return info;
    }

    @Override
    @Transactional
    public List<TicketImageResponseDto> ticketImagePost(Integer id, Set<MultipartFile> images) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket Not Found")
        );

        if (ticket.getSeller().getId() != getUserIdInHeader()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인의 글에만 이미지를 추가 할 수 있습니다.");
        }

        int ticketImageCount = ticket.getImages().size();

        // 이미지 5개 이상 업로드 막음.
        if (images.size() + ticketImageCount > 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미지가 5개를 초과합니다.");
        }

        List<TicketImage> ticketImages = fileToTicketImages(ticket, images);

        if (ticketImages.size() == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "업로드 된 이미지가 없습니다. 파일 형식을 확인 해 주세요.");
        }

        ticketImageRepository.saveAll(ticketImages);

        if (ticket.getMainImage() == null) {
            ticket.setMainImage(ticketImages.get(0).getThumbnailUrl());
            ticketRepository.save(ticket);
        }

        return ticketImages.stream().map(TicketImageResponseDto::of).collect(Collectors.toList());
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

    private User getUserByUserIdInHeader() {
        return userRepository.getById(getUserIdInHeader());
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

    private List<TicketImage> fileToTicketImages(Ticket ticket, Set<MultipartFile> images) {
        return images.stream()
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
    }
}
