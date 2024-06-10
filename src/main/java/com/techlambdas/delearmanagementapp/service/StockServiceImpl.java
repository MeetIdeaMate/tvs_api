package com.techlambdas.delearmanagementapp.service;

import com.techlambdas.delearmanagementapp.exception.DataNotFoundException;
import com.techlambdas.delearmanagementapp.mapper.CommonMapper;
import com.techlambdas.delearmanagementapp.mapper.StockMapper;
import com.techlambdas.delearmanagementapp.model.Stock;
import com.techlambdas.delearmanagementapp.model.TransferDetail;
import com.techlambdas.delearmanagementapp.repository.CustomStockRepository;
import com.techlambdas.delearmanagementapp.repository.StockRepository;
import com.techlambdas.delearmanagementapp.request.ItemDetailRequest;
import com.techlambdas.delearmanagementapp.request.StockRequest;
import com.techlambdas.delearmanagementapp.request.TransferItemReq;
import com.techlambdas.delearmanagementapp.request.TransferRequest;
import com.techlambdas.delearmanagementapp.response.StockResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    @Override
    public Stock createStock(StockRequest stockRequest) {
        try {
            Stock stock = stockMapper.mapStockRequestToStock(stockRequest);
            return stockRepository.save(stock);
        }catch (Exception ex) {
            throw new RuntimeException("Internel Server Error -- " + ex.getMessage(),ex.getCause());
        }
    }

    @Override
    public List<StockResponse> getAllStocks(String partNo, String itemName, String engineNo, String frameNo) {
        List<Stock> stocks=customStockRepository.getAllStocks(partNo,itemName,engineNo,frameNo);
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
    public Page<Stock> getAllStocksWithPage(String partNo,String itemName,String engineNo,String frameNo, Pageable pageable) {
        return customStockRepository.getAllStocksWithPage(partNo,itemName,engineNo,frameNo,pageable);
    }

    @Override
    public String createTransfer(TransferRequest transferRequest) {
       for (TransferItemReq transferItemReq:transferRequest.getTransferItems()){
             List<Stock>stocks=customStockRepository.findByPartNoAndBranchId(transferItemReq.getPartNo(),transferRequest.getTransferFromBranch());
             for (Stock stock:stocks){
//                  stock.setBranchId(transferRequest.getTransferToBranch());
                 TransferDetail transferDetail=new TransferDetail();
                 transferDetail.setTransferToBranch(transferRequest.getTransferFromBranch());
                 transferDetail.setTransferToBranch(transferRequest.getTransferToBranch());
                 transferDetail.setTransferDate(LocalDateTime.now());
                 if (Optional.ofNullable(stock.getTransferDetails()).isPresent()){
                     stock.getTransferDetails().add(transferDetail);
                 }else {
                     List<TransferDetail>transferDetails=new ArrayList<>();
                     transferDetails.add(transferDetail);
                     stock.setTransferDetails(transferDetails);
                 }
                 stockRepository.save(stock);
             }
       }
        return "Transfered Successfully";
    }
}
