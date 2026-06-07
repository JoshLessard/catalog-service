package com.polarbookshop.catalogservice.web;

import com.polarbookshop.catalogservice.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.boot.test.json.ObjectContent;

import java.io.IOException;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookJsonTests {

    @Autowired
    private JacksonTester<Book> json;

    @Test
    public void testSerialize() throws IOException {
        Book book = new Book(
            339L,
            "1234567890",
            "Title",
            "Author",
            9.90,
            "Wombly Publishing",
            Instant.ofEpochMilli( 43785239324L ),
            Instant.ofEpochMilli( 58234872358L ),
            18
        );

        JsonContent<Book> jsonContent = json.write( book );

        assertThat( jsonContent )
            .extractingJsonPathNumberValue( "@.id" )
            .satisfies( id -> assertThat( id.longValue() ).isEqualTo( book.id() ) );
        assertThat( jsonContent )
            .extractingJsonPathStringValue( "@.isbn" )
            .isEqualTo( book.isbn() );
        assertThat( jsonContent )
            .extractingJsonPathStringValue( "@.title" )
            .isEqualTo( book.title() );
        assertThat( jsonContent )
            .extractingJsonPathStringValue( "@.author" )
            .isEqualTo( book.author() );
        assertThat( jsonContent )
            .extractingJsonPathNumberValue( "@.price" )
            .isEqualTo( book.price() );
        assertThat( jsonContent )
            .extractingJsonPathStringValue( "@.publisher" )
            .isEqualTo( "Wombly Publishing" );
        assertThat( jsonContent )
            .extractingJsonPathValue( "@.createdDate" )
            .isEqualTo( book.createdDate().toString() );
        assertThat( jsonContent )
            .extractingJsonPathValue( "@.lastModifiedDate" )
            .isEqualTo( book.lastModifiedDate().toString() );
        assertThat( jsonContent )
            .extractingJsonPathNumberValue( "@.version" )
            .isEqualTo( book.version() );
    }

    @Test
    public void testDeserialize() throws IOException {
        String content =
            """
            {
                "id": 14,
                "isbn": "1234567890",
                "title": "Title",
                "author": "Author",
                "price": 9.90,
                "publisher": "O'Reilly",
                "createdDate": "2008-05-06T12:11:27.319Z",
                "lastModifiedDate": "2012-06-18T09:37:33.973Z",
                "version": 27
            }
            """;

        ObjectContent<Book> parsedBook = json.parse( content );

        assertThat( parsedBook )
            .usingRecursiveComparison()
            .isEqualTo(
                new Book(
                    14L,
                    "1234567890",
                    "Title",
                    "Author",
                    9.90,
                    "O'Reilly",
                    Instant.parse( "2008-05-06T12:11:27.319Z" ),
                    Instant.parse( "2012-06-18T09:37:33.973Z" ),
                    27
                )
            );
    }
}
