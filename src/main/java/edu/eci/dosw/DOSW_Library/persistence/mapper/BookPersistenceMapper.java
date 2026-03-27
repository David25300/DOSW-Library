package edu.eci.dosw.DOSW_Library.persistence.mapper;

import edu.eci.dosw.DOSW_Library.core.model.Book;
import edu.eci.dosw.DOSW_Library.persistence.entity.BookEntity;
import org.springframework.stereotype.Component;

@Component
public class BookPersistenceMapper {

    public Book toDomain(BookEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Book(entity.getId(), entity.getTitle(), entity.getAuthor(), entity.isAvailable());
    }

    public BookEntity toEntity(Book domain) {
        if (domain == null) {
            return null;
        }
        return new BookEntity(domain.getId(), domain.getTitle(), domain.getAuthor(), domain.isAvailable());
    }
}
