CREATE TABLE date(
    dateID SERIAL PRIMARY KEY,
    day int NOT NULL,
    month int NOT NULL,
    year int NOT NULL,
    quarter int NOT NULL
);


CREATE TABLE sales(
    saleID SERIAL PRIMARY KEY,
    date int REFERENCES date (dateID),
    shopID int REFERENCES shop (shopid),
    articleID int REFERENCES article (articleid),
    sold int NOT NULL,
    revenue double precision
);
