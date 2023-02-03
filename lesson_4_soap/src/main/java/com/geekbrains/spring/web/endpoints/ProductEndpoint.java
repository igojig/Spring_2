package com.geekbrains.spring.web.endpoints;

import com.geekbrains.spring.web.entities.Product;
import com.geekbrains.spring.web.exceptions.ResourceNotFoundException;
import com.geekbrains.spring.web.services.ProductsService;
import com.geekbrains.spring.web.soap.products.*;
import lombok.RequiredArgsConstructor;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

@Endpoint
@RequiredArgsConstructor
public class ProductEndpoint {

    private static final String NAMESPACE_URI = "http://www.igojig.com/spring/ws/products";
    private final ProductsService productsService;
 /*
     Пример запроса: POST http://localhost:8189/app/ws
     Header -> Content-Type: text/xml

    <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:f="http://www.igojig.com/spring/ws/products">
    <soapenv:Header/>
            <soapenv:Body>
                <f:getProductByIdRequest>
                    <f:id>1</f:id>
                </f:getProductByIdRequest>
            </soapenv:Body>
        </soapenv:Envelope>

  */

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getProductByIdRequest")
    @ResponsePayload
    public GetProductByIdResponse getProductById(@RequestPayload GetProductByIdRequest request) {
        GetProductByIdResponse response = new GetProductByIdResponse();
        SoapProduct soapProduct = new SoapProduct();
        Product product = productsService.findById(request.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Product no found: " + request.getId()));
        soapProduct.setId(product.getId());
        soapProduct.setTitle(product.getTitle());
        soapProduct.setPrice(product.getPrice());
        response.setProduct(soapProduct);
        return response;
    }

    /*
        Пример запроса: POST http://localhost:8189/app/ws
        Header -> Content-Type: text/xml

        <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:f="http://www.igojig.com/spring/ws/products">
            <soapenv:Header/>
            <soapenv:Body>
                <f:getAllProductsRequest/>
            </soapenv:Body>
        </soapenv:Envelope>
     */

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllProductsRequest")
    @ResponsePayload
    public GetAllProductsResponse getAllProducts(@RequestPayload GetAllProductsRequest request) {
        GetAllProductsResponse response = new GetAllProductsResponse();

        productsService.findAllForSoap().forEach(o -> {
            SoapProduct soapProduct = new SoapProduct();
            soapProduct.setId(o.getId());
            soapProduct.setTitle(o.getTitle());
            soapProduct.setPrice(o.getPrice());
            response.getProducts().add(soapProduct);
        });

        return response;

//        return new JAXBElement<GetAllProductsResponse>(new QName("GetAllProductsResponse"), GetAllProductsResponse.class, response);
    }
}
