# Customer Balance Tracking

## Project Purpose
This project aims to show the date when a customer reaches the maximum debt. Users can select a specific customer to track that customer's debt status.

## Features
- Customer definitions and invoice information are stored in the database.
- When a user selects a customer, they can see the date when that customer reached the maximum debt.
- Invoices can be issued on different dates, and customers can pay these invoices whenever they want.

## Technologies Used
- Spring Boot
- Docker
- PostgreSQL
- JPA (Java Persistence API)
- Thymeleaf

## Database Structure
### `musteri_tanim_table`
| Column Name | Column Type | Length | Description                   |
|-------------|-------------|--------|-------------------------------|
| id          | NUMERIC     | 10     | Customer ID (PRIMARY)         |
| unvan       | CHAR        | 255    | Customer title                |

### `musteri_fatura_table`
| Column Name      | Column Type | Length | Description                                          |
|------------------|-------------|--------|------------------------------------------------------|
| id               | NUMERIC     | 10     | Invoice ID (PRIMARY)                                 |
| musteri_id      | NUMERIC     | 10     | Customer ID                                          |
| fatura_tarihi     | DATE        |        | Date the invoice was issued to the customer         |
| fatura_tutari    | NUMERIC     | 18,2   | Amount of the invoice issued to the customer        |
| odeme_tarihi     | DATE        |        | Date the customer paid the invoice                   |

## Installation
1. Clone the project:
   ```bash
   git clone https://github.com/f2rkan/customer-balance-tracking.git
   ```
2. Install Java, Spring Boot, and Docker.
3. Update the database connection settings in the `application.properties` file or continue using the default entries.
4. Start the PostgreSQL Docker:
   ```bash
   docker-compose up -d
   ```
5. Import the local database dump into the Docker container. This step involves importing the dump of the database from the PostgreSQL inside the Docker container used while creating the application.
   ```bash
   docker cp /your/file/path/musteri-bakiye-seyri/musteri_db_backup.dump musteri-bakiye-db:/var/lib/postgresql/data/
   ```
6. Enter the Docker container:
   ```bash
   docker exec -it musteri-bakiye-db bash
   ```
7. Use the following command to load the database (replace the `yourusername` part with your PostgreSQL username; the default username is set as `yourusername`):
   ```bash
   pg_restore -U yourusername -d musteri_db /var/lib/postgresql/data/musteri_db_backup.dump
   ```
8. While in the project directory, load dependencies with Maven:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```
9. Access the application by navigating to the following address in your browser:
   ```
   http://localhost:8080/musteri/list
   ```

## PostgreSQL Operations in Docker Container

To perform operations on the PostgreSQL database within the Docker container, follow these steps:

1. Access the PostgreSQL command line interface:
   ```bash
   docker exec -it musteri-bakiye-db psql -U yourusername -d musteri_db
   ```

2. To view the tables in the database, use the following command:
   ```sql
   \dt
   ```

   **Example output:**
   ```
                  List of relations
   Schema |         Name         | Type  |    Owner     
   --------+----------------------+-------+--------------
    public | fatura               | table | yourusername
    public | musteri              | table | yourusername
    public | musteri_fatura_table | table | yourusername
    public | musteri_tanim_table  | table | yourusername
   ```

3. To see all records in the `musteri_tanim_table`, execute:
   ```sql
   SELECT * FROM musteri_tanim_table;
   ```

4. To see all records in the `musteri_fatura_table`, execute:
   ```sql
   SELECT * FROM musteri_fatura_table;
   ```

5. You can execute queries to test the JPA operations implemented in Java. Here’s an example query to calculate the maximum debt date for each customer:
   ```sql
   WITH BorcHesabi AS (
       SELECT 
           f.musteri_id,
           f.fatura_tarihi,
           SUM(f.fatura_tutari) OVER (PARTITION BY f.musteri_id ORDER BY f.fatura_tarihi ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW) AS toplam_borc
       FROM 
           musteri_fatura_table f
       WHERE 
           f.odeme_tarihi IS NULL 
   )
   SELECT 
       musteri_id,
       fatura_tarihi AS max_borc_tarihi,
       toplam_borc
   FROM 
       BorcHesabi
   WHERE 
       toplam_borc = (
           SELECT MAX(toplam_borc) 
           FROM BorcHesabi bh 
           WHERE bh.musteri_id = BorcHesabi.musteri_id
       )
   ORDER BY 
       musteri_id; 
   ```

   **Example Output:**
   ```
    musteri_id |   max_borc_tarihi   | toplam_borc 
   ------------+---------------------+-------------
        127098 | 2022-05-18 00:00:00 |       27000
        127747 | 2022-06-30 00:00:00 |  6873915.13
   (2 rows)
   ```
