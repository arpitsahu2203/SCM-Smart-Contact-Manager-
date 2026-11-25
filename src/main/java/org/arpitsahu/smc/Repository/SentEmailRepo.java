package org.arpitsahu.smc.Repository;

import org.arpitsahu.smc.Entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface SentEmailRepo extends JpaRepository<SentEmail, String> {
    List<SentEmail> findByContactAndUserOrderBySentAtDesc(Contact contact, Users user);

    List<SentEmail> findByUserOrderBySentAtDesc(Users user);
}
