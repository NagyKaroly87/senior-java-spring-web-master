package hu.ponte.hr.repository;

import hu.ponte.hr.entity.Doc;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocRepository extends JpaRepository<Doc, Long> {
}
