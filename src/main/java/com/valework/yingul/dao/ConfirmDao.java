package com.valework.yingul.dao;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import com.valework.yingul.model.Yng_Buy;
import com.valework.yingul.model.Yng_Confirm;
import com.valework.yingul.model.Yng_User;

public interface ConfirmDao extends CrudRepository<Yng_Confirm, Long>{
	Yng_Confirm findByConfirmId(Long confirmId);
	List<Yng_Confirm> findByOrderByConfirmIdDesc();
	List<Yng_Confirm> findByStatus(String status);
	List<Yng_Confirm> findByBuyerAndStatusOrderByConfirmIdDesc(Yng_User buyer, String status);
	List<Yng_Confirm> findBySellerAndStatusOrderByConfirmIdDesc(Yng_User seller, String status);
	List<Yng_Confirm> findByBuyerOrderByConfirmIdDesc(Yng_User buyer);
	List<Yng_Confirm> findBySellerOrderByConfirmIdDesc(Yng_User seller);
	Yng_Confirm findByBuy(Yng_Buy buy);
}
