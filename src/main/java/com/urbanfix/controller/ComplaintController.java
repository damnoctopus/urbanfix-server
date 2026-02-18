package com.urbanfix.controller;

import com.urbanfix.dto.ComplaintRequest;
import com.urbanfix.dto.ComplaintResponse;
import com.urbanfix.exception.BadRequestException;
import com.urbanfix.service.ComplaintService;
import com.urbanfix.util.SecurityUtils;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/complaints")
@RequiredArgsConstructor
public class ComplaintController {
    private final ComplaintService complaintService;

    @PostMapping
    public ResponseEntity<ComplaintResponse> createComplaint(@Valid @ModelAttribute ComplaintRequest request) {
        Long userId = SecurityUtils.getCurrentUserId()
                .orElseThrow(() -> new BadRequestException("User identity missing"));
        return ResponseEntity.ok(complaintService.createComplaint(request, userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComplaintResponse> getComplaint(@PathVariable Long id) {
        return ResponseEntity.ok(complaintService.getComplaint(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ComplaintResponse>> getUserComplaints(@PathVariable Long userId) {
        return ResponseEntity.ok(complaintService.getUserComplaints(userId));
    }

    // Admin: Get all complaints
    @GetMapping
    public ResponseEntity<List<ComplaintResponse>> getAllComplaints() {
        return ResponseEntity.ok(complaintService.getAllComplaints());
    }

    // Admin: Update status
    @PutMapping("/{id}/status")
    public ResponseEntity<ComplaintResponse> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> payload) {
        String newStatus = payload.get("status");
        return ResponseEntity.ok(complaintService.updateStatus(id, newStatus));
    }
}