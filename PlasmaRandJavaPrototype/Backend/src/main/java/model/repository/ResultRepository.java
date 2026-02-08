package model.repository;

import model.PlasmaService;

import java.util.List;

public interface ResultRepository {
    void saveResult(PlasmaService.ProcessedFrame processedFrame);
    void saveResultAsList(List<PlasmaService.ProcessedFrame> result);
}