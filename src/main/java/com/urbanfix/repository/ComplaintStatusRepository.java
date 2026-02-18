package com.urbanfix.repository;

import com.urbanfix.entity.ComplaintStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplaintStatusRepository extends JpaRepository<ComplaintStatus, Long> {
    List<ComplaintStatus> findByComplaintIdOrderByTimestampAsc(Long complaintId);
}
