package org.arpitsahu.smc.Repository;

import ch.qos.logback.core.joran.action.ContextPropertyAction;
import org.arpitsahu.smc.Entities.Contact;
import org.arpitsahu.smc.Entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.ListIterator;

@Repository
public interface contactRepo extends JpaRepository<Contact,String> {

    List<Contact> findByUser(Users user);

    @Query("select c from Contact c where c.user.id=:id")
    List<Contact> findByUserId(@Param("id") String id);
}
