package com.valework.yingul.batch;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.mail.MessagingException;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.client.ClientProtocolException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.valework.yingul.PayUFunds;
import com.valework.yingul.SmtpMailSender;
import com.valework.yingul.XubioFunds;
import com.valework.yingul.dao.AccountDao;
import com.valework.yingul.dao.CommissionDao;
import com.valework.yingul.dao.ConfirmDao;
import com.valework.yingul.dao.ItemDao;
import com.valework.yingul.dao.PaymentDao;
import com.valework.yingul.dao.PersonDao;
import com.valework.yingul.dao.StandardDao;
import com.valework.yingul.dao.TransactionDao;
import com.valework.yingul.dao.TransactionDetailDao;
import com.valework.yingul.logistic.GetStateSend;
import com.valework.yingul.logistic.GetTraceability;
import com.valework.yingul.logistic.Yng_AndreaniTrazabilidad;
import com.valework.yingul.model.Yng_Account;
import com.valework.yingul.model.Yng_Buy;
import com.valework.yingul.model.Yng_Commission;
import com.valework.yingul.model.Yng_Confirm;
import com.valework.yingul.model.Yng_Item;
import com.valework.yingul.model.Yng_Payment;
import com.valework.yingul.model.Yng_Person;
import com.valework.yingul.model.Yng_Standard;
import com.valework.yingul.model.Yng_StateShipping;
import com.valework.yingul.model.Yng_Transaction;
import com.valework.yingul.model.Yng_TransactionDetail;
import com.valework.yingul.service.MotorizedService;
import com.valework.yingul.service.PersonService;
import com.valework.yingul.service.PropertyService;

@Component
public class GreetingBatchBean {
	@Autowired
	private SmtpMailSender smtpMailSender;
	@Autowired
	ConfirmDao confirmDao;
	@Autowired
	TransactionDao transactionDao;
	@Autowired
	AccountDao accountDao;

	@Autowired 
	CommissionDao commissionDao;
	@Autowired
	PersonService personService;
	@Autowired
	PropertyService propertyService;
	@Autowired 
	MotorizedService motorizedService;

	@Autowired
	StandardDao standardDao;
	@Autowired
	TransactionDetailDao transactionDetailDao;
	@Autowired
	PaymentDao paymentDao;
	@Autowired
	ItemDao itemDao;
	@Autowired
	PayUFunds payUFunds;
	@Autowired
	PersonDao personDao;
	@Autowired
	XubioFunds xubioFunds;
	
