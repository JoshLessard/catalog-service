package com.polarbookshop.catalogservice.domain;

import com.polarbookshop.catalogservice.config.DataConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jdbc.test.autoconfigure.DataJdbcTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@Import( DataConfig.class )
@AutoConfigureTestDatabase(
    replace = AutoConfigureTestDatabase.Replace.NONE
)
@ActiveProfiles( "integration" )
public class BookRepositoryJdbcTests {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private JdbcAggregateTemplate jdbcAggregateTemplate;

    @Test
    public void findBookByIsbnWhenExisting() {
        String isbn = "123456237";
        Book book = Book.of( isbn, "Title", "Author", 12.90, "Publisher" );
        jdbcAggregateTemplate.insert( book );

        Optional<Book> actualBook = bookRepository.findByIsbn( isbn );

        assertThat( actualBook )
            .isPresent();
        assertThat( actualBook.get().isbn() )
            .isEqualTo( isbn );
    }
}
