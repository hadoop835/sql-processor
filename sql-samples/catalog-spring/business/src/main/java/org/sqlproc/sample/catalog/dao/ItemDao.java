package org.sqlproc.sample.catalog.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.sqlproc.engine.SqlEngineFactory;
import org.sqlproc.engine.SqlSession;
import org.sqlproc.sample.catalog.form.ItemForm;
import org.sqlproc.sample.catalog.model.Item;
import org.sqlproc.sample.catalog.model.OrderIds;
import org.sqlproc.sample.catalog.to.ItemTO;

public class ItemDao {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected SqlEngineFactory sqlFactory;

    @Cacheable(value = "itemCache", key = "#id")
    public Item findById(SqlSession session, Long id) {
        Item item = sqlFactory.getCheckedCrudEngine("GET_ITEM").get(session, Item.class, new Item().withItemid(id));
        logger.info("findById: " + item.toString());
        return item;
    }

    @CacheEvict(value = "itemCache", key = "#item.itemid")
    public Item create(SqlSession session, Item item) {
        logger.info("create: " + item.toString());
        sqlFactory.getCheckedCrudEngine("INSERT_ITEM").insert(session, item);
        logger.info("create: " + item.toString());
        return item;
    }

    @CacheEvict(value = "itemCache", key = "#item.itemid")
    public Item update(SqlSession session, Item item) {
        logger.info("update: " + item.toString());
        sqlFactory.getCheckedCrudEngine("UPDATE_ITEM").update(session, item);
        logger.info("update: " + item.toString());
        return item;
    }

    @CacheEvict(value = "itemCache", key = "#id")
    public void delete(SqlSession session, Long id) {
        logger.info("delete: " + id);
        sqlFactory.getCheckedCrudEngine("DELETE_ITEM").delete(session, new Item().withItemid(id));
    }

    public List<ItemTO> find(SqlSession session, ItemForm criteria) {
        logger.info("find: " + criteria);
        return sqlFactory.getCheckedQueryEngine("ITEMS").query(session, ItemTO.class, criteria, null,
                OrderIds.getOrder(criteria.getOrder()), 0, criteria.getCount(), criteria.getFirst());
    }

    public int findCount(SqlSession session, ItemForm criteria) {
        logger.info("findCount: " + criteria);
        return sqlFactory.getCheckedQueryEngine("ITEMS").queryCount(session, criteria);
    }

    @Required
    public void setSqlFactory(SqlEngineFactory sqlFactory) {
        this.sqlFactory = sqlFactory;
    }
}
