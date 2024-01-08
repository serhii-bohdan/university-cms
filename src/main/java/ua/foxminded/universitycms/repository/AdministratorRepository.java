package ua.foxminded.universitycms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxminded.universitycms.model.Administrator;

/**
 * The {@code AdministratorRepository} interface is a Spring Data JPA repository
 * for {@link Administrator} entities.
 * <p>
 * This interface extends {@link JpaRepository}, which provides JPA related
 * methods such as save(), findOne(), findAll(), count(), delete(). This
 * interface is annotated with {@code @Repository}, indicating that it's a
 * "Repository" bean. A Repository is a mechanism for encapsulating storage,
 * retrieval, and search behavior which emulates a collection of objects.
 *
 * @author Serhii Bohdan
 */
@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, Long> {

}
