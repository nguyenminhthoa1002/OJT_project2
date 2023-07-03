package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import project.model.dto.response.Revenue;
import project.model.entity.Cart;
import project.model.entity.Product;
import project.model.entity.Wish;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Cart,Integer>  {
    @Query(value = "SELECT\n" +
            "    date_list.date AS date,\n" +
            "    IFNULL(SUM(cart.tax), 0) AS totalTax,\n" +
            "    IFNULL(SUM(cart.shipping), 0) AS totalShip,\n" +
            "    IFNULL(SUM(cart.discount), 0) AS totalDiscount,\n" +
            "    IFNULL(SUM(cart.total), 0) AS total,\n" +
            "    IFNULL(count(cart.total), 0) AS numberOder,\n" +
            "    IFNULL(city,:city) AS city\n" +
            "FROM (\n" +
            "         SELECT DATE_ADD(:start, INTERVAL n DAY) AS date\n" +
            "         FROM (\n" +
            "                  SELECT @row \\:= @row + 1 AS n\n" +
            "                  FROM (\n" +
            "                           SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4\n" +
            "                           UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9\n" +
            "                       ) r1,\n" +
            "                       (\n" +
            "                           SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4\n" +
            "                           UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9\n" +
            "                       ) r2,\n" +
            "                       (SELECT @row \\:= -1) x\n" +
            "              ) numbers\n" +
            "         WHERE DATE_ADD(:start, INTERVAL n DAY) BETWEEN :start AND :end\n" +
            "     ) date_list\n" +
            "         LEFT JOIN cart ON cart.creatDate = date_list.date AND cart.status = :status AND cart.city=:city\n" +
            "GROUP BY date_list.date\n" +
            "ORDER BY date_list.date",nativeQuery = true)
    List<Object[]> find_by_day_address(@Param("start") LocalDate start, @Param("end") LocalDate end, @Param("city") String city, @Param("status") Integer status);
    @Query(value = "SELECT CONCAT(weeks.weekDate, ' - ', DATE_ADD(weeks.weekDate, INTERVAL 6 DAY)) AS date,\n" +
            "       IFNULL(SUM(cart.tax), 0) AS totalTax,\n" +
            "       IFNULL(SUM(cart.shipping), 0) AS totalShip,\n" +
            "       IFNULL(SUM(cart.discount), 0) AS totalDiscount,\n" +
            "       IFNULL(SUM(cart.total), 0) AS total,\n" +
            "       IFNULL(count(cart.id), 0) AS numberOder,\n" +
            "       IFNULL(city, :city) city\n" +
            "FROM (\n" +
            "         SELECT ADDDATE(:start, INTERVAL n WEEK) AS weekDate\n" +
            "         FROM (\n" +
            "                  SELECT @row \\:= @row + 1 AS n\n" +
            "                  FROM (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3) r1,\n" +
            "                       (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3) r2,\n" +
            "                       (SELECT @row \\:= -1) x\n" +
            "              ) numbers\n" +
            "     ) weeks\n" +
            "         LEFT JOIN cart ON cart.creatDate BETWEEN weeks.weekDate AND DATE_ADD(weeks.weekDate, INTERVAL 6 DAY) and cart.city=:city and cart.status=:status\n" +
            "WHERE weeks.weekDate BETWEEN :start AND :end\n" +
            "GROUP BY weeks.weekDate",nativeQuery = true)
    List<Object[]> find_by_week_address(@Param("start") LocalDate start, @Param("end") LocalDate end, @Param("city") String city, @Param("status") Integer status);


    @Query(value = "SELECT CONCAT(weeks.weekDate, ' - ', LAST_DAY(weeks.weekDate)) AS date,\n" +
            "       IFNULL(SUM(cart.tax), 0) AS totalTax,\n" +
            "       IFNULL(SUM(cart.shipping), 0) AS totalShip,\n" +
            "       IFNULL(SUM(cart.discount), 0) AS totalDiscount,\n" +
            "       IFNULL(SUM(cart.total), 0) AS total,\n" +
            "       IFNULL(count(cart.id), 0) AS numberOder,\n" +
            "       IFNULL(city, ?3) city\n" +
            "FROM (\n" +
            "         SELECT ADDDATE(?1, INTERVAL n MONTH) AS weekDate\n" +
            "         FROM (\n" +
            "                  SELECT @row \\:= @row + 1 AS n\n" +
            "                  FROM (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3) r1,\n" +
            "                       (SELECT @row \\:= -1) x\n" +
            "              ) numbers\n" +
            "     ) weeks\n" +
            "         LEFT JOIN cart ON cart.creatDate BETWEEN weeks.weekDate AND LAST_DAY(weeks.weekDate) and cart.city=?3 and cart.status=?4\n" +
            "WHERE weeks.weekDate BETWEEN ?1 AND ?2\n" +
            "GROUP BY weeks.weekDate, city",nativeQuery = true)
    List<Object[]> find_by_month_address(@Param("start") LocalDate start, @Param("end") LocalDate end, @Param("city") String city, @Param("status") Integer status);


    @Query(value = "SELECT\n" +
            "    date_list.date AS date,\n" +
            "    IFNULL(SUM(cart.tax), 0) AS totalTax,\n" +
            "    IFNULL(SUM(cart.shipping), 0) AS totalShip,\n" +
            "    IFNULL(SUM(cart.discount), 0) AS totalDiscount,\n" +
            "    IFNULL(SUM(cart.total), 0) AS total,\n" +
            "    IFNULL(count(cart.id), 0) AS numberOder\n" +
            "FROM (\n" +
            "         SELECT DATE_ADD(:start, INTERVAL n DAY) AS date\n" +
            "         FROM (\n" +
            "                  SELECT @row \\:= @row + 1 AS n\n" +
            "                  FROM (\n" +
            "                           SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4\n" +
            "                           UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9\n" +
            "                       ) r1,\n" +
            "                       (\n" +
            "                           SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4\n" +
            "                           UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9\n" +
            "                       ) r2,\n" +
            "                       (SELECT @row \\:= -1) x\n" +
            "              ) numbers\n" +
            "         WHERE DATE_ADD(:start, INTERVAL n DAY) BETWEEN :start AND :end\n" +
            "     ) date_list\n" +
            "         LEFT JOIN cart ON cart.creatDate = date_list.date AND cart.status =:status \n" +
            "GROUP BY date_list.date\n" +
            "ORDER BY date_list.date;",nativeQuery = true)
    List<Object[]> find_by_day_total(@Param("start") LocalDate start, @Param("end") LocalDate end, @Param("status") Integer status);

    @Query(value = "SELECT CONCAT(weeks.weekDate, ' - ', DATE_ADD(weeks.weekDate, INTERVAL 6 DAY)) AS date,\n" +
            "       IFNULL(SUM(cart.tax), 0)                 AS totalTax,\n" +
            "       IFNULL(SUM(cart.shipping), 0)            AS totalShip,\n" +
            "       IFNULL(SUM(cart.discount), 0)            AS totalDiscount,\n" +
            "       IFNULL(SUM(cart.total), 0)               AS total,\n" +
            "       IFNULL(count(cart.total), 0) AS numberOder \n" +
            "FROM (SELECT ADDDATE(:start, INTERVAL n WEEK) AS weekDate\n" +
            "      FROM (SELECT @row \\:= @row + 1 AS n\n" +
            "            FROM (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3) r1,\n" +
            "                 (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3) r2,\n" +
            "                 (SELECT @row \\:= -1) x) numbers) weeks\n" +
            "         LEFT JOIN cart ON cart.creatDate BETWEEN weeks.weekDate AND DATE_ADD(weeks.weekDate, INTERVAL 6 DAY) and\n" +
            "                           cart.status = :status\n" +
            "WHERE weeks.weekDate BETWEEN :start AND :end\n" +
            "GROUP BY weeks.weekDate;",nativeQuery = true)
    List<Object[]> find_by_week_total(@Param("start") LocalDate start, @Param("end") LocalDate end, @Param("status") Integer status);
    @Query(value = "SELECT CONCAT(weeks.weekDate, ' - ', LAST_DAY(weeks.weekDate)) AS date,\n" +
            "                   IFNULL(SUM(cart.tax), 0) AS totalTax,\n" +
            "                  IFNULL(SUM(cart.shipping), 0) AS totalShip,\n" +
            "                   IFNULL(SUM(cart.discount), 0) AS totalDiscount,\n" +
            "                  IFNULL(SUM(cart.total), 0) AS total,\n" +
            "               IFNULL(count(cart.id), 0) AS numberOder \n" +
            "            FROM (\n" +
            "                    SELECT ADDDATE(:start, INTERVAL n MONTH) AS weekDate\n" +
            "                     FROM (\n" +
            "                              SELECT @row \\:= @row + 1 AS n\n" +
            "                              FROM (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3) r1,\n" +
            "                                   (SELECT @row \\:= -1) x\n" +
            "                          ) numbers\n" +
            "                 ) weeks\n" +
            "                     LEFT JOIN cart ON cart.creatDate BETWEEN weeks.weekDate AND LAST_DAY(weeks.weekDate)  and cart.status=:status\n" +
            "            WHERE weeks.weekDate BETWEEN :start AND :end\n" +
            "            GROUP BY weeks.weekDate",nativeQuery = true)
    List<Object[]> find_by_month_total(@Param("start") LocalDate start, @Param("end") LocalDate end, @Param("status") Integer status);


}
