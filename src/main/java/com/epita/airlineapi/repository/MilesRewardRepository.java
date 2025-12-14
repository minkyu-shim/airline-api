package com.epita.airlineapi.repository;

import com.epita.airlineapi.model.MilesReward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MilesRewardRepository extends JpaRepository<MilesReward, Long> {
    // Custom Derived Query
    // Spring interprets this as:
    // 1. Look at MilesReward
    // 2. Find the 'client' field
    // 3. Find the 'userId' field inside Client
    // 4. Generate SQL: WHERE client.user_id = ?
//    List<MilesReward> findByClient_UserId(Long clientId);
//
//    // You can also add one for Flight if needed
//    List<MilesReward> findByFlight_Id(Long flightId);
}
