package com.brainz.sms_backend.Classroom;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassRoomRepository extends JpaRepository<ClassRoom, Long> {
}
