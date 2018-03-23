package com.valework.yingul.logistic;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.springframework.beans.factory.annotation.Autowired;

import com.valework.yingul.controller.LogisticsController;
import com.valework.yingul.model.Yng_Item;
import com.valework.yingul.model.Yng_Person;
import com.valework.yingul.model.Yng_Product;
import com.valework.yingul.model.Yng_Shipping;
import com.valework.yingul.model.Yng_User;
import com.valework.yingul.service.StandardService;

public class Logistic {
	@Autowired
	StandardService standardService;
	//String 
 	public String andreaniHttpConection(AndreaniProperty andreaniProp) throws MalformedURLException, IOException {
		//Code to make a webservice HTTP request
		String responseString = "";
		String outputString = "";
		URL url = new URL(andreaniProp.getWsURL());
		URLConnection connection = url.openConnection();
		HttpURLConnection httpConn = (HttpURLConnection)connection;
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		String xmlInput = andreaniProp.getXmlInput();
		byte[] buffer = new byte[xmlInput.length()];
		buffer = xmlInput.getBytes();
		bout.write(buffer);
		byte[] b = bout.toByteArray();
		httpConn.setRequestProperty("Content-Length",String.valueOf(b.length));
		httpConn.setRequestProperty("Accept-Encoding", "gzip,deflate");
		httpConn.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
		httpConn.setRequestProperty("SOAPAction", andreaniProp.getSOAPAction());
		httpConn.setRequestProperty("Host",andreaniProp.getHost() );
		httpConn.setRequestProperty("Connection", "Keep-Alive");
		httpConn.setRequestMethod("POST");
		httpConn.setDoOutput(true);
		httpConn.setDoInput(true);
		OutputStream out = httpConn.getOutputStream();
		//Write the content of the request to the outputstream of the HTTP Connection.
		out.write(b);
		out.close();
		//Ready with sending the request.

		//Read the response.
		InputStreamReader isr = null;
		if (httpConn.getResponseCode() == 200) {
		  isr = new InputStreamReader(httpConn.getInputStream());
		} else {
		  isr = new InputStreamReader(httpConn.getErrorStream());
		}

		BufferedReader in = new BufferedReader(isr);

		//Write the SOAP message response to a String.
		int a=0;
		while ((responseString = in.readLine()) != null) {
			if(a>0) {
				outputString = outputString + responseString;
			}	a++;	
		System.out.println("outputString:"+responseString);
		}
		System.out.println("outputString1 :"+outputString);
		return ""+outputString;
		}
	public String andreaniStringRe(Yng_Person per,Yng_Shipping shi,Yng_Person perItem,Yng_Product pro) {
		//LogisticsController  logisticsController=new LogisticsController();
		//Yng_Product prod = logisticsController.getProductByIdItem(shi.getYng_Quote().getYng_Item().getItemId());
		   String confirmarcompra="<soapenv:Envelope \r\n" + 
		   		"xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" \r\n" + 
		   		"xmlns:tem=\"http://tempuri.org/\" \r\n" + 
		   		"xmlns:ecom=\"http://ics.andreani.com/eCommerce\" \r\n" + 
		   		"xmlns:and=\"http://schemas.datacontract.org/2004/07/Andreani.eCommerce.Servicios.DatosImpresion\"\r\n" + 
		   		"xmlns:ns3=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\">\r\n" + 
		   		"<soapenv:Header>\r\n" + 
		   		"<ns3:Security soapenv:mustUnderstand=\"1\">\r\n" + 
		   		"<ns3:UsernameToken>\r\n" + 
		   		"<ns3:Username>"
		   			+ "STAGING_WS"
		   		+ "</ns3:Username>\r\n" + 
		   		"<ns3:Password>"
		   			+ "ANDREANI"
		   		+ "</ns3:Password>\r\n" + 
		   		"</ns3:UsernameToken>\r\n" + 
		   		"</ns3:Security>\r\n" + 
		   		"</soapenv:Header>\r\n" + 
		   		"<soapenv:Body>\r\n" + 
		   		"<tem:GenerarEnvioConDatosDeImpresionYRemitente>\r\n" + 
		   		"<!--Optional:-->\r\n" + 
		   		"<tem:parametros>\r\n" + 
		   		"<!--Optional:-->\r\n" + 
		   		"<ecom:categoriaDePeso>"
		   			+ "1"
		   		+ "</ecom:categoriaDePeso>\r\n" + 
		   		"<ecom:contrato>"
		   			+ "400006711"
		   		+ "</ecom:contrato>\r\n" + 
		   		"<ecom:destinatario>\r\n" + 
			   		"<and:apellido>"
			   			+ per.getLastname()
			   		+ "</and:apellido>\r\n" + 
			   		"<!--Optional:-->\r\n" + 
			   		"<and:apellidoAlternativo>"
			   			+ ""
			   		+ "</and:apellidoAlternativo>\r\n" + 
			   		"<and:email>"
			   			+ per.getYng_User().getEmail()
			   		+ "</and:email>\r\n" + 
			   		"<!--Optional:-->\r\n" + 
			   		"<and:nombre>"
			   			+ per.getName()
			   		+ "</and:nombre>\r\n" + 
			   		"<!--Optional:-->\r\n" + 
			   		"<and:nombreAlternativo>"
			   			+ ""
			   		+ "</and:nombreAlternativo>\r\n" + 
			   		"<!--Optional:-->\r\n" + 
			   		"<and:numeroDeDocumento>"
			   			+ per.getYng_User().getDocumentNumber()
			   		+ "</and:numeroDeDocumento>\r\n" + 
			   		"<!--Optional:-->\r\n" + 
			   		"<and:telefonos>\r\n" + 
			   		"<!--Zero or more repetitions:-->\r\n" + 
			   		"<and:Telefono>\r\n" + 
			   		"<!--Optional:-->\r\n" + 
			   		"<and:numero>"
			   			+ per.getYng_User().getPhone()
			   		+ "</and:numero>\r\n" + 
			   		"<!--Optional:-->\r\n" + 
			   		"<and:tipo>"
			   			+ "movil"
			   		+ "</and:tipo>\r\n" + 
			   		"</and:Telefono>\r\n" + 
			   		"</and:telefonos>\r\n" + 
			   		"<!--Optional:-->\r\n" + 
			   		"<and:tipoDeDocumento>"
			   			+ per.getYng_User().getDocumentType()
			   		+ "</and:tipoDeDocumento>\r\n" + 
		   		"</ecom:destinatario>\r\n" + 
		   		"<ecom:destino>\r\n" + 
			   		"<!--Optional:-->\r\n" + 
			   		"<and:alturaDeDomicilio></and:alturaDeDomicilio>\r\n" + 
			   		"<and:calle>"
			   			+ per.getYng_User().getYng_Ubication().getStreet()
			   		+ "</and:calle>\r\n" + 
			   		"<and:codigoPostal>"
			   			+ per.getYng_User().getYng_Ubication().getPostalCode()
			   		+ "</and:codigoPostal>\r\n" + 
			   		"<!--Optional:-->\r\n" + 
			   		"<and:departamento></and:departamento>\r\n" + 
			   		"<!--Optional:-->\r\n" + 
			   		"<and:localidad></and:localidad>\r\n" + 
			   		"<!--Optional:-->\r\n" + 
			   		"<and:pais>"
			   			+ ""
			   		+ "</and:pais>\r\n" + 
			   		"<!--Optional:-->\r\n" + 
			   		"<and:piso></and:piso>\r\n" + 
			   		"<!--Optional:-->\r\n" + 
			   		"<and:provincia>"
			   			+ ""
			   		+ "</and:provincia>\r\n" + 
		   		"</ecom:destino>\r\n" + 
		   		"<!--Optional:-->\r\n" + 
		   		"<ecom:detalleDeProductoAEntregar></ecom:detalleDeProductoAEntregar>\r\n" + 
		   		"<!--Optional:-->\r\n" + 
		   		"<ecom:detalleDeProductoARetirar></ecom:detalleDeProductoARetirar>\r\n" + 
		   		"<!--Optional:-->\r\n" + 
		   		"<ecom:idCliente>"
		   			+ "CL0003750"
		   		+ "</ecom:idCliente>\r\n" + 
		   		"<ecom:origen>\r\n" + 
			   		"<!--Optional:-->\r\n" + 
			   		"<and:alturaDeDomicilio></and:alturaDeDomicilio>\r\n" + 
			   		"<and:calle>calle alamos</and:calle>\r\n" + 
			   		"<and:codigoPostal>"
			   			+ shi.getYng_Quote().getYng_Item().getYng_Ubication().getPostalCode()//pro.getYng_Item().getUser().getYng_Ubication().getPostalCode()
			   		+ "</and:codigoPostal>\r\n" + 
			   		"<!--Optional:-->\r\n" + 
			   		"<and:departamento></and:departamento>\r\n" + 
			   		"<!--Optional:-->\r\n" + 
			   		"<and:localidad></and:localidad>\r\n" + 
			   		"<!--Optional:-->\r\n" + 
			   		"<and:pais></and:pais>\r\n" + 
			   		"<!--Optional:-->\r\n" + 
			   		"<and:piso></and:piso>\r\n" + 
			   		"<!--Optional:-->\r\n" + 
			   		"<and:provincia></and:provincia>\r\n" + 
		   		"</ecom:origen>\r\n" + 
		   		"<!--Optional:-->\r\n" + 
		   		"<ecom:pesoNetoDelEnvioEnGr>"
		   			+ pro.getProductPeso()
		   		+ "</ecom:pesoNetoDelEnvioEnGr>\r\n" + 
		   		"<ecom:remitente>\r\n" + 
			   		"<and:apellido>"
			   			+ perItem.getLastname()
			   		+ "</and:apellido>\r\n" + 
			   		"<!--Optional:-->\r\n" + 
			   		"<and:apellidoAlternativo>"
			   			+ ""
			   		+ "</and:apellidoAlternativo>\r\n" + 
			   		"<and:email>"
			   			+ perItem.getYng_User().getEmail()
			   		+ "</and:email>\r\n" + 
			   		"<!--Optional:-->\r\n" + 
			   		"<and:nombre>"
			   			+ perItem.getName()
			   		+ "</and:nombre>\r\n" + 
			   		"<!--Optional:-->\r\n" + 
			   		"<and:nombreAlternativo>"
			   			+ ""
			   		+ "</and:nombreAlternativo>\r\n" + 
			   		"<!--Optional:-->\r\n" + 
			   		"<and:numeroDeDocumento>"
			   		+ perItem.getYng_User().getDocumentNumber()
			   		+ "</and:numeroDeDocumento>\r\n" + 
			   		"<!--Optional:-->\r\n" + 
			   		"<and:telefonos>\r\n" + 
			   		"<!--Zero or more repetitions:-->\r\n" + 
			   		"<and:Telefono>\r\n" + 
			   		"<!--Optional:-->\r\n" + 
			   		"<and:numero>"
			   			+ perItem.getYng_User().getPhone()
			   		+ "</and:numero>\r\n" + 
			   		"<!--Optional:-->\r\n" + 
			   		"<and:tipo>"
			   		+ "movil"
			   		+ "</and:tipo>\r\n" + 
			   		"</and:Telefono>\r\n" + 
			   		"</and:telefonos>\r\n" + 
			   		"<!--Optional:-->\r\n" + 
			   		"<and:tipoDeDocumento>"
			   			+ perItem.getYng_User().getDocumentType()
			   		+ "</and:tipoDeDocumento>\r\n" + 
		   		"</ecom:remitente>\r\n" + 
		   		"<ecom:sucursalDeImposicion>"
		   			+ "17"
		   		+ "</ecom:sucursalDeImposicion>\r\n" + 
		   		"<!--Optional:-->\r\n" + 
		   		"<ecom:sucursalDeRetiro>"
		   			+ "17"
		   		+ "</ecom:sucursalDeRetiro>\r\n" + 
		   		"<!--Optional:-->\r\n" + 
		   		"<ecom:tarifa>"
		   			+ shi.getYng_Quote().getRate()
		   		+ "</ecom:tarifa>\r\n" + 
		   		"<ecom:valorDeclaradoConIva>"
		   			+ shi.getYng_Quote().getYng_Item().getPrice()
		   		+ "</ecom:valorDeclaradoConIva>\r\n" + 
		   		"<!--Optional:-->\r\n" + 
		   		"<ecom:volumenDelEnvioEnCm3>"
		   			+ pro.getProducVolumen()
		   		+ "</ecom:volumenDelEnvioEnCm3>\r\n" + 
		   		"</tem:parametros>\r\n" + 
		   		"</tem:GenerarEnvioConDatosDeImpresionYRemitente>\r\n" + 
		   		"</soapenv:Body>\r\n" + 
		   		"</soapenv:Envelope>";
		   return ""+confirmarcompra;
	}
    public String andreaniRemitenteWSDL(String a) throws Exception{
		   AndreaniProperty andr=new AndreaniProperty();
		   andr.setHost("integraciones.andreani.com:5000");
		   andr.setSOAPAction("http://tempuri.org/IDatosImpresion/GenerarEnvioConDatosDeImpresionYRemitente");
		   andr.setWsURL("https://integraciones.andreani.com:5000/DatosImpresion");
		   andr.setXmlInput(a);			  
		   return andreaniHttpConection(andr);
	   }
    public String andreaniPdfLink(String numberAndreani) throws Exception{
    	System.out.println("numeroAndre: "+numberAndreani);
 	   AndreaniProperty andr=new AndreaniProperty();
 	   andr.setHost("integraciones.andreani.com:5000");
 	   andr.setSOAPAction("http://www.andreani.com.ar/IImprimirConstancia/ImprimirConstancia");
 	   andr.setWsURL("https://integraciones.andreani.com:5000/E-ImposicionRemota");
 	   andr.setXmlInput(andreaniPrintTicketData(numberAndreani));
 	   return andreaniHttpConection(andr);
    }
    public String andreaniPrintTicketData(String numberAndreni) {
 	   String imprimirEtiqueta="<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:and=\"http://www.andreani.com.ar\">\r\n" + 
 	   		"   <soapenv:Header/>\r\n" + 
 	   		"   <soapenv:Body>\r\n" + 
 	   		"      <and:ImprimirConstancia>\r\n" + 
 	   		"         <!--Optional:-->\r\n" + 
 	   		"         <and:entities>\r\n" + 
 	   		"            <!--Zero or more repetitions:-->\r\n" + 
 	   		"            <and:ParamImprimirConstancia>\r\n" + 
 	   		"               <and:NumeroAndreani>"
 	   		+ numberAndreni
 	   		+ "</and:NumeroAndreani>\r\n" + 
 	   		"            </and:ParamImprimirConstancia>\r\n" + 
 	   		"         </and:entities>\r\n" + 
 	   		"      </and:ImprimirConstancia>\r\n" + 
 	   		"   </soapenv:Body>\r\n" + 
 	   		"</soapenv:Envelope>";	   
 	   return ""+imprimirEtiqueta;
    }
    public String errorPDF() {
    return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:and=\"http://www.andreani.com.ar\"><soapenv:Body><NS1:Fault xmlns:NS1=\"http://schemas.xmlsoap.org/soap/envelope/\"><faultcode xmlns:a=\"http://andreani.com/IntegraCoreServices/faults\">a:FaultSinCodigo</faultcode><faultstring xml:lang=\"es-AR\">El n&#250;mero ingresado no pertenece a una entidad._businessEntity -> EntityNumber _originalCode -> A-EntityNumber[GetEntity-17] _define -> dgm.Model.Operation.BEODefine _currentIndexRule -> 1 _businessRules -> System.Collections.Generic.List`1[dgm.Model.Operation.BusinessRule] item valor: _define -> dgm.Model.Operation.BRDefine _rtn -> dgm.Model.Return _context -> null _rtn -> dgm.Model.Return _values -> System.Collections.Generic.Dictionary`2[System.String,System.Object] item valor: key -> entity value -> null _returnCodes -> System.Collections.Generic.Dictionary`2[System.Int32,System.String] item valor: key -> 0 value -> OK item valor: key -> -1 value -> ERROR item valor: key -> 100 value -> OP NO EXISTE item valor: key -> 101 value -> OP INACTIVA item valor: key -> 102 value -> OP SIN REGLA item valor: key -> 103 value -> OP SIN PERMISO item valor: key -> 104 value -> NO EXISTE PROXIMA REGLA item valor: key -> 105 value -> RETURN CODE INEXISITENTE item valor: key -> 106 value -> RETURN CODE INVALIDO </faultstring></NS1:Fault></soapenv:Body></soapenv:Envelope>";	
    } 
    public String errorPDF2() {
    	return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:and=\"http://www.andreani.com.ar\"><soapenv:Body><NS1:Fault xmlns:NS1=\"http://schemas.xmlsoap.org/soap/envelope/\"><faultcode xmlns:a=\"http://andreani.com/IntegraCoreServices/faults\">a:FaultSinCodigo</faultcode><faultstring xml:lang=\"es-AR\">NOT REGISTERTable:pieceKeys:, piece:132619795</faultstring></NS1:Fault></soapenv:Body></soapenv:Envelope>";
    }
    public String xmlDhlQuoteRequest() {
		String XML="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
				"<p:DCTRequest xmlns:p=\"http://www.dhl.com\" xmlns:p1=\"http://www.dhl.com/datatypes\" xmlns:p2=\"http://www.dhl.com/DCTRequestdatatypes\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.dhl.com DCT-req.xsd \">\r\n" + 
				"  <GetQuote>\r\n" + 
				"    <Request>\r\n" + 
				"      <ServiceHeader>\r\n" + 
				"        <MessageTime>2002-08-20T11:28:56.000-08:00</MessageTime>\r\n" + 
				"        <MessageReference>1234567890123456789012345678901</MessageReference>\r\n" + 
				"            <SiteID>"
				+ "DServiceVal"
				+ "</SiteID>\r\n" + 
				"            <Password>"
				+ "testServVal"
				+ "</Password>\r\n" + 
				"      </ServiceHeader>\r\n" + 
				"    </Request>\r\n" + 
				"    <From>\r\n" + 
				"      <CountryCode>"
				+ "SG"
				+ "</CountryCode>\r\n" + 
				"      <Postalcode>"
				+ "100000"
				+ "</Postalcode>\r\n" + 
				"    </From>\r\n" + 
				"    <BkgDetails>\r\n" + 
				"      <PaymentCountryCode>"
				+ "SG"
				+ "</PaymentCountryCode>\r\n" + 
				"      <Date>"
				+ "2016-08-24"
				+ "</Date>\r\n" + 
				"      <ReadyTime>"
				+ "PT10H21M"
				+ "</ReadyTime>\r\n" + 
				"      <ReadyTimeGMTOffset>"
				+ "+01:00"
				+ "</ReadyTimeGMTOffset>\r\n" + 
				"      <DimensionUnit>"
				+ "CM"
				+ "</DimensionUnit>\r\n" + 
				"      <WeightUnit>"
				+ "KG"
				+ "</WeightUnit>\r\n" + 
				"      <Pieces>\r\n" + 
				"        <Piece>\r\n" + 
				"          <PieceID>"
				+ "1"
				+ "</PieceID>\r\n" + 
				"          <Height>"
				+ "1"
				+ "</Height>\r\n" + 
				"          <Depth>"
				+ "1"
				+ "</Depth>\r\n" + 
				"          <Width>"
				+ "1"
				+ "</Width>\r\n" + 
				"          <Weight>"
				+ "5.0"
				+ "</Weight>\r\n" + 
				"        </Piece>\r\n" + 
				"      </Pieces> \r\n" + 
				"	  <PaymentAccountNumber>"
				+ "CASHSIN"
				+ "</PaymentAccountNumber>	  \r\n" + 
				"      <IsDutiable>"
				+ "N"
				+ "</IsDutiable>\r\n" + 
				"      <NetworkTypeCode>"
				+ "AL"
				+ "</NetworkTypeCode>\r\n" + 
				"	  <QtdShp>\r\n" + 
				"		 <GlobalProductCode>"
				+ "D"
				+ "</GlobalProductCode>\r\n" + 
				"	     <LocalProductCode>"
				+ "D"
				+ "</LocalProductCode>		\r\n" + 
				"	     <QtdShpExChrg>\r\n" + 
				"            <SpecialServiceType>"
				+ "AA"
				+ "</SpecialServiceType>\r\n" + 
				"         </QtdShpExChrg>\r\n" + 
				"	  </QtdShp>\r\n" + 
				"    </BkgDetails>\r\n" + 
				"    <To>\r\n" + 
				"      <CountryCode>"
				+ "AU"
				+ "</CountryCode>\r\n" + 
				"      <Postalcode>"
				+ "2007"
				+ "</Postalcode>\r\n" + 
				"    </To>\r\n" + 
				"   <Dutiable>\r\n" + 
				"      <DeclaredCurrency>"
				+ "EUR"
				+ "</DeclaredCurrency>\r\n" + 
				"      <DeclaredValue>"
				+ "1.0"
				+ "</DeclaredValue>\r\n" + 
				"    </Dutiable>\r\n" + 
				"  </GetQuote>\r\n" + 
				"</p:DCTRequest>";
		return ""+XML;
	}
	public String xmlDhlQuoteResponce() {
		String XML="<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><res:DCTResponse xmlns:res='http://www.dhl.com' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xsi:schemaLocation= 'http://www.dhl.com DCT-Response.xsd'>\r\n" + 
				"    <GetQuoteResponse>\r\n" + 
				"        <Response>\r\n" + 
				"            <ServiceHeader>\r\n" + 
				"                <MessageTime>2017-11-08T09:55:49.601+01:00</MessageTime>\r\n" + 
				"                <MessageReference>1234567890123456789012345678901</MessageReference>\r\n" + 
				"				<SiteID>customerSiteID</SiteID>\r\n" + 
				"            </ServiceHeader>\r\n" + 
				"        </Response>\r\n" + 
				"        <BkgDetails>\r\n" + 
				"            <QtdShp>\r\n" + 
				"                <OriginServiceArea>\r\n" + 
				"                    <FacilityCode>PNM</FacilityCode>\r\n" + 
				"                    <ServiceAreaCode>MES</ServiceAreaCode>\r\n" + 
				"                </OriginServiceArea>\r\n" + 
				"                <DestinationServiceArea>\r\n" + 
				"                    <FacilityCode>FKS</FacilityCode>\r\n" + 
				"                    <ServiceAreaCode>NRT</ServiceAreaCode>\r\n" + 
				"                </DestinationServiceArea>\r\n" + 
				"                <GlobalProductCode>D</GlobalProductCode>\r\n" + 
				"                <LocalProductCode>D</LocalProductCode>\r\n" + 
				"                <ProductShortName>EXPRESS WORLDWIDE</ProductShortName>\r\n" + 
				"                <LocalProductName>EXPRESS WORLDWIDE DOC</LocalProductName>\r\n" + 
				"                <NetworkTypeCode>TD</NetworkTypeCode>\r\n" + 
				"                <POfferedCustAgreement>N</POfferedCustAgreement>\r\n" + 
				"                <TransInd>Y</TransInd>\r\n" + 
				"                <PickupDate>2017-11-10</PickupDate>\r\n" + 
				"                <PickupCutoffTime>PT19H15M</PickupCutoffTime>\r\n" + 
				"                <BookingTime>PT18H15M</BookingTime>\r\n" + 
				"                <ExchangeRate>0.000</ExchangeRate>\r\n" + 
				"                <WeightCharge>0</WeightCharge>\r\n" + 
				"                <WeightChargeTax>0.000</WeightChargeTax>\r\n" + 
				"                <TotalTransitDays>3</TotalTransitDays>\r\n" + 
				"                <PickupPostalLocAddDays>0</PickupPostalLocAddDays>\r\n" + 
				"                <DeliveryPostalLocAddDays>2</DeliveryPostalLocAddDays>\r\n" + 
				"                <PickupNonDHLCourierCode> </PickupNonDHLCourierCode>\r\n" + 
				"                <DeliveryNonDHLCourierCode> </DeliveryNonDHLCourierCode>\r\n" + 
				"                <DeliveryDate>\r\n" + 
				"                    <DeliveryType>QDDC</DeliveryType>\r\n" + 
				"                    <DlvyDateTime>2017-11-15 11:59:00</DlvyDateTime>\r\n" + 
				"                    <DeliveryDateTimeOffset>+00:00</DeliveryDateTimeOffset>\r\n" + 
				"                </DeliveryDate>\r\n" + 
				"                <DeliveryTime>PT23H59M</DeliveryTime>\r\n" + 
				"                <DimensionalWeight>1.200</DimensionalWeight>\r\n" + 
				"                <WeightUnit>KG</WeightUnit>\r\n" + 
				"                <PickupDayOfWeekNum>5</PickupDayOfWeekNum>\r\n" + 
				"                <DestinationDayOfWeekNum>3</DestinationDayOfWeekNum>\r\n" + 
				"                <QuotedWeight>1.500</QuotedWeight>\r\n" + 
				"                <QuotedWeightUOM>KG</QuotedWeightUOM>\r\n" + 
				"                <PricingDate>2017-11-08</PricingDate>\r\n" + 
				"                <TotalTaxAmount>0.000</TotalTaxAmount>\r\n" + 
				"                <PickupWindowEarliestTime>09:00:00</PickupWindowEarliestTime>\r\n" + 
				"                <PickupWindowLatestTime>19:15:00</PickupWindowLatestTime>\r\n" + 
				"                <BookingCutoffOffset>PT1H</BookingCutoffOffset>\r\n" + 
				"            </QtdShp>\r\n" + 
				"            <QtdShp>\r\n" + 
				"                <OriginServiceArea>\r\n" + 
				"                    <FacilityCode>PNM</FacilityCode>\r\n" + 
				"                    <ServiceAreaCode>MES</ServiceAreaCode>\r\n" + 
				"                </OriginServiceArea>\r\n" + 
				"                <DestinationServiceArea>\r\n" + 
				"                    <FacilityCode>FKS</FacilityCode>\r\n" + 
				"                    <ServiceAreaCode>NRT</ServiceAreaCode>\r\n" + 
				"                </DestinationServiceArea>\r\n" + 
				"                <GlobalProductCode>7</GlobalProductCode>\r\n" + 
				"                <LocalProductCode>7</LocalProductCode>\r\n" + 
				"                <ProductShortName>EXPRESS EASY</ProductShortName>\r\n" + 
				"                <LocalProductName>EXPRESS EASY DOC</LocalProductName>\r\n" + 
				"                <NetworkTypeCode>TD</NetworkTypeCode>\r\n" + 
				"                <POfferedCustAgreement>Y</POfferedCustAgreement>\r\n" + 
				"                <TransInd>N</TransInd>\r\n" + 
				"                <PickupDate>2017-11-10</PickupDate>\r\n" + 
				"                <PickupCutoffTime>PT19H15M</PickupCutoffTime>\r\n" + 
				"                <BookingTime>PT18H15M</BookingTime>\r\n" + 
				"                <ExchangeRate>0.000</ExchangeRate>\r\n" + 
				"                <WeightCharge>0</WeightCharge>\r\n" + 
				"                <WeightChargeTax>0.000</WeightChargeTax>\r\n" + 
				"                <TotalTransitDays>3</TotalTransitDays>\r\n" + 
				"                <PickupPostalLocAddDays>0</PickupPostalLocAddDays>\r\n" + 
				"                <DeliveryPostalLocAddDays>2</DeliveryPostalLocAddDays>\r\n" + 
				"                <PickupNonDHLCourierCode> </PickupNonDHLCourierCode>\r\n" + 
				"                <DeliveryNonDHLCourierCode> </DeliveryNonDHLCourierCode>\r\n" + 
				"                <DeliveryDate>\r\n" + 
				"                    <DeliveryType>QDDC</DeliveryType>\r\n" + 
				"                    <DlvyDateTime>2017-11-15 11:59:00</DlvyDateTime>\r\n" + 
				"                    <DeliveryDateTimeOffset>+00:00</DeliveryDateTimeOffset>\r\n" + 
				"                </DeliveryDate>\r\n" + 
				"                <DeliveryTime>PT23H59M</DeliveryTime>\r\n" + 
				"                <DimensionalWeight>0.600</DimensionalWeight>\r\n" + 
				"                <WeightUnit>KG</WeightUnit>\r\n" + 
				"                <PickupDayOfWeekNum>5</PickupDayOfWeekNum>\r\n" + 
				"                <DestinationDayOfWeekNum>3</DestinationDayOfWeekNum>\r\n" + 
				"                <QuotedWeight>1.000</QuotedWeight>\r\n" + 
				"                <QuotedWeightUOM>KG</QuotedWeightUOM>\r\n" + 
				"                <PricingDate>2017-11-08</PricingDate>\r\n" + 
				"                <TotalTaxAmount>0.000</TotalTaxAmount>\r\n" + 
				"                <PickupWindowEarliestTime>09:00:00</PickupWindowEarliestTime>\r\n" + 
				"                <PickupWindowLatestTime>19:15:00</PickupWindowLatestTime>\r\n" + 
				"                <BookingCutoffOffset>PT1H</BookingCutoffOffset>\r\n" + 
				"            </QtdShp>\r\n" + 
				"        </BkgDetails>\r\n" + 
				"        <Srvs>\r\n" + 
				"            <Srv>\r\n" + 
				"                <GlobalProductCode>D</GlobalProductCode>\r\n" + 
				"                <MrkSrv>\r\n" + 
				"                    <LocalProductCode>D</LocalProductCode>\r\n" + 
				"                    <ProductShortName>EXPRESS WORLDWIDE</ProductShortName>\r\n" + 
				"                    <LocalProductName>EXPRESS WORLDWIDE DOC</LocalProductName>\r\n" + 
				"                    <ProductDesc>EXPRESS WORLDWIDE DOC</ProductDesc>\r\n" + 
				"                    <NetworkTypeCode>TD</NetworkTypeCode>\r\n" + 
				"                    <POfferedCustAgreement>N</POfferedCustAgreement>\r\n" + 
				"                    <TransInd>Y</TransInd>\r\n" + 
				"                    <LocalProductCtryCd>ID</LocalProductCtryCd>\r\n" + 
				"                    <GlobalServiceType>D</GlobalServiceType>\r\n" + 
				"                    <LocalServiceName>EXPRESS WORLDWIDE DOC</LocalServiceName>\r\n" + 
				"                </MrkSrv>\r\n" + 
				"            </Srv>\r\n" + 
				"            <Srv>\r\n" + 
				"                <GlobalProductCode>7</GlobalProductCode>\r\n" + 
				"                <MrkSrv>\r\n" + 
				"                    <LocalProductCode>7</LocalProductCode>\r\n" + 
				"                    <ProductShortName>EXPRESS EASY</ProductShortName>\r\n" + 
				"                    <LocalProductName>EXPRESS EASY DOC</LocalProductName>\r\n" + 
				"                    <ProductDesc>EXPRESS EASY DOC</ProductDesc>\r\n" + 
				"                    <NetworkTypeCode>TD</NetworkTypeCode>\r\n" + 
				"                    <POfferedCustAgreement>Y</POfferedCustAgreement>\r\n" + 
				"                    <TransInd>N</TransInd>\r\n" + 
				"                    <LocalProductCtryCd>ID</LocalProductCtryCd>\r\n" + 
				"                    <GlobalServiceType>7</GlobalServiceType>\r\n" + 
				"                    <LocalServiceName>EXPRESS EASY DOC</LocalServiceName>\r\n" + 
				"                </MrkSrv>\r\n" + 
				"            </Srv>\r\n" + 
				"        </Srvs>\r\n" + 
				"        <Note>\r\n" + 
				"            <ActionStatus>Success</ActionStatus>\r\n" + 
				"        </Note>\r\n" + 
				"    </GetQuoteResponse></res:DCTResponse>\r\n" + 
				"";
		return ""+XML;
	}
	public String andreaniStringShippingStatus() {
	    	String status="<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:and=\"http://www.andreani.com.ar\" xmlns:req=\"http://www.andreani.com.ar/req\">\r\n" + 
	    			"   <soap:Header/>\r\n" + 
	    			"   <soap:Body>\r\n" + 
	    			"      <and:ObtenerTrazabilidad>\r\n" + 
	    			"         <!--Optional:-->\r\n" + 
	    			"         <and:Pieza>\r\n" + 
	    			"            <req:NroPieza></req:NroPieza>\r\n" + 
	    			"            <req:NroAndreani>310000003518239</req:NroAndreani>\r\n" + 
	    			"            <req:CodigoCliente>CL0003750</req:CodigoCliente>\r\n" + 
	    			"         </and:Pieza>\r\n" + 
	    			"      </and:ObtenerTrazabilidad>\r\n" + 
	    			"   </soap:Body>\r\n" + 
	    			"</soap:Envelope>";
	    	return ""+status;
	 }
	 public String andreaniShippingStatusWSDL(String b) throws MalformedURLException, IOException{
		 
		 AndreaniProperty andr=new AndreaniProperty();
		   andr.setHost("www.e-andreani.com");
		   andr.setSOAPAction("http://www.andreani.com.ar/IService/ObtenerTrazabilidad");
		   andr.setWsURL("https://www.e-andreani.com/eAndreaniWS/Service.svc/soap12");
		   andr.setXmlInput(b);			  
		   return andreaniHttpConection(andr);
	 }
	 public String andreaniSeguimiento() throws MalformedURLException, IOException {
			//Code to make a webservice HTTP request
			String responseString = "";
			String outputString = "";
			URL url = new URL("https://www.e-andreani.com/eAndreaniWS/Service.svc/soap12");
			URLConnection connection = url.openConnection();
			HttpURLConnection httpConn = (HttpURLConnection)connection;
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			String xmlInput = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:and=\"http://www.andreani.com.ar\" xmlns:req=\"http://www.andreani.com.ar/req\">\r\n" + 
					"   <soap:Header/>\r\n" + 
					"   <soap:Body>\r\n" + 
					"      <and:ObtenerTrazabilidad>\r\n" + 
					"         <!--Optional:-->\r\n" + 
					"         <and:Pieza>\r\n" + 
					"            <req:NroPieza></req:NroPieza>\r\n" + 
					"            <req:NroAndreani>310000003518239</req:NroAndreani>\r\n" + 
					"            <req:CodigoCliente>CL0003750</req:CodigoCliente>\r\n" + 
					"         </and:Pieza>\r\n" + 
					"      </and:ObtenerTrazabilidad>\r\n" + 
					"   </soap:Body>\r\n" + 
					"</soap:Envelope>";
			byte[] buffer = new byte[xmlInput.length()];
			buffer = xmlInput.getBytes();
			bout.write(buffer);
			byte[] b = bout.toByteArray();
			httpConn.setRequestProperty("Content-Length",String.valueOf(b.length));
			httpConn.setRequestProperty("Accept-Encoding", "gzip,deflate");
			httpConn.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
			httpConn.setRequestProperty("action", "http://www.andreani.com.ar/IService/ObtenerTrazabilidad");
			httpConn.setRequestProperty("Host","www.e-andreani.com");
			httpConn.setRequestProperty("Connection", "Keep-Alive");
			httpConn.setRequestMethod("POST");
			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);
			OutputStream out = httpConn.getOutputStream();
			//Write the content of the request to the outputstream of the HTTP Connection.
			out.write(b);
			out.close();
			//Ready with sending the request.

			//Read the response.
			InputStreamReader isr = null;
			if (httpConn.getResponseCode() == 200) {
			  isr = new InputStreamReader(httpConn.getInputStream());
			} else {
			  isr = new InputStreamReader(httpConn.getErrorStream());
			}

			BufferedReader in = new BufferedReader(isr);

			//Write the SOAP message response to a String.
			int a=0;
			while ((responseString = in.readLine()) != null) {
				if(a>0) {
					outputString = outputString + responseString;
				}	a++;	
			System.out.println("outputString:"+responseString);
			}
			System.out.println("outputString1 :"+outputString);
			return ""+outputString;
			}
}
