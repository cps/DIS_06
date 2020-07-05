CREATE TABLE sales(
    saleID SERIAL PRIMARY KEY,
    day int NOT NULL,
    month int NOT NULL,
    year int NOT NULL,
    quarter int NOT NULL,
    shopID int REFERENCES shop (shopid),
    articleID int REFERENCES article (articleid),
    sold int NOT NULL,
    revenue double precision
);
