package com.valework.yingul.batch;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.valework.yingul.dao.AccountDao;
import com.valework.yingul.dao.ConfirmDao;
import com.valework.yingul.dao.TransactionDao;
import com.valework.yingul.model.Yng_Account;
import com.valework.yingul.model.Yng_Confirm;
import com.valework.yingul.model.Yng_Transaction;

@Component
public class GreetingBatchBean {
	
	@Autowired
	ConfirmDao confirmDao;
	
	@Autowired
	TransactionDao transactionDao;
	
	@Autowired
	AccountDao accountDao;
	
	//@Scheduled(cron = "0,30 * * * * *")//para cada 30 segundos
	@Scheduled(cron = "0 0 10 * * *")//cada dia a las 6 de la mañana
	public void cronJob() {
		Date date = new Date();
    	DateFormat hourdateFormat = new SimpleDateFormat("dd");
    	DateFormat hourdateFormat1 = new SimpleDateFormat("MM");
    	DateFormat hourdateFormat2 = new SimpleDateFormat("yyyy");
    	DateFormat hourdateFormat4 = new SimpleDateFormat("HH");
    	DateFormat hourdateFormat5 = new SimpleDateFormat("mm");
    	DateFormat hourdateFormat6 = new SimpleDateFormat("ss");
		List<Yng_Confirm> listConfirm = confirmDao.findByStatus("confirm");
		for (Yng_Confirm s : listConfirm) {
			if(s.getDayEndClaim()<=Integer.parseInt(hourdateFormat.format(date))&&s.getMonthEndClaim()<=Integer.parseInt(hourdateFormat1.format(date))&&s.getYearEndClaim()<=Integer.parseInt(hourdateFormat2.format(date))) {
				s.setStatus("closed");
				Yng_Account accountTemp= accountDao.findByUser(s.getBuy().getYng_item().getUser());
				Yng_Transaction transactionTemp = new Yng_Transaction();
				transactionTemp.setAccount(accountTemp);
				transactionTemp.setAmount(s.getBuy().getYng_item().getPrice());
				transactionTemp.setCity("Moreno");
				transactionTemp.setCountry("Argentina");
				transactionTemp.setCountryCode("AR");
				transactionTemp.setCurrency("ARS");
				transactionTemp.setDay(Integer.parseInt(hourdateFormat.format(date)));
				transactionTemp.setDescription("Acreditación por venta del producto ");
				transactionTemp.setIp("181.115.199.143");
				transactionTemp.setLat("-16.5");
				transactionTemp.setLon("-68.15");
				transactionTemp.setMonth(Integer.parseInt(hourdateFormat1.format(date)));
				transactionTemp.setOrg("Entel S.A. - EntelNet");
				transactionTemp.setRegionName("Buenos Aires");
				transactionTemp.setType("Acreditacion");
				transactionTemp.setYear(Integer.parseInt(hourdateFormat2.format(date)));
				transactionTemp.setZip("1744");
				transactionTemp.setHour(Integer.parseInt(hourdateFormat4.format(date)));
				transactionTemp.setMinute(Integer.parseInt(hourdateFormat5.format(date)));
				transactionTemp.setSecond(Integer.parseInt(hourdateFormat6.format(date)));
				transactionTemp.setAWireTransfer(false);
				transactionTemp.setAYingulTransaction(true);
				double saldo=accountTemp.getAvailableMoney();
				accountTemp.setAvailableMoney(saldo+s.getBuy().getYng_item().getPrice());
				accountDao.save(accountTemp);
				transactionDao.save(transactionTemp);
				confirmDao.save(s);
			}
    	}
	}
	
}
