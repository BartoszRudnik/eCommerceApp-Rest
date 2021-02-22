package ecommerce.dao;

import ecommerce.entities.State;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestParam;

@RepositoryRestResource
@CrossOrigin
public interface StateRepository extends JpaRepository<State, Integer> {

    Page<State> findByCountryId(@RequestParam("id") int id, Pageable page);

}
