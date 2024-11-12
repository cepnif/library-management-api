package config;

public enum Endpoints {
    LOGIN("/member/login/"),
    SHOW_ALL_BOOKS("/books/all/"),
    SEARCH_BOOK_BY_TITLE("/books/title/"),
    SEARCH_BOOK_BY_AUTHOR("/books/author/"),
    CHECK_BOOK_AVAILABILITY("/books/availability/{title}"),
    BORROW_BOOK("/transactions/borrow/"),
    RETURN_BOOK("/transactions/return/"),
    BORROWING_HISTORY("/transactions/borrowing-history/");

    private final String path;

    Endpoints(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
