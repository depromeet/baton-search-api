package depromeet.batonsearch.domain.ticket;

import depromeet.batonsearch.domain.bookmark.Bookmark;
import depromeet.batonsearch.domain.buy.Buy;
import depromeet.batonsearch.domain.ticketimage.TicketImage;
import depromeet.batonsearch.domain.tickettag.TicketTag;
import depromeet.batonsearch.domain.user.User;
import depromeet.batonsearch.global.util.GeometryUtil;
import lombok.*;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Ticket")
public class Ticket {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User seller;

    @Column
    private String location;

    @Column
    private String address;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column
    private Integer price;

    @Column(nullable = false)
    private TicketState state = TicketState.SALE;

    @Column(nullable = false)
    private TicketType type = TicketType.HEALTH;

    @Column(nullable = false, name = "can_nego")
    private Boolean canNego = false;

    @Column(nullable = false, name = "trade_type")
    private TicketTradeType tradeType = TicketTradeType.BOTH;

    @Column(nullable = false, name = "has_shower")
    private Boolean hasShower = false;

    @Column(nullable = false, name = "has_locker")
    private Boolean hasLocker = false;

    @Column(nullable = false, name = "has_clothes")
    private Boolean hasClothes = false;

    @Column(nullable = false, name = "has_gx")
    private Boolean hasGx = false;

    @Column(nullable = false, name = "can_resell")
    private Boolean canResell = false;

    @Column(nullable = false, name = "can_refund")
    private Boolean canRefund = false;

    @Column(nullable = false)
    private String description = "";

    @Column(nullable = false, name = "transfer_fee")
    private TicketTransferFee transferFee = TicketTransferFee.NONE;

    @Column(nullable = false)
    private Point point;

    @Column(name = "tag_hash")
    private Long tagHash;

    @Column(nullable = false, name = "is_membership")
    private Boolean isMembership;

    @Column(nullable = false, name = "is_holding")
    private Boolean isHolding = false;

    @Column(nullable = false, name = "remaining_number")
    private Integer remainingNumber;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "main_image")
    private String mainImage;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ticket_id", updatable = false)
    private Set<TicketTag> ticketTags = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ticket_id", updatable = false)
    private Set<Buy> buys = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ticket_id", updatable = false)
    private Set<TicketImage> images = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ticket_id", updatable = false)
    private Set<Bookmark> bookmarks = new HashSet<>();

    @Builder
    public Ticket(User seller, String location, String address, LocalDateTime createdAt, Integer price, TicketState state, TicketType type, Boolean canNego, TicketTradeType tradeType, Boolean hasShower, Boolean hasLocker, Boolean hasClothes, Boolean hasGx, Boolean canResell, Boolean canRefund, String description, TicketTransferFee transferFee, Double latitude, Double longitude, Boolean isMembership, Boolean isHolding, Integer remainingNumber, String mainImage, LocalDate expiryDate) {
        this.seller = seller;
        this.location = location;
        this.address = address;
        this.createdAt = createdAt;
        this.price = price;
        this.state = state == null ? TicketState.SALE : state;
        this.type = type == null ? TicketType.HEALTH : type;
        this.canNego = canNego != null && canNego;
        this.tradeType = tradeType == null ? TicketTradeType.BOTH : tradeType;
        this.hasShower = hasShower != null && hasShower;
        this.hasLocker = hasLocker != null && hasLocker;
        this.hasClothes = hasClothes != null && hasClothes;
        this.hasGx = hasGx != null && hasGx;
        this.canResell = canResell != null && canResell;
        this.canRefund = canRefund != null && canRefund;
        this.description = description == null ? "" : description;
        this.transferFee = transferFee == null ? TicketTransferFee.NONE : transferFee;
        this.point = (Point) GeometryUtil.wktToGeometry(String.format("POINT(%s %s)", longitude, latitude));
        this.point.setSRID(4326);
        this.tagHash = 0L;
        this.isMembership = isMembership;
        this.isHolding = isHolding != null && isHolding;
        this.remainingNumber = remainingNumber;
        this.mainImage = mainImage;
        this.expiryDate = expiryDate;
    }

    public void addTicketTag(TicketTag ticketTag) {
        if (this.ticketTags != null)
            this.ticketTags.add(ticketTag);
        this.tagHash |= 1L << (ticketTag.getTag().getId() - 1);
    }

    public void removeTicketTag(TicketTag ticketTag) {
        if (this.ticketTags != null)
            this.ticketTags.remove(ticketTag);
        this.tagHash -= (1L << (ticketTag.getTag().getId() - 1)) & this.tagHash;
    }

    @PreRemove
    void preRemove() {
        getTicketTags().clear();
        getBuys().clear();
        getImages().clear();
        getBookmarks().clear();
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }
}
