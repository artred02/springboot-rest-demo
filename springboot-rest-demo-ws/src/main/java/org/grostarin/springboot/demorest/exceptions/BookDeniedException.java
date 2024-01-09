package org.grostarin.springboot.demorest.exceptions;

public class BookDeniedException extends RuntimeException {

    public BookDeniedException() {
        super();
    }

    public BookDeniedException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public BookDeniedException(final String message) {
        super(message);
    }

    public BookDeniedException(final Throwable cause) {
        super(cause);
    }
}