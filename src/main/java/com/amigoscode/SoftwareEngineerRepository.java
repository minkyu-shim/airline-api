package com.amigoscode;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import java.util.List;
// or it can extend Crud
// classes created stored in global classes
// SoftwareEngineer class is scanned if available

@Service
public interface SoftwareEngineerRepository extends JpaRepository<SoftwareEngineer, Integer> {  //Generic Type

}