	//@Scheduled(cron = "0,30 * * * * *")//para cada 30 segundos
	//@Scheduled(cron = "0 0 6 * * *")//cada dia a las 6 de la mañana
	//@Scheduled(cron = "0 0/16 12 * * ?")//cada 8 minutos desde las 10:45
	@Scheduled(cron = "0 15/16 21 * * ?")//cada 8 minutos desde las 10:45
	public void cronJob() throws ClientProtocolException, IOException, Exception {
		smtpMailSender.send("quenallataeddy@gmail.com", "INICIO DE LOS CRONS", "CRONS");
		System.out.println("primer cron");
		Date date = new Date();
    	DateFormat hourdateFormat = new SimpleDateFormat("dd");
    	DateFormat hourdateFormat1 = new SimpleDateFormat("MM");
    	DateFormat hourdateFormat2 = new SimpleDateFormat("yyyy");
    	DateFormat hourdateFormat4 = new SimpleDateFormat("HH");
    	DateFormat hourdateFormat5 = new SimpleDateFormat("mm");
    	DateFormat hourdateFormat6 = new SimpleDateFormat("ss");
		List<Yng_Confirm> listConfirm = confirmDao.findByStatus("confirm");
		for (Yng_Confirm s : listConfirm) {
			String str_date="";
    		if(s.getDayEndClaim()>=10) {
    			str_date=""+s.getDayEndClaim();
    		}else{
    			str_date="0"+s.getDayEndClaim();
    		}
    		if(s.getMonthEndClaim()>=10) {
    			str_date+="-"+s.getMonthEndClaim();
    		}else {
    			str_date+="-0"+s.getMonthEndClaim();
    		}
    		str_date += "-"+s.getYearEndClaim();
    		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
    		Date endClaim= formatter.parse(str_date);
			if(date.after(endClaim)&&(!DateUtils.isSameDay(date,endClaim))) {
				System.out.println("============"+s.getBuy().getYng_Payment().getPaymentId());
				String paymentStatus = payUFunds.getStatusPayment(s.getBuy().getYng_Payment());
				if(paymentStatus.equals("APPROVED")) {
					s.setStatus("closed");
					Yng_Account accountTemp= accountDao.findByUser(s.getBuy().getYng_item().getUser());
					//crear la transaccion con todo el costo del producto para luego descontar comisiones o costo de envio
					Yng_Transaction transactionTemp = new Yng_Transaction();
					transactionTemp.setAmount((double)Math.round(s.getBuy().getItemCost() * 100d) / 100d);
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
					transactionTemp.setInvoiceStatus("unrequited");
					transactionTemp.setTypeCode("AVP");
					double saldo=accountTemp.getAvailableMoney();
					accountTemp.setAvailableMoney(saldo+transactionTemp.getAmount());
					accountTemp=accountDao.save(accountTemp);
					transactionTemp.setAccount(accountTemp);
					transactionDao.save(transactionTemp);
					
					//transaccion del cobro de comisiones
					Yng_Transaction commissionTemp = new Yng_Transaction();
					commissionTemp.setCity("Moreno");
					commissionTemp.setCountry("Argentina");
					commissionTemp.setCountryCode("AR");
					commissionTemp.setCurrency("ARS");
					commissionTemp.setDay(Integer.parseInt(hourdateFormat.format(date)));
					commissionTemp.setDescription("Cobro de transaccion por venta a travez de Yingul Pay");
					commissionTemp.setIp("181.115.199.143");
					commissionTemp.setLat("-16.5");
					commissionTemp.setLon("-68.15");
					commissionTemp.setMonth(Integer.parseInt(hourdateFormat1.format(date)));
					commissionTemp.setOrg("Entel S.A. - EntelNet");
					commissionTemp.setRegionName("Buenos Aires");
					commissionTemp.setType("Débito");
					commissionTemp.setYear(Integer.parseInt(hourdateFormat2.format(date)));
					commissionTemp.setZip("1744");
					commissionTemp.setHour(Integer.parseInt(hourdateFormat4.format(date)));
					commissionTemp.setMinute(Integer.parseInt(hourdateFormat5.format(date)));
					commissionTemp.setSecond(Integer.parseInt(hourdateFormat6.format(date)));
					commissionTemp.setAWireTransfer(false);
					commissionTemp.setAYingulTransaction(true);
					commissionTemp.setInvoiceStatus("pending");
					commissionTemp.setTypeCode("CTV");
					double costCommission=0;
					Yng_TransactionDetail transactionDetail=new Yng_TransactionDetail();
					System.out.println(s.getBuy().getYng_item().getUser().getUsername());
					List<Yng_Person> personList= personDao.findAll();
					Yng_Person person = new Yng_Person();
					for (Yng_Person yng_Person : personList) {
						if(yng_Person.getYng_User().getUsername().equals(s.getBuy().getYng_item().getUser().getUsername())) {
							person = yng_Person;
							System.out.println(person.getName()+" todo bien");
						}
					}
					
					Yng_Commission commission= new Yng_Commission();
					Yng_Commission commissionPAYU= new Yng_Commission();
					
					//condicional para el cobro de comisiones				
					if(s.getBuy().getShippingCost()==0) {
						//cobro de comisiones
						
						switch(s.getBuy().getYng_item().getType()) {
						case "Product":
							if(person.isBusiness()) {
								commission =commissionDao.findByToWhoAndWhy("Business", "Product");
								commissionPAYU = commissionDao.findByConditionAndWhy("ARS", "PAYU");
							}else {
								commission =commissionDao.findByToWhoAndWhy("Person", "Product");
								commissionPAYU = commissionDao.findByConditionAndWhy("ARS", "PAYU");
							}
							break;
						case "Property":
							if(s.getBuy().getYng_item().getCondition().equals("Rental")) {
								commission =commissionDao.findByConditionAndWhy("Rental", "Property");
								commissionPAYU = commissionDao.findByConditionAndWhy("ARS", "PAYU");
							}else {
								commission =commissionDao.findByToWhoAndWhy("All", "Property");
								commissionPAYU = commissionDao.findByConditionAndWhy("ARS", "PAYU");
							}
							break;
						case "Motorized":
							if(s.getBuy().getYng_item().getCondition().equals("New")) {
								commission =commissionDao.findByConditionAndWhy("New", "Motorized");
								commissionPAYU = commissionDao.findByConditionAndWhy("ARS", "PAYU");
							}else {
								commission =commissionDao.findByConditionAndWhy("All", "All");
								commissionPAYU = commissionDao.findByConditionAndWhy("ARS", "PAYU");
							}
							break;
						default:
							commission =commissionDao.findByConditionAndWhy("All", "All");
							commissionPAYU = commissionDao.findByConditionAndWhy("ARS", "PAYU");
							break;
						}
						//transactionDetail.setCostCommission((double)Math.round((((s.getBuy().getCost()*commission.getPercentage())/100)+commission.getFixedPrice()) * 100d) / 100d);
						transactionDetail.setCostCommission((double)Math.round(((((s.getBuy().getCost()-((double)Math.round((((s.getBuy().getCost()*commissionPAYU.getPercentage())/100)+commissionPAYU.getFixedPrice()) * 100d) / 100d)-2)*commission.getPercentage())/100)+commission.getFixedPrice()) * 100d) / 100d);
					}else {
						if(s.getBuy().getCost()==s.getBuy().getItemCost()) {
							//cobro de comisiones
							Yng_Transaction payShipping = new Yng_Transaction();
							payShipping.setCity("Moreno");
							payShipping.setCountry("Argentina");
							payShipping.setCountryCode("AR");
							payShipping.setCurrency("ARS");
							payShipping.setDay(Integer.parseInt(hourdateFormat.format(date)));
							payShipping.setDescription("Cobro de envio por venta a travez de Yingul Express");
							payShipping.setIp("181.115.199.143");
							payShipping.setLat("-16.5");
							payShipping.setLon("-68.15");
							payShipping.setMonth(Integer.parseInt(hourdateFormat1.format(date)));
							payShipping.setOrg("Entel S.A. - EntelNet");
							payShipping.setRegionName("Buenos Aires");
							payShipping.setType("Débito");
							payShipping.setYear(Integer.parseInt(hourdateFormat2.format(date)));
							payShipping.setZip("1744");
							payShipping.setHour(Integer.parseInt(hourdateFormat4.format(date)));
							payShipping.setMinute(Integer.parseInt(hourdateFormat5.format(date)));
							payShipping.setSecond(Integer.parseInt(hourdateFormat6.format(date)));
							payShipping.setAWireTransfer(false);
							payShipping.setAYingulTransaction(true);
							payShipping.setAmount(s.getBuy().getShippingCost());
							payShipping.setInvoiceStatus("unrequited");
							payShipping.setTypeCode("CEV");
							saldo=accountTemp.getAvailableMoney();
							accountTemp.setAvailableMoney(saldo-payShipping.getAmount());
							accountTemp=accountDao.save(accountTemp);
							payShipping.setAccount(accountTemp);
							transactionDao.save(payShipping);
							
							switch(s.getBuy().getYng_item().getType()) {
							case "Product":
								if(person.isBusiness()) {
									commission =commissionDao.findByToWhoAndWhy("Business", "Product");
									commissionPAYU = commissionDao.findByConditionAndWhy("ARS", "PAYU");
								}else {
									commission =commissionDao.findByToWhoAndWhy("Person", "Product");
									commissionPAYU = commissionDao.findByConditionAndWhy("ARS", "PAYU");
								}
								break;
							case "Property":
									if(s.getBuy().getYng_item().getCondition().equals("Rental")) {
										commission =commissionDao.findByConditionAndWhy("Rental", "Property");
										commissionPAYU = commissionDao.findByConditionAndWhy("ARS", "PAYU");
									}else {
										commission =commissionDao.findByToWhoAndWhy("All", "Property");
										commissionPAYU = commissionDao.findByConditionAndWhy("ARS", "PAYU");
									}
								break;
							case "Motorized":
								if(s.getBuy().getYng_item().getCondition().equals("New")) {
									commission =commissionDao.findByConditionAndWhy("New", "Motorized");
									commissionPAYU = commissionDao.findByConditionAndWhy("ARS", "PAYU");
								}else {
									commission =commissionDao.findByConditionAndWhy("All", "All");
									commissionPAYU = commissionDao.findByConditionAndWhy("ARS", "PAYU");
								}
								break;
							default:
								commission =commissionDao.findByConditionAndWhy("All", "All");
								commissionPAYU = commissionDao.findByConditionAndWhy("ARS", "PAYU");
								break;
							}
							//transactionDetail.setCostCommission((double)Math.round(((((s.getBuy().getCost()-s.getBuy().getShippingCost())*commission.getPercentage())/100)+commission.getFixedPrice()) * 100d) / 100d);
							transactionDetail.setCostCommission((double)Math.round(((((s.getBuy().getCost()-s.getBuy().getShippingCost()-((double)Math.round((((s.getBuy().getCost()*commissionPAYU.getPercentage())/100)+commissionPAYU.getFixedPrice()) * 100d) / 100d)-2)*commission.getPercentage())/100)+commission.getFixedPrice()) * 100d) / 100d);
						}else {
							//cobro de comisiones
							switch(s.getBuy().getYng_item().getType()) {
							case "Product":						
								if(person.isBusiness()) {
									commission =commissionDao.findByToWhoAndWhy("Business", "Product");
									commissionPAYU = commissionDao.findByConditionAndWhy("ARS", "PAYU");
								}else {
									commission =commissionDao.findByToWhoAndWhy("Person", "Product");
									commissionPAYU = commissionDao.findByConditionAndWhy("ARS", "PAYU");
								}
								break;
							case "Property":
									if(s.getBuy().getYng_item().getCondition().equals("Rental")) {
										commission =commissionDao.findByConditionAndWhy("Rental", "Property");
										commissionPAYU = commissionDao.findByConditionAndWhy("ARS", "PAYU");
									}else {
										commission =commissionDao.findByToWhoAndWhy("All", "Property");
										commissionPAYU = commissionDao.findByConditionAndWhy("ARS", "PAYU");
									}
								break;
							case "Motorized":
								if(s.getBuy().getYng_item().getCondition().equals("New")) {
									commission =commissionDao.findByConditionAndWhy("New", "Motorized");
									commissionPAYU = commissionDao.findByConditionAndWhy("ARS", "PAYU");
								}else {
									commission =commissionDao.findByConditionAndWhy("All", "All");
									commissionPAYU = commissionDao.findByConditionAndWhy("ARS", "PAYU");
								}
								break;
							default:
								commission =commissionDao.findByConditionAndWhy("All", "All");
								commissionPAYU = commissionDao.findByConditionAndWhy("ARS", "PAYU");
								break;
							}
							//transactionDetail.setCostCommission((double)Math.round((((s.getBuy().getItemCost()*commission.getPercentage())/100)+commission.getFixedPrice()) * 100d) / 100d);
							//a mi parecer esta mal
							transactionDetail.setCostCommission((double)Math.round(((((s.getBuy().getItemCost()-((double)Math.round((((s.getBuy().getCost()*commissionPAYU.getPercentage())/100)+commissionPAYU.getFixedPrice()) * 100d) / 100d)-2)*commission.getPercentage())/100)+commission.getFixedPrice()) * 100d) / 100d);
						}
					}
					//corregir para envios gratis
					
					
					transactionDetail.setCostPAYU((double)Math.round((((s.getBuy().getCost()*commissionPAYU.getPercentage())/100)+commissionPAYU.getFixedPrice()) * 100d) / 100d);
					costCommission=((double)Math.round((transactionDetail.getCostCommission()+transactionDetail.getCostPAYU()) * 100d) / 100d);
					transactionDetail.setCostTotal(costCommission);
					
					commissionTemp.setAmount(costCommission);
					saldo=accountTemp.getAvailableMoney();
					accountTemp.setAvailableMoney((double)Math.round((saldo-commissionTemp.getAmount()) * 100d) / 100d);
					accountTemp=accountDao.save(accountTemp);
					commissionTemp.setAccount(accountTemp);
					transactionDetail.setTransaction(transactionDao.save(commissionTemp));
					transactionDetailDao.save(transactionDetail);
					confirmDao.save(s);
				}else {
					Yng_Payment tempPayment = new Yng_Payment();
					tempPayment= s.getBuy().getYng_Payment();
					tempPayment.setStatus(paymentStatus);
					tempPayment = paymentDao.save(tempPayment);   
					
					
					
					s.setStatus("closedWithheld");
					Yng_Account accountTemp= accountDao.findByUser(s.getBuy().getYng_item().getUser());
					//crear la transaccion con todo el costo del producto para luego descontar comisiones o costo de envio
					Yng_Transaction transactionTemp = new Yng_Transaction();
					transactionTemp.setAmount((double)Math.round(s.getBuy().getItemCost() * 100d) / 100d);
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
					transactionTemp.setTypeCode("AVP");
					transactionTemp.setYear(Integer.parseInt(hourdateFormat2.format(date)));
					transactionTemp.setZip("1744");
					transactionTemp.setHour(Integer.parseInt(hourdateFormat4.format(date)));
					transactionTemp.setMinute(Integer.parseInt(hourdateFormat5.format(date)));
					transactionTemp.setSecond(Integer.parseInt(hourdateFormat6.format(date)));
					transactionTemp.setAWireTransfer(false);
					transactionTemp.setAYingulTransaction(true);
					transactionTemp.setInvoiceStatus("unrequited");
					double saldo=accountTemp.getWithheldMoney();
					accountTemp.setWithheldMoney(saldo+transactionTemp.getAmount());
					accountTemp=accountDao.save(accountTemp);
					transactionTemp.setAccount(accountTemp);
					transactionDao.save(transactionTemp);
					
					//transaccion del cobro de comisiones
					Yng_Transaction commissionTemp = new Yng_Transaction();
					commissionTemp.setCity("Moreno");
					commissionTemp.setCountry("Argentina");
					commissionTemp.setCountryCode("AR");
					commissionTemp.setCurrency("ARS");
					commissionTemp.setDay(Integer.parseInt(hourdateFormat.format(date)));
					commissionTemp.setDescription("Cobro de transaccion por venta a travez de Yingul Pay");
					commissionTemp.setIp("181.115.199.143");
					commissionTemp.setLat("-16.5");
					commissionTemp.setLon("-68.15");
					commissionTemp.setMonth(Integer.parseInt(hourdateFormat1.format(date)));
					commissionTemp.setOrg("Entel S.A. - EntelNet");
					commissionTemp.setRegionName("Buenos Aires");
					commissionTemp.setType("Débito");
					commissionTemp.setYear(Integer.parseInt(hourdateFormat2.format(date)));
					commissionTemp.setZip("1744");
					commissionTemp.setHour(Integer.parseInt(hourdateFormat4.format(date)));
					commissionTemp.setMinute(Integer.parseInt(hourdateFormat5.format(date)));
					commissionTemp.setSecond(Integer.parseInt(hourdateFormat6.format(date)));
					commissionTemp.setAWireTransfer(false);
					commissionTemp.setAYingulTransaction(true);
					commissionTemp.setInvoiceStatus("unrequited");
					commissionTemp.setTypeCode("CTV");
					double costCommission=0;
					Yng_TransactionDetail transactionDetail=new Yng_TransactionDetail();
					System.out.println(s.getBuy().getYng_item().getUser().getUsername());
					List<Yng_Person> personList= personDao.findAll();
					Yng_Person person = new Yng_Person();
					for (Yng_Person yng_Person : personList) {
						if(yng_Person.getYng_User().getUsername().equals(s.getBuy().getYng_item().getUser().getUsername())) {
							person = yng_Person;
							System.out.println(person.getName()+" todo bien");
						}
					}
					Yng_Commission commission= new Yng_Commission();
					Yng_Commission commissionPAYU= new Yng_Commission();
					//condicional para el cobro de comisiones				
					if(s.getBuy().getShippingCost()==0) {
						//cobro de comisiones
						
						switch(s.getBuy().getYng_item().getType()) {
						case "Product":
							if(person.isBusiness()) {
								commission =commissionDao.findByToWhoAndWhy("Business", "Product");
								commissionPAYU = commissionDao.findByConditionAndWhy("ARS", "PAYU");
							}else {
								commission =commissionDao.findByToWhoAndWhy("Person", "Product");
								commissionPAYU = commissionDao.findByConditionAndWhy("ARS", "PAYU");
							}
							break;
						case "Property":
							if(s.getBuy().getYng_item().getCondition().equals("Rental")) {
								commission =commissionDao.findByConditionAndWhy("Rental", "Property");
								commissionPAYU = commissionDao.findByConditionAndWhy("ARS", "PAYU");
							}else {
								commission =commissionDao.findByToWhoAndWhy("All", "Property");
								commissionPAYU = commissionDao.findByConditionAndWhy("ARS", "PAYU");
							}
							break;
						case "Motorized":
							if(s.getBuy().getYng_item().getCondition().equals("New")) {
								commission =commissionDao.findByConditionAndWhy("New", "Motorized");
								commissionPAYU = commissionDao.findByConditionAndWhy("ARS", "PAYU");
							}else {
								commission =commissionDao.findByConditionAndWhy("All", "All");
								commissionPAYU = commissionDao.findByConditionAndWhy("ARS", "PAYU");
							}
							break;
						default:
							commission =commissionDao.findByConditionAndWhy("All", "All");
							commissionPAYU = commissionDao.findByConditionAndWhy("ARS", "PAYU");
							break;
						}
						//transactionDetail.setCostCommission((double)Math.round((((s.getBuy().getCost()*commission.getPercentage())/100)+commission.getFixedPrice()) * 100d) / 100d);
						transactionDetail.setCostCommission((double)Math.round(((((s.getBuy().getCost()-((double)Math.round((((s.getBuy().getCost()*commissionPAYU.getPercentage())/100)+commissionPAYU.getFixedPrice()) * 100d) / 100d)-2)*commission.getPercentage())/100)+commission.getFixedPrice()) * 100d) / 100d);
					}else {
						if(s.getBuy().getCost()==s.getBuy().getItemCost()) {
							//cobro de comisiones
							Yng_Transaction payShipping = new Yng_Transaction();
							payShipping.setCity("Moreno");
							payShipping.setCountry("Argentina");
							payShipping.setCountryCode("AR");
							payShipping.setCurrency("ARS");
							payShipping.setDay(Integer.parseInt(hourdateFormat.format(date)));
							payShipping.setDescription("Cobro de envio por venta a travez de Yingul Express");
							payShipping.setIp("181.115.199.143");
							payShipping.setLat("-16.5");
							payShipping.setLon("-68.15");
							payShipping.setMonth(Integer.parseInt(hourdateFormat1.format(date)));
							payShipping.setOrg("Entel S.A. - EntelNet");
							payShipping.setRegionName("Buenos Aires");
							payShipping.setType("Débito");
							payShipping.setYear(Integer.parseInt(hourdateFormat2.format(date)));
							payShipping.setZip("1744");
							payShipping.setHour(Integer.parseInt(hourdateFormat4.format(date)));
							payShipping.setMinute(Integer.parseInt(hourdateFormat5.format(date)));
							payShipping.setSecond(Integer.parseInt(hourdateFormat6.format(date)));
							payShipping.setAWireTransfer(false);
							payShipping.setAYingulTransaction(true);
							payShipping.setAmount(s.getBuy().getShippingCost());
							payShipping.setInvoiceStatus("unrequited");
							payShipping.setTypeCode("CEV");
							saldo=accountTemp.getWithheldMoney();
							accountTemp.setWithheldMoney(saldo-payShipping.getAmount());
							accountTemp=accountDao.save(accountTemp);
							payShipping.setAccount(accountTemp);
							transactionDao.save(payShipping);
							
							switch(s.getBuy().getYng_item().getType()) {
							case "Product":
								if(person.isBusiness()) {
									commission =commissionDao.findByToWhoAndWhy("Business", "Product");
									commissionPAYU = commissionDao.findByConditionAndWhy("ARS", "PAYU");
								}else {
									commission =commissionDao.findByToWhoAndWhy("Person", "Product");
									commissionPAYU = commissionDao.findByConditionAndWhy("ARS", "PAYU");
								}
								break;
							case "Property":
									if(s.getBuy().getYng_item().getCondition().equals("Rental")) {
										commission =commissionDao.findByConditionAndWhy("Rental", "Property");
										commissionPAYU = commissionDao.findByConditionAndWhy("ARS", "PAYU");
									}else {
										commission =commissionDao.findByToWhoAndWhy("All", "Property");
										commissionPAYU = commissionDao.findByConditionAndWhy("ARS", "PAYU");
									}
								break;
							case "Motorized":
								if(s.getBuy().getYng_item().getCondition().equals("New")) {
									commission =commissionDao.findByConditionAndWhy("New", "Motorized");
									commissionPAYU = commissionDao.findByConditionAndWhy("ARS", "PAYU");
								}else {
									commission =commissionDao.findByConditionAndWhy("All", "All");
									commissionPAYU = commissionDao.findByConditionAndWhy("ARS", "PAYU");
								}
								break;
							default:
								commission =commissionDao.findByConditionAndWhy("All", "All");
								commissionPAYU = commissionDao.findByConditionAndWhy("ARS", "PAYU");
								break;
							}
							//transactionDetail.setCostCommission((double)Math.round(((((s.getBuy().getCost()-s.getBuy().getShippingCost())*commission.getPercentage())/100)+commission.getFixedPrice()) * 100d) / 100d);
							transactionDetail.setCostCommission((double)Math.round(((((s.getBuy().getCost()-s.getBuy().getShippingCost()-((double)Math.round((((s.getBuy().getCost()*commissionPAYU.getPercentage())/100)+commissionPAYU.getFixedPrice()) * 100d) / 100d)-2)*commission.getPercentage())/100)+commission.getFixedPrice()) * 100d) / 100d);
						}else {
							//cobro de comisiones
							switch(s.getBuy().getYng_item().getType()) {
							case "Product":						
								if(person.isBusiness()) {
									commission =commissionDao.findByToWhoAndWhy("Business", "Product");
									commissionPAYU = commissionDao.findByConditionAndWhy("ARS", "PAYU");
								}else {
									commission =commissionDao.findByToWhoAndWhy("Person", "Product");
									commissionPAYU = commissionDao.findByConditionAndWhy("ARS", "PAYU");
								}
								break;
							case "Property":
									if(s.getBuy().getYng_item().getCondition().equals("Rental")) {
										commission =commissionDao.findByConditionAndWhy("Rental", "Property");
										commissionPAYU = commissionDao.findByConditionAndWhy("ARS", "PAYU");
									}else {
										commission =commissionDao.findByToWhoAndWhy("All", "Property");
										commissionPAYU = commissionDao.findByConditionAndWhy("ARS", "PAYU");
									}
								break;
							case "Motorized":
								if(s.getBuy().getYng_item().getCondition().equals("New")) {
									commission =commissionDao.findByConditionAndWhy("New", "Motorized");
									commissionPAYU = commissionDao.findByConditionAndWhy("ARS", "PAYU");
								}else {
									commission =commissionDao.findByConditionAndWhy("All", "All");
									commissionPAYU = commissionDao.findByConditionAndWhy("ARS", "PAYU");
								}
								break;
							default:
								commission =commissionDao.findByConditionAndWhy("All", "All");
								commissionPAYU = commissionDao.findByConditionAndWhy("ARS", "PAYU");
								break;
							}
							//transactionDetail.setCostCommission((double)Math.round((((s.getBuy().getItemCost()*commission.getPercentage())/100)+commission.getFixedPrice()) * 100d) / 100d);
							transactionDetail.setCostCommission((double)Math.round(((((s.getBuy().getItemCost()-((double)Math.round((((s.getBuy().getCost()*commissionPAYU.getPercentage())/100)+commissionPAYU.getFixedPrice()) * 100d) / 100d)-2)*commission.getPercentage())/100)+commission.getFixedPrice()) * 100d) / 100d);
						}
					}
					//corregir para envios gratis
					transactionDetail.setCostPAYU((double)Math.round((((s.getBuy().getCost()*commissionPAYU.getPercentage())/100)+commissionPAYU.getFixedPrice()) * 100d) / 100d);
					costCommission=((double)Math.round((transactionDetail.getCostCommission()+transactionDetail.getCostPAYU()) * 100d) / 100d);
					transactionDetail.setCostTotal(costCommission);
					commissionTemp.setAmount(costCommission);
					saldo=accountTemp.getWithheldMoney();
					accountTemp.setWithheldMoney((double)Math.round((saldo-commissionTemp.getAmount()) * 100d) / 100d);
					accountTemp=accountDao.save(accountTemp);
					commissionTemp.setAccount(accountTemp);
					transactionDetail.setTransaction(transactionDao.save(commissionTemp));
					transactionDetailDao.save(transactionDetail);
					confirmDao.save(s);	
				}
			}
    	}
	}
	
