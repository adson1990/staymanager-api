package com.adson.staymanager.controller.v1;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adson.staymanager.entity.BookingStatus;
import com.adson.staymanager.entity.GuestProfile;
import com.adson.staymanager.exception.BusinessRuleException;
import com.adson.staymanager.mapper.BookingMapper;
import com.adson.staymanager.repository.BookingRepository;
import com.adson.staymanager.repository.GuestProfileRepository;
import com.adson.staymanager.repository.UserRepository;
import com.adson.staymanager.dto.response.MeResponseDTO;
import com.adson.staymanager.dto.response.MeSummaryResponseDTO;
import com.adson.staymanager.dto.response.MyBookingResponseDTO;

@RestController
@RequestMapping("/api/v1/me")
public class MeController {

    private final UserRepository userRepository;
    private final GuestProfileRepository guestProfileRepository;
    private final BookingRepository bookingRepository;   

    public MeController(UserRepository userRepository, 
                        GuestProfileRepository guestProfileRepository, 
                        BookingRepository bookingRepository
                        ) {
        this.userRepository = userRepository;
        this.guestProfileRepository = guestProfileRepository;
        this.bookingRepository = bookingRepository;
    }

    @PreAuthorize("hasRole('CLIENTE')")
    @GetMapping
    public MeResponseDTO me(Authentication authentication) {

        String email = authentication.getName();
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessRuleException("Usuário não encontrado"));

        GuestProfile profile = guestProfileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new BusinessRuleException("Perfil de hóspede não encontrado"));

        return MeResponseDTO.from(user, profile);
    }

    @PreAuthorize("hasRole('CLIENTE')")
    @GetMapping("/bookings")
    public List<MyBookingResponseDTO> myBookings(Authentication authentication) {

        String email = authentication.getName();

        var user = userRepository.findByEmail(email)
            .orElseThrow(() -> new BusinessRuleException("Usuário não encontrado"));

        GuestProfile profile = guestProfileRepository.findByUserId(user.getId())
            .orElseThrow(() -> new BusinessRuleException("Perfil de hóspede não encontrado"));

        return bookingRepository
            .findByGuestIdOrderByCheckInDateDesc(profile.getId())
            .stream()
            .map(BookingMapper::toMyBookingDTO)
            .toList();
    }

    @PreAuthorize("hasRole('CLIENTE')")
    @GetMapping("/summary")
    public MeSummaryResponseDTO summary(Authentication authentication) {

      String email = authentication.getName();

      var user = userRepository.findByEmail(email)
              .orElseThrow(() -> new BusinessRuleException("Usuário não encontrado"));

      var profile = guestProfileRepository.findByUserId(user.getId())
              .orElseThrow(() -> new BusinessRuleException("Perfil de hóspede não encontrado"));

      Long guestId = profile.getId();

      long totalBookings = bookingRepository.countByGuestId(guestId);
      var totalSpent = bookingRepository.sumTotalPriceByGuestId(guestId);

      boolean existsReserved = bookingRepository.existsByGuestIdAndStatus(guestId, BookingStatus.RESERVED);
      long totalCheckedIn = (bookingRepository.countByGuestIdAndStatus(guestId, BookingStatus.CHECKED_IN)
                             + bookingRepository.countByGuestIdAndStatus(guestId, BookingStatus.CHECKED_OUT));
      long totalCheckedOut = bookingRepository.countByGuestIdAndStatus(guestId, BookingStatus.CHECKED_OUT);
      long totalCancelled = bookingRepository.countByGuestIdAndStatus(guestId, BookingStatus.CANCELLED);

      var lastCheckInDate = bookingRepository.findLastCheckInDateByGuestId(guestId).orElse(null);

      return new MeSummaryResponseDTO(
              totalBookings,
              totalSpent,
              existsReserved,
              totalCheckedIn,
              totalCheckedOut,
              totalCancelled,
              lastCheckInDate
      );
    }

}