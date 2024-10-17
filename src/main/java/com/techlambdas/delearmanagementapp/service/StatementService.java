package com.techlambdas.delearmanagementapp.service;
import com.techlambdas.delearmanagementapp.model.Statement;
import com.techlambdas.delearmanagementapp.response.StatementFileDetailsRes;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface StatementService {


    Statement uploadFile(MultipartFile file);

   Statement getByStatementId(String statementId,  LocalDate fromDate, LocalDate toDate);

   List<Statement> getAllStatement();

   List<StatementFileDetailsRes> getStatementFileInfo();


}
