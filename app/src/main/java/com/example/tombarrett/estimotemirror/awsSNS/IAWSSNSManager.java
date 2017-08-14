package com.example.tombarrett.estimotemirror.awsSNS;

/**
 * Created by tombarrett on 14/08/2017.
 */

public interface IAWSSNSManager {
    void publishMessageToShopAssistant(String message, String subject);
    void publishMessageToCustomer(String message, String subject);
    void publishMessageForNearExpiredToken(String message, String subject);
}
