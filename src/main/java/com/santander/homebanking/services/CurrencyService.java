package com.santander.homebanking.services;

import com.santander.homebanking.dtos.CurrencyCryptoDTO;
import com.santander.homebanking.dtos.CurrencyDTO;
import com.santander.homebanking.dtos.CurrencyUsdDTO;
import com.santander.homebanking.models.CurrencyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;

@Service
public class CurrencyService {

    Double taxUSD = 1.65;

    @Autowired
    TransactionService transactionService;

    @Autowired
    MessageService messageService;


    public ArrayList<Object> getPrice(String currBuy, String currSale) {

        if(currBuy.equals(currSale)){
            return new ArrayList<>(Arrays.asList(1,messageService.getMessage("currencyService.sameCurrencyType"),403));
        }

        if ((currBuy.equals("ars"))){

            //  /ARS/*noARS

            CurrencyDTO currencyDTO = getCurrencyDTO(currSale,"ars");

            Double buyValueAux= currencyDTO.getCompra();
            Double saleValueAux = currencyDTO.getVenta();

            currencyDTO.setCompra(1/saleValueAux);
            currencyDTO.setVenta(1/buyValueAux);

            return new ArrayList<>(Arrays.asList(0,currencyDTO,200));

        }

        CurrencyDTO currencyDTO = getCurrencyDTO(currBuy,"ars");

        if (!(currSale.equals("ars"))) {

            //  /*noARS/*noARS

            currencyDTO.setCompra(currencyDTO.getCompra() / saleValue(currSale));

            currencyDTO.setVenta(currencyDTO.getVenta() / saleValue(currSale));

            return new ArrayList<>(Arrays.asList(0,currencyDTO,200));

        }

        //  /*noARS/ARS
        return new ArrayList<>(Arrays.asList(0,currencyDTO,200));
    }

    public ArrayList<Object> buySaleCurr(
            Authentication authentication,
            Double amount,
            String currBuy,
            String currSale,
            String accountFrom,
            String accountTo){

        if(currBuy.equals(currSale)){
            return new ArrayList<>(Arrays.asList(1,messageService.getMessage("currencyService.sameCurrencyType"),403));
        }

        //amount a vender
        double amountSale = amount;

        //calcular amountBuy
        double amountBuy = getBuySaleAmount(amount, currBuy.toLowerCase(), currSale.toLowerCase());

        ArrayList<Object> response = transactionService.createTransactionBuySaleCurr(
                authentication,
                amountSale,
                amountBuy,
                currBuy,
                currSale,
                accountFrom,
                accountTo);

        if(response.get(0).equals(1)){
            return response;
        }

        return new ArrayList<>(Arrays.asList(0,messageService.getMessage("currencyService.purchaseOk"),200));
    }

    public double getBuySaleAmount(Double amount,String currBuy, String currSale){

        if(!(currBuy.equals("ars")) && !(currSale.equals("ars"))){

            Double price = getPriceAmount(currBuy,currSale,0);

            return amount * price;

        } else if ((currBuy.equals("ars"))) {

            Double price = getPriceAmount(currBuy,currSale,1);

            return amount * price;

        }else{

            Double price = getPriceAmount(currBuy,currSale,2);

            return amount / price;
        }

    }

    public Double getPriceAmount(String currBuy, String currSale,int numCase){

        switch(numCase) {
            case 0:
                return buyValue(currSale) / saleValue(currBuy);
            case 1:
                return buyValue(currSale);
            case 2:
                return saleValue(currBuy);
        }

        return 0.0;
    }

    public Double buyValue(String curr){
        CurrencyDTO currencyDTOAux = getCurrencyDTO(curr,"ars");

        return currencyDTOAux.getCompra();

    }

    public Double saleValue(String curr){

        CurrencyDTO currencyDTOAux = getCurrencyDTO(curr,"ars");

        return currencyDTOAux.getVenta();

    }


    public CurrencyDTO getCurrencyDTO(String currBuy, String currSale){

        if(currBuy.equals("usd")){
            String urlAPI = "https://mercados.ambito.com/dolar/oficial/variacion";
            RestTemplate restTemplate = new RestTemplate();
            CurrencyUsdDTO response = restTemplate.getForEntity(urlAPI, CurrencyUsdDTO.class).getBody();

            String compra = response.getCompra().replaceAll(",",".");
            String venta = response.getVenta().replaceAll(",",".");

            return new CurrencyDTO(Double.valueOf(compra)*taxUSD, Double.valueOf(venta)*taxUSD);
        }else{
            String urlAPI = "https://criptoya.com/api/buenbit/"+currBuy+"/"+currSale;
            RestTemplate restTemplate = new RestTemplate();
            CurrencyCryptoDTO response = restTemplate.getForEntity(urlAPI, CurrencyCryptoDTO.class).getBody();

            return new CurrencyDTO(response.getTotalBid(), response.getTotalAsk());
        }

    }

    public ArrayList<Object> getTypesCurrencies(){
        CurrencyType[] response = CurrencyType.values();
        return new ArrayList<>(Arrays.asList(0,response,200));
    }

    public ArrayList<Object> getBuyAmount(Double amount,String currBuy, String currSale){

        if(amount.isNaN() || currBuy.isBlank() || currSale.isBlank()){
            return new ArrayList<>(Arrays.asList(1,messageService.getMessage("transaction.createTransaction.emptyValues"),403));
        }

        if(currBuy.equals(currSale)){
            return new ArrayList<>(Arrays.asList(1,messageService.getMessage("currencyService.sameCurrencyType"),403));
        }

        return new ArrayList<>(Arrays.asList(0,getBuySaleAmount(amount,currBuy,currSale),200));
    }


    public ArrayList<Object> getSaleAmount(Double amount,String currBuy, String currSale){

        if(amount.isNaN() || currBuy.isBlank() || currSale.isBlank()){
            return new ArrayList<>(Arrays.asList(1,messageService.getMessage("transaction.createTransaction.emptyValues"),403));
        }

        if(currBuy.equals(currSale)){
            return new ArrayList<>(Arrays.asList(1,messageService.getMessage("currencyService.sameCurrencyType"),403));
        }

        double saleAmount;

        if(!(currBuy.equals("ars")) && !(currSale.equals("ars"))){

            Double price = getPriceAmount(currBuy,currSale,0);

            saleAmount = amount / price;

        } else if ((currBuy.equals("ars"))) {

            Double price = getPriceAmount(currBuy,currSale,1);

            saleAmount = amount / price;

        }else{

            Double price = getPriceAmount(currBuy,currSale,2);

            saleAmount = amount * price;
        }

        return new ArrayList<>(Arrays.asList(0,saleAmount,200));
    }

}
