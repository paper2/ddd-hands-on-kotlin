DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS stocks;

CREATE TYPE Status AS ENUM (
    'IN_STOCK',
    'LOW_STOCK',
    'OUT_OF_STOCK'
);

CREATE TABLE books (
    book_id VARCHAR PRIMARY KEY,
    title TEXT NOT NULL,
    price_amount FLOAT NOT NULL
);

CREATE TABLE stocks (
    stock_id VARCHAR PRIMARY KEY,
    quantity_available INTEGER NOT NULL,
    status Status NOT NULL DEFAULT 'OUT_OF_STOCK',
    book_id VARCHAR UNIQUE NOT NULL,
    CONSTRAINT fk_book FOREIGN KEY (book_id) REFERENCES books(book_id) ON DELETE CASCADE
);
