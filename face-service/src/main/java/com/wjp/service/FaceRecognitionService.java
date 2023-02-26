package com.wjp.service;

public interface FaceRecognitionService {

    String getFaceToken(String detect);

    void faceEntry(String faceToken, String companyId);
}
