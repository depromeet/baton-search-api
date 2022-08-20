package depromeet.batonsearch.domain.inquiry.repository;

import depromeet.batonsearch.domain.inquiry.Inquiry;
import depromeet.batonsearch.domain.ticket.Ticket;
import depromeet.batonsearch.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InquiryRepository extends JpaRepository<Inquiry, Integer> {
    List<Inquiry> findByUser(User user);

    @Query("select i from Inquiry i where i.ticket in (select t from Ticket t where t.seller = :user)")
    List<Inquiry> findByReceivedUser(@Param("user") User user);
    List<Inquiry> findByTicket(Ticket ticket);
    Integer countByTicketEquals(Ticket ticket);
}
