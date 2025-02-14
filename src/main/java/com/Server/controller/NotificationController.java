package com.Server.controller;

import com.Server.dto.Response;
import com.Server.entity.Notification;
import com.Server.service.api.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @PostMapping("/book-room/{roomId}/{userId}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<Response> saveBooking(
            @PathVariable String roomId,
            @PathVariable String userId,
            @RequestBody Notification bookingRequest
    ) {
        Response response = bookingService.saveBooking(roomId, userId, bookingRequest);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getAllBookings(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "6") int limit,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "asc") String order
    ) {
        Response response = bookingService.getAllBookings(page, limit, sort, order);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/get-user-bookings/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getUserBookings(
            @PathVariable String userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "6") int limit,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "asc") String order
    ) {
        Response response = bookingService.getUserBookings(page, limit, sort, order, userId);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/get-by-confirmation-code/{confirmationCode}")
    public ResponseEntity<Response> getBookingsByConfirmationCode(@PathVariable String confirmationCode) {
        Response response = bookingService.findBookingByConfirmationCode(confirmationCode);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/cancel/{bookingId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> cancelBooking(@PathVariable String bookingId) {
        Response response = bookingService.cancelBooking(bookingId);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
