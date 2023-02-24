package com.amwaydemo.producer.utils;

import com.amwaydemo.producer.model.Order;
import com.amwaydemo.producer.model.OrderProduct;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class ExtractOrdersFromFile {

    public List<Order> getOrderDetailsFromJSON(byte[] inputFile) throws IOException {

        List<Order> orders = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        orders =Arrays.asList(mapper.readValue(inputFile, Order[].class));
        return orders;
    }

    private Order getNewOrderFromJSON(JSONObject data) {
        Order order = new Order();
        OrderProduct product = null;
        List<OrderProduct> products = new ArrayList<>();

        order.setCustomerEmail((String) data.get("email"));

        JSONArray array = new JSONArray(data.get("products"));
        for(int i=0; i<array.length(); i++) {
            JSONObject productObj = array.getJSONObject(i);
            product = new OrderProduct();
            product.setSku(Long.parseLong((String) productObj.get("sku")));
            product.setProductName((String) productObj.get("productName"));
            product.setQuantity(Integer.parseInt((String) productObj.get("quantity")));
            products.add(product);
        }

        order.setProducts(products);
        order.setPrice(Double.parseDouble((String) data.get("price")));
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse((String) data.get("orderedDate"));
            order.setOrderDate(sdf.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        order.setOrderStatus(OrderStatus.CREATED);
        return order;
    }

    /**
     * Extracting orders from CSV
     * @param filePath
     * @return List of orders
     */

    public List<Order> getOrderDetails(String filePath) {

        List<Order> orders = new ArrayList<>();
        Path path = Paths.get(filePath);

        try(BufferedReader br = Files.newBufferedReader(path)){
            br.readLine();
            String row = br.readLine();
            while(row != null) {
                String[] data = row.split(",");
                Order order = getNewOrder(data);
                orders.add(order);
                System.out.println("row products-> "+ row.toString());
                Arrays.stream(data).forEach(d -> System.out.println(d));

                row = br.readLine();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return orders;
    }

    private Order getNewOrder(String[] data) {
        Order order = new Order();
        OrderProduct product = null;
        List<OrderProduct> products = new ArrayList<>();

        order.setCustomerEmail((String) data[1]);
        String productsArrayString = data[2].substring(1, data[2].length()-2);
        JSONArray array = new JSONArray(productsArrayString);
        for(int i=0; i<array.length(); i++) {
            JSONObject productObj = array.getJSONObject(i);
            product = new OrderProduct();
            product.setSku(Long.parseLong((String) productObj.get("sku")));
            product.setProductName((String) productObj.get("productName"));
            product.setQuantity(Integer.parseInt((String) productObj.get("quantity")));
            products.add(product);
        }

        order.setProducts(products);
        order.setPrice(Double.parseDouble((String) data[3]));
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse((String) data[4]);
            order.setOrderDate(sdf.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        order.setOrderStatus(OrderStatus.CREATED);
        return order;
    }
}
