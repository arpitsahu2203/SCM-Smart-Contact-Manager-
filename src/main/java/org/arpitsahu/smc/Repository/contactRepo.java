package org.arpitsahu.smc.Repository;

import org.arpitsahu.smc.Entities.Contact;
import org.arpitsahu.smc.Entities.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface contactRepo extends JpaRepository<Contact,String> {

    Page<Contact> findByUser(Users user, PageRequest pageable);

    @Query("select c from Contact c where c.user.id=:id")
    List<Contact> findByUserId(@Param("id") String id);
}