	//@Scheduled(cron = "0,59 * * * * *")//para cada 30 segundos
	//@Scheduled(cron = "0 0 4 * * *")//cada dia a las 5 de la mañana
	@Scheduled(cron = "0 19/30 21 * * ?")//cada 5 minutos desde las 10:45
	public void cronJob1() throws ClientProtocolException, IOException, Exception {
		System.out.println("segundo cron");
		List<Yng_Payment> confirmCashPayment= paymentDao.findByTypeAndStatusAndBuyStatus("CASH","PENDING","PENDING");
		for (Yng_Payment s : confirmCashPayment) {
			Date fecha = new Date();
			if(s.getCashPayment().getExpiration().before(fecha)) {
				s.setBuyStatus("EXPIRED");
				s=paymentDao.save(s);
				ObjectMapper mapper = new ObjectMapper();
				Yng_Buy buyTemp = new Yng_Buy();
				try {
					buyTemp = mapper.readValue(s.getCashPayment().getBuyJson(), Yng_Buy.class);
					// System.out.println(token.toString());	 
				} catch (JsonParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JsonMappingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Yng_Item itemTemp= itemDao.findByItemId(buyTemp.getYng_item().getItemId());
				itemTemp.setQuantity(itemTemp.getQuantity()+buyTemp.getQuantity());
				itemTemp.setEnabled(true);
				itemTemp=itemDao.save(itemTemp);
			}else {
				if(payUFunds.queryByOrderId(s).equals("save")){
					s.setBuyStatus("AUTHORIZED");
					s=paymentDao.save(s);
				}else {
					//poner algo para guardar los que no se pudo por alguna razon
				}
			}
			
		}
	}
	
	//@Scheduled(cron = "0,30 * * * * *")//para cada 30 segundos
	//@Scheduled(cron = "0 0 5 * * *")//cada dia a las 6 de la mañana
	@Scheduled(cron = "0 23/16 21 * * ?")//cada 8 minutos desde las 10:45
	public void deliveryConfirmation() throws MessagingException{
		System.out.println("tercer cron");
		List<Yng_Confirm> listConfirm = confirmDao.findByStatus("pending");
		for (Yng_Confirm s : listConfirm) {
			Yng_Confirm confirmTemp=s;
			if(confirmTemp.getBuy().getShipping()!=null) {
		    	if(confirmTemp.getBuy().getShipping().getTypeShipping().equals("branch")) {
		    		String confirmStateDao=standardDao.findByKey("codeConfirmAndreani").getValue();
		    		Yng_AndreaniTrazabilidad andreaniTrazabilidad=new Yng_AndreaniTrazabilidad();
		    		GetTraceability getTrazability = new GetTraceability();
			    	String confirmState=confirmTemp.getBuy().getShipping().getYng_Shipment().getShipmentCod();
			    	String stateApi ="";
			    	
			    	Yng_Standard clienteStandard = standardDao.findByKey("Cliente");
			    	Yng_Standard codeDeliveryConfirmAndreani = standardDao.findByKey("codeDeliveryConfirmAndreani");//entre ala sucurlas el envio ingresado 
			    	boolean enterOnBranch=false;
			    	try {
			    		andreaniTrazabilidad=getTrazability.sendState(""+confirmState,clienteStandard.getValue());
			    		//stateApi=andreaniTrazabilidad.getEstado();
			    		System.out.println("null:"+andreaniTrazabilidad);
			    		if(andreaniTrazabilidad!=null) {
			    			enterOnBranch=getTrazability.serchState(andreaniTrazabilidad,codeDeliveryConfirmAndreani.getValue());	
			    		}
			    		
			    		System.out.println("state:"+stateApi+"enterOnBranch:"+enterOnBranch +" confirmState: "+confirmState);
			    		
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    		String status=stateApi;
		    		
		    		System.out.println("status:"+status+":"+"confirmcode:"+codeDeliveryConfirmAndreani.getValue()+"id confirm"+confirmTemp.getConfirmId());
		    		//if(status.equals(codeDeliveryConfirmAndreani.getValue())) {
		    		if(enterOnBranch==true)	{
		    			System.out.println("entro para confirmar");
		    			confirmTemp.setBuyerConfirm(false);
		        		confirmTemp.setSellerConfirm(true);
		        		Date date = new Date();
		            	DateFormat hourdateFormat = new SimpleDateFormat("dd");
		            	DateFormat hourdateFormat1 = new SimpleDateFormat("MM");
		            	DateFormat hourdateFormat2 = new SimpleDateFormat("yyyy");
		            	DateTime now = new DateTime( date );
		            	confirmTemp.setDaySellerConfirm(Integer.parseInt(hourdateFormat.format(date)));
		            	confirmTemp.setMonthSellerConfirm(Integer.parseInt(hourdateFormat1.format(date)));
		            	confirmTemp.setYearSellerConfirm(Integer.parseInt(hourdateFormat2.format(date)));
		            	confirmTemp.setStatus("delivered");
		            	confirmDao.save(confirmTemp);
		            	smtpMailSender.send(confirmTemp.getBuy().getYng_item().getUser().getEmail(), "CONFIRMACIÓN DE ENTREGA EXITOSA","Se realizo la confirmacion de la entrega del producto en una sucursal andreani:  "+confirmTemp.getBuy().getYng_item().getName() +"  Descripción : "+confirmTemp.getBuy().getYng_item().getDescription()+ "  " +"  Precio: " +confirmTemp.getBuy().getYng_item().getPrice()
		            			+ "<br/>--Despues de que tu comprador recoja el producto tendra 10 dias vigentes para realizar reclamos acerca del producto.");
		            	DateTime endClaim = now.plusDays(4);
		            	smtpMailSender.send(confirmTemp.getBuy().getUser().getEmail(), "CONFIRMACIÓN DE ENTREGA EXITOSA", "Tu vendedor realizo la entrega del producto : "+confirmTemp.getBuy().getQuantity()+" "+confirmTemp.getBuy().getYng_item().getName()+" a:"+confirmTemp.getBuy().getCost()+" en la sucursal andreani"
		    					+ "<br/>--Puedes recoger el producto de la sucursal Andreani desde "+Integer.parseInt(hourdateFormat.format(endClaim.toDate()))+"/"+Integer.parseInt(hourdateFormat1.format(endClaim.toDate()))+"/"+Integer.parseInt(hourdateFormat2.format(endClaim.toDate())));
		    		}		
		    	}
			}
		}
	}
	
	//@Scheduled(cron = "0,30 * * * * *")//para cada 30 segundos
	//@Scheduled(cron = "0 0 7 * * *")//cada dia a las 6 de la mañana
	@Scheduled(cron = "0 27/16 21 * * ?")//cada 8 minutos desde las 10:51
	public void whithdrawalConfirmation() throws MessagingException{
		System.out.println("cuarto cron");
		List<Yng_Confirm> listConfirm = confirmDao.findByStatus("delivered");
		for (Yng_Confirm s : listConfirm) {
			Yng_Confirm confirmTemp=s;
			//codeConfirmAndreani
	       	String confirmStateDao=standardDao.findByKey("codeConfirmAndreani").getValue();
	       	//System.out.println("confirmTemp:"+confirmTemp.toString()+" value:"+);
	       	String cliente=standardDao.findByKey("Cliente").getValue();
	       	Yng_Standard codeWhithdrawalConfirmAndreani = standardDao.findByKey("codeWhithdrawalConfirmAndreani");
	       	Yng_AndreaniTrazabilidad andreaniTrazabilidad=new Yng_AndreaniTrazabilidad();
    		GetTraceability getTrazability = new GetTraceability();
	    	String confirmState=confirmTemp.getBuy().getShipping().getYng_Shipment().getShipmentCod();
	    	String stateApi ="";
	    	boolean deliveredBuyer=false;
	    	try {
	    		andreaniTrazabilidad=getTrazability.sendState(""+confirmState,cliente);
	    		//stateApi=stateShipping.getEstado();
	    		if(andreaniTrazabilidad!=null) {
	    			deliveredBuyer=getTrazability.serchState(andreaniTrazabilidad,codeWhithdrawalConfirmAndreani.getValue());	
	    		}
	    		System.out.println("state:"+stateApi+":"+confirmStateDao);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			String status=stateApi;//envio entregado al comprador
			if(deliveredBuyer==true)	{
	    	//if(status.equals(codeWhithdrawalConfirmAndreani.getValue())) {
	        		confirmTemp.setBuyerConfirm(true);
	        		confirmTemp.setSellerConfirm(true);
	        		Date date = new Date();
	            	DateFormat hourdateFormat = new SimpleDateFormat("dd");
	            	DateFormat hourdateFormat1 = new SimpleDateFormat("MM");
	            	DateFormat hourdateFormat2 = new SimpleDateFormat("yyyy");
	            	DateTime now = new DateTime( date );
	            	Yng_Standard daysForClaims = standardDao.findByKey("daysForClaims");
	            	DateTime endClaim = now.plusDays( Integer.parseInt(daysForClaims.getValue()) );
	            	
	            	confirmTemp.setDayBuyerConfirm(Integer.parseInt(hourdateFormat.format(date)));
	            	confirmTemp.setMonthBuyerConfirm(Integer.parseInt(hourdateFormat1.format(date)));
	            	confirmTemp.setYearBuyerConfirm(Integer.parseInt(hourdateFormat2.format(date)));
	            	confirmTemp.setDayInitClaim(Integer.parseInt(hourdateFormat.format(date)));
	            	confirmTemp.setMonthInitClaim(Integer.parseInt(hourdateFormat1.format(date)));
	            	confirmTemp.setYearInitiClaim(Integer.parseInt(hourdateFormat2.format(date)));
	            	confirmTemp.setDayEndClaim(Integer.parseInt(hourdateFormat.format(endClaim.toDate())));
	            	confirmTemp.setMonthEndClaim(Integer.parseInt(hourdateFormat1.format(endClaim.toDate())));
	            	confirmTemp.setYearEndClaim(Integer.parseInt(hourdateFormat2.format(endClaim.toDate())));
	            	confirmTemp.setStatus("confirm");
	            	confirmDao.save(confirmTemp);
	            	smtpMailSender.send(confirmTemp.getBuy().getYng_item().getUser().getEmail(), "CONFIRMACIÓN DE RECEPCIÓN EXITOSA","Tu comprador realizo la confirmacion de la recepción del producto :  "+confirmTemp.getBuy().getYng_item().getName() +"  Descripción : "+confirmTemp.getBuy().getYng_item().getDescription()+ "  " +"  Precio: " +confirmTemp.getBuy().getYng_item().getPrice()+" de la suscursal Andreani"
	            			+ "<br/>--Si tu comprador no tiene ninguna observacion del producto en "+daysForClaims.getValue()+" días podras recoger tu dinero ingresando a : https://www.yingul.com/frontYingulPay");
	    			smtpMailSender.send(confirmTemp.getBuy().getUser().getEmail(), "CONFIRMACIÓN DE RECEPCIÓN EXITOSA", "Se realizo la confirmacion de la recepción del producto : "+confirmTemp.getBuy().getQuantity()+" "+confirmTemp.getBuy().getYng_item().getName()+" a:"+confirmTemp.getBuy().getCost()+" de la sucursal Andreani"
	    					+ "<br/> --Tiene "+daysForClaims.getValue()+" días de garantia con Yingul para realizar alguna observación ingrese a: https://www.yingul.com/userFront/claims despues de ese lapso no se aceptaran reclamos.");
	    	}
		}
	}
	//@Scheduled(cron = "0 46/16 17 * * ?")//cada 8 minutos desde las 10:51
	
	@Scheduled(cron = "0 0 0-23 * * ?")//para cada 30 segundos este metodo puede ser a cualquier hora
	public void	invoiceCommissions() throws Exception{
		smtpMailSender.send("quenallataeddy@gmail.com", "CRONS INVO", "CRONS");
		List<Yng_Transaction> listTransaction = transactionDao.findByInvoiceStatus("pending");
		for (Yng_Transaction s : listTransaction) {
			String responseXubio = xubioFunds.postCreateInvoice(s);
			System.out.println(responseXubio);
			if(responseXubio.equals("save")) {
				s.setInvoiceStatus("invoiced");
				s = transactionDao.save(s); 
			}
		}
	}
	
	@Scheduled(cron = "0 5 0-23 * * ?")//para cada 30 segundos
	public void	sendInvoiceCommissions() throws Exception{
		smtpMailSender.send("quenallataeddy@gmail.com", "CRONS SEND INVO", "CRONS");
		List<Yng_Transaction> listTransaction = transactionDao.findByInvoiceStatus("invoiced");
		for (Yng_Transaction s : listTransaction) {
			String responseXubio = xubioFunds.sendInvoiceByEmail(s);
			if(responseXubio.equals("save")) {
				s.setInvoiceStatus("sentBill");
				s = transactionDao.save(s); 
			}
		}
	}
	
	
	public void labelQuery() throws MessagingException{
		System.out.println("quinto cron");
		List<Yng_Confirm> listConfirm = confirmDao.findByStatus("pending");
		for (Yng_Confirm s : listConfirm) {
			Yng_Confirm confirmTemp=s;
	    	if(confirmTemp.getBuy().getShipping().getTypeShipping().equals("branch")) {

	    		String confirmStateDao=standardDao.findByKey("codeConfirmAndreani").getValue();
	    		String cliente=standardDao.findByKey("Cliente").getValue();
	    		Yng_StateShipping stateShipping=new Yng_StateShipping();
		    	GetStateSend getState = new GetStateSend();
		    	String confirmState=confirmTemp.getBuy().getShipping().getYng_Shipment().getShipmentCod();
		    	String stateApi ="";
		    	try {
		    		stateShipping=getState.sendState(""+confirmState,cliente);
		    		stateApi=stateShipping.getEstado();
		    		System.out.println("state:"+stateApi+":"+confirmStateDao);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		String status=stateApi;
	    		Yng_Standard codeDeliveryConfirmAndreani = standardDao.findByKey("codeDeliveryConfirmAndreani");//entre ala sucurlas el envio ingresado 
	    		if(status.equals(codeDeliveryConfirmAndreani.getValue())) {
	    			confirmTemp.setBuyerConfirm(false);
	        		confirmTemp.setSellerConfirm(true);
	        		Date date = new Date();
	            	DateFormat hourdateFormat = new SimpleDateFormat("dd");
	            	DateFormat hourdateFormat1 = new SimpleDateFormat("MM");
	            	DateFormat hourdateFormat2 = new SimpleDateFormat("yyyy");
	            	DateTime now = new DateTime( date );
	            	confirmTemp.setDaySellerConfirm(Integer.parseInt(hourdateFormat.format(date)));
	            	confirmTemp.setMonthSellerConfirm(Integer.parseInt(hourdateFormat1.format(date)));
	            	confirmTemp.setYearSellerConfirm(Integer.parseInt(hourdateFormat2.format(date)));
	            	confirmTemp.setStatus("delivered");
	            	confirmDao.save(confirmTemp);
	            	smtpMailSender.send(confirmTemp.getBuy().getYng_item().getUser().getEmail(), "CONFIRMACIÓN DE ENTREGA EXITOSA","Se realizo la confirmacion de la entrega del producto en una sucursal andreani:  "+confirmTemp.getBuy().getYng_item().getName() +"  Descripción : "+confirmTemp.getBuy().getYng_item().getDescription()+ "  " +"  Precio: " +confirmTemp.getBuy().getYng_item().getPrice()
	            			+ "<br/>--Despues de que tu comprador recoja el producto tendra 10 dias vigentes para realizar reclamos acerca del producto.");
	            	DateTime endClaim = now.plusDays(4);
	            	smtpMailSender.send(confirmTemp.getBuy().getUser().getEmail(), "CONFIRMACIÓN DE ENTREGA EXITOSA", "Tu vendedor realizo la entrega del producto : "+confirmTemp.getBuy().getQuantity()+" "+confirmTemp.getBuy().getYng_item().getName()+" a:"+confirmTemp.getBuy().getCost()+" en la sucursal andreani"
	    					+ "<br/>--Puedes recoger el producto de la sucursal Andreani desde "+Integer.parseInt(hourdateFormat.format(endClaim.toDate()))+"/"+Integer.parseInt(hourdateFormat1.format(endClaim.toDate()))+"/"+Integer.parseInt(hourdateFormat2.format(endClaim.toDate())));
	    		}		
	    	}
		}
	}
}
