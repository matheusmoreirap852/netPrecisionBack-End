package com.netprecision.taskmanager.application.usecase;

import java.util.List;

public interface BaseCrudUseCase<CreateCommand, Response> {

    List<Response> findAll();

    Response findById(Long id);

    Response create(CreateCommand command);

    void delete(Long id);
}
