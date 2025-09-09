package com.example.commerceapi.cart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;




@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final Map<String, List<Integer>> carts = new ConcurrentHashMap<>();     

    @PostMapping("/add")    
    public ResponseEntity<Map<String, Object>> add(
        @RequestParam("session") String session,
        @RequestParam("id") int productId
    ){
        carts.computeIfAbsent(session, k-> new ArrayList<>()).add(productId);
        Map<String, Object> body = map(
            "ok", true,
            "count", carts.get(session).size(),
            "session",session
        );
        return ResponseEntity.ok(body);
    }

    @GetMapping("/view")
    public Map<String, Object> view (
        @RequestParam("session") String session
    ){
         List<Integer> items = carts.getOrDefault(session, Collections.<Integer>emptyList());
         return map("items", items, "session", session, "size", items.size());
    }

    @PostMapping("/checkout")
    public ResponseEntity<Map<String, Object>> checkout(
    @RequestParam("session") String session
    ){
        List<Integer> removed = carts.remove(session);
        if(removed == null || removed.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(map("ok",false, "error","cart is empty or session not found", "session", session));
        }
        String orderId = UUID.randomUUID().toString();
        return ResponseEntity.ok(map("status", "OK", "orderId", orderId, "items", "removed"));
    }

    private Map<String, Object> map(Object... kv){
        Map<String, Object> m = new LinkedHashMap<String, Object>();
        for(int i=0; i < kv.length ; i+= 2){
            m.put(String.valueOf(kv[i]), kv[i+1]);
        }
        return m;
    }


}
