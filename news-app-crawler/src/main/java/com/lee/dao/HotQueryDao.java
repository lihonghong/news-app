package com.lee.dao;

import com.lee.model.HotQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

/**
 * Created by honghong on 17/2/24.
 */
public class HotQueryDao {

  private static final Logger LOGGER = LoggerFactory.getLogger(HotQueryDao.class);
  private static HotQueryDao hotQueryDao = null;
  private NamedParameterJdbcTemplate jdbcTemplate;


  public static HotQueryDao getInstance() {
    if (hotQueryDao == null) {
      ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
      hotQueryDao = (HotQueryDao) context.getBean("hotQueryDao");
    }
    return hotQueryDao;
  }

  public static void main(String[] args) {
    HotQueryDao hotQueryDao = getInstance();
    HotQuery hotQuery = new HotQuery();
    hotQuery.setName("game");
    hotQuery.setType("news");
    hotQuery.setPosition(2);
    System.out.println(hotQueryDao.update(hotQuery));
    System.out.println(hotQueryDao.query());
  }

  public void setDataSource(DataSource dataSource) {
    System.out.println(dataSource);
    this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
  }

  public List query() {
    String sql = "select * from news_hotquery";
    SqlParameterSource namedParams = new MapSqlParameterSource();
    return jdbcTemplate.query(sql, namedParams, new RowMapper() {
      @Override
      public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        return rs.getString("name");
      }
    });
  }

  public int update(HotQuery hotQuery) {
    String selectSql = "select * from news_hotquery where name = :name";
    String name = hotQuery.getName().replaceAll("\t", "").trim();
    SqlParameterSource namedParams = new MapSqlParameterSource()
        .addValue("name", name, Types.VARCHAR)
        .addValue("source", hotQuery.getSource(), Types.VARCHAR)
        .addValue("type", hotQuery.getType(), Types.VARCHAR)
        .addValue("category", hotQuery.getCategory(), Types.VARCHAR)
        .addValue("position", hotQuery.getPosition(), Types.INTEGER)
        .addValue("valid", 1, Types.INTEGER);

    List list = jdbcTemplate.query(selectSql, namedParams, new RowMapper() {
      @Override
      public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        return rs.getString("type");
      }
    });

    LOGGER.info("store {},{}", hotQuery.getName(), hotQuery.getType());
    if (list.size() <= 0) {      //不存在数据，那就插入
      String insertSql = "insert into news_hotquery (name,source,type,category,valid,position,create_time) values(:name,:source,:type,:category,:valid,:position,now())";
      return jdbcTemplate.update(insertSql, namedParams);
    } else {
      String updateSql = "update news_hotquery set source = :source, type = :type, category = :category, valid = :valid, position = :position, update_time = now() where name = :name";
      return jdbcTemplate.update(updateSql, namedParams);
    }
  }

  public void updateHotQueryList(List<HotQuery> hotQueryList) {
    for (HotQuery hotQuery : hotQueryList) {
      update(hotQuery);
    }
  }
}
