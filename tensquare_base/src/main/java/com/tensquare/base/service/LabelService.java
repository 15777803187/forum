package com.tensquare.base.service;

import com.tensquare.base.dao.LabelDao;
import com.tensquare.base.pojo.Label;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LabelService {
    @Autowired
    private LabelDao labelDao;

    @Autowired
    private IdWorker idWorker;

    public Result findAll() {
        List<Label> list = labelDao.findAll();
        return new Result(true, StatusCode.OK, "查询所有", list);
    }

    public Result findById(String labelId) {
        Optional<Label> optional = labelDao.findById(labelId);
        if (optional.isPresent()) {
            Label label = optional.get();
            return new Result(true, StatusCode.OK, "id查询", label);
        }
        throw new RuntimeException("查询失败");
    }

    public Result update(Label label) {
        if (label == null) {
            throw new RuntimeException("参数错误");
        }
        String labelId = label.getId();
        if (labelId == null || "".equals(labelId)) {
            throw new RuntimeException("参数错误");
        }
        Optional<Label> optional = labelDao.findById(labelId);
        if (!optional.isPresent()) {
            throw new RuntimeException("数据不存在");
        }
        labelDao.save(label);//jpa中保存和更新使用同一个方法，但是进行更新时需要pojo的id存在
        return new Result(true, StatusCode.OK, "更新成功");
    }

    public Result save(Label label) {
        if (label == null) {
            throw new RuntimeException("参数错误");
        }
        label.setId(idWorker.nextId() + "");//保存操作需要指定id
        labelDao.save(label);
        return new Result(true, StatusCode.OK, "保存成功");
    }

    public Result delete(String labelId) {
        if (labelId == null) {
            throw new RuntimeException("参数错误");
        }
        Optional<Label> optional = labelDao.findById(labelId);
        if (!optional.isPresent()) {
            throw new RuntimeException("数据不存在");
        }
        labelDao.deleteById(labelId);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    public Result findSearch(Label label) {
        if (label == null) {
            return new Result(true, StatusCode.OK, "查询所有", labelDao.findAll());
        }
        List<Label> labels = labelDao.findAll(new Specification<Label>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                if (label.getLabelname() != null && !"".equals(label.getLabelname())) {
                    Predicate predicate = criteriaBuilder.like(root.get("labelname").as(String.class), "%" + label.getLabelname() + "%");
                    list.add(predicate);
                }
                if (label.getState() != null && !"".equals(label.getState())) {
                    Predicate predicate = criteriaBuilder.equal(root.get("state").as(String.class), label.getState());
                    list.add(predicate);
                }
                Predicate[] predicates = new Predicate[list.size()];
                list.toArray(predicates);
                return criteriaBuilder.and(predicates);
            }
        });
        return new Result(true, StatusCode.OK, "查询条件", labels);
    }

    public Result findSearchWithPage(Label label, int page, int size) {
        if (page <= 0) {
            page = 1;
        }
        if (size <= 0) {
            size = 1;
        }
        Pageable pageable = PageRequest.of(page - 1, size);
        if (label == null) {
            return new Result(true, StatusCode.OK, "查询所有", labelDao.findAll());
        }
        Page<Label> pages = labelDao.findAll(new Specification<Label>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                if (label.getLabelname() != null && !"".equals(label.getLabelname())) {
                    Predicate predicate = criteriaBuilder.like(root.get("labelname").as(String.class), "%" + label.getLabelname() + "%");
                    list.add(predicate);
                }
                if (label.getState() != null && !"".equals(label.getState())) {
                    Predicate predicate = criteriaBuilder.equal(root.get("state").as(String.class), label.getState());
                    list.add(predicate);
                }
                Predicate[] predicates = new Predicate[list.size()];
                list.toArray(predicates);
                return criteriaBuilder.and(predicates);
            }
        }, pageable);
        PageResult<Label> pageResult = new PageResult<>();
        pageResult.setTotal(pages.getTotalPages());
        pageResult.setRows(pages.getContent());
        return new Result(true, StatusCode.OK, "查询条件", pageResult);
    }
}
