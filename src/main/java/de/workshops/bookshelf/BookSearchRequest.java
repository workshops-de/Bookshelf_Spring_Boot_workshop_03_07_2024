package de.workshops.bookshelf;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

record BookSearchRequest(String isbn, @NotBlank @Size(min = 3) String author) {}
