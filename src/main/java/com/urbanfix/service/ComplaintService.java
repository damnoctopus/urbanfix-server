package com.urbanfix.service;

import com.urbanfix.entity.Complaint;
import com.urbanfix.dto.ComplaintRequest;
import com.urbanfix.dto.ComplaintResponse;
import com.urbanfix.entity.User;
import com.urbanfix.exception.ResourceNotFoundException;
import com.urbanfix.repository.ComplaintRepository;
import com.urbanfix.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class ComplaintService {
    private final ComplaintRepository complaintRepository;
    private final UserRepository userRepository;
    private final GeminiService geminiService;
    private final ImageService imageService;
    private final NotificationService notificationService;

    public ComplaintResponse createComplaint(ComplaintRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String department = geminiService.routeComplaint(request.getDescription(), request.getCategory());
        Double priority = request.getPriority() != null ? request.getPriority() : 1.0;
        String imagePath = imageService.storeImage(request.getImage());

        Complaint complaint = Complaint.builder()
                .userId(user.getId())
                .description(request.getDescription())
                .category(request.getCategory())
                .assignedDepartment(department)
                .severity(request.getSeverity())
                .priorityScore(priority)
                .status("Reported")
                .imageUrl(imagePath)
                .latitude(request.getLatitude() != null ? request.getLatitude() : 0.0)
                .longitude(request.getLongitude() != null ? request.getLongitude() : 0.0)
                .build();

        Complaint saved = complaintRepository.save(complaint);

        try {
            notificationService.createNotification(user.getId(), "Complaint submitted",
                    "Your complaint has been received and routed to " + department);
        } catch (Exception ignored) {}

        return map(saved);
    }

    public ComplaintResponse updateStatus(Long id, String newStatus) {
        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint not found"));

        complaint.setStatus(newStatus);
        Complaint saved = complaintRepository.save(complaint);

        try {
            notificationService.createNotification(
                    complaint.getUserId(),
                    "Status Updated",
                    "Your complaint #" + id + " status has been changed to: " + newStatus
            );
        } catch (Exception ignored) {}

        return map(saved);
    }

    public ComplaintResponse getComplaint(Long id) {
        return complaintRepository.findById(id)
                .map(this::map)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint not found"));
    }

    public List<ComplaintResponse> getUserComplaints(Long userId) {
        return complaintRepository.findByUserIdOrderByCreatedAtDesc(userId).stream().map(this::map).toList();
    }

    public List<ComplaintResponse> getAllComplaints() {
        return complaintRepository.findAll().stream().map(this::map).toList();
    }

    private ComplaintResponse map(Complaint complaint) {
        User user = userRepository.findById(complaint.getUserId()).orElse(null);
        LocalDateTime createdAt = null;
        if (complaint.getCreatedAt() != null) {
            createdAt = LocalDateTime.ofInstant(complaint.getCreatedAt(), ZoneId.systemDefault());
        }

        return ComplaintResponse.builder()
                .id(complaint.getId())
                .userId(complaint.getUserId())
                .userName(user != null ? user.getName() : null)
                .description(complaint.getDescription())
                .category(complaint.getCategory())
                .department(complaint.getAssignedDepartment())
                .priority(complaint.getPriorityScore())
                .status(complaint.getStatus())
                .imagePath(complaint.getImageUrl())
                .latitude(complaint.getLatitude())
                .longitude(complaint.getLongitude())
                .createdAt(createdAt)
                .build();
    }
}