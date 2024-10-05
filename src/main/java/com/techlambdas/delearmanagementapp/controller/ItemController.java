package com.techlambdas.delearmanagementapp.controller;

import com.techlambdas.delearmanagementapp.model.Item;
import com.techlambdas.delearmanagementapp.request.ItemRequest;
import com.techlambdas.delearmanagementapp.response.ItemResponse;
import com.techlambdas.delearmanagementapp.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.techlambdas.delearmanagementapp.response.AppResponse.successResponse;

@RestController
@RequestMapping("/item")
public class ItemController {
    @Autowired
    private ItemService itemService;
    @PostMapping
    public ResponseEntity createItem(@RequestBody ItemRequest itemRequest) {
        Item item = itemService.createItem(itemRequest);
        return successResponse(HttpStatus.CREATED,"item",item);
    }
    @GetMapping
    public ResponseEntity<List<Item>>getAllItem(@RequestParam (required = false)String itemId,
                                                         @RequestParam (required = false)String itemName,
                                                         @RequestParam (required = false)String partNo){
        List<Item> items=itemService.getAllItems(itemId,itemName,partNo);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }
    @PutMapping("/{itemId}")
    public ResponseEntity<Item> updateItem(@PathVariable String itemId, @RequestBody ItemRequest itemRequest){
        Item item=itemService.updateItem(itemId,itemRequest);
        return new ResponseEntity<>(item, HttpStatus.OK);
    }
    @GetMapping("/page")
    public ResponseEntity<Page<ItemResponse>> getAllItemWithPage(
            @RequestParam(required = false) String itemId,
            @RequestParam(required = false) String itemName,
            @RequestParam(required = false) String partNo,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) String hsnCode,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ItemResponse> itemsPage = itemService.getAllItemsWithPage(itemId, itemName, partNo, pageable, categoryName , hsnCode);

        return successResponse(HttpStatus.OK,"itemsWithPage",itemsPage);
    }
    @GetMapping("/categoryId/{categoryId}/partNo/{partNo}")
    public ResponseEntity<Item> getItemByIdCategoryIdPartNo(@PathVariable String categoryId,
                                            @PathVariable String partNo){
        Item item=itemService.getItemByIdCategoryIdPartNo(categoryId,partNo);
        return new ResponseEntity<>(item, HttpStatus.OK);
    }
    @PatchMapping("/addOns/{itemId}")
    public  ResponseEntity<Item> updateAddons(@RequestBody Map<String ,Double> addOns,@PathVariable String itemId){
        Item item = itemService.updateAddons(addOns,itemId);
        return new ResponseEntity<>(item, HttpStatus.OK);
    }
}
