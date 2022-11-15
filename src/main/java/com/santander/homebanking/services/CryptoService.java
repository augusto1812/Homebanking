package com.santander.homebanking.services;

import com.santander.homebanking.dtos.CurrencyCryptoDTO;
import com.santander.homebanking.dtos.CurrencyDTO;
import com.santander.homebanking.dtos.CurrencyUsdDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;

@Service
public class CryptoService {

    Double taxUSD = 1.65;

    @Autowired
    TransactionService transactionService;

    @Autowired
    private MessageSource message;

    public String  getMensaje (String mensaje)
    {
        return   message.getMessage(mensaje,null, LocaleContextHolder.getLocale());

    }


    public ArrayList<Object> getPrice(String currBuy, String currSale) {


        /*String urlAPI = "https://criptoya.com/api/buenbit/"+currBuy+"/ars";
        RestTemplate restTemplate = new RestTemplate();
        CurrencyAPIDTO response = restTemplate.getForEntity(urlAPI, CurrencyAPIDTO.class).getBody();

        CurrencyDTO currencyDTO = new CurrencyDTO(response.getTotalBid(), response.getTotalAsk());*/



        return new ArrayList<>(Arrays.asList(0,getCurrencyDTO(currBuy,"ars"),200));


//        return new ArrayList<>(Arrays.asList(1,"Error al consultar la cotizacion",403));

    }

    /*
    btc-ars
    eth-ars
    dai-ars
    usd-ars

    btc-eth
    btc-dai
    btc-usd

    eth-btc

    dai-usd

    usd-btc

    ars-btc
    */

    public ArrayList<Object> buySaleCurr(
            Authentication authentication,
            Double amount,
            String currBuy,
            String currSale,
            String accountFrom,
            String accountTo){

        if(currBuy.equals(currSale)){
            return new ArrayList<>(Arrays.asList(1,"La divisa de compra y venta no pueden ser iguales",403));
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

        return new ArrayList<>(Arrays.asList(0,"Compra realizada correctamente",200));

        //return new ArrayList<>(Arrays.asList(1,"Error al realizar la compra",403));
    }

    public double getBuySaleAmount(Double amount,String currBuy, String currSale){

        if(!(currBuy.equals("ars")) && !(currSale.equals("ars"))){

            /*
            *cotizacion = compraDeCurrSale / ventaDeCurrBuy
            *amountNewCurr = amount *cotizacion
            *
            *
            * */

            Double price = buyValue(currSale) / saleValue(currBuy);

            return amount * price;

        } else if ((currBuy.equals("ars"))) {

            Double price = buyValue(currSale);

            return amount * price;

        }else{

            Double price = saleValue(currBuy);

            return amount / price;
        }

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


}
