package com.lovisgod.iswhpay.utils.networkHandler.simplecalladapter;

public interface SimpleHandler<T> {
    void accept(T response, Throwable throwable);
}