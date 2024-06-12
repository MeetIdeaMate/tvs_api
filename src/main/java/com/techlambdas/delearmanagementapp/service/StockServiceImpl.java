package com.techlambdas.delearmanagementapp.service;

import com.techlambdas.delearmanagementapp.constant.Status;
import com.techlambdas.delearmanagementapp.constant.StockStatus;
import com.techlambdas.delearmanagementapp.constant.TransferStatus;
import com.techlambdas.delearmanagementapp.exception.DataNotFoundException;
import com.techlambdas.delearmanagementapp.mapper.CommonMapper;
import com.techlambdas.delearmanagementapp.mapper.SalesMapper;
import com.techlambdas.delearmanagementapp.mapper.StockMapper;
import com.techlambdas.delearmanagementapp.model.Stock;
import com.techlambdas.delearmanagementapp.model.TransferDetail;
import com.techlambdas.delearmanagementapp.model.*;
import com.techlambdas.delearmanagementapp.repository.CustomStockRepository;
import com.techlambdas.delearmanagementapp.repository.PurchaseRepository;
import com.techlambdas.delearmanagementapp.repository.SalesRepository;
import com.techlambdas.delearmanagementapp.repository.StockRepository;
import com.techlambdas.delearmanagementapp.request.*;
import com.techlambdas.delearmanagementapp.response.StockResponse;
import com.techlambdas.delearmanagementapp.response.TransferItem;
import com.techlambdas.delearmanagementapp.response.TransferResponse;
import com.techlambdas.delearmanagementapp.utils.RandomIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class StockServiceImpl implements StockService{
    @Autowired
    private StockMapper stockMapper;
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private CustomStockRepository customStockRepository;
    @Autowired
    private CommonMapper commonMapper;
    @Autowired
    private PurchaseRepository purchaseRepository;
    @Autowired
    private SalesRepository salesRepository;
    @Autowired
    private SalesMapper salesMapper;

    @Override
    public Stock createStock(StockRequest stockRequest) {
        try {
            Stock stock = stockMapper.mapStockRequestToStock(stockRequest);
            stock.setStockStatus(StockStatus.Available);
            return stockRepository.save(stock);
        }catch (Exception ex) {
            throw new RuntimeException("Internel Server Error -- " + ex.getMessage(),ex.getCause());
        }
    }

    @Override
    public List<StockResponse> getAllStocks(String partNo, String itemName, String engineNo, String frameNo,String categoryName) {
        List<Stock> stocks=customStockRepository.getAllStocks(partNo,itemName,engineNo,frameNo,categoryName);
        return stocks.stream()
                .map(commonMapper:: toStockResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Stock updateStockDetails(String id, StockRequest stockRequest) {
        try {
            Stock existingStock=stockRepository.findStockById(id);
            if (existingStock==null)
                throw new DataNotFoundException("stocks not found with Id"+id);
            stockMapper.updateStockDetailFromRequest(stockRequest,existingStock);
            return stockRepository.save(existingStock);
        }catch (DataNotFoundException ex){
            throw new DataNotFoundException("Data not found -- "+ex.getMessage());
        }catch (Exception ex){
            throw new RuntimeException("Internal Server Error --"+ex.getMessage(),ex.getCause());
        }
    }

    @Override
    public Page<StockResponse> getAllStocksWithPage(String partNo,String itemName,String engineNo,String frameNo, Pageable pageable,String categoryName) {
        Page<Stock>stockPage =customStockRepository.getAllStocksWithPage(partNo,itemName,engineNo,frameNo,pageable,categoryName);
        List<StockResponse> stockResponses=stockPage.getContent().stream()
                .map(commonMapper:: toStockResponse)
                .collect(Collectors.toList());
        return new PageImpl<>(stockResponses,stockPage.getPageable(),stockPage.getTotalElements());
    }

    @Override
    public List<StockResponse> createStockFromPurchase(String purchaseId, StockAddReq stockAddReq) {
        Optional<Purchase> purchaseOptional = purchaseRepository.findByPurchaseId(purchaseId);
        if (!purchaseOptional.isPresent()) {
            throw new DataNotFoundException("Purchase not found with id: " + purchaseId);
        }
            Purchase purchase = purchaseOptional.get();
            List<Stock> stocks = new ArrayList<>();

            for (ItemDetail itemDetail : purchase.getItemDetails()) {
                if (stockAddReq.getPartNos().contains(itemDetail.getPartNo())) {
                    Stock stock = stockMapper.itemDetailToStock(itemDetail, purchase.getBranchId());
                    PurchaseItem purchaseItem = stockMapper.itemDetailToPurchaseItem(itemDetail);
                    SalesItem salesItem = stockMapper.itemDetailToSalesItem(itemDetail);
                    stock.setPurchaseItem(purchaseItem);
                    stock.setSalesItem(salesItem);
                    stock.setStockStatus(StockStatus.Available);
                    stockRepository.save(stock);
                    stocks.add(stock);
                }
            }

            return stocks.stream()
                    .map(commonMapper::toStockResponse)
                    .collect(Collectors.toList());
    }
    @Override
    public void mapSalesRequestToStock(SalesRequest salesRequest)
    {
        for (ItemDetail itemDetail:salesRequest.getItemDetails()) {
            Stock stock = stockMapper.itemDetailToStock(itemDetail, salesRequest.getBranchId());
            PurchaseItem purchaseItem = stockMapper.itemDetailToPurchaseItem(itemDetail);
            SalesItem salesItem = stockMapper.itemDetailToSalesItem(itemDetail);
            stock.setPurchaseItem(purchaseItem);
            stock.setSalesItem(salesItem);
            stockRepository.save(stock);
        }
    }

    @Override
    public String createTransfer(TransferRequest transferRequest) {
        String transferId=RandomIdGenerator.getRandomId();
       for (TransferItemReq transferItemReq:transferRequest.getTransferItems()){
             List<Stock>stocks=customStockRepository.findByPartNoAndBranchId(transferItemReq.getPartNo(),transferRequest.getTransferFromBranch());
             for (Stock stock:stocks){
                 if (stock.getStockId().equals(transferItemReq.getStockId())) {
                     if (transferItemReq.getQuantity() < stock.getQuantity()&&!Optional.ofNullable(stock.getMainSpecValue()).isPresent()) {
                         Stock newStock=new Stock();
                         newStock=stock;
                         newStock.setId(null);
                         newStock.setStockId(RandomIdGenerator.getRandomId());
                         newStock.setQuantity(transferItemReq.getQuantity());
                         stock.setQuantity(stock.getQuantity()- transferItemReq.getQuantity());
                         newStock.setStockStatus(StockStatus.Transfer);
                         stockRepository.save(stock);
                         updateTransferDetails(newStock,transferRequest,transferId);
                     } else {
                       updateTransferDetails(stock,transferRequest,transferId);
                 }
                 }
             }
       }
        return "Transfered Successfully";
    }

    private void updateTransferDetails(Stock stock, TransferRequest transferRequest, String transferId) {
        TransferDetail transferDetail = new TransferDetail();
        transferDetail.setTransferId(transferId);
        transferDetail.setTransferFromBranch(transferRequest.getTransferFromBranch());
        transferDetail.setTransferToBranch(transferRequest.getTransferToBranch());
        transferDetail.setTransferDate(LocalDateTime.now());
        transferDetail.setStatus(Status.CURRENT);
        List<TransferDetail> transferDetails = new ArrayList<>();
        if (Optional.ofNullable(stock.getTransferDetails()).isPresent()) {
            for (TransferDetail tD : stock.getTransferDetails()) {
                tD.setStatus(Status.COMPLETED);
                transferDetails.add(tD);
            }
            transferDetails.add(transferDetail);
        } else {
            transferDetails.add(transferDetail);
        }
        stock.setStockStatus(StockStatus.Transfer);
        stock.setTransferDetails(transferDetails);
        stockRepository.save(stock);
    }


    @Override
    public List<TransferResponse> getTransferDetails(String branchId, TransferStatus transferStatus) {
        List<TransferResponse> transferResponses = customStockRepository.findTransferDetails(branchId);
        transferResponses.forEach(transferResponse ->
                transferResponse.setTransferItems(
                        transferResponse.getTransferItems().stream()
                                .map(commonMapper::mapTransferItem)
                                .collect(Collectors.toList())
                )
        );
        return transferResponses;
    }


    @Override
    public List<TransferResponse> getTransferReceivedDetails(String branchId, TransferStatus transferStatus) {
        return null;
    }
}
