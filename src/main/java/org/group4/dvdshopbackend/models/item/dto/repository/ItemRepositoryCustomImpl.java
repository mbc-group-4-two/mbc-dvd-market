package org.group4.dvdshopbackend.models.item.dto.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.group4.dvdshopbackend.common.entity.Item;
import org.group4.dvdshopbackend.common.enums.ItemSellStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<Item> search(String keyword, ItemSellStatus status, Pageable pageable) {
        String where = " where 1=1";
        if (keyword != null && !keyword.isBlank()) {
            where += " and (lower(i.itemNm) like lower(concat('%', :kw, '%'))" +
                    " or lower(i.itemDetail) like lower(concat('%', :kw, '%')))";
        }
        if (status != null) {
            where += " and i.itemSellStatus = :st";
        }

        var dataQ = em.createQuery("select i from Item i" + where + " order by i.id desc", Item.class);
        var cntQ  = em.createQuery("select count(i) from Item i" + where, Long.class);

        if (keyword != null && !keyword.isBlank()) {
            dataQ.setParameter("kw", keyword);
            cntQ.setParameter("kw", keyword);
        }
        if (status != null) {
            dataQ.setParameter("st", status);
            cntQ.setParameter("st", status);
        }

        dataQ.setFirstResult((int) pageable.getOffset());
        dataQ.setMaxResults(pageable.getPageSize());

        List<Item> content = dataQ.getResultList();
        long total = cntQ.getSingleResult();

        return new PageImpl<>(content, pageable, total);
    }
}
