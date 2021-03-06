package com.valework.yingul.dao;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import com.valework.yingul.model.Yng_Card;
import com.valework.yingul.model.Yng_User;


public interface CardDao extends CrudRepository<Yng_Card, Long>{
	List<Yng_Card> findAll();
	Yng_Card findByNumber(Long number);
	Yng_Card findByNumberAndUser(Long number, Yng_User user);
}
