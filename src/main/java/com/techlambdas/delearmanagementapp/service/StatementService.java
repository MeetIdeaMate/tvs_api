package com.techlambdas.delearmanagementapp.service;
import com.techlambdas.delearmanagementapp.model.Statement;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StatementService {


    Statement uploadFile(MultipartFile file);

   Statement getByStatementId(String statementId);

   List<Statement> getAllStatement();




}
